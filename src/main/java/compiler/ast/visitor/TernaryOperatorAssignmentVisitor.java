package compiler.ast.visitor;

import compiler.ast.model.expression.Expression;
import compiler.ast.model.statements.TernaryOperatorAssignment;
import grammar.HVJVGrammarBaseVisitor;
import grammar.HVJVGrammarParser;

public class TernaryOperatorAssignmentVisitor extends HVJVGrammarBaseVisitor<TernaryOperatorAssignment> {
    @Override
    public TernaryOperatorAssignment visitTernaryOperatorAssignment(HVJVGrammarParser.TernaryOperatorAssignmentContext ctx) {
        String identifier = ctx.identifier().getText();
        Expression condition = new ExpressionVisitor().visit(ctx.expression(0));
        Expression trueExpression = new ExpressionVisitor().visit(ctx.expression(1));
        Expression falseExpression = new ExpressionVisitor().visit(ctx.expression(2));

        return new TernaryOperatorAssignment(identifier, condition, trueExpression, falseExpression);
    }
}
