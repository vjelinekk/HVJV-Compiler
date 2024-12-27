package compiler.ast.visitor;

import compiler.ast.model.variables.BooleanValue;
import grammar.HVJVGrammarBaseVisitor;
import grammar.HVJVGrammarParser;

public class BooleanValueVisitor extends HVJVGrammarBaseVisitor<BooleanValue> {
    @Override
    public BooleanValue visitBooleanValue(HVJVGrammarParser.BooleanValueContext ctx) {
        return new BooleanValue(Boolean.parseBoolean(ctx.getText()));
    }
}
