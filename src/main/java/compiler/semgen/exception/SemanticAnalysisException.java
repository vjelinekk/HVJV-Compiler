package compiler.semgen.exception;

public abstract class SemanticAnalysisException extends Exception {
    public SemanticAnalysisException(String message) {
        super(message);
    }

    @Override
    public abstract String toString();
}
