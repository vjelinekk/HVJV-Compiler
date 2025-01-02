package compiler.semgen;

import compiler.ast.model.functions.Function;
import compiler.ast.model.functions.Functions;
import compiler.semgen.enums.EInstruction;
import compiler.semgen.enums.ESymbolTableType;

import java.util.List;

public class SemanticFunctionsGenerator extends BaseSemanticCodeGenerator<Functions> {

    public SemanticFunctionsGenerator(Functions functions, SymbolTable symbolTable) {
        super(functions, symbolTable);
    }

    public void run() {
        List<Function> functionList = getNode().getFunctions();
        for (Function function : functionList) {
            SymbolTableItem item = new SymbolTableItem(
                    function.getIdentifier(),
                    getSymbolTable().getCurrentScope(),
                    -1,
                    ESymbolTableType.FUNCTION
            );
            item.setReturnType(function.getReturnType());
            getSymbolTable().addItem(item);
        }

        for (Function function : functionList) {
            SemanticFunctionGenerator functionAnalyzer = new SemanticFunctionGenerator(function, getSymbolTable());
            functionAnalyzer.run();
        }

        CodeBuilder.mainAddress = getSymbolTable().getItem("main").getAddress();
    }
}
