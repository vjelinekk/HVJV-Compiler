package compiler.ast.enums;

public enum EOperatorArithmetic {
    MULT("*"),
    DIV("/"),
    PLUS("+"),
    MINUS("-");

    private final String translation;

    EOperatorArithmetic(String translation) {
        this.translation = translation;
    }

    public static EOperatorArithmetic getSymbol(String value) {
        for (EOperatorArithmetic e : EOperatorArithmetic.values()) {
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
