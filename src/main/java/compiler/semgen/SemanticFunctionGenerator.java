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
        // Add function to symbol table
        CodeBuilder.addInstruction(new Instruction(EInstruction.INT, 0, 3));
        getSymbolTable().getItem(getNode().getIdentifier()).setAddress(CodeBuilder.getLineNumber());
        ExceptionContext.setFunctionName(getNode().getIdentifier());

        getSymbolTable().enterScope(true, 0); // <<------ new scope here

        // Add parameters to symbol table
        int parametersCount = getNode().getParameters() != null ? getNode().getParameters().getParameters().size() : 0;
        for (int i = parametersCount; i > 0; i--) {
            CodeBuilder.addInstruction(new Instruction(EInstruction.LOD, 0, -i));

            getSymbolTable().addItem(new SymbolTableItem(
                    getNode().getParameters().getParameters().get(parametersCount - i).getIdentifier(),
                    0,
                    getSymbolTable().assignAddress(),
                    getNode().getParameters().getParameters().get(parametersCount - i).getDataType() == EDataType.INT ? ESymbolTableType.INT : ESymbolTableType.BOOL
            ));
        }

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
        Instruction lastInstruction = CodeBuilder.getLastInstructionAndRemove();
        getSymbolTable().exitScope();
        CodeBuilder.addInstruction(lastInstruction);

        boolean mustReturn = getNode().getReturnType() == EReturnType.BOOL || getNode().getReturnType() == EReturnType.INT;
        if (!mustReturn && lastInstruction.getInstruction() != EInstruction.RET) {
            CodeBuilder.addInstruction(new Instruction(EInstruction.RET, 0, 0));
        }
        if (mustReturn && lastInstruction.getInstruction() != EInstruction.RET) {
            throw new GeneralSemanticAnalysisException("Function must return a value", ExceptionContext.getLineNumber(), ExceptionContext.getFunctionName());
        }
    }
}
