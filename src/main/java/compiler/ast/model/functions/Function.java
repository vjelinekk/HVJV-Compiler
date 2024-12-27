package compiler.ast.model.functions;

import compiler.ast.enums.EReturnType;

public class Function {
    private final EReturnType returnType;
    private final String identifier;
    private final Parameters parameters;

    public Function(EReturnType returnType, String identifier, Parameters parameters) {
        this.returnType = returnType;
        this.identifier = identifier;
        this.parameters = parameters;
    }

    public EReturnType getReturnType() {
        return returnType;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Parameters getParameters() {
        return parameters;
    }

    public String toString() {
        String parameters = this.parameters == null ? "" : this.parameters.toString();

        return returnType + " " + identifier + "(" + parameters + ")";
    }
}
