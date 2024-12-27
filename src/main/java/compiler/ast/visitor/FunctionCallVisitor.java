package compiler.ast.visitor;

import compiler.ast.model.functions.Arguments;
import compiler.ast.model.functions.FunctionCall;
import grammar.HVJVGrammarBaseVisitor;
import grammar.HVJVGrammarParser;

public class FunctionCallVisitor extends HVJVGrammarBaseVisitor<FunctionCall> {
    @Override
    public FunctionCall visitFunctionCall(HVJVGrammarParser.FunctionCallContext ctx) {
        String identifier = ctx.identifier().getText();
        Arguments arguments = null;
        if (ctx.arguments() != null) {
            arguments = new ArgumentsVisitor().visit(ctx.arguments());
        }

        return new FunctionCall(identifier, arguments);
    }
}
