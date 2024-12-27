package compiler.ast.model.statements;

import compiler.ast.enums.EStatementType;
import compiler.ast.model.variables.Assignment;

public class StatementAssignment extends Statement {
    private final Assignment assignment;

    public StatementAssignment(Assignment assignment, int lineNumber) {
        super(EStatementType.ASSIGNMENT, lineNumber);
        this.assignment = assignment;
    }

    public Assignment getAssignment() {
        return assignment;
    }

    public String toString() {
        return assignment.toString();
    }
}
