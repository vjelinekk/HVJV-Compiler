package compiler.semgen.exception;

import compiler.ast.enums.EReturnType;

public class InvalidMainSignatureException extends SemanticAnalysisException {
    public InvalidMainSignatureException(EReturnType returnType) {
        super("Invalid \"main\" function signature (expected \"VOID main()\", found \"" + returnType + " main()\")");
    }
}
