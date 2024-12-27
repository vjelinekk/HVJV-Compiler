package compiler.ast.model.flow;

import compiler.ast.model.expression.Expression;
import compiler.ast.model.variables.Assignment;
import compiler.ast.model.variables.Declaration;
import compiler.ast.model.statements.Statements;

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
