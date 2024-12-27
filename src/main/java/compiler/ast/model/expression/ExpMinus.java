package compiler.ast.model.expression;

import compiler.ast.enums.EExpressionType;

public class ExpMinus extends Expression {
    private final Expression expression;

    public ExpMinus(Expression expression) {
        super(EExpressionType.MINUS);
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }

    public String toString() {
        return "-" + expression;
    }
}
