package compiler.ast.visitor;

import compiler.ast.model.functions.Parameter;
import compiler.ast.model.functions.Parameters;
import grammar.HVJVGrammarBaseVisitor;
import grammar.HVJVGrammarParser;

import java.util.ArrayList;
import java.util.List;

public class ParametersVisitor extends HVJVGrammarBaseVisitor<Parameters> {
    @Override
    public Parameters visitParameters(HVJVGrammarParser.ParametersContext ctx) {
        List<Parameter> parameters = getParameters(ctx.parameter());

        return new Parameters(parameters);
    }

    private List<Parameter> getParameters(List<HVJVGrammarParser.ParameterContext> parameterContextList) {
        if (parameterContextList == null) {
            return null;
        }

        List<Parameter> parameters = new ArrayList<>();
        Parameter parameter;

        for (HVJVGrammarParser.ParameterContext parameterContext : parameterContextList) {
            parameter = new ParameterVisitor().visit(parameterContext);
            parameters.add(parameter);
        }

        return parameters;
    }
}
