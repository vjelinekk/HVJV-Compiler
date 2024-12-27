package compiler.ast.visitor;

import compiler.ast.model.statements.WhileStatement;
import grammar.HVJVGrammarBaseVisitor;
import grammar.HVJVGrammarParser;

public class WhileStatementVisitor extends HVJVGrammarBaseVisitor<WhileStatement> {
    @Override
    public WhileStatement visitWhileStatement(HVJVGrammarParser.WhileStatementContext ctx) {
        return new WhileStatement(
                new ExpressionVisitor().visit(ctx.expression()),
                new StatementsVisitor().visit(ctx.statements())
        );
    }
}
