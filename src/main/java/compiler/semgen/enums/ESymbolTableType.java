package compiler.semgen.enums;

public enum ESymbolTableType {
    FUNCTION,
    INT,
    BOOL;

    public boolean equals(Enum<?> other) {
        return other.name().equals(this.name());
    }
}