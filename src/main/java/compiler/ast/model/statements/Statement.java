package compiler.ast.model.statements;

import compiler.ast.enums.EStatementType;

abstract public class Statement {
    private final EStatementType statementType;
    private final int lineNumber;

    public Statement(EStatementType statementType, int lineNumber) {
        this.statementType = statementType;
        this.lineNumber = lineNumber;
    }

    public EStatementType getStatementType() {
        return statementType;
    }

    public int getLineNumber() {
        return lineNumber;
    }
}
