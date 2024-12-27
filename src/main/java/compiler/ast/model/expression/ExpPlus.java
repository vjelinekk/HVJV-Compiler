package compiler.ast.model.expression;

import compiler.ast.enums.EExpressionType;

public class ExpPlus extends Expression {
    private final Expression expression;

    public ExpPlus(Expression expression) {
        super(EExpressionType.PLUS);
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }

    public String toString() {
        return "+" + expression;
    }
}
