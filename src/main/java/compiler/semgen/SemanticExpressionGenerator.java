package compiler.semgen;

import compiler.ast.enums.EOperatorArithmetic;
import compiler.ast.enums.EOperatorLogical;
import compiler.ast.enums.EReturnType;
import compiler.ast.model.expression.*;
import compiler.semgen.enums.ESymbolTableType;

public class SemanticExpressionGenerator extends BaseSemanticCodeGenerator<Expression> {
    public SemanticExpressionGenerator(Expression expression, SymbolTable symbolTable) {
        super(expression, symbolTable);
    }

    @Override
    public void run() {
        return;
    }

    public int evaluate(Expression expression) {
        switch (expression.getExpressionType()) {
            case MULT_DIV:
                break;
            case PLUS_MINUS:
                int leftExpArithmetic = evaluate(((ExpPlusMinus)expression).getLeft());
                int rightExpArithmetic = evaluate(((ExpPlusMinus)expression).getRight());
                EOperatorArithmetic operatorArithmetic = ((ExpPlusMinus)expression).getOperator();
                return arithmeticExpressionEvaluate(leftExpArithmetic, rightExpArithmetic, operatorArithmetic);
            case AND_OR:
                break;
            case LOGICAL:
                int leftExpLogical = evaluate(((ExpLogical)expression).getLeft());
                int rightExpLogical = evaluate(((ExpLogical)expression).getRight());
                EOperatorLogical operatorLogical = ((ExpLogical)expression).getOperator();
                return logicalExpressionEvaluate(leftExpLogical, rightExpLogical, operatorLogical);
            case DECIMAL_VALUE:
                return ((ExpDecimalValue)expression).getDecimalValue().getValue();
            case BOOLEAN_VALUE:
                return ((ExpBooleanValue)expression).getBooleanValue().getValue();
            case IDENTIFIER:

                return getSymbolTable().getItem(((ExpIdentifier)expression).getIdentifier()).getAddress();
            case FUNCTION_CALL:
                break;
            case PARENTHESIS:
                break;
            case PLUS:
                return evaluate(((ExpMinus)expression).getExpression());
            case MINUS:
                return -evaluate(((ExpMinus)expression).getExpression());
            case NOT:

                break;

            default:
                break;
        }

        return 0;
    }

    int logicalExpressionEvaluate(int left, int right, EOperatorLogical operator) {
        switch (operator) {
            case AND:
                return (left * right) != 0 ? 1 : 0;
            case OR:
                return (left != 0 || right != 0) ? 1 : 0;
            case EQUAL:
                return left == right ? 1 : 0;
            case NOT_EQUAL:
                return left != right ? 1 : 0;
            case GREATER_THAN:
                return left > right ? 1 : 0;
            case LESS_THAN:
                return left < right ? 1 : 0;
            case GREATER_THAN_OR_EQUAL:
                return left >= right ? 1 : 0;
            case LESS_THAN_OR_EQUAL:
                return left <= right ? 1 : 0;
            default:
                return 0;
        }
    }

    int arithmeticExpressionEvaluate(int left, int right, EOperatorArithmetic operator) {
        switch (operator) {
            case PLUS:
                return left + right;
            case MINUS:
                return left - right;
            case MULT:
                return left * right;
            case DIV:
                return left / right;
            default:
                return 0;
        }
    }
}
