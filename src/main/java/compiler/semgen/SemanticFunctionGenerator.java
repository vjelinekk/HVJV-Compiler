package compiler.semgen;

import compiler.ast.enums.EDataType;
import compiler.ast.model.functions.Function;
import compiler.ast.model.statements.Statements;
import compiler.semgen.enums.EInstruction;
import compiler.semgen.enums.ESymbolTableType;

public class SemanticFunctionGenerator extends BaseSemanticCodeGenerator<Function> {
    public SemanticFunctionGenerator(Function function, SymbolTable symbolTable) {
        super(function, symbolTable);
    }

    @Override
    public void run() {
        // Add function to symbol table
        CodeBuilder.addInstruction(new Instruction(EInstruction.INT, 0, 3));
        getSymbolTable().getItem(getNode().getIdentifier()).setAddress(CodeBuilder.getLineNumber());

        getSymbolTable().enterScope(true); // <<------ new scope here

        // Add parameters to symbol table
        int parametersCount = getNode().getParameters().getParameters().size();
        for (int i = parametersCount; i > 0; i--) {
            CodeBuilder.addInstruction(new Instruction(EInstruction.LOD, 0, -i));

            getSymbolTable().addItem(new SymbolTableItem(
                    getNode().getParameters().getParameters().get(parametersCount - i).getIdentifier(),
                    0,
                    getSymbolTable().assignAddress(),
                    getNode().getParameters().getParameters().get(parametersCount - i).getDataType() == EDataType.INT ? ESymbolTableType.INT : ESymbolTableType.BOOL
            ));
        }

        Statements statements = getNode().getFunctionBlock().getStatements();
        SemanticStatementsGenerator statementsAnalyzer = new SemanticStatementsGenerator(statements, getSymbolTable());
        statementsAnalyzer.run();
        getSymbolTable().exitScope();
        CodeBuilder.addInstruction(new Instruction(EInstruction.RET, 0, 0));
    }
}
