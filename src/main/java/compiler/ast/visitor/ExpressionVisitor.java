package compiler.ast.visitor;

import compiler.ast.model.expression.ExpDecimalValue;
import compiler.ast.model.expression.Expression;
import grammar.HVJVGrammarBaseVisitor;
import grammar.HVJVGrammarParser;

public class ExpressionVisitor extends HVJVGrammarBaseVisitor<Expression> {
    @Override
    public Expression visitExpDecimalValue(HVJVGrammarParser.ExpDecimalValueContext ctx) {
        return new ExpDecimalValue(new DecimalValueVisitor().visit(ctx.decimalValue()));
    }
}
