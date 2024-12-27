package compiler.ast.model.statements;

import compiler.ast.model.expression.Expression;

public class TernaryOperatorExpression {
    private final Expression condition;
    private final Expression trueExpression;
    private final Expression falseExpression;

    public TernaryOperatorExpression(Expression condition, Expression trueExpression, Expression falseExpression) {
        this.condition = condition;
        this.trueExpression = trueExpression;
        this.falseExpression = falseExpression;
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
        return condition + " ? " + trueExpression + " : " + falseExpression;
    }
}
