package compiler.ast.model.functions;

import compiler.ast.enums.EDataType;

public class Parameter {
    private final EDataType dataType;
    private final String identifier;

    public Parameter(EDataType dataType, String identifier) {
        this.dataType = dataType;
        this.identifier = identifier;
    }

    public EDataType getDataType() {
        return dataType;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String toString() {
        return dataType + " " + identifier;
    }
}
