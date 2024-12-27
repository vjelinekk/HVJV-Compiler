package compiler.ast.model.statements;

import compiler.ast.enums.EStatementType;

public class StatementDeclaration extends Statement {
    private final Declaration declaration;

    public StatementDeclaration(Declaration declaration, int lineNumber) {
        super(EStatementType.WHILE, lineNumber);
        this.declaration = declaration;
    }

    public Declaration getDeclaration() {
        return declaration;
    }

    public String toString() {
        return declaration.toString();
    }
}
