package compiler.ast.visitor;

import compiler.ast.enums.EOperatorArithmetic;
import compiler.ast.enums.EOperatorLogical;
import compiler.ast.model.expression.*;
import grammar.HVJVGrammarBaseVisitor;
import grammar.HVJVGrammarParser;

public class ExpressionVisitor extends HVJVGrammarBaseVisitor<Expression> {
    @Override
    public Expression visitExpMultDiv(HVJVGrammarParser.ExpMultDivContext ctx) {
        return new ExpMultDiv(
                new ExpressionVisitor().visit(ctx.expression(0)),
                new ExpressionVisitor().visit(ctx.expression(1)),
                EOperatorArithmetic.getSymbol(ctx.op.getText())
        );
    }

    @Override
    public Expression visitExpPlusMinus(HVJVGrammarParser.ExpPlusMinusContext ctx) {
        return new ExpPlusMinus(
                new ExpressionVisitor().visit(ctx.expression(0)),
                new ExpressionVisitor().visit(ctx.expression(1)),
                EOperatorArithmetic.getSymbol(ctx.op.getText())
        );
    }

    @Override
    public Expression visitExpAndOr(HVJVGrammarParser.ExpAndOrContext ctx) {
        return new ExpAndOr(
                new ExpressionVisitor().visit(ctx.expression(0)),
                new ExpressionVisitor().visit(ctx.expression(1)),
                EOperatorLogical.getSymbol(ctx.op.getText())
        );
    }

    @Override
    public Expression visitExpLogical(HVJVGrammarParser.ExpLogicalContext ctx) {
        return new ExpLogical(
                new ExpressionVisitor().visit(ctx.expression(0)),
                new ExpressionVisitor().visit(ctx.expression(1)),
                EOperatorLogical.getSymbol(ctx.op.getText())
        );
    }

    @Override
    public Expression visitExpDecimalValue(HVJVGrammarParser.ExpDecimalValueContext ctx) {
        return new ExpDecimalValue(new DecimalValueVisitor().visit(ctx.decimalValue()));
    }

    @Override
    public Expression visitExpBooleanValue(HVJVGrammarParser.ExpBooleanValueContext ctx) {
        return new ExpBooleanValue(new BooleanValueVisitor().visit(ctx.booleanValue()));
    }

    @Override
    public Expression visitExpIdentifier(HVJVGrammarParser.ExpIdentifierContext ctx) {
        return new ExpIdentifier(ctx.identifier().getText());
    }

    @Override
    public Expression visitExpFunctionCall(HVJVGrammarParser.ExpFunctionCallContext ctx) {
        return new ExpFunctionCall(new FunctionCallVisitor().visit(ctx.functionCall()));
    }

    @Override
    public Expression visitExpParenthesis(HVJVGrammarParser.ExpParenthesisContext ctx) {
        return new ExpParenthesis(new ExpressionVisitor().visit(ctx.expression()));
    }

    @Override
    public Expression visitExpPlus(HVJVGrammarParser.ExpPlusContext ctx) {
        return new ExpPlus(new ExpressionVisitor().visit(ctx.expression()));
    }

    @Override
    public Expression visitExpMinus(HVJVGrammarParser.ExpMinusContext ctx) {
        return new ExpMinus(new ExpressionVisitor().visit(ctx.expression()));
    }

    @Override
    public Expression visitExpNot(HVJVGrammarParser.ExpNotContext ctx) {
        return new ExpNot(new ExpressionVisitor().visit(ctx.expression()));
    }
}
