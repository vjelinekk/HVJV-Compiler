package compiler.semgen.exception;

public class MissingMainFunctionException extends SemanticAnalysisException {
    public MissingMainFunctionException() {
        super("Missing \"main\" function");
    }
}
