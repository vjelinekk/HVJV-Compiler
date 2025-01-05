package compiler.semgen.exception;

public class SemanticAnalysisException extends Exception {
    private final int lineNumber;
    private final String functionName;

    public SemanticAnalysisException(String message, int lineNumber, String functionName) {
        super(message);
        this.lineNumber = lineNumber;
        this.functionName = functionName;
    }

    @Override
    public String toString() {
        return "Semantic error in function \"" + functionName + "\" at line " + lineNumber + ": " + getMessage();
    }
}
