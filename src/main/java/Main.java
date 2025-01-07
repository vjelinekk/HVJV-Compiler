import compiler.Compiler;
import org.antlr.v4.runtime.CharStream;
import validation.CompilerArgumentsValidation;

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
        CompilerArgumentsValidation compilerArgumentsValidation = new CompilerArgumentsValidation(args);
        try {
            compilerArgumentsValidation.validateCompilerArguments();
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            printUsage();
            return;
        }

        String outputFile = compilerArgumentsValidation.getOutputFileName();

        CharStream input;
        try {
            input = compilerArgumentsValidation.getInputFileStream();
        } catch (IOException e) {
            System.err.println("Could not read input file. Are you sure it exists?");
            printUsage();
            return;
        }

        Compiler compiler = new Compiler(input, outputFile, compilerArgumentsValidation.isOptimizationEnabled(), compilerArgumentsValidation.getOptimizationFileName());
        compiler.compile();
    }

    /**
     * Prints the usage of the HVJV compiler.
     */
    private static void printUsage() {
        System.err.println("Usage:");
        System.err.println("  java -jar HVJV.jar <input file>.hvjv <output file>.txt");
        System.err.println("  java -jar HVJV.jar <input file>.hvjv <output file>.txt -o");
        System.err.println("  java -jar HVJV.jar <input file> <output file>.hvjv -o <optimized output file>.txt");
        System.err.println("Parameter -o enables optimization.");
    }
}
