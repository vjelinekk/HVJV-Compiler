package compiler.ast.model;

import compiler.ast.model.functions.Functions;

public class Program {
    private final Functions functions;

    public Program(Functions functions) {
        this.functions = functions;
    }

    public Functions getFunctions() {
        return functions;
    }

    public String toString() {
        return functions.toString();
    }
}
