import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestOutputCreator {

    private static final String RESOURCES_DIR = "src/test/resources";
    private static final String OUTPUT_DIR = "src/test/output";
    private static final String OUTPUT_OPTIMIZED_DIR = "src/test/output-optimized";

    @BeforeEach
    public void setUp() throws IOException {
        // Clean up output directories
        cleanOutputDirectories();
    }

    @Test
    public void generateTestOutputs() throws IOException, InterruptedException {
        // Process files in the resources directory
        Files.walk(Paths.get(RESOURCES_DIR))
                .filter(Files::isRegularFile)
                .filter(file -> file.toString().endsWith(".hvjv"))
                .forEach(file -> {
                    try {
                        // Build the command to run the compiler as an external process
                        String inputFile = file.toString();
                        String outputFile = OUTPUT_DIR + "/" + file.getFileName().toString().replace(".hvjv", ".txt");
                        String outputOptimizedFile = OUTPUT_OPTIMIZED_DIR + "/" + file.getFileName().toString().replace(".hvjv", "-optimized.txt");

                        String command = String.format("java -jar target/hvjv-compiler-1.0-SNAPSHOT-jar-with-dependencies.jar %s %s -o %s", inputFile, outputFile, outputOptimizedFile);
                        System.out.println("Running command: " + command);

                        // Run the external process
                        Process process = new ProcessBuilder(command.split(" ")).start();

                        // Capture and print the output from the process
                        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                            String line;
                            while ((line = reader.readLine()) != null) {
                                System.out.println(line);
                            }
                        }

                        // Wait for the process to finish and check if it was successful
                        int exitCode = process.waitFor();
                        if (exitCode != 0) {
                            throw new IOException("Compiler process failed with exit code " + exitCode);
                        }

                        System.out.println("File compiled successfully: " + outputFile);
                    } catch (IOException | InterruptedException e) {
                        System.err.println("Error compiling file: " + e.getMessage());
                    }
                });
    }

    private void cleanOutputDirectories() throws IOException {
        // Delete existing output files and recreate the directories
        Path outputPath = Paths.get(OUTPUT_DIR);
        if (Files.exists(outputPath)) {
            Files.walk(outputPath)
                    .sorted((p1, p2) -> p2.compareTo(p1)) // Delete files first, then directories
                    .map(Path::toFile)
                    .forEach(file -> {
                        if (!file.delete()) {
                            System.err.println("Failed to delete " + file);
                        }
                    });
        }

        Path outputOptimizedPath = Paths.get(OUTPUT_OPTIMIZED_DIR);
        if (Files.exists(outputOptimizedPath)) {
            Files.walk(outputOptimizedPath)
                    .sorted((p1, p2) -> p2.compareTo(p1)) // Delete files first, then directories
                    .map(Path::toFile)
                    .forEach(file -> {
                        if (!file.delete()) {
                            System.err.println("Failed to delete " + file);
                        }
                    });
        }

        Files.createDirectories(outputPath);
        Files.createDirectories(outputOptimizedPath);
    }
}
