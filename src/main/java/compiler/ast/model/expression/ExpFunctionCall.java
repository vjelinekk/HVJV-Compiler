package compiler.ast.model.expression;

import compiler.ast.enums.EExpressionType;
import compiler.ast.model.functions.FunctionCall;

public class ExpFunctionCall extends Expression {
    private final FunctionCall functionCall;

    public ExpFunctionCall(FunctionCall functionCall) {
        super(EExpressionType.FUNCTION_CALL);
        this.functionCall = functionCall;
    }

    public FunctionCall getFunctionCall() {
        return functionCall;
    }

    public String toString() {
        return functionCall.toString();
    }
}
