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
    public Statement visitStatementTernaryOperatorAssignment(HVJVGrammarParser.StatementTernaryOperatorAssignmentContext ctx) {
        return new StatementTernaryOperatorAssignment(
                new TernaryOperatorAssignmentVisitor().visit(ctx.ternaryOperatorAssignment()),
                ctx.getStart().getLine()
        );
    }

    @Override
    public Statement visitStatementTernaryOperatorExpression(HVJVGrammarParser.StatementTernaryOperatorExpressionContext ctx) {
        return new StatementTernaryOperatorExpression(
                new TernaryOperatorExpressionVisitor().visit(ctx.ternaryOperatorExpression()),
                ctx.getStart().getLine()
        );
    }

    @Override
    public Statement visitStatementGoto(HVJVGrammarParser.StatementGotoContext ctx) {
        return new StatementGoto(ctx.label().getText(), ctx.getStart().getLine());
    }

    @Override
    public Statement visitStatementLabel(HVJVGrammarParser.StatementLabelContext ctx) {
        return new StatementLabel(ctx.label().getText(), ctx.getStart().getLine());
    }

    @Override
    public Statement visitStatementFunctionCall(HVJVGrammarParser.StatementFunctionCallContext ctx) {
        return new StatementFunctionCall(new FunctionCallVisitor().visit(ctx.functionCall()), ctx.getStart().getLine());
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
