package compiler.semgen;

import compiler.semgen.enums.EInstruction;
import compiler.semgen.symboltable.SymbolTable;
import compiler.semgen.symboltable.SymbolTableItem;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CodeBuilder {
    public static List<Instruction> instructions = new ArrayList<>(Collections.singletonList(new Instruction(EInstruction.JMP, 0, 0)));
    private static List<Integer> instructionsToRemove = new ArrayList<>();
    public static int mainAddress = -1;

    public static void addInstruction(Instruction instruction) {
        instructions.add(instruction);
    }

    public static int addPlaceholderInstruction(Instruction instruction) {
        instructions.add(instruction);
        return instructions.size() - 1;
    }

    public static Instruction getPlaceholderInstruction(int index) {
        return instructions.get(index);
    }

    public static int getLineNumber() {
        return instructions.size() - 1;
    }

    public static int nextInstructionLineNumber() {
        return instructions.size();
    }

    public static Instruction getLastInstruction() {
        return instructions.get(instructions.size() - 1);
    }

    public static void removeUnusedCode() {

        List<Instruction> newInstructions = new ArrayList<>();
        int shift = 0;
        int[] indexShiftMap = new int[instructions.size()];

        for (int i = 0; i < instructions.size(); i++) {
            if (instructionsToRemove.contains(i)) {
                shift++;
            } else {
                newInstructions.add(instructions.get(i));
                indexShiftMap[i] = i - shift;
            }
        }

        for (Instruction inst : newInstructions) {
            if(inst.getInstruction() == EInstruction.JMP
                    || inst.getInstruction() == EInstruction.CAL
                    || inst.getInstruction() == EInstruction.JMC) {

            int targetIndex = inst.getArg2();
            inst.setArg2(indexShiftMap[targetIndex]);
            }
        }

        instructions = newInstructions;
    }


    public static void analyzeFunctionsCode(SymbolTable symbolTable) {

        for (SymbolTableItem fncItem : symbolTable.getFunctions()) {
            if (!fncItem.isUsed() && !fncItem.getId().equals("main")) {
                int start = fncItem.getAddress();
                int count = fncItem.getLastAddress() - start;
                for (int j = 0; j <= count; j++) {
                    instructionsToRemove.add(start + j);
                }
            }
        }
    }



    public static void insertMain(int mainAddress) {
        instructions.get(0).setArg2(mainAddress);
    }

    public static void generateCode(String fileName) {
        try {
            File file = new File(fileName);
            FileWriter writer = new FileWriter(file);

            for (Instruction instruction : instructions) {
                writer.write(instruction.toString() + "\n");
            }

            writer.close();
        } catch (IOException e) {
            System.err.println("Error while writing to file: " + e.getMessage());
        }
    }
}

