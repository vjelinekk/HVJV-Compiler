package compiler.ast.model.functions;

import compiler.ast.enums.EReturnType;

public class Function {
    private final EReturnType returnType;
    private final String identifier;
    private final Parameters parameters;
    private final FunctionBlock functionBlock;

    public Function(
            EReturnType returnType,
            String identifier,
            Parameters parameters,
            FunctionBlock functionBlock
    ) {
        this.returnType = returnType;
        this.identifier = identifier;
        this.parameters = parameters;
        this.functionBlock = functionBlock;
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

    public FunctionBlock getFunctionBlock() {
        return functionBlock;
    }

    public String toString() {
        String parameters = this.parameters == null ? "" : this.parameters.toString();

        return returnType + " " + identifier + "(" + parameters + ")" + " {\n" + functionBlock.toString() + "}\n";
    }
}
