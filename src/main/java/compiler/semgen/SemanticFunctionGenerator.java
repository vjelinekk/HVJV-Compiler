package compiler.semgen;

import compiler.ast.enums.EDataType;
import compiler.ast.enums.EReturnType;
import compiler.ast.model.functions.Function;
import compiler.ast.model.statements.Statements;
import compiler.semgen.enums.EInstruction;
import compiler.semgen.enums.ESymbolTableType;
import compiler.semgen.exception.ExceptionContext;
import compiler.semgen.exception.GeneralSemanticAnalysisException;
import compiler.semgen.exception.SemanticAnalysisException;
import compiler.semgen.symboltable.SymbolTable;
import compiler.semgen.symboltable.SymbolTableItem;

public class SemanticFunctionGenerator extends BaseSemanticCodeGenerator<Function> {
    public SemanticFunctionGenerator(Function function, SymbolTable symbolTable) {
        super(function, symbolTable);
    }

    @Override
    public void run() throws SemanticAnalysisException {
        int parametersCount = getNode().getParameters() != null ? getNode().getParameters().getParameters().size() : 0;
        functionSetUp(parametersCount);

        if (getNode().getFunctionBlock() == null) {
            getSymbolTable().exitScope();
            CodeBuilder.addInstruction(new Instruction(EInstruction.RET, 0, 0));
            return;
        }

        Statements statements = getNode().getFunctionBlock().getStatements();
        SemanticStatementsGenerator statementsAnalyzer = new SemanticStatementsGenerator(
                statements,
                getSymbolTable(),
                getNode().getReturnType(),
                -(parametersCount + 1)
        );
        statementsAnalyzer.run();
        Instruction lastInstruction = CodeBuilder.getLastInstruction();
        getSymbolTable().exitScope();

        boolean mustReturn = getNode().getReturnType() == EReturnType.BOOL || getNode().getReturnType() == EReturnType.INT;
        if (!mustReturn && lastInstruction.getInstruction() != EInstruction.RET) {
            CodeBuilder.addInstruction(new Instruction(EInstruction.RET, 0, 0));
        }
        getSymbolTable().getItem(getNode().getIdentifier()).setLastAddress(CodeBuilder.getLineNumber());

        if (mustReturn && lastInstruction.getInstruction() != EInstruction.RET) {
            throw new GeneralSemanticAnalysisException("Function must return a value", ExceptionContext.getLineNumber(), ExceptionContext.getFunctionName());
        }
    }

    private void functionSetUp(int parametersCount) throws SemanticAnalysisException {
        // Add function address into symbol table and initialize stack
        CodeBuilder.addInstruction(new Instruction(EInstruction.INT, 0, 3));
        getSymbolTable().getItem(getNode().getIdentifier()).setAddress(CodeBuilder.getLineNumber());
        ExceptionContext.setFunctionName(getNode().getIdentifier());

        //enter function scope
        getSymbolTable().enterScope(true, getNode().getFunctionBlock().getStatements().getVariablesCount());

        // Load parameters and add them into function scope
        for (int i = parametersCount; i > 0; i--) {
            CodeBuilder.addInstruction(new Instruction(EInstruction.LOD, 0, -i));

            getSymbolTable().addItem(new SymbolTableItem(
                    getNode().getParameters().getParameters().get(parametersCount - i).getIdentifier(),
                    getSymbolTable().assignAddress(),
                    getNode().getParameters().getParameters().get(parametersCount - i).getDataType() == EDataType.INT ? ESymbolTableType.INT : ESymbolTableType.BOOL
            ));
        }
    }
}
