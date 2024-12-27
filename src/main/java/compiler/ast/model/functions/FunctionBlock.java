package compiler.ast.model.functions;

import compiler.ast.model.statements.Statements;

public class FunctionBlock {
    private final Statements statements;

    public FunctionBlock(Statements statements) {
        this.statements = statements;
    }


    public Statements getStatements() {
        return statements;
    }

    public String toString() {
        return statements.toString();
    }
}
