package compiler.ast.model.statements;

import compiler.ast.model.expression.Expression;

public class ForStatement {
    private final Declaration declaration;
    private final Expression condition;
    private final Assignment assignment;
    private final Statements statements;

    public ForStatement(Declaration declaration, Expression condition, Assignment assignment, Statements statements) {
        this.declaration = declaration;
        this.condition = condition;
        this.assignment = assignment;
        this.statements = statements;
    }

    public Declaration getDeclaration() {
        return declaration;
    }

    public Expression getCondition() {
        return condition;
    }

    public Assignment getAssignment() {
        return assignment;
    }

    public Statements getStatements() {
        return statements;
    }

    public String toString() {
        return "for (" + declaration + "; " + condition + "; " + assignment + ")";
    }
}
