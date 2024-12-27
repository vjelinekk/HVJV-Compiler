package compiler.ast.model.statements;

import compiler.ast.model.expression.Expression;

public class Assignment {
    private final String identifier;
    private final Expression expression;


    public Assignment(String identifier, Expression expression) {
        this.identifier = identifier;
        this.expression = expression;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Expression getExpression() {
        return expression;
    }

    public String toString() {
        return identifier + " = " + expression;
    }
}
