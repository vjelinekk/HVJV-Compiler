package compiler.ast.enums;

public enum EOperatorLogical {
    AND("&&"),
    OR("||"),
    EQUAL("=="),
    NOT_EQUAL("!="),
    GREATER_THAN(">"),
    LESS_THAN("<"),
    GREATER_THAN_OR_EQUAL(">="),
    LESS_THAN_OR_EQUAL("<=");

    private final String translation;

    EOperatorLogical(String translation) {
        this.translation = translation;
    }

    public static EOperatorLogical getSymbol(String value) {
        for (EOperatorLogical e : EOperatorLogical.values()) {
            if (e.translation.equals(value)) {
                return e;
            }
        }
        return null;
    }

    public String getTranslation() {
        return translation;
    }
}
