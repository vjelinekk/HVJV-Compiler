package compiler.semgen;

import compiler.ast.enums.EStatementType;
import compiler.ast.model.functions.Function;
import compiler.ast.model.statements.Statement;
import compiler.ast.model.statements.StatementDeclaration;
import compiler.ast.model.statements.Statements;
import compiler.ast.model.variables.Declaration;

import java.util.List;

public class SemanticFunctionGenerator extends BaseSemanticCodeGenerator<Function> {
    public SemanticFunctionGenerator(Function function, SymbolTable symbolTable) {
        super(function, symbolTable);
    }

    @Override
    public void run() {
        getSymbolTable().enterScope(); // <<------ new scope here

        Statements statements = getNode().getFunctionBlock().getStatements();
        SemanticStatementsGenerator statementsAnalyzer = new SemanticStatementsGenerator(statements, getSymbolTable());
        statementsAnalyzer.run();
    }
}
