package compiler.semgen;

import compiler.ast.model.functions.Function;
import compiler.ast.model.statements.Statement;
import compiler.ast.model.statements.Statements;

public class SemanticStatementsGenerator extends BaseSemanticCodeGenerator<Statements>{
    public SemanticStatementsGenerator(Statements statements, SymbolTable symbolTable) {
        super(statements, symbolTable);
    }

    @Override
    public void run() {
        for(Statement statement : getNode().getStatements()) {
            SemanticStatementGenerator statementAnalyzer = new SemanticStatementGenerator(statement, getSymbolTable());
            statementAnalyzer.run();
        }
    }
}
