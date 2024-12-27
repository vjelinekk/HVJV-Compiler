package validation;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;

import java.io.IOException;

/**
 * Validates the input file names.
 */
public class CompilerInputValidation {
    /**
     * The input file name.
     */
    private final String inputFileName;
    /**
     * The output file name.
     */
    private final String outputFileName;

    /**
     * The input file extension.
     */
    private static final String INPUT_FILE_EXTENSION = ".hvjv";
    /**
     * The output file extension.
     */
    private static final String OUTPUT_FILE_EXTENSION = ".txt";

    /**
     * Creates a new instance of the CompilerInputValidation class.
     * @param inputFileName The input file name.
     * @param outputFileName The output file name.
     */
    public CompilerInputValidation(String inputFileName, String outputFileName) {
        this.inputFileName = inputFileName;
        this.outputFileName = outputFileName;
    }

    /**
     * Gets the input file stream.
     * @return The input file stream.
     * @throws IOException If the input file stream could not be created.
     */
    public CharStream getInputFileStream() throws IOException {
        return CharStreams.fromFileName(inputFileName);
    }

    /**
     * Validates the compiler input.
     * @throws IllegalArgumentException If the input is invalid.
     */
    public void validateCompilerInput() throws IllegalArgumentException {
        validateInputFileName();
        validateOutputFileName();
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
}
