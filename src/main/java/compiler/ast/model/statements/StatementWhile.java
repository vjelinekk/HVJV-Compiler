package compiler.ast.model.statements;

import compiler.ast.enums.EStatementType;

public class StatementWhile extends Statement {
    private final WhileStatement whileStatement;

    public StatementWhile(WhileStatement whileStatement, int lineNumber) {
        super(EStatementType.WHILE, lineNumber);
        this.whileStatement = whileStatement;
    }

    public WhileStatement getWhileStatement() {
        return whileStatement;
    }

    public String toString() {
        return whileStatement.toString();
    }
}
