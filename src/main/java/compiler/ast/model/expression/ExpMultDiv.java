package compiler.ast.model.expression;

import compiler.ast.enums.EExpressionType;
import compiler.ast.enums.EOperatorArithmetic;

public class ExpMultDiv extends Expression {
    private final Expression left;
    private final Expression right;
    private final EOperatorArithmetic operator;

    public ExpMultDiv(Expression left, Expression right, EOperatorArithmetic operator) {
        super(EExpressionType.MULT_DIV);
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
