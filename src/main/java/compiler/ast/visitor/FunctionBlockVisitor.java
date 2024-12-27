package compiler.ast.visitor;

import compiler.ast.model.functions.FunctionBlock;
import grammar.HVJVGrammarBaseVisitor;
import grammar.HVJVGrammarParser;

public class FunctionBlockVisitor extends HVJVGrammarBaseVisitor<FunctionBlock> {
    @Override
    public FunctionBlock visitFunctionBlock(HVJVGrammarParser.FunctionBlockContext ctx) {
        return new FunctionBlock(new StatementsVisitor().visit(ctx.statements()));
    }
}
