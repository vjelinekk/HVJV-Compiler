package compiler;

import compiler.ast.model.Program;
import compiler.ast.visitor.ProgramVisitor;
import compiler.error.EErrorCode;
import compiler.error.LexerErrorListener;
import compiler.error.ParserErrorListener;
import compiler.semgen.CodeBuilder;
import compiler.semgen.SemanticCodeGenerator;
import compiler.semgen.exception.SemanticAnalysisException;
import grammar.HVJVGrammarLexer;
import grammar.HVJVGrammarParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class Compiler {
    private static final String DEFAULT_OPTIMIZED_OUTPUT = "optimized_";

    /**
     * The input stream for the compiler.
     */
    private final CharStream input;
    /**
     * The output file for the compiler.
     */
    private final String output;
    /**
     * Whether to optimize the generated code.
     */
    private final boolean optimize;
    /**
     * The output file for the optimized code.
     */
    private final String optimizedOutput;

    /**
     * The constructor of the compiler.
     * @param input The input stream for the compiler.
     * @param output The output file for the compiler.
     * @throws NullPointerException Thrown when the input or output is null.
     */
    public Compiler(CharStream input, String output, boolean optimize, String optimizedOutput) throws NullPointerException {
        if (input == null) {
            throw new NullPointerException("Input cannot be null.");
        }
        if (output == null) {
            throw new NullPointerException("Output cannot be null.");
        }

        this.input = input;
        this.output = output;
        this.optimize = optimize;
        this.optimizedOutput = optimizedOutput == null ? DEFAULT_OPTIMIZED_OUTPUT + output : optimizedOutput;
    }

    /**
     * Compiles the input stream into PL/0 instructions.
     */
    public void compile() {
        HVJVGrammarLexer lexer = new HVJVGrammarLexer(input);
        lexer.removeErrorListeners();
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        HVJVGrammarParser parser = new HVJVGrammarParser(tokens);
        parser.removeErrorListeners();

        lexer.addErrorListener(new LexerErrorListener());
        parser.addErrorListener(new ParserErrorListener());

        parser.setBuildParseTree(true);

        ParseTree tree = parser.program();

        Program program = new ProgramVisitor().visit(tree);
        SemanticCodeGenerator semanticCodeGenerator = new SemanticCodeGenerator(program);
        try {
            semanticCodeGenerator.run();
        } catch (SemanticAnalysisException e) {
            System.err.println(e.getMessage());
            System.exit(EErrorCode.SEMANTIC.getCode());
        }

        CodeBuilder.generateCode(output);

        if (!optimize) {
            return;
        }

        CodeBuilder.removeUnusedCode();
        CodeBuilder.generateCode(optimizedOutput);
    }
}
