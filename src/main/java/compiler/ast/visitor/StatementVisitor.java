package compiler.ast.visitor;

import compiler.ast.model.statements.*;
import grammar.HVJVGrammarBaseVisitor;
import grammar.HVJVGrammarParser;

public class StatementVisitor extends HVJVGrammarBaseVisitor<Statement> {
    @Override
    public Statement visitStatementIf(HVJVGrammarParser.StatementIfContext ctx) {
        return new StatementIf(new IfStatementVisitor().visit(ctx.ifStatement()), ctx.getStart().getLine());
    }

    @Override
    public Statement visitStatementIfElse(HVJVGrammarParser.StatementIfElseContext ctx) {
        return new StatementIfElse(new IfElseStatementVisitor().visit(ctx.ifElseStatement()), ctx.getStart().getLine());
    }

    @Override
    public Statement visitStatementFor(HVJVGrammarParser.StatementForContext ctx) {
        return new StatementFor(new ForStatementVisitor().visit(ctx.forStatement()), ctx.getStart().getLine());
    }

    @Override
    public Statement visitStatementWhile(HVJVGrammarParser.StatementWhileContext ctx) {
        return new StatementWhile(new WhileStatementVisitor().visit(ctx.whileStatement()), ctx.getStart().getLine());
    }

    @Override
    public Statement visitStatementDeclaration(HVJVGrammarParser.StatementDeclarationContext ctx) {
        return new StatementDeclaration(new DeclarationVisitor().visit(ctx.declaration()), ctx.getStart().getLine());
    }

    @Override
    public Statement visitStatementAssignment(HVJVGrammarParser.StatementAssignmentContext ctx) {
        return new StatementAssignment(new AssignmentVisitor().visit(ctx.assignment()), ctx.getStart().getLine());
    }

    @Override
    public Statement visitStatementReturn(HVJVGrammarParser.StatementReturnContext ctx) {
        return new StatementReturn(ctx.getStart().getLine());
    }

    @Override
    public Statement visitStatementReturnExpression(HVJVGrammarParser.StatementReturnExpressionContext ctx) {
        return new StatementReturnExpression(new ExpressionVisitor().visit(ctx.expression()), ctx.getStart().getLine());
    }
}
