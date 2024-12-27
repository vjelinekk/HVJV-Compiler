package compiler.ast.enums;

public enum ESignSymbol {
    PLUS("+"),
    MINUS("-");

    private final String translation;

    ESignSymbol(String translation) {
        this.translation = translation;
    }

    public static ESignSymbol getSymbol(String value) {
        for (ESignSymbol e : ESignSymbol.values()) {
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
