package compiler.ast.visitor;

import compiler.ast.model.statements.Statement;
import compiler.ast.model.statements.Statements;
import grammar.HVJVGrammarBaseVisitor;
import grammar.HVJVGrammarParser;

import java.util.ArrayList;
import java.util.List;

public class StatementsVisitor extends HVJVGrammarBaseVisitor<Statements> {
    @Override
    public Statements visitStatements(HVJVGrammarParser.StatementsContext ctx) {
        List<Statement> statements = getStatements(ctx.statement());
        return new Statements(statements);
    }

    private List<Statement> getStatements(List<HVJVGrammarParser.StatementContext> statementContextList) {
        if (statementContextList == null) {
            return null;
        }

        List<Statement> statements = new ArrayList<>();
        Statement statement;

        for (HVJVGrammarParser.StatementContext statementContext : statementContextList) {
            statement = new StatementVisitor().visit(statementContext);
            statements.add(statement);
        }

        return statements;
    }
}
