package compiler.ast.visitor;

import compiler.ast.model.statements.IfStatement;
import grammar.HVJVGrammarBaseVisitor;
import grammar.HVJVGrammarParser;

public class IfStatementVisitor extends HVJVGrammarBaseVisitor<IfStatement> {
    @Override
    public IfStatement visitIfStatement(HVJVGrammarParser.IfStatementContext ctx) {
        return new IfStatement(new ExpressionVisitor().visit(ctx.expression()), new StatementsVisitor().visit(ctx.statements()));
    }
}
