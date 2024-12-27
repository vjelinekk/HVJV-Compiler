package compiler.ast.visitor;

import compiler.ast.model.Program;
import grammar.HVJVGrammarBaseVisitor;
import grammar.HVJVGrammarParser;


public class ProgramVisitor extends HVJVGrammarBaseVisitor<Program> {
    @Override
    public Program visitProgram(HVJVGrammarParser.ProgramContext ctx) {
        return new Program(new FunctionsVisitor().visit(ctx.functions()));
    }
}
