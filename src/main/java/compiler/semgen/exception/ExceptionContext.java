package compiler.semgen.exception;

public class ExceptionContext {
    private static int lineNumber = -1;
    private static String functionName = null;

    public static void setLineNumber(int lineNumber) {
        ExceptionContext.lineNumber = lineNumber;
    }

    public static void setFunctionName(String functionName) {
        ExceptionContext.functionName = functionName;
    }

    public static int getLineNumber() {
        return lineNumber;
    }

    public static String getFunctionName() {
        return functionName;
    }
}
