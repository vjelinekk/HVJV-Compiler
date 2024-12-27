package compiler.ast.model.variables;

public class BooleanValue {
    private final boolean value;

    public BooleanValue(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    public String toString() {
        return value ? "true" : "false";
    }
}
