package hw4;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.nio.file.Path;

/**
 * This class calculates the semantic similarity between words using a given
 * dataset and runs a similarity test using a test file.
 */
public class SemanticSimilarity {

    /**
     * The main method that reads input files and calls other methods to
     * process and run the similarity test.
     *
     * @param args command line arguments (not used)
     * @throws IOException if there is an issue reading or writing files
     */
    public static void main(String[] args) throws IOException {
        List<String> filenames = Arrays.asList("pg2600.txt", "pg7178.txt");
        List<List<String>> sentenceLists = SentenceUtils.getSentenceListsFromFiles(filenames);
        System.out.println("Number of sentences: " + sentenceLists.size());

        HashMap<String, HashMap<String, Integer>> semanticDescriptors = SemanticDescriptor.buildSemanticDescriptors(sentenceLists);
        System.out.println("Number of unique words: " + semanticDescriptors.size());

        String testFilename = "brown-train-sentences.txt";
        double accuracy = runSimilarityTest(testFilename, semanticDescriptors);
        System.out.printf("Accuracy: %.2f%%\n", accuracy);
    }

    /**
     * This method reads a test file, runs a similarity test and calculates the
     * accuracy of the similarity test.
     *
     * @param filename            the name of the test file
     * @param semanticDescriptors the semantic descriptors built from the dataset
     * @return the accuracy of the similarity test
     * @throws IOException if there is an issue reading or writing files
     */
    public static double runSimilarityTest(String filename, HashMap<String, HashMap<String, Integer>> semanticDescriptors) throws IOException {
        int numQuestions = 0;
        int numCorrect = 0;

        // Prepare the output file path
        Path outputPath = Paths.get("output.txt");

        // If output file exists and is not empty, truncate the file
        if (Files.exists(outputPath) && Files.size(outputPath) > 0) {
            Files.newBufferedWriter(outputPath, StandardOpenOption.TRUNCATE_EXISTING).close();
        }

        // Read the test file line by line
        try (BufferedReader reader = new BufferedReader(new FileReader(filename));
             BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt", true))) {
            String line;
            while ((line = reader.readLine()) != null) {
                List<String> parts = Arrays.asList(line.split("\\s+"));

                if (parts.size() < 3) {
                    continue;
                }

                String word = parts.get(0);
                String correctAnswer = parts.get(1);
                List<String> choices = parts.subList(2, parts.size());

                // Print debug information
//                System.out.println("Word: " + word);
//                System.out.println("Correct Answer: " + correctAnswer);
//                System.out.println("Choices: " + Arrays.toString(choices.toArray()));

                // Find the most similar word from the given choices
                String mostSimilar = SemanticDescriptor.mostSimilarWord(word, choices, semanticDescriptors);

                // Print debug information
//                System.out.println("Most similar: " + mostSimilar);

                // Update the counters for correct answers and total questions
                if (!mostSimilar.isEmpty() && mostSimilar.equals(correctAnswer)) {
                    numCorrect++;
                }
                numQuestions++;

                // Write the word and its most similar counterpart to the output file
                if (!mostSimilar.isEmpty() && !word.isEmpty()) {
                    writer.write(word + ": " + mostSimilar);
                    writer.newLine();
                }
            }
        }

        // Calculate and return the accuracy of the similarity test
        return (double) numCorrect / numQuestions;
    }
}