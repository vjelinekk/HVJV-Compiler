package compiler.semgen;

import compiler.ast.enums.EDataType;
import compiler.ast.enums.EReturnType;
import compiler.ast.model.expression.Expression;
import compiler.ast.model.flow.ForStatement;
import compiler.ast.model.flow.IfElseStatement;
import compiler.ast.model.flow.IfStatement;
import compiler.ast.model.flow.WhileStatement;
import compiler.ast.model.functions.FunctionCall;
import compiler.ast.model.statements.*;
import compiler.ast.model.variables.Assignment;
import compiler.ast.model.variables.Declaration;
import compiler.semgen.enums.EInstruction;
import compiler.semgen.enums.ESymbolTableType;

import java.util.List;

public class SemanticStatementsGenerator extends BaseSemanticCodeGenerator<Statements> {
    public SemanticStatementsGenerator(Statements statements, SymbolTable symbolTable) {
        super(statements, symbolTable);
    }

    @Override
    public void run() {
        for (Statement statement : getNode().getStatements()) {
            switch (statement.getStatementType()) {
                case DECLARATION:
                    createDeclaration(statement);
                    break;
                case IF:
                    IfStatement st = ((StatementIf) statement).getIfStatement();

                    SemanticExpressionGenerator.evaluate(st.getCondition(), getSymbolTable());
                    int placeholder = CodeBuilder.addPlaceholderInstruction(new Instruction(EInstruction.JMC, 0, 0));

                    getSymbolTable().enterScope(false);
                    SemanticStatementsGenerator generator = new SemanticStatementsGenerator(st.getStatements(), getSymbolTable());
                    generator.run();
                    getSymbolTable().exitScope();

                    CodeBuilder.getPlaceholderInstruction(placeholder).setArg2(CodeBuilder.getLineNumber());
                    break;
                case IF_ELSE:
                    IfElseStatement ifElseStatement = ((StatementIfElse) statement).getIfElseStatement();

                    SemanticExpressionGenerator.evaluate(ifElseStatement.getCondition(), getSymbolTable());
                    placeholder = CodeBuilder.addPlaceholderInstruction(new Instruction(EInstruction.JMC, 0, 0));

                    getSymbolTable().enterScope(false);
                    generator = new SemanticStatementsGenerator(ifElseStatement.getIfStatements(), getSymbolTable());
                    generator.run();
                    getSymbolTable().exitScope();
                    int elsePlaceholder = CodeBuilder.addPlaceholderInstruction(new Instruction(EInstruction.JMP, 0, 0));
                    CodeBuilder.getPlaceholderInstruction(placeholder).setArg2(CodeBuilder.getLineNumber() + 1);

                    getSymbolTable().enterScope(false);
                    generator = new SemanticStatementsGenerator(ifElseStatement.getElseStatements(), getSymbolTable());
                    generator.run();
                    getSymbolTable().exitScope();
                    CodeBuilder.getPlaceholderInstruction(elsePlaceholder).setArg2(CodeBuilder.getLineNumber() + 1);

                    break;
                case WHILE:
                    WhileStatement whileStatement = ((StatementWhile) statement).getWhileStatement();
                    break;
                case FOR:
                    ForStatement forStatement = ((StatementFor) statement).getForStatement();
                    createDeclaration(forStatement.getDeclaration());

                    break;
                case ASSIGNMENT:
                    Assignment as = ((StatementAssignment) statement).getAssignment();
                    SymbolTableItem item = getSymbolTable().getItem(as.getIdentifier());
                    EDataType type = SemanticExpressionGenerator.evaluate(as.getExpression(), getSymbolTable());

                    if (!item.getType().equals(type))
                        throw new RuntimeException("Trying to assign " + item.getType() + " into " + type + " variable");

                    CodeBuilder.addInstruction(new Instruction(EInstruction.STO, 0, item.getAddress()));
                    break;
                case FUNCTION_CALL:
                    FunctionCall call = ((StatementFunctionCall) statement).getFunctionCall();
                    SymbolTableItem fnc = getSymbolTable().getFromGlobalScope(call.getIdentifier());

                    List<Expression> arguments = call.getArguments().getArguments();
                    if (arguments.size() != fnc.getParametersTypes().size())
                        throw new RuntimeException(
                                "Arguments count mismatch, expected " + arguments.size() + " got " + fnc.getParametersTypes().size()
                        );

                    CodeBuilder.addInstruction(new Instruction(EInstruction.INT, 0, 1));

                    for (int j = 0; j < arguments.size(); j++) {
                        EDataType paramType = SemanticExpressionGenerator.evaluate(arguments.get(j), getSymbolTable());
                        if (paramType != fnc.getParametersTypes().get(j))
                            throw new RuntimeException("Arguments mismatch expected " + fnc.getParametersTypes().get(j) + " got " + paramType);
                    }
                    CodeBuilder.addInstruction(new Instruction(EInstruction.CAL, 0, fnc.getAddress()));
                    CodeBuilder.addInstruction(new Instruction(EInstruction.INT, 0, -arguments.size()));

                    if (fnc.getReturnType() != EReturnType.VOID)
                        CodeBuilder.addInstruction(new Instruction(EInstruction.INT, 0, -1));
                    break;
                case RETURN:
                    // Nothing to do here
                    break;
                case RETURN_EXPRESSION:
                    SemanticExpressionGenerator.evaluate(((StatementReturnExpression) statement).getExpression(), getSymbolTable());
                    break;
                case LABEL:
//                    getSymbolTable().getItem(((StatementLabel) statement).getLabel());
                    break;
                case GOTO:
                    break;
                default:
                    break;
            }
        }
    }

    private void createDeclaration(Declaration declaration) {
        StatementDeclaration statementDeclaration = (StatementDeclaration) statement;
        getSymbolTable().addItem(new SymbolTableItem(
                statementDeclaration.getDeclaration().getIdentifier(),
                0,
                getSymbolTable().assignAddress(),
                statementDeclaration.getDeclaration().getDataType()
                        == EDataType.INT ? ESymbolTableType.INT : ESymbolTableType.BOOL));

        EDataType type = SemanticExpressionGenerator.evaluate(
                statementDeclaration.getDeclaration().getExpression(),
                getSymbolTable()
        );
        if(statementDeclaration.getDeclaration().getDataType() != type) {
            throw new RuntimeException("Type mismatch: " + statementDeclaration.getDeclaration().getDataType() + " != " + type);
        }
    }
}
