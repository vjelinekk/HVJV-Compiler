package compiler.ast.model.expression;

import compiler.ast.enums.EExpressionType;
import compiler.ast.model.variables.BooleanValue;

public class ExpBooleanValue extends Expression {
    private final BooleanValue booleanValue;

    public ExpBooleanValue(BooleanValue booleanValue) {
        super(EExpressionType.BOOLEAN_VALUE);
        this.booleanValue = booleanValue;
    }

    public BooleanValue getBooleanValue() {
        return booleanValue;
    }

    public String toString() {
        return booleanValue.toString();
    }
}
