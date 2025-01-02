package compiler.ast.model.variables;

public class BooleanValue {
    private final int value;

    public BooleanValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public String toString() {
        return Integer.toString(value);
    }
}
