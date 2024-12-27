package compiler.ast.visitor;

import compiler.ast.model.statements.TernaryOperatorExpression;
import grammar.HVJVGrammarBaseVisitor;
import grammar.HVJVGrammarParser;

public class TernaryOperatorExpressionVisitor extends HVJVGrammarBaseVisitor<TernaryOperatorExpression> {
    @Override
    public TernaryOperatorExpression visitTernaryOperatorExpression(HVJVGrammarParser.TernaryOperatorExpressionContext ctx) {
        return new TernaryOperatorExpression(
                new ExpressionVisitor().visit(ctx.expression(0)),
                new ExpressionVisitor().visit(ctx.expression(1)),
                new ExpressionVisitor().visit(ctx.expression(2))
        );
    }
}
