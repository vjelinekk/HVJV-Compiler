package compiler.ast.model.statements;

import compiler.ast.enums.EStatementType;

public class StatementGoto extends Statement {
    private final String label;

    public StatementGoto(String label, int lineNumber) {
        super(EStatementType.GOTO, lineNumber);
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public String toString() {
        return getLineNumber() + " goto " + label + ";";
    }
}
