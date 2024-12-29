package compiler.semgen;

import compiler.ast.enums.EStatementType;
import compiler.ast.model.functions.Function;
import compiler.ast.model.statements.Statement;
import compiler.ast.model.statements.StatementDeclaration;
import compiler.ast.model.variables.Declaration;

import java.util.List;

public class SemanticFunctionGenerator extends BaseSemanticCodeGenerator<Function> {
    public SemanticFunctionGenerator(Function function, SymbolTable symbolTable) {
        super(function, symbolTable);
    }

    public void run() {
        
    }
}
