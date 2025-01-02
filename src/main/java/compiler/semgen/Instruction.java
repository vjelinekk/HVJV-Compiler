package compiler.semgen;

import compiler.semgen.enums.EInstruction;

public class Instruction {
    private final EInstruction instruction;
    private final int arg1;
    private final int arg2;

    public Instruction(EInstruction instruction, int arg1, int arg2) {
        this.instruction = instruction;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    public EInstruction getInstruction() {
        return instruction;
    }

    public int getArg1() {
        return arg1;
    }

    public int getArg2() {
        return arg2;
    }

    public String toString() {
        return instruction.getTranslation() + " " + arg1 + " " + arg2;
    }
}
