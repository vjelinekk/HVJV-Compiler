package compiler.ast.enums;

public enum EReturnType {
    INT("int"),
    BOOL("bool"),
    VOID("void");

    private final String translation;

    EReturnType(String translation) {
        this.translation = translation;
    }

    public static EReturnType getSymbol(String value) {
        for (EReturnType e : EReturnType.values()) {
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
