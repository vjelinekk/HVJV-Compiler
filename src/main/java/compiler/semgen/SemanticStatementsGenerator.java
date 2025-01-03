package compiler.semgen;

import compiler.ast.enums.EDataType;
import compiler.ast.model.statements.Statement;
import compiler.ast.model.statements.StatementDeclaration;
import compiler.ast.model.statements.Statements;
import compiler.semgen.enums.EInstruction;
import compiler.semgen.enums.ESymbolTableType;

public class SemanticStatementsGenerator extends BaseSemanticCodeGenerator<Statements>{
    public SemanticStatementsGenerator(Statements statements, SymbolTable symbolTable) {
        super(statements, symbolTable);
    }

    @Override
    public void run() {
        for (Statement statement : getNode().getStatements()) {
            switch (statement.getStatementType()) {
                case DECLARATION:
                    StatementDeclaration statementDeclaration = (StatementDeclaration) statement;
                    getSymbolTable().addItem(new SymbolTableItem(
                            statementDeclaration.getDeclaration().getIdentifier(),
                            0, getSymbolTable().assignAddress(),
                            statementDeclaration.getDeclaration().getDataType()
                            == EDataType.INT ? ESymbolTableType.INT : ESymbolTableType.BOOL));

                    SemanticExpressionGenerator.evaluate(statementDeclaration.getDeclaration().getExpression(),
                                                         getSymbolTable(),
                                                         statementDeclaration.getDeclaration().getDataType());
                    break;
                case IF:
//                    SemanticIfGenerator ifAnalyzer = new SemanticIfGenerator(statement, getSymbolTable());
//                    ifAnalyzer.run();
                    break;
                case WHILE:
//                    SemanticWhileGenerator whileAnalyzer = new SemanticWhileGenerator(statement, getSymbolTable());
//                    whileAnalyzer.run();
                    break;
                case FOR:
//                    SemanticForGenerator forAnalyzer = new SemanticForGenerator(statement, getSymbolTable());
//                    forAnalyzer.run();
                    break;
                case ASSIGNMENT:
//                    SemanticAssignmentGenerator assignmentAnalyzer = new SemanticAssignmentGenerator(statement, getSymbolTable());
//                    assignmentAnalyzer.run();
                    break;
                case FUNCTION_CALL:
//                    SemanticFunctionCallGenerator functionCallAnalyzer = new SemanticFunctionCallGenerator(statement, getSymbolTable());
//                    functionCallAnalyzer.run();
                    break;
                case RETURN:
//                    SemanticReturnGenerator returnAnalyzer = new SemanticReturnGenerator(statement, getSymbolTable());
//                    returnAnalyzer.run();
                    break;
                case RETURN_EXPRESSION:
//                    SemanticReturnExpressionGenerator returnExpressionAnalyzer = new SemanticReturnExpressionGenerator(statement, getSymbolTable());
                    break;
                case LABEL:
//                    SemanticLabelGenerator labelAnalyzer = new SemanticLabelGenerator(statement, getSymbolTable());
//                    labelAnalyzer.run();
                    break;
                case GOTO:
//                    SemanticGotoGenerator gotoAnalyzer = new SemanticGotoGenerator(statement, getSymbolTable());
//                    gotoAnalyzer.run();
                    break;
                default:
                    break;
            }

        }
    }
}
