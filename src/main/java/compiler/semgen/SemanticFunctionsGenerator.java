package compiler.semgen;

import compiler.ast.enums.EReturnType;
import compiler.ast.model.functions.Function;
import compiler.ast.model.functions.Functions;
import compiler.semgen.enums.ESymbolTableType;
import compiler.semgen.exception.InvalidMainSignatureException;
import compiler.semgen.exception.MissingMainFunctionException;
import compiler.semgen.exception.SemanticAnalysisException;
import compiler.semgen.symboltable.SymbolTable;
import compiler.semgen.symboltable.SymbolTableItem;

import java.util.List;

public class SemanticFunctionsGenerator extends BaseSemanticCodeGenerator<Functions> {

    public SemanticFunctionsGenerator(Functions functions, SymbolTable symbolTable) {
        super(functions, symbolTable);
    }

    public void run() throws SemanticAnalysisException {
        addFunctionsToSymbolTable();

        List<Function> functionList = getNode().getFunctions();
        for (Function function : functionList) {
            SemanticFunctionGenerator functionAnalyzer = new SemanticFunctionGenerator(function, getSymbolTable());
            functionAnalyzer.run();
        }

        CodeBuilder.insertMain( getSymbolTable().getItem("main").getAddress());
        CodeBuilder.analyzeFunctionsCode(getSymbolTable());
    }

    private void addFunctionsToSymbolTable() throws SemanticAnalysisException {
        List<Function> functionList = getNode().getFunctions();
        boolean hasMain = false;
        boolean isMainVoid = false;
        EReturnType mainReturnType = null;

        for (Function function : functionList) {
            if (function.getIdentifier().equals("main")) {
                hasMain = true;
                if (function.getReturnType() == EReturnType.VOID) {
                    isMainVoid = true;
                } else {
                    mainReturnType = function.getReturnType();
                }
            }

            SymbolTableItem item = new SymbolTableItem(
                    function.getIdentifier(), -1, ESymbolTableType.FUNCTION);
            item.setReturnType(function.getReturnType());
            item.setParametersTypes(function.getParametersTypes());
            getSymbolTable().addItem(item);
        }

        if (!hasMain) {
            throw new MissingMainFunctionException();
        }

        if (!isMainVoid) {
            throw new InvalidMainSignatureException(mainReturnType);
        }
    }
}
