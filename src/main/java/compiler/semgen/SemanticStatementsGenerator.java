package compiler.semgen;

import compiler.ast.enums.EDataType;
import compiler.ast.enums.EReturnType;
import compiler.ast.enums.EStatementType;
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
import compiler.semgen.exception.ExceptionContext;
import compiler.semgen.exception.GeneralSemanticAnalysisException;
import compiler.semgen.exception.SemanticAnalysisException;
import compiler.semgen.symboltable.SymbolTable;
import compiler.semgen.symboltable.SymbolTableItem;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

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
    public void run() throws SemanticAnalysisException {
        ListIterator<Statement> iterator = getNode().getStatements().listIterator();

        while (iterator.hasNext()) {
            Statement statement = iterator.next();

            switch (statement.getStatementType()) {
                case DECLARATION:
                    ExceptionContext.setLineNumber(statement.getLineNumber());
                    createDeclaration(((StatementDeclaration)statement).getDeclaration());
                    break;
                case IF:
                    IfStatement st = ((StatementIf) statement).getIfStatement();
                    ExceptionContext.setLineNumber(statement.getLineNumber());

                    SemanticExpressionEvaluator.evaluate(st.getCondition(), getSymbolTable());
                    int placeholder = CodeBuilder.addPlaceholderInstruction(new Instruction(EInstruction.JMC, 0, 0));

                    getSymbolTable().enterScope(false, st.getStatements().getVariablesCount());
                    SemanticStatementsGenerator generator = new SemanticStatementsGenerator(st.getStatements(), getSymbolTable(), returnType, returnAddress);
                    generator.run();
                    getSymbolTable().exitScope();
                    CodeBuilder.getPlaceholderInstruction(placeholder).setArg2(CodeBuilder.getLineNumber() + 1);
                    break;
                case IF_ELSE:
                    IfElseStatement ifElseStatement = ((StatementIfElse) statement).getIfElseStatement();
                    ExceptionContext.setLineNumber(statement.getLineNumber());

                    SemanticExpressionEvaluator.evaluate(ifElseStatement.getCondition(), getSymbolTable());
                    int skipIfPlaceholder = CodeBuilder.addPlaceholderInstruction(new Instruction(EInstruction.JMC, 0, 0));

                    getSymbolTable().enterScope(false, ifElseStatement.getIfStatements().getVariablesCount());
                    generator = new SemanticStatementsGenerator(ifElseStatement.getIfStatements(), getSymbolTable(), returnType, returnAddress);
                    generator.run();
                    getSymbolTable().exitScope();
                    int skipElsePlaceholder = CodeBuilder.addPlaceholderInstruction(new Instruction(EInstruction.JMP, 0, 0));
                    CodeBuilder.getPlaceholderInstruction(skipIfPlaceholder).setArg2(CodeBuilder.getLineNumber() + 1);


                    getSymbolTable().enterScope(false, ifElseStatement.getElseStatements().getVariablesCount());
                    generator = new SemanticStatementsGenerator(ifElseStatement.getElseStatements(), getSymbolTable(), returnType, returnAddress);
                    generator.run();
                    getSymbolTable().exitScope();
                    CodeBuilder.getPlaceholderInstruction(skipElsePlaceholder).setArg2(CodeBuilder.getLineNumber() + 1);

                    break;
                case WHILE:
                    WhileStatement whileStatement = ((StatementWhile) statement).getWhileStatement();
                    ExceptionContext.setLineNumber(statement.getLineNumber());
                    getSymbolTable().enterScope(false, whileStatement.getStatements().getVariablesCount());

                    int whileStatementStart = CodeBuilder.getLineNumber() + 1;
                    SemanticExpressionEvaluator.evaluate(whileStatement.getExpression(), getSymbolTable());
                    int whilePlaceholderEnd = CodeBuilder.addPlaceholderInstruction(new Instruction(EInstruction.JMC, 0, 0));
                    generator = new SemanticStatementsGenerator(whileStatement.getStatements(), getSymbolTable(), returnType, returnAddress);
                    generator.run();
                    CodeBuilder.addInstruction(new Instruction(EInstruction.JMP, 0, whileStatementStart));
                    CodeBuilder.getPlaceholderInstruction(whilePlaceholderEnd).setArg2(CodeBuilder.getLineNumber() + 1);
                    getSymbolTable().exitScope();
                    break;
                case FOR:
                    ForStatement forStatement = ((StatementFor) statement).getForStatement();
                    ExceptionContext.setLineNumber(statement.getLineNumber());
                    getSymbolTable().enterScope(false, forStatement.getStatements().getVariablesCount() + 1);

                    createDeclaration(forStatement.getDeclaration());
                    int forStatementStart = CodeBuilder.getLineNumber() + 1;
                    SemanticExpressionEvaluator.evaluate(forStatement.getCondition(), getSymbolTable());
                    int forPlaceholderEnd = CodeBuilder.addPlaceholderInstruction(new Instruction(EInstruction.JMC, 0, 0));
                    generator = new SemanticStatementsGenerator(forStatement.getStatements(), getSymbolTable(), returnType, returnAddress);
                    generator.run();
                    createAssignment(forStatement.getAssignment());
                    CodeBuilder.addInstruction(new Instruction(EInstruction.JMP, 0, forStatementStart));
                    CodeBuilder.getPlaceholderInstruction(forPlaceholderEnd).setArg2(CodeBuilder.getLineNumber() + 1);
                    getSymbolTable().exitScope();
                    break;
                case ASSIGNMENT:
                    Assignment as = ((StatementAssignment) statement).getAssignment();
                    ExceptionContext.setLineNumber(statement.getLineNumber());
                    createAssignment(as);
                    break;
                case FUNCTION_CALL:
                    FunctionCall call = ((StatementFunctionCall) statement).getFunctionCall();
                    ExceptionContext.setLineNumber(statement.getLineNumber());
                    SymbolTableItem fnc = getSymbolTable().getFromGlobalScope(call.getIdentifier());

                    List<Expression> arguments = call.getArguments().getArguments();
                    if (arguments.size() != fnc.getParametersTypes().size())
                        throw new GeneralSemanticAnalysisException(
                                "Arguments count mismatch, expected " + arguments.size() + " got " + fnc.getParametersTypes().size(),
                                statement.getLineNumber(),
                                ExceptionContext.getFunctionName()
                        );

                    if (returnType != EReturnType.VOID)
                        CodeBuilder.addInstruction(new Instruction(EInstruction.INT, 0, 1));

                    for (int j = 0; j < arguments.size(); j++) {
                        EDataType paramType = SemanticExpressionEvaluator.evaluate(arguments.get(j), getSymbolTable());
                        if (paramType != fnc.getParametersTypes().get(j))
                            throw new GeneralSemanticAnalysisException(
                                    "Arguments mismatch expected " + fnc.getParametersTypes().get(j) + " got " + paramType,
                                    statement.getLineNumber(),
                                    ExceptionContext.getFunctionName()
                            );
                    }

                    CodeBuilder.addInstruction(new Instruction(EInstruction.CAL, 0, fnc.getAddress()));
                    if (!arguments.isEmpty())
                        CodeBuilder.addInstruction(new Instruction(EInstruction.INT, 0, -arguments.size()));

                    if (fnc.getReturnType() != EReturnType.VOID)
                        CodeBuilder.addInstruction(new Instruction(EInstruction.INT, 0, -1));
                    break;
                case RETURN:
                    if (returnType != EReturnType.VOID)
                        throw new GeneralSemanticAnalysisException("Trying to return void from NON-VOID function", statement.getLineNumber(), ExceptionContext.getFunctionName());

                    CodeBuilder.addInstruction(new Instruction(EInstruction.RET, 0, 0));
                    return;
                case RETURN_EXPRESSION:
                    if (returnType == EReturnType.VOID)
                        throw new GeneralSemanticAnalysisException("Trying to return value from VOID function", statement.getLineNumber(), ExceptionContext.getFunctionName());

                    ExceptionContext.setLineNumber(statement.getLineNumber());
                    EDataType type = SemanticExpressionEvaluator.evaluate(((StatementReturnExpression) statement).getExpression(), getSymbolTable());
                    EDataType returnDataType = returnType == EReturnType.INT ? EDataType.INT : EDataType.BOOL;
                    if (type != returnDataType)
                        throw new GeneralSemanticAnalysisException(
                                "Type mismatch trying to return: " + type + " from " + returnType + " function",
                                statement.getLineNumber(),
                                ExceptionContext.getFunctionName()
                        );

                    CodeBuilder.addInstruction(new Instruction(EInstruction.STO, 0, returnAddress));
                    CodeBuilder.addInstruction(new Instruction(EInstruction.RET, 0, 0));
                    return;
                case LABEL:
                    StatementLabel label = ((StatementLabel) statement);
                    ExceptionContext.setLineNumber(statement.getLineNumber());
                    getSymbolTable().addGotoLabel(label.getLabel(), CodeBuilder.getLineNumber() + 1);
                    break;
                case GOTO:
                    StatementGoto gotoStatement = ((StatementGoto) statement);
                    ExceptionContext.setLineNumber(statement.getLineNumber());
                    createGoToJump(gotoStatement.getLabel(), iterator);
                    break;
                case TERNARY_ASSIGNMENT:
                    // a = (a > 0) ? 1 : 0;
                    TernaryOperatorAssignment ternary = ((StatementTernaryOperatorAssignment) statement).getTernaryOperatorAssignment();
                    ExceptionContext.setLineNumber(statement.getLineNumber());
                    Expression condition = ternary.getCondition();
                    SemanticExpressionEvaluator.evaluate(condition, getSymbolTable());
                    int ternaryPlaceholder = CodeBuilder.addPlaceholderInstruction(new Instruction(EInstruction.JMC, 0, 0));

                    SymbolTableItem item = getSymbolTable().getItem(ternary.getIdentifier());
                    Expression trueExpression = ternary.getTrueExpression();
                    EDataType trueType = SemanticExpressionEvaluator.evaluate(trueExpression, getSymbolTable());
                    if (!item.getType().equals(trueType))
                        throw new GeneralSemanticAnalysisException(
                                "Trying to assign " + item.getType() + " into " + trueType + " variable",
                                statement.getLineNumber(),
                                ExceptionContext.getFunctionName()
                        );
                    CodeBuilder.addInstruction(new Instruction(EInstruction.STO, 0, item.getAddress()));
                    int ternaryPlaceholderEnd = CodeBuilder.addPlaceholderInstruction(new Instruction(EInstruction.JMP, 0, 0));
                    CodeBuilder.getPlaceholderInstruction(ternaryPlaceholder).setArg2(CodeBuilder.getLineNumber() + 1);

                    Expression falseExpression = ternary.getFalseExpression();
                    EDataType falseType = SemanticExpressionEvaluator.evaluate(falseExpression, getSymbolTable());
                    if (!item.getType().equals(falseType))
                        throw new GeneralSemanticAnalysisException(
                                "Trying to assign " + item.getType() + " into " + falseType + " variable",
                                statement.getLineNumber(),
                                ExceptionContext.getFunctionName()
                        );
                    CodeBuilder.addInstruction(new Instruction(EInstruction.STO, 0, item.getAddress()));
                    CodeBuilder.getPlaceholderInstruction(ternaryPlaceholderEnd).setArg2(CodeBuilder.getLineNumber() + 1);
                    break;
                default:
                    break;
            }
        }
    }

    private void createGoToJump(String label, ListIterator<Statement> iterator) throws SemanticAnalysisException {
        int jumpAddress = getSymbolTable().getGotoLabelAddress(label);

        if(jumpAddress > 0) {
            getSymbolTable().returnToLabel(label);
            CodeBuilder.addInstruction(new Instruction(EInstruction.JMP, 0, jumpAddress));
        }
        else {
            CodeBuilder.addInstruction(new Instruction(EInstruction.INT, 0, 0));
            int JumpInstructionId = CodeBuilder.addPlaceholderInstruction(new Instruction(EInstruction.JMP, 0, 0));
            getSymbolTable().addRequiredGoToLabel(label, JumpInstructionId);
        }

    }

    private void createDeclaration(Declaration declaration) throws SemanticAnalysisException {
        boolean hasFreeSpace = getSymbolTable().getCurrentScopeFreeMemory() > 0;
        SymbolTableItem item = new SymbolTableItem(
                declaration.getIdentifier(), 0,
                getSymbolTable().assignAddress(),
                declaration.getDataType() == EDataType.INT ? ESymbolTableType.INT : ESymbolTableType.BOOL);

        getSymbolTable().addItem(item);
        EDataType type = SemanticExpressionEvaluator.evaluate(declaration.getExpression(), getSymbolTable());

        if(declaration.getDataType() != type) {
            throw new GeneralSemanticAnalysisException(
                    "Type mismatch: " + declaration.getDataType() + " != " + type,
                    ExceptionContext.getLineNumber(),
                    ExceptionContext.getFunctionName()
            );
        }

        if(hasFreeSpace)
            CodeBuilder.addInstruction(new Instruction(EInstruction.STO, 0, item.getAddress()));
    }

    private void createAssignment(Assignment assignment) throws SemanticAnalysisException {
        SymbolTableItem item = getSymbolTable().getItem(assignment.getIdentifier());
        EDataType type = SemanticExpressionEvaluator.evaluate(assignment.getExpression(), getSymbolTable());

        if (!item.getType().equals(type))
            throw new GeneralSemanticAnalysisException(
                    "Trying to assign " + item.getType() + " into " + type + " variable",
                    ExceptionContext.getLineNumber(),
                    ExceptionContext.getFunctionName()
            );

        CodeBuilder.addInstruction(new Instruction(EInstruction.STO, 0, item.getAddress()));
    }
}
