package compiler.ast.model.statements;

import compiler.ast.enums.EStatementType;

public class StatementTernaryOperatorAssignment extends Statement {
    private final TernaryOperatorAssignment ternaryOperatorAssignment;

    public StatementTernaryOperatorAssignment(TernaryOperatorAssignment ternaryOperatorAssignment, int lineNumber) {
        super(EStatementType.TERNARY_ASSIGNMENT, lineNumber);
        this.ternaryOperatorAssignment = ternaryOperatorAssignment;
    }

    public TernaryOperatorAssignment getTernaryOperatorAssignment() {
        return ternaryOperatorAssignment;
    }

    public String toString() {
        return ternaryOperatorAssignment.toString();
    }
}
