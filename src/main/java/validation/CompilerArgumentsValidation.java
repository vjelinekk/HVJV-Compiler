package validation;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;

import java.io.IOException;

public class CompilerArgumentsValidation {
    /**
     * The input file extension.
     */
    private static final String INPUT_FILE_EXTENSION = ".hvjv";
    /**
     * The output file extension.
     */
    private static final String OUTPUT_FILE_EXTENSION = ".txt";
    private static final String OPTIMIZATION_FILE_EXTENSION = ".txt";
    private static final String OPTIMIZATION_FLAG = "-o";

    private final String[] args;
    private String inputFileName;
    private String outputFileName;
    private boolean isOptimizationEnabled = false;
    private String optimizationFileName;

    public CompilerArgumentsValidation(String[] args) {
        this.args = args;
    }

    public void validateCompilerArguments() throws IllegalArgumentException {
        if (args.length < 2 || args.length > 4) {
            throw new IllegalArgumentException("Invalid number of arguments.");
        }

        this.inputFileName = args[0];
        this.outputFileName = args[1];

        if (args.length > 2) {
            if (args[2].equals(OPTIMIZATION_FLAG)) {
                this.isOptimizationEnabled = true;
                if (args.length == 4) {
                    this.optimizationFileName = args[3];
                }
            } else {
                throw new IllegalArgumentException("Invalid argument.");
            }
        }

        validateInputFileName();
        validateOutputFileName();
        validateOptimizationFileName();
    }

    /**
     * Validates the input file name.
     * @throws IllegalArgumentException If the input file name is invalid.
     */
    private void validateInputFileName() throws IllegalArgumentException {
        if (inputFileName == null || inputFileName.isEmpty()) {
            throw new IllegalArgumentException("Input file name is invalid.");
        }

        if (!inputFileName.endsWith(INPUT_FILE_EXTENSION)) {
            throw new IllegalArgumentException("Input file name must end with " + INPUT_FILE_EXTENSION + ".");
        }
    }

    /**
     * Validates the output file name.
     * @throws IllegalArgumentException If the output file name is invalid.
     */
    private void validateOutputFileName() throws IllegalArgumentException {
        if (outputFileName == null || outputFileName.isEmpty()) {
            throw new IllegalArgumentException("Output file name is invalid.");
        }

        if (!outputFileName.endsWith(OUTPUT_FILE_EXTENSION)) {
            throw new IllegalArgumentException("Output file name must end with " + OUTPUT_FILE_EXTENSION + ".");
        }
    }

    /**
     * Validates the optimization file name.
     * @throws IllegalArgumentException If the optimization file name is invalid.
     */
    private void validateOptimizationFileName() throws IllegalArgumentException {
        if (!isOptimizationEnabled || optimizationFileName == null) {
            return;
        }

        if (!optimizationFileName.endsWith(OPTIMIZATION_FILE_EXTENSION)) {
            throw new IllegalArgumentException("Optimization file name must end with " + OPTIMIZATION_FILE_EXTENSION + ".");
        }
    }

    public String getInputFileName() {
        return inputFileName;
    }

    public CharStream getInputFileStream() throws IOException {
        return CharStreams.fromFileName(inputFileName);
    }

    public String getOutputFileName() {
        return outputFileName;
    }

    public boolean isOptimizationEnabled() {
        return isOptimizationEnabled;
    }

    public String getOptimizationFileName() {
        return optimizationFileName;
    }
}
