package compiler.semgen;

import compiler.ast.model.Program;

public class SemanticCodeGenerator extends BaseSemanticCodeGenerator<Program> {
    public SemanticCodeGenerator(Program program) {
        super(program, new SymbolTable());
    }

    public void run() {
        SemanticFunctionsGenerator functionsAnalyzer = new SemanticFunctionsGenerator(getNode().getFunctions(), getSymbolTable());
        functionsAnalyzer.run();
    }
}
