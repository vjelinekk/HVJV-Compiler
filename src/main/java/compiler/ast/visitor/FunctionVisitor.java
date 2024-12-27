package compiler.ast.visitor;

import compiler.ast.enums.EReturnType;
import compiler.ast.model.functions.Function;
import compiler.ast.model.functions.FunctionBlock;
import compiler.ast.model.functions.Parameters;
import compiler.ast.model.statements.Statements;
import grammar.HVJVGrammarBaseVisitor;
import grammar.HVJVGrammarParser;

import java.util.Locale;

public class FunctionVisitor extends HVJVGrammarBaseVisitor<Function> {
    @Override
    public Function visitFunction(HVJVGrammarParser.FunctionContext ctx) {
        EReturnType returnType = EReturnType.getSymbol(ctx.returnType().getText().toLowerCase(Locale.ROOT));
        String identifier = ctx.identifier().getText();
        Parameters parameters = null;
        if (ctx.parameters() != null) {
            parameters = new ParametersVisitor().visit(ctx.parameters());
        }
        FunctionBlock functionBlock = new FunctionBlockVisitor().visit(ctx.functionBlock());

        return new Function(returnType, identifier, parameters, functionBlock);
    }
}
