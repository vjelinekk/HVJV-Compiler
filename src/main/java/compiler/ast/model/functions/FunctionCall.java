package compiler.ast.model.functions;

public class FunctionCall {
    private final String identifier;
    private final Arguments arguments;

    public FunctionCall(String identifier, Arguments arguments) {
        this.identifier = identifier;
        this.arguments = arguments;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Arguments getArguments() {
        return arguments;
    }

    public String toString() {
        String arguments = this.arguments == null ? "" : this.arguments.toString();

        return identifier + "(" + arguments + ");";
    }
}
