package compiler.ast.model.statements;

import compiler.ast.enums.EStatementType;

public class StatementReturn extends Statement {
    public StatementReturn(int lineNumber) {
        super(EStatementType.RETURN, lineNumber);
    }

    public String toString() {
        return getLineNumber() + " return;";
    }
}
