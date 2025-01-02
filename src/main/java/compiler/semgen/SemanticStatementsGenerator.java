package compiler.semgen;

import compiler.ast.model.statements.Statement;
import compiler.ast.model.statements.StatementDeclaration;
import compiler.ast.model.statements.Statements;
import compiler.semgen.enums.EInstruction;

public class SemanticStatementsGenerator extends BaseSemanticCodeGenerator<Statements>{
    public SemanticStatementsGenerator(Statements statements, SymbolTable symbolTable) {
        super(statements, symbolTable);
    }

    @Override
    public void run() {
        for (Statement statement : getNode().getStatements()) {
            switch (statement.getStatementType()) {
                case DECLARATION:
                    SemanticExpressionGenerator declarationAnalyzer = new SemanticExpressionGenerator(
                            ((StatementDeclaration)statement).getDeclaration().getExpression(),
                            getSymbolTable()
                    );

                    int res = declarationAnalyzer.evaluate(((StatementDeclaration)statement).getDeclaration().getExpression());
                    System.out.println("res: " + res);

                    CodeBuilder.addInstruction(new Instruction(EInstruction.LIT, 0, res));

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
