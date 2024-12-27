package compiler.ast.model.statements;

import compiler.ast.enums.EStatementType;

public class StatementIf extends Statement {
    private final IfStatement ifStatement;

    public StatementIf(IfStatement ifStatement, int lineNumber) {
        super(EStatementType.IF, lineNumber);
        this.ifStatement = ifStatement;
    }

    public IfStatement getIfStatement() {
        return ifStatement;
    }

    public String toString() {
        return ifStatement.toString();
    }
}
