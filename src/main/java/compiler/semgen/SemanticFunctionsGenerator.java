package compiler.semgen;

import compiler.ast.model.functions.Function;
import compiler.ast.model.functions.Functions;

import java.util.List;

public class SemanticFunctionsGenerator extends BaseSemanticCodeGenerator<Functions> {

    public SemanticFunctionsGenerator(Functions functions, SymbolTable symbolTable) {
        super(functions, symbolTable);
    }

    public void run() {
        List<Function> functionList = getNode().getFunctions();
        for (Function function : functionList) {
            SymbolTableItem item = new SymbolTableItem(function.getIdentifier(), 0, 0);
            getSymbolTable().addItem(item);
            SemanticFunctionGenerator functionAnalyzer = new SemanticFunctionGenerator(function, getSymbolTable());
            functionAnalyzer.run();
        }
    }
}
