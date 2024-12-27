package compiler.ast.model.statements;

import compiler.ast.enums.EDataType;
import compiler.ast.model.expression.Expression;

public class Declaration {
    private final EDataType dataType;
    private final String identifier;
    private final Expression expression;

    public Declaration(EDataType dataType, String identifier, Expression expression) {
        this.dataType = dataType;
        this.identifier = identifier;
        this.expression = expression;
    }

    public EDataType getDataType() {
        return dataType;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Expression getExpression() {
        return expression;
    }

    public String toString() {
        return dataType + " " + identifier + (expression != null ? " = " + expression : "");
    }
}
