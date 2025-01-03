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
    private final EReturnType returnType;
    private final int returnAddress;

    public SemanticStatementsGenerator(Statements statements, SymbolTable symbolTable, EReturnType returnType, int returnAddress) {
        super(statements, symbolTable);
        this.returnType = returnType;
        if (returnType != EReturnType.VOID)
            this.returnAddress = returnAddress;
        else
            this.returnAddress = 0;
    }

    @Override
    public void run() {
        for (Statement statement : getNode().getStatements()) {
            switch (statement.getStatementType()) {
                case DECLARATION:
                    createDeclaration(((StatementDeclaration)statement).getDeclaration());
                    break;
                case IF:
                    IfStatement st = ((StatementIf) statement).getIfStatement();

                    SemanticExpressionGenerator.evaluate(st.getCondition(), getSymbolTable());
                    int placeholder = CodeBuilder.addPlaceholderInstruction(new Instruction(EInstruction.JMC, 0, 0));

                    getSymbolTable().enterScope(false);
                    SemanticStatementsGenerator generator = new SemanticStatementsGenerator(st.getStatements(), getSymbolTable(), returnType, returnAddress);
                    generator.run();
                    getSymbolTable().exitScope();

                    CodeBuilder.getPlaceholderInstruction(placeholder).setArg2(CodeBuilder.getLineNumber() + 1);
                    break;
                case IF_ELSE:
                    IfElseStatement ifElseStatement = ((StatementIfElse) statement).getIfElseStatement();

                    SemanticExpressionGenerator.evaluate(ifElseStatement.getCondition(), getSymbolTable());
                    int ifPlaceholder = CodeBuilder.addPlaceholderInstruction(new Instruction(EInstruction.JMC, 0, 0));

                    getSymbolTable().enterScope(false);
                    generator = new SemanticStatementsGenerator(ifElseStatement.getIfStatements(), getSymbolTable(), returnType, returnAddress);
                    generator.run();
                    getSymbolTable().exitScope();
                    int elsePlaceholder = CodeBuilder.addPlaceholderInstruction(new Instruction(EInstruction.JMP, 0, 0));
                    CodeBuilder.getPlaceholderInstruction(ifPlaceholder).setArg2(CodeBuilder.getLineNumber() + 1);

                    getSymbolTable().enterScope(false);
                    generator = new SemanticStatementsGenerator(ifElseStatement.getElseStatements(), getSymbolTable(), returnType, returnAddress);
                    generator.run();
                    getSymbolTable().exitScope();
                    CodeBuilder.getPlaceholderInstruction(elsePlaceholder).setArg2(CodeBuilder.getLineNumber() + 1);

                    break;
                case WHILE:
                    WhileStatement whileStatement = ((StatementWhile) statement).getWhileStatement();
                    getSymbolTable().enterScope(false);

                    int whileStatementStart = CodeBuilder.getLineNumber() + 1;
                    SemanticExpressionGenerator.evaluate(whileStatement.getExpression(), getSymbolTable());
                    int whilePlaceholderEnd = CodeBuilder.addPlaceholderInstruction(new Instruction(EInstruction.JMC, 0, 0));
                    generator = new SemanticStatementsGenerator(whileStatement.getStatements(), getSymbolTable(), returnType, returnAddress);
                    generator.run();
                    getSymbolTable().exitScope();
                    CodeBuilder.addInstruction(new Instruction(EInstruction.JMP, 0, whileStatementStart));
                    CodeBuilder.getPlaceholderInstruction(whilePlaceholderEnd).setArg2(CodeBuilder.getLineNumber() + 1);
                    break;
                case FOR:
                    ForStatement forStatement = ((StatementFor) statement).getForStatement();
                    getSymbolTable().enterScope(false);

                    createDeclaration(forStatement.getDeclaration());
                    int forStatementStart = CodeBuilder.getLineNumber() + 1;
                    SemanticExpressionGenerator.evaluate(forStatement.getCondition(), getSymbolTable());
                    int forPlaceholderEnd = CodeBuilder.addPlaceholderInstruction(new Instruction(EInstruction.JMC, 0, 0));
                    generator = new SemanticStatementsGenerator(forStatement.getStatements(), getSymbolTable(), returnType, returnAddress);
                    generator.run();
                    createAssignment(forStatement.getAssignment());
                    getSymbolTable().exitScope();
                    CodeBuilder.addInstruction(new Instruction(EInstruction.JMP, 0, forStatementStart));
                    CodeBuilder.getPlaceholderInstruction(forPlaceholderEnd).setArg2(CodeBuilder.getLineNumber() + 1);
                    break;
                case ASSIGNMENT:
                    Assignment as = ((StatementAssignment) statement).getAssignment();
                    createAssignment(as);
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
                    if (returnType != EReturnType.VOID)
                        throw new RuntimeException("Trying to return void from NON-VOID function");

                    CodeBuilder.addInstruction(new Instruction(EInstruction.RET, 0, 0));
                    break;
                case RETURN_EXPRESSION:
                    if (returnType == EReturnType.VOID)
                        throw new RuntimeException("Trying to return value from VOID function");

                    EDataType type = SemanticExpressionGenerator.evaluate(((StatementReturnExpression) statement).getExpression(), getSymbolTable());
                    EDataType returnDataType = returnType == EReturnType.INT ? EDataType.INT : EDataType.BOOL;
                    if (type != returnDataType)
                        throw new RuntimeException("Type mismatch trying to return: " + type + " from " + returnType + " function");

                    CodeBuilder.addInstruction(new Instruction(EInstruction.STO, 0, returnAddress));
                    CodeBuilder.addInstruction(new Instruction(EInstruction.RET, 0, 0));
                    break;
                case LABEL:
                    StatementLabel label = ((StatementLabel) statement);
                    getSymbolTable().addGotoLabel(label.getLabel(), CodeBuilder.getLineNumber() + 1);
                    break;
                case GOTO:
                    StatementGoto gotoStatement = ((StatementGoto) statement);
                    CodeBuilder.addInstruction(new Instruction(EInstruction.JMP, 0, getSymbolTable().getGotoLabelAddress(gotoStatement.getLabel())));
                    break;
                case TERNARY_ASSIGNMENT:
                    TernaryOperatorAssignment ternary = ((StatementTernaryOperatorAssignment) statement).getTernaryOperatorAssignment();
                    Expression condition = ternary.getCondition();
                    SemanticExpressionGenerator.evaluate(condition, getSymbolTable());
                    int ternaryPlaceholder = CodeBuilder.addPlaceholderInstruction(new Instruction(EInstruction.JMC, 0, 0));

                    SymbolTableItem item = getSymbolTable().getItem(ternary.getIdentifier());
                    Expression trueExpression = ternary.getTrueExpression();
                    EDataType trueType = SemanticExpressionGenerator.evaluate(trueExpression, getSymbolTable());
                    if (!item.getType().equals(trueType))
                        throw new RuntimeException("Trying to assign " + item.getType() + " into " + trueType + " variable");
                    CodeBuilder.addInstruction(new Instruction(EInstruction.STO, 0, item.getAddress()));
                    int ternaryPlaceholderEnd = CodeBuilder.addPlaceholderInstruction(new Instruction(EInstruction.JMP, 0, 0));
                    CodeBuilder.getPlaceholderInstruction(ternaryPlaceholder).setArg2(CodeBuilder.getLineNumber() + 1);

                    Expression falseExpression = ternary.getFalseExpression();
                    EDataType falseType = SemanticExpressionGenerator.evaluate(falseExpression, getSymbolTable());
                    if (!item.getType().equals(falseType))
                        throw new RuntimeException("Trying to assign " + item.getType() + " into " + falseType + " variable");
                    CodeBuilder.addInstruction(new Instruction(EInstruction.STO, 0, item.getAddress()));
                    CodeBuilder.getPlaceholderInstruction(ternaryPlaceholderEnd).setArg2(CodeBuilder.getLineNumber() + 1);
                    break;
                case TERNARY_EXPRESSION:
                    break;
                default:
                    break;
            }
        }
    }

    private void createDeclaration(Declaration declaration) {
        SymbolTableItem item = new SymbolTableItem(
                declaration.getIdentifier(), 0,
                getSymbolTable().assignAddress(),
                declaration.getDataType() == EDataType.INT ? ESymbolTableType.INT : ESymbolTableType.BOOL);

        getSymbolTable().addItem(item);
        EDataType type = SemanticExpressionGenerator.evaluate(declaration.getExpression(), getSymbolTable());

        if(declaration.getDataType() != type) {
            throw new RuntimeException("Type mismatch: " + declaration.getDataType() + " != " + type);
        }
    }

    private void createAssignment(Assignment assignment) {
        SymbolTableItem item = getSymbolTable().getItem(assignment.getIdentifier());
        EDataType type = SemanticExpressionGenerator.evaluate(assignment.getExpression(), getSymbolTable());

        if (!item.getType().equals(type))
            throw new RuntimeException("Trying to assign " + item.getType() + " into " + type + " variable");

        CodeBuilder.addInstruction(new Instruction(EInstruction.STO, 0, item.getAddress()));
    }
}
