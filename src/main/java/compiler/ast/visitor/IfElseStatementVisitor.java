package compiler.ast.visitor;

import compiler.ast.model.statements.IfElseStatement;
import grammar.HVJVGrammarBaseVisitor;
import grammar.HVJVGrammarParser;

public class IfElseStatementVisitor extends HVJVGrammarBaseVisitor<IfElseStatement> {
    @Override
    public IfElseStatement visitIfElseStatement(HVJVGrammarParser.IfElseStatementContext ctx) {
        return new IfElseStatement(new ExpressionVisitor().visit(ctx.expression()), new StatementsVisitor().visit(ctx.statements(0)), new StatementsVisitor().visit(ctx.statements(1)));
    }
}
