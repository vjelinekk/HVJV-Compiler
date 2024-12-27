package compiler.ast.visitor;

import compiler.ast.model.expression.Expression;
import compiler.ast.model.statements.Assignment;
import grammar.HVJVGrammarBaseVisitor;
import grammar.HVJVGrammarParser;

public class AssignmentVisitor extends HVJVGrammarBaseVisitor<Assignment> {
    @Override
    public Assignment visitAssignment(HVJVGrammarParser.AssignmentContext ctx) {
        String identifier = ctx.identifier().getText();
        Expression expression = new ExpressionVisitor().visit(ctx.expression());

        return new Assignment(identifier, expression);
    }
}
