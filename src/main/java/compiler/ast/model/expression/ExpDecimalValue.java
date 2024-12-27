package compiler.ast.model.expression;

public class ExpDecimalValue extends Expression {
    private final DecimalValue decimalValue;

    public ExpDecimalValue(DecimalValue decimalValue) {
        this.decimalValue = decimalValue;
    }

    public DecimalValue getDecimalValue() {
        return decimalValue;
    }

    public String toString() {
        return decimalValue.toString();
    }
}
