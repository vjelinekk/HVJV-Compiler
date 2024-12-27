package compiler.ast.model.expression;

import compiler.ast.enums.EExpressionType;

public class ExpNot extends Expression {
    private final Expression expression;

    public ExpNot(Expression expression) {
        super(EExpressionType.NOT);
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }

    public String toString() {
        return "!" + expression;
    }
}
