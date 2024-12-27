package compiler.ast.model.statements;

import compiler.ast.enums.EStatementType;
import compiler.ast.model.expression.Expression;

public class StatementReturnExpression extends Statement {
    private final Expression expression;

    public StatementReturnExpression(Expression expression, int lineNumber) {
        super(EStatementType.RETURN_EXPRESSION, lineNumber);
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }

    public String toString() {
        return getLineNumber() + " return " + expression.toString() + ";";
    }
}
