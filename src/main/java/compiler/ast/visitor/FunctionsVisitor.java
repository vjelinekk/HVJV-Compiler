package compiler.ast.visitor;

import compiler.ast.model.functions.Function;
import compiler.ast.model.functions.Functions;
import grammar.HVJVGrammarBaseVisitor;
import grammar.HVJVGrammarParser;

import java.util.ArrayList;
import java.util.List;

public class FunctionsVisitor extends HVJVGrammarBaseVisitor<Functions> {
    @Override
    public Functions visitFunctions(HVJVGrammarParser.FunctionsContext ctx) {
        List<Function> functions = getFunctions(ctx.function());
        return new Functions(functions);
    }

    private List<Function> getFunctions(List<HVJVGrammarParser.FunctionContext> functionContextList) {
        if (functionContextList == null) {
            return null;
        }

        List<Function> functions = new ArrayList<>();
        Function function;

        for (HVJVGrammarParser.FunctionContext functionContext : functionContextList) {
            function = new FunctionVisitor().visit(functionContext);
            functions.add(function);
        }

        return functions;
    }
}
