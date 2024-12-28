package compiler;

import compiler.ast.model.Program;
import compiler.ast.visitor.ProgramVisitor;
import compiler.semgen.SemanticCodeGenerator;
import compiler.semgen.SymbolTable;
import grammar.HVJVGrammarLexer;
import grammar.HVJVGrammarParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class Compiler {
    /**
     * The input stream for the compiler.
     */
    private final CharStream input;
    /**
     * The output file for the compiler.
     */
    private final String output;

    /**
     * The constructor of the compiler.
     * @param input The input stream for the compiler.
     * @param output The output file for the compiler.
     * @throws NullPointerException Thrown when the input or output is null.
     */
    public Compiler(CharStream input, String output) throws NullPointerException {
        if (input == null) {
            throw new NullPointerException("Input cannot be null.");
        }
        if (output == null) {
            throw new NullPointerException("Output cannot be null.");
        }

        this.input = input;
        this.output = output;
    }

    /**
     * Compiles the input stream into PL/0 instructions.
     */
    public void compile() {
        HVJVGrammarLexer lexer = new HVJVGrammarLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        HVJVGrammarParser parser = new HVJVGrammarParser(tokens);

        parser.setBuildParseTree(true);

        ParseTree tree = parser.program();

        Program program = new ProgramVisitor().visit(tree);
        System.out.println(program);
        SemanticCodeGenerator semanticCodeGenerator = new SemanticCodeGenerator(program);
        semanticCodeGenerator.run();
    }
}
