package compiler.ast.model.expression;

import compiler.ast.enums.EExpressionType;

public class ExpParenthesis extends Expression {
    private final Expression expression;

    public ExpParenthesis(Expression expression) {
        super(EExpressionType.PARENTHESIS);
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }

    public String toString() {
        return "(" + expression + ")";
    }
}
