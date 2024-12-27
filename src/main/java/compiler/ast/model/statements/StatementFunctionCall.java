package compiler.ast.model.statements;

import compiler.ast.enums.EStatementType;
import compiler.ast.model.functions.FunctionCall;

public class StatementFunctionCall extends Statement {
    private final FunctionCall functionCall;

    public StatementFunctionCall(FunctionCall functionCall, int line) {
        super(EStatementType.FUNCTION_CALL, line);
        this.functionCall = functionCall;
    }

    public FunctionCall getFunctionCall() {
        return functionCall;
    }

    public String toString() {
        return functionCall.toString();
    }
}
