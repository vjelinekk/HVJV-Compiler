package compiler.ast.model.statements;

import compiler.ast.model.expression.Expression;

public class TernaryOperatorAssignment {
    private final String identifier;
    private final Expression condition;
    private final Expression trueExpression;
    private final Expression falseExpression;

    public TernaryOperatorAssignment(String identifier, Expression condition, Expression trueExpression, Expression falseExpression) {
        this.identifier = identifier;
        this.condition = condition;
        this.trueExpression = trueExpression;
        this.falseExpression = falseExpression;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Expression getCondition() {
        return condition;
    }

    public Expression getTrueExpression() {
        return trueExpression;
    }

    public Expression getFalseExpression() {
        return falseExpression;
    }

    public String toString() {
        return identifier + " = " + condition + " ? " + trueExpression + " : " + falseExpression;
    }
}
