package compiler.semgen;

import compiler.ast.enums.EDataType;
import compiler.ast.enums.EOperatorArithmetic;
import compiler.ast.enums.EOperatorLogical;
import compiler.ast.enums.EReturnType;
import compiler.ast.model.expression.*;
import compiler.ast.model.functions.FunctionCall;
import compiler.semgen.enums.EInstruction;
import compiler.semgen.enums.EOperator;
import compiler.semgen.enums.ESymbolTableType;
import compiler.semgen.exception.ExceptionContext;
import compiler.semgen.exception.GeneralSemanticAnalysisException;
import compiler.semgen.exception.SemanticAnalysisException;
import compiler.semgen.symboltable.SymbolTable;
import compiler.semgen.symboltable.SymbolTableItem;

import java.util.List;

public class SemanticExpressionEvaluator {
    public static EDataType evaluate(Expression expression, SymbolTable symbolTable) throws SemanticAnalysisException {
        EDataType left;
        EDataType right;
        int value;

        switch (expression.getExpressionType()) {
            case MULT_DIV:
                left = evaluate(((ExpMultDiv) expression).getLeft(), symbolTable);
                right = evaluate(((ExpMultDiv) expression).getRight(), symbolTable);

                if (left == EDataType.INT && right == EDataType.INT) {
                    EOperatorArithmetic operatorArithmetic = ((ExpMultDiv) expression).getOperator();
                    arithmeticExpressionEvaluate(operatorArithmetic);
                    return left;
                }
                else throw new GeneralSemanticAnalysisException("INT value expected, got BOOL", ExceptionContext.getLineNumber(), ExceptionContext.getFunctionName());
            case PLUS_MINUS:
                 left = evaluate(((ExpPlusMinus) expression).getLeft(), symbolTable);
                 right = evaluate(((ExpPlusMinus) expression).getRight(), symbolTable);

                if (left == EDataType.INT && right == EDataType.INT) {
                    EOperatorArithmetic operatorArithmetic = ((ExpPlusMinus) expression).getOperator();
                    arithmeticExpressionEvaluate(operatorArithmetic);
                    return left;
                }
                else throw new GeneralSemanticAnalysisException("INT value expected, got BOOL", ExceptionContext.getLineNumber(), ExceptionContext.getFunctionName());
            case AND_OR:
                    left = evaluate(((ExpAndOr) expression).getLeft(), symbolTable);
                    right = evaluate(((ExpAndOr) expression).getRight(), symbolTable);

                if (left == EDataType.BOOL && right == EDataType.BOOL) {
                    EOperatorLogical operatorLogical = ((ExpAndOr) expression).getOperator();
                    logicalExpressionEvaluate(operatorLogical);
                    return left;
                }
                else throw new GeneralSemanticAnalysisException("BOOL value expected, got INT", ExceptionContext.getLineNumber(), ExceptionContext.getFunctionName());
            case LOGICAL:
                left = evaluate(((ExpLogical) expression).getLeft(), symbolTable);
                right = evaluate(((ExpLogical) expression).getRight(), symbolTable);
                    EOperatorLogical operatorLogical = ((ExpLogical) expression).getOperator();
                    logicalExpressionEvaluate(operatorLogical);

                if (left == right) return EDataType.BOOL;
                else throw new GeneralSemanticAnalysisException("Logical operation mismatch: comparing: "
                        + left + " " + operatorLogical + " " + right,
                        ExceptionContext.getLineNumber(),
                        ExceptionContext.getFunctionName()
                );
            case DECIMAL_VALUE:
                value = ((ExpDecimalValue)expression).getDecimalValue().getValue();
                CodeBuilder.addInstruction( new Instruction(EInstruction.LIT,0, value));
                return EDataType.INT;
            case BOOLEAN_VALUE:
                value = ((ExpBooleanValue)expression).getBooleanValue().getValue();
                CodeBuilder.addInstruction( new Instruction(EInstruction.LIT,0, value));
                return EDataType.BOOL;
            case IDENTIFIER:
                SymbolTableItem i = symbolTable.getItem(((ExpIdentifier)expression).getIdentifier());
                CodeBuilder.addInstruction(new Instruction(EInstruction.LOD, i.getLevel(), i.getAddress()));

                if (i.getType() == ESymbolTableType.FUNCTION)
                    throw new GeneralSemanticAnalysisException("Value expected, got function", ExceptionContext.getLineNumber(), ExceptionContext.getFunctionName());

                return i.getType() == ESymbolTableType.BOOL ? EDataType.BOOL : EDataType.INT;
            case FUNCTION_CALL:
                FunctionCall call = ((ExpFunctionCall)expression).getFunctionCall();
                SymbolTableItem fnc = symbolTable.getFromGlobalScope(call.getIdentifier());
                List<Expression> arguments = call.getArguments().getArguments();
                if (arguments.size() != fnc.getParametersTypes().size())
                    throw new GeneralSemanticAnalysisException(
                            "Arguments count mismatch, expected " + arguments.size() + " got " + fnc.getParametersTypes().size(),
                            ExceptionContext.getLineNumber(),
                            ExceptionContext.getFunctionName()
                    );

                CodeBuilder.addInstruction(new Instruction(EInstruction.INT, 0, 1));

                for (int j = 0; j < arguments.size(); j++) {
                    EDataType paramType = evaluate(arguments.get(j), symbolTable);
                    if(paramType != fnc.getParametersTypes().get(j))
                        throw new GeneralSemanticAnalysisException(
                                "Arguments mismatch expected " + fnc.getParametersTypes().get(j) + " got " + paramType,
                                ExceptionContext.getLineNumber(),
                                ExceptionContext.getFunctionName()
                        );
                }
                CodeBuilder.addInstruction(new Instruction(EInstruction.CAL, 0, fnc.getAddress()));
                if (!arguments.isEmpty())
                    CodeBuilder.addInstruction(new Instruction(EInstruction.INT, 0, -arguments.size()));

                if(fnc.getReturnType() == EReturnType.VOID)
                    throw new GeneralSemanticAnalysisException("Trying to assign value of type VOID", ExceptionContext.getLineNumber(), ExceptionContext.getFunctionName());
                return fnc.getReturnType() == EReturnType.INT ? EDataType.INT : EDataType.BOOL;

            case PARENTHESIS:
                return evaluate(((ExpParenthesis)expression).getExpression(),symbolTable);
            case PLUS:
                if (evaluate(((ExpPlus)expression).getExpression(), symbolTable) == EDataType.INT)
                    return EDataType.INT;
                else throw new GeneralSemanticAnalysisException("INT value expected, got BOOL", ExceptionContext.getLineNumber(), ExceptionContext.getFunctionName());
            case MINUS:
                if (evaluate(((ExpPlus)expression).getExpression(), symbolTable) == EDataType.INT) {
                    CodeBuilder.addInstruction(new Instruction(EInstruction.OPR, 0, 1));
                    return EDataType.INT;
                }
                else throw new GeneralSemanticAnalysisException("INT value expected, got BOOL", ExceptionContext.getLineNumber(), ExceptionContext.getFunctionName());
            case NOT:
                if (evaluate(((ExpNot)expression).getExpression(), symbolTable) == EDataType.BOOL) {
                    CodeBuilder.addInstruction(new Instruction(EInstruction.LIT, 0, 0));
                    CodeBuilder.addInstruction(new Instruction(EInstruction.OPR, 0, 8));
                }
                else throw new GeneralSemanticAnalysisException("BOOL value expected, got INT", ExceptionContext.getLineNumber(), ExceptionContext.getFunctionName());
            default:
                break;
        }
        return EDataType.INT;
    }

    public static void logicalExpressionEvaluate (EOperatorLogical operator) {
        EOperator operation  = null;
        switch (operator) {
            case AND:
                operation = EOperator.MULTIPLY;
                break;
            case OR:
                CodeBuilder.addInstruction(new Instruction(EInstruction.OPR, 0, EOperator.PLUS.getValue()));
                CodeBuilder.addInstruction(new Instruction(EInstruction.LIT, 0, 1));
                operation = EOperator.GREATER_EQUAL;
                break;
            case EQUAL:
               operation = EOperator.EQUAL;
               break;
            case NOT_EQUAL:
                operation = EOperator.NOT_EQUAL;
                break;
            case GREATER_THAN:
                operation = EOperator.GREATER_THAN;
                break;
            case LESS_THAN:
                operation = EOperator.LESS_THAN;
                break;
            case GREATER_THAN_OR_EQUAL:
                operation = EOperator.GREATER_EQUAL;
                break;
            case LESS_THAN_OR_EQUAL:
                operation = EOperator.LESS_EQUAL;
                break;
        }

        CodeBuilder.addInstruction(new Instruction(EInstruction.OPR, 0, operation.getValue()));
    }

    public static void arithmeticExpressionEvaluate(EOperatorArithmetic operator) {
        EOperator operation = null;

        switch (operator) {
            case PLUS:
                operation = EOperator.PLUS;
                break;
            case MINUS:
                operation = EOperator.MINUS;
                break;
            case MULT:
                operation = EOperator.MULTIPLY;
                break;
            case DIV:
                operation = EOperator.DIVIDE;
                break;
        }

        CodeBuilder.addInstruction(new Instruction(EInstruction.OPR,0, operation.getValue()));
    }
}
