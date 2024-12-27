package compiler.ast.model.statements;

import compiler.ast.enums.EStatementType;
import compiler.ast.model.flow.ForStatement;

public class StatementFor extends Statement {
    private final ForStatement forStatement;

    public StatementFor(ForStatement forStatement, int lineNumber) {
        super(EStatementType.FOR, lineNumber);
        this.forStatement = forStatement;
    }

    public ForStatement getForStatement() {
        return forStatement;
    }

    public String toString() {
        return forStatement.toString();
    }
}
