package compiler.ast.model.statements;

import compiler.ast.enums.EStatementType;

public class StatementTernaryOperatorExpression extends Statement {
    private final TernaryOperatorExpression ternaryOperatorExpression;

    public StatementTernaryOperatorExpression(TernaryOperatorExpression ternaryOperatorExpression, int line) {
        super(EStatementType.TERNARY_EXPRESSION, line);
        this.ternaryOperatorExpression = ternaryOperatorExpression;
    }

    public TernaryOperatorExpression getTernaryOperatorExpression() {
        return ternaryOperatorExpression;
    }

    public String toString() {
        return ternaryOperatorExpression.toString();
    }
}
