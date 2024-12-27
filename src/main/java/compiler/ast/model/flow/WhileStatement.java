package compiler.ast.model.flow;

import compiler.ast.model.expression.Expression;
import compiler.ast.model.statements.Statements;

public class WhileStatement {
    private final Expression expression;
    private final Statements statements;

    public WhileStatement(Expression expression, Statements statements) {
        this.expression = expression;
        this.statements = statements;
    }

    public Expression getExpression() {
        return expression;
    }

    public Statements getStatements() {
        return statements;
    }

    public String toString() {
        return "while (" + expression + ") " + statements;
    }
}
