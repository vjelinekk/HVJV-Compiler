import compiler.Compiler;
import compiler.semgen.SymbolTable;
import compiler.semgen.SymbolTableItem;
import org.antlr.v4.runtime.CharStream;
import validation.CompilerInputValidation;

import java.io.Console;
import java.io.IOException;

/**
 * The main class of the HVJV compiler.
 */
public class Main {
    /**
     * The main method of the HVJV compiler.
     * @param args The command line arguments.
     */
    public static void main(String[] args) {

        if (args.length != 2) {
            System.err.println("Invalid number of arguments.");
            printUsage();
            return;
        }

        String inputFile = args[0];
        String outputFile = args[1];
        CompilerInputValidation compilerInputValidation = new CompilerInputValidation(inputFile, outputFile);

        CharStream input;
        try {
            compilerInputValidation.validateCompilerInput();
            input = compilerInputValidation.getInputFileStream();
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            printUsage();
            return;
        } catch (IOException e) {
            System.err.println("Could not read input file.");
            printUsage();
            return;
        }

        Compiler compiler = new Compiler(input, outputFile);
        compiler.compile();
    }

    /**
     * Prints the usage of the HVJV compiler.
     */
    private static void printUsage() {
        System.err.println("Usage: java -jar HVJV.jar <input file> <output file>");
    }

    private static void debug() {
        SymbolTable st = new SymbolTable();
        st.enterScope();
        st.addItem(new SymbolTableItem("A1", st.getCurrentScope(), 1));
        st.enterScope();
        st.addItem(new SymbolTableItem("A1", st.getCurrentScope(), 2));
        st.enterScope();
        st.addItem(new SymbolTableItem("A1", st.getCurrentScope(), 3));

        ;

        System.out.println("addr:" + st.getItem("A1").getAddress());
        for(int i = 0; i < 10; i++)
        {
            int a = 10;
        }
        int i = 10;
        int a = 10;

        {
            int b = 10;
        }
        int b = 10;
    }
}
