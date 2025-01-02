package compiler.semgen;

import compiler.semgen.enums.EInstruction;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CodeBuilder {
    public static List<Instruction> instructions = new ArrayList<>();
    public static int mainAddress = -1;

    public static void addInstruction(Instruction instruction) {
        instructions.add(instruction);
    }


    public static int getLineNumber() {
        return instructions.size();
    }

    public static void generateCode(String fileName) {
        try {
            File file = new File(fileName);
            FileWriter writer = new FileWriter(file);

            writer.write( "JMP 0 " + mainAddress + "\n");

            for (Instruction instruction : instructions) {
                writer.write(instruction.toString() + "\n");
            }

            writer.close();
        } catch (IOException e) {
            System.err.println("Error while writing to file: " + e.getMessage());
        }
    }

}

