package compiler.ast.model.flow;

import compiler.ast.model.expression.Expression;
import compiler.ast.model.statements.Statements;

public class IfStatement {
    private final Expression condition;
    private final Statements statements;

    public IfStatement(Expression condition, Statements statements) {
        this.condition = condition;
        this.statements = statements;
    }

    public Expression getCondition() {
        return condition;
    }

    public Statements getStatements() {
        return statements;
    }

    public String toString() {
        return "if (" + condition + ") {\n" + statements + "}";
    }
}
