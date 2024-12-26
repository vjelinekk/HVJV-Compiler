import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class Main {
    public static void main(String[] args) {
        String inputFile = args[0];
        String outputFile = args[1];

        CharStream input;

        try {
            input = CharStreams.fromFileName(inputFile);
        } catch (Exception e) {
            System.out.println("Error: File not found.");
            return;
        }

        HVJVGrammarLexer lexer = new HVJVGrammarLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        HVJVGrammarParser parser = new HVJVGrammarParser(tokens);

        ParseTree tree = parser.start();

        System.out.println(tree.toStringTree(parser));
    }
}
