package compiler.ast.model.statements;

import compiler.ast.model.expression.Expression;

public class IfElseStatement {
    private final Expression condition;
    private final Statements ifStatements;
    private final Statements elseStatements;

    public IfElseStatement(Expression condition, Statements ifStatements, Statements elseStatements) {
        this.condition = condition;
        this.ifStatements = ifStatements;
        this.elseStatements = elseStatements;
    }

    public Expression getCondition() {
        return condition;
    }

    public Statements getIfStatements() {
        return ifStatements;
    }

    public Statements getElseStatements() {
        return elseStatements;
    }

    public String toString() {
        return "if (" + condition + ") {\n" + ifStatements + "} else {\n" + elseStatements + "}";
    }
}
