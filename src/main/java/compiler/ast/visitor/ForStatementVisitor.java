package compiler.ast.visitor;

import compiler.ast.model.flow.ForStatement;
import grammar.HVJVGrammarBaseVisitor;
import grammar.HVJVGrammarParser;

public class ForStatementVisitor extends HVJVGrammarBaseVisitor<ForStatement> {
    @Override
    public ForStatement visitForStatement(HVJVGrammarParser.ForStatementContext ctx) {
        return new ForStatement(
                new DeclarationVisitor().visit(ctx.declaration()),
                new ExpressionVisitor().visit(ctx.expression()),
                new AssignmentVisitor().visit(ctx.assignment()),
                new StatementsVisitor().visit(ctx.statements())
        );
    }
}
