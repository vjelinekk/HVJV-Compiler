package compiler.error;

public enum EErrorCode {
    LEXER(1),
    PARSER(2),
    SEMANTIC(3);

    private final int code;

    EErrorCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
