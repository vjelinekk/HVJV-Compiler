package compiler.ast.model.expression;

import compiler.ast.enums.EExpressionType;

public class ExpIdentifier extends Expression {
    private final String identifier;

    public ExpIdentifier(String identifier) {
        super(EExpressionType.IDENTIFIER);
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String toString() {
        return identifier;
    }
}
