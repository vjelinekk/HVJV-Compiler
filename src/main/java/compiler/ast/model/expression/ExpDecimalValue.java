package compiler.ast.model.expression;

import compiler.ast.enums.EExpressionType;
import compiler.ast.model.variables.DecimalValue;

public class ExpDecimalValue extends Expression {
    private final DecimalValue decimalValue;

    public ExpDecimalValue(DecimalValue decimalValue) {
        super(EExpressionType.DECIMAL_VALUE);
        this.decimalValue = decimalValue;
    }

    public DecimalValue getDecimalValue() {
        return decimalValue;
    }

    public String toString() {
        return decimalValue.toString();
    }
}
