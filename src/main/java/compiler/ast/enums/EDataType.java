package compiler.ast.enums;

public enum EDataType {
    INT("int"),
    BOOL("bool");

    private final String translation;

    EDataType(String translation) {
        this.translation = translation;
    }

    public static EDataType getSymbol(String value) {
        for (EDataType e : EDataType.values()) {
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
