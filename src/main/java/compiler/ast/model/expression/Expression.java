package compiler.ast.model.expression;

import compiler.ast.enums.EExpressionType;

abstract public class Expression {
    private final EExpressionType expressionType;

    public Expression(EExpressionType expressionType) {
        this.expressionType = expressionType;
    }

    public EExpressionType getExpressionType() {
        return expressionType;
    }
}
