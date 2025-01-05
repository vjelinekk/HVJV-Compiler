package compiler.ast.model.statements;

import compiler.ast.enums.EStatementType;

import java.util.List;

public class Statements {
    private final List<Statement> statements;

    public Statements(List<Statement> statements) {
        this.statements = statements;
    }

    public List<Statement> getStatements() {
        return statements;
    }

    public int getVariablesCount() {
        int count = 0;
        for (Statement statement : statements) {
            if (statement.getStatementType() == EStatementType.DECLARATION) {
                count++;
            }
        }

        return count;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Statement statement : statements) {
            sb.append("    ").append(statement.toString()).append("\n");
        }
        return sb.toString();
    }
}
