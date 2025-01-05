package compiler.semgen;

import compiler.ast.model.Program;
import compiler.semgen.exception.SemanticAnalysisException;
import compiler.semgen.symboltable.SymbolTable;

public class SemanticCodeGenerator extends BaseSemanticCodeGenerator<Program> {
    public SemanticCodeGenerator(Program program) {
        super(program, new SymbolTable());
    }

    public void run() throws SemanticAnalysisException {
        SemanticFunctionsGenerator functionsAnalyzer = new SemanticFunctionsGenerator(getNode().getFunctions(), getSymbolTable());
        functionsAnalyzer.run();
    }
}
