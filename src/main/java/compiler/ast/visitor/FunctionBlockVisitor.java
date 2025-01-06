package compiler.ast.visitor;

import compiler.ast.model.functions.FunctionBlock;
import grammar.HVJVGrammarBaseVisitor;
import grammar.HVJVGrammarParser;

public class FunctionBlockVisitor extends HVJVGrammarBaseVisitor<FunctionBlock> {
    @Override
    public FunctionBlock visitFunctionBlock(HVJVGrammarParser.FunctionBlockContext ctx) {
        if (ctx.statements() == null) {
            return null;
        }
        return new FunctionBlock(new StatementsVisitor().visit(ctx.statements()));
    }
}
