package compiler.ast.model.statements;

import compiler.ast.enums.EStatementType;

public class StatementLabel extends Statement {
    private final String label;

    public StatementLabel(String label, int lineNumber) {
        super(EStatementType.LABEL, lineNumber);
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public String toString() {
        return getLineNumber() + " " + label + ":";
    }
}
