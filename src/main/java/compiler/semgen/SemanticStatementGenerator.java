package compiler.semgen;


import compiler.ast.model.statements.Statement;

public class SemanticStatementGenerator extends BaseSemanticCodeGenerator<Statement> {
    public SemanticStatementGenerator(Statement statement, SymbolTable symbolTable) {
        super(statement, symbolTable);
    }

    @Override
    public void run() {
        return;
    }
}
