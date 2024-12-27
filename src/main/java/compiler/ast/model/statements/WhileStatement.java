package compiler.ast.model.statements;

import compiler.ast.model.expression.Expression;

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
