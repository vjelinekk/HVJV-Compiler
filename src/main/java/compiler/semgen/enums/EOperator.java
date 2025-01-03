package compiler.semgen.enums;

public enum EOperator {
    UNARY_MINUS(1),
    PLUS(2),
    MINUS(3),
    MULTIPLY(4),
    DIVIDE(5),
    MODULO(6),
    IS_ODD(7),
    EQUAL(8),
    NOT_EQUAL(9),
    LESS_THAN(10),
    GREATER_EQUAL(11),
    GREATER_THAN(12),
    LESS_EQUAL(13);

    private final int value;

    EOperator(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
