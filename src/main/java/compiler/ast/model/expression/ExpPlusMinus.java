package compiler.ast.model.expression;

import compiler.ast.enums.EExpressionType;
import compiler.ast.enums.EOperatorArithmetic;

public class ExpPlusMinus extends Expression {
    private final Expression left;
    private final Expression right;
    private final EOperatorArithmetic operator;

    public ExpPlusMinus(Expression left, Expression right, EOperatorArithmetic operator) {
        super(EExpressionType.PLUS_MINUS);
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    public Expression getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }

    public EOperatorArithmetic getOperator() {
        return operator;
    }

    public String toString() {
        return left + " " + operator + " " + right;
    }
}
