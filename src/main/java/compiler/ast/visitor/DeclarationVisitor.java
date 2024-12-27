package compiler.ast.visitor;

import compiler.ast.enums.EDataType;
import compiler.ast.model.expression.Expression;
import compiler.ast.model.variables.Declaration;
import grammar.HVJVGrammarBaseVisitor;
import grammar.HVJVGrammarParser;

import java.util.Locale;

public class DeclarationVisitor extends HVJVGrammarBaseVisitor<Declaration> {
    @Override
    public Declaration visitDeclaration(HVJVGrammarParser.DeclarationContext ctx) {
        EDataType dataType = EDataType.getSymbol(ctx.dataType().getText().toLowerCase(Locale.ROOT));
        String identifier = ctx.identifier().getText();
        Expression expression = new ExpressionVisitor().visit(ctx.expression());

        return new Declaration(dataType, identifier, expression);
    }
}
