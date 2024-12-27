package compiler.ast.visitor;

import compiler.ast.enums.ESignSymbol;
import compiler.ast.model.expression.DecimalValue;
import grammar.HVJVGrammarBaseVisitor;
import grammar.HVJVGrammarParser;

import java.util.Locale;

public class DecimalValueVisitor extends HVJVGrammarBaseVisitor<DecimalValue> {
    @Override
    public DecimalValue visitDecimalValue(HVJVGrammarParser.DecimalValueContext ctx) {
        ESignSymbol signSymbol = ESignSymbol.PLUS;
        if (ctx.signSymbol() != null) {
            signSymbol = ESignSymbol.getSymbol(ctx.signSymbol().getText().toLowerCase(Locale.ROOT));
        }
        int value = Integer.parseInt(ctx.DECIMAL().getText());

        return new DecimalValue(signSymbol, value);
    }
}
