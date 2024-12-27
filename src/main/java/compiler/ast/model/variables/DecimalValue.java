package compiler.ast.model.variables;

import compiler.ast.enums.ESignSymbol;

public class DecimalValue {
    private final ESignSymbol signSymbol;
    private final int value;

    public DecimalValue(ESignSymbol signSymbol, int value) {
        this.signSymbol = signSymbol;
        this.value = value;
    }

    public ESignSymbol getSignSymbol() {
        return signSymbol;
    }

    public int getValue() {
        return value;
    }

    public String toString() {
        return signSymbol.getTranslation() + value;
    }
}
