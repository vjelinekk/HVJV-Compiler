package compiler.semgen;

import compiler.ast.enums.EDataType;
import compiler.ast.enums.EOperatorArithmetic;
import compiler.ast.enums.EOperatorLogical;
import compiler.ast.model.expression.*;
import compiler.semgen.enums.EInstruction;
import compiler.semgen.enums.ESymbolTableType;

public class SemanticExpressionGenerator {

    public static void evaluate(Expression expression, SymbolTable symbolTable, EDataType dataType) {
        switch (expression.getExpressionType()) {
            case MULT_DIV:
                if (dataType == EDataType.INT) {
                    evaluate(((ExpMultDiv) expression).getLeft(), symbolTable, EDataType.INT);
                    evaluate(((ExpMultDiv) expression).getRight(), symbolTable, EDataType.INT);
                    EOperatorArithmetic operatorArithmetic = ((ExpMultDiv) expression).getOperator();
                    arithmeticExpressionEvaluate(operatorArithmetic);
                }
                else
                    throw new RuntimeException("int value expected, got bool");
                break;
            case PLUS_MINUS:
                if(dataType == EDataType.INT) {
                    evaluate(((ExpPlusMinus) expression).getLeft(), symbolTable, EDataType.INT);
                    evaluate(((ExpPlusMinus) expression).getRight(), symbolTable, EDataType.INT);
                    EOperatorArithmetic operatorArithmetic = ((ExpPlusMinus) expression).getOperator();
                    arithmeticExpressionEvaluate(operatorArithmetic);
                }
                else
                    throw new RuntimeException("int value expected, got bool");
                break;
            case AND_OR:
                if (dataType == EDataType.BOOL) {
                    evaluate(((ExpAndOr) expression).getLeft(), symbolTable, EDataType.BOOL);
                    evaluate(((ExpAndOr) expression).getRight(), symbolTable, EDataType.BOOL);
                    EOperatorLogical operatorLogical = ((ExpAndOr) expression).getOperator();
                    logicalExpressionEvaluate(operatorLogical);
                }
                else
                    throw new RuntimeException("bool value expected, got int");
                break;
            case LOGICAL:
                if (dataType == EDataType.BOOL) {
                    evaluate(((ExpLogical) expression).getLeft(), symbolTable, EDataType.BOOL);
                    evaluate(((ExpLogical) expression).getRight(), symbolTable, EDataType.BOOL);
                    EOperatorLogical operatorLogical = ((ExpLogical) expression).getOperator();
                    logicalExpressionEvaluate(operatorLogical);
                }
                else if (dataType == EDataType.INT) {
                    evaluate(((ExpLogical) expression).getLeft(), symbolTable, EDataType.INT);
                    evaluate(((ExpLogical) expression).getRight(), symbolTable, EDataType.INT);
                    EOperatorLogical operatorLogical = ((ExpLogical) expression).getOperator();
                    logicalExpressionEvaluate(operatorLogical);
            }
                else
                    throw new RuntimeException("value expected, got function");
                break;
            case DECIMAL_VALUE:
                if(dataType == EDataType.INT) {
                    int value = ((ExpDecimalValue)expression).getDecimalValue().getValue();
                    CodeBuilder.addInstruction( new Instruction(EInstruction.LIT,0, value));
                }
                else
                    throw new RuntimeException("int value expected, got bool");
                break;
            case BOOLEAN_VALUE:
                if(dataType == EDataType.BOOL) {
                    int value = ((ExpBooleanValue)expression).getBooleanValue().getValue();
                    CodeBuilder.addInstruction( new Instruction(EInstruction.LIT,0, value));
                }
                else
                    throw new RuntimeException("bool value expected, got int");
                break;
            case IDENTIFIER:
                SymbolTableItem i = symbolTable.getItem(((ExpIdentifier)expression).getIdentifier());
                if(true) {
                    CodeBuilder.addInstruction(new Instruction(EInstruction.LOD, i.getLevel(), i.getAddress()));
                }
                else
                    throw new RuntimeException("identifier expected, got function");
                break;
            case FUNCTION_CALL:
                break;
            case PARENTHESIS:
                evaluate(((ExpParenthesis)expression).getExpression(),symbolTable,dataType);
                break;
            case PLUS:
                if(dataType == EDataType.INT) {
                    evaluate(((ExpPlus)expression).getExpression(), symbolTable, EDataType.INT);
                }
                else
                    throw new RuntimeException("int value expected, got bool");
                break;
            case MINUS:
                if(dataType == EDataType.INT) {
                    evaluate(((ExpMinus)expression).getExpression(), symbolTable, EDataType.INT);
                    CodeBuilder.addInstruction(new Instruction(EInstruction.OPR, 0, 1));
                }
                else
                    throw new RuntimeException("int value expected, got bool");
                break;
            case NOT:
                if(dataType == EDataType.BOOL) {
                    evaluate(((ExpNot)expression).getExpression(), symbolTable, EDataType.BOOL);
                    CodeBuilder.addInstruction(new Instruction(EInstruction.LIT,0, 0));
                    CodeBuilder.addInstruction(new Instruction(EInstruction.OPR, 0, 8));
                }
                else
                    throw new RuntimeException("bool value expected, got int");
                break;
            default:
                break;
        }
    }

    public static void logicalExpressionEvaluate (EOperatorLogical operator) {
        int operation  = 0;
        switch (operator) {
            case AND:
                CodeBuilder.addInstruction( new Instruction(EInstruction.OPR, 0, 4));
                CodeBuilder.addInstruction( new Instruction(EInstruction.LIT, 0, 1));
                operation = 8;
                break;
            case OR:
                CodeBuilder.addInstruction( new Instruction(EInstruction.OPR, 0, 2));
                CodeBuilder.addInstruction( new Instruction(EInstruction.LIT, 0, 1));
                operation = 11;
                break;
            case EQUAL:
               operation = 8;
               break;
            case NOT_EQUAL:
                operation = 9;
                break;
            case GREATER_THAN:
                operation = 12;
                break;
            case LESS_THAN:
                operation = 10;
                break;
            case GREATER_THAN_OR_EQUAL:
                operation = 11;
                break;
            case LESS_THAN_OR_EQUAL:
                operation = 13;
                break;
        }

        CodeBuilder.addInstruction(new Instruction(EInstruction.OPR, 0, operation));
    }

    private static void arithmeticExpressionEvaluate(EOperatorArithmetic operator) {
        int operation = 0;

        switch (operator) {
            case PLUS:
                operation = 2;
                break;
            case MINUS:
                operation = 3;
                break;
            case MULT:
                operation = 4;
                break;
            case DIV:
                operation = 5;
                break;
        }

        CodeBuilder.addInstruction(new Instruction(EInstruction.OPR,0,operation));
    }
}
