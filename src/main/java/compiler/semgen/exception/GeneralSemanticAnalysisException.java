package compiler.semgen.exception;

public class GeneralSemanticAnalysisException extends SemanticAnalysisException {
    private final int lineNumber;
    private final String functionName;

    public GeneralSemanticAnalysisException(String message, int lineNumber, String functionName) {
        super(message);
        this.lineNumber = lineNumber;
        this.functionName = functionName;
    }

    @Override
    public String getMessage() {
        return "Semantic error in function \"" + functionName + "\" at line " + lineNumber + ": " + super.getMessage();
    }
}
