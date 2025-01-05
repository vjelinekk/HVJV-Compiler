package compiler.semgen;

import compiler.ast.model.functions.Function;
import compiler.ast.model.functions.Functions;
import compiler.semgen.enums.ESymbolTableType;
import compiler.semgen.exception.SemanticAnalysisException;
import compiler.semgen.symboltable.SymbolTable;
import compiler.semgen.symboltable.SymbolTableItem;

import java.util.List;

public class SemanticFunctionsGenerator extends BaseSemanticCodeGenerator<Functions> {

    public SemanticFunctionsGenerator(Functions functions, SymbolTable symbolTable) {
        super(functions, symbolTable);
    }

    public void run() throws SemanticAnalysisException {
        List<Function> functionList = getNode().getFunctions();
        for (Function function : functionList) {
            SymbolTableItem item = new SymbolTableItem(
                    function.getIdentifier(),
                    0,
                    -1,
                    ESymbolTableType.FUNCTION
            );
            item.setReturnType(function.getReturnType());
            item.setParametersTypes(function.getParametersTypes());
            getSymbolTable().addItem(item);
        }

        for (Function function : functionList) {
            SemanticFunctionGenerator functionAnalyzer = new SemanticFunctionGenerator(function, getSymbolTable());
            functionAnalyzer.run();
        }

        CodeBuilder.mainAddress = getSymbolTable().getItem("main").getAddress();
    }
}
