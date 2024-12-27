package compiler.ast.model.statements;

import compiler.ast.enums.EStatementType;
import compiler.ast.model.flow.IfElseStatement;

public class StatementIfElse extends Statement {
    private final IfElseStatement ifElseStatement;

    public StatementIfElse(IfElseStatement ifElseStatement, int lineNumber) {
        super(EStatementType.IF_ELSE, lineNumber);
        this.ifElseStatement = ifElseStatement;
    }

    public IfElseStatement getIfElseStatement() {
        return ifElseStatement;
    }

    public String toString() {
        return ifElseStatement.toString();
    }
}
