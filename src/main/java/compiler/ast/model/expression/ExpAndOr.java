package compiler.ast.model.expression;

import compiler.ast.enums.EExpressionType;
import compiler.ast.enums.EOperatorLogical;

public class ExpAndOr extends Expression {
    private final Expression left;
    private final Expression right;
    private final EOperatorLogical operator;

    public ExpAndOr(Expression left, Expression right, EOperatorLogical operator) {
        super(EExpressionType.AND_OR);
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

    public EOperatorLogical getOperator() {
        return operator;
    }

    public String toString() {
        return left + " " + operator + " " + right;
    }
}
