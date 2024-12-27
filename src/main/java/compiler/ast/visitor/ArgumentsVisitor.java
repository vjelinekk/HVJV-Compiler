package compiler.ast.visitor;

import compiler.ast.model.expression.Expression;
import compiler.ast.model.functions.Arguments;
import grammar.HVJVGrammarBaseVisitor;
import grammar.HVJVGrammarParser;

import java.util.ArrayList;
import java.util.List;

public class ArgumentsVisitor extends HVJVGrammarBaseVisitor<Arguments> {
    @Override
    public Arguments visitArguments(HVJVGrammarParser.ArgumentsContext ctx) {
        List<Expression> arguments = getArguments(ctx);

        return new Arguments(arguments);
    }

    private List<Expression> getArguments(HVJVGrammarParser.ArgumentsContext argumentsContext) {
        if (argumentsContext == null) {
            return null;
        }

        List<Expression> arguments = new ArrayList<>();
        Expression argument;
        ExpressionVisitor expressionVisitor = new ExpressionVisitor();

        for (HVJVGrammarParser.ExpressionContext argumentContext : argumentsContext.expression()) {
            argument = expressionVisitor.visit(argumentContext);
            arguments.add(argument);
        }

        return arguments;
    }
}
