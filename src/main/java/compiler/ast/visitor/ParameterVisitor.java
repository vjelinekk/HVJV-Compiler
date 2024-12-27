package compiler.ast.visitor;

import compiler.ast.enums.EDataType;
import compiler.ast.model.functions.Parameter;
import grammar.HVJVGrammarBaseVisitor;
import grammar.HVJVGrammarParser;

public class ParameterVisitor extends HVJVGrammarBaseVisitor<Parameter> {
    @Override
    public Parameter visitParameter(HVJVGrammarParser.ParameterContext ctx) {
        EDataType returnType = EDataType.getSymbol(ctx.dataType().getText());
        String identifier = ctx.identifier().getText();
        return new Parameter(returnType, identifier);
    }
}
