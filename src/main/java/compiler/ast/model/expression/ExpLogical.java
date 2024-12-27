package compiler.ast.model.expression;

import compiler.ast.enums.EExpressionType;
import compiler.ast.enums.EOperatorLogical;

public class ExpLogical extends Expression {
    private final Expression left;
    private final Expression right;
    private final EOperatorLogical operator;

    public ExpLogical(Expression left, Expression right, EOperatorLogical operator) {
        super(EExpressionType.LOGICAL);
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
