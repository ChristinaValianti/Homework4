package hw4;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * SentenceUtils is a utility class that provides methods for reading sentences from input files and
 * processing them into a format suitable for further processing.
 */
public class SentenceUtils {

    /**
     * Reads the content of the files specified by their filenames, extracts sentences from each file,
     * and returns a list of lists of words that represent sentences.
     *
     * @param filenames List of filenames from which sentences will be extracted
     * @return List of lists of words, where each inner list represents a sentence
     * @throws IOException If an I/O error occurs while reading the files
     */
    public static List<List<String>> getSentenceListsFromFiles(List<String> filenames) throws IOException {
        // Initialize an empty list to store the extracted sentences.
        List<List<String>> sentenceLists = new ArrayList<>();

        // Iterate through the list of filenames.
        for (String filename : filenames) {
            // Use a try-with-resources statement to open and read the file.
            try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
                String line;
                // Read each line in the file.
                while ((line = reader.readLine()) != null) {
                    // Extract sentences from the line and store them in a list of lists of words.
                    List<List<String>> lineSentenceLists = getSentenceLists(line);
                    // Add the extracted sentences to the main list.
                    sentenceLists.addAll(lineSentenceLists);
                }
            }
        }

        // Return the list of extracted sentences.
        return sentenceLists;
    }

    /**
     * Splits the given text into sentences using ".", "?", and "!" as delimiters, and returns a list
     * of lists of words that represent sentences.
     *
     * @param text Input text to split into sentences
     * @return List of lists of words, where each inner list represents a sentence
     */
    public static List<List<String>> getSentenceLists(String text) {
        // Initialize an empty list to store lists of words for each sentence.
        List<List<String>> sentenceLists = new ArrayList<>();

        // Split the input text into sentences using the regex pattern "[.?!]\\s*".
        String[] sentences = text.split("[.?!]\\s*");

        // Iterate through the sentences array.
        for (String sentence : sentences) {
            // Replace unwanted characters in the sentence with a space.
            sentence = sentence.replaceAll("[‘’“”\"()\\[\\]<>{}\\-_,;:]", " ");

            // This line is intended to deal with words containing apostrophes, but the regex pattern is incorrect.
            // It has no real effect on the code and can be skipped.
            sentence = sentence.replaceAll("(?<![a-z])(?![a-z])", " ");

            // Replace multiple consecutive spaces with a single space in the sentence.
            sentence = sentence.replaceAll(" +", " ");

            // Trim any leading or trailing spaces from the sentence.
            sentence = sentence.trim();

            // Initialize an empty list to store words in the sentence.
            List<String> words = new ArrayList<>();

            // Split the sentence into words using the regex pattern "\\s+" and iterate through the words.
            for (String word : sentence.split("\\s+")) {
                // Check if the word is not empty.
                if (!word.isEmpty()) {
                    // Convert the word to lowercase and add it to the words list.
                    words.add(word.toLowerCase());
                }
            }

            // Check if the words list is not empty.
            if (!words.isEmpty()) {
                // Add the words list to the sentenceLists.
                sentenceLists.add(words);
            }
        }

        // Return the list of lists of words representing sentences.
        return sentenceLists;
    }

//    /**
//     * Extracts words from the given sentence using a regular expression, removes unwanted
//     * characters, and returns a list of cleaned, lowercase words.
//     *
//     * @param sentence Input sentence to extract words from
//     * @return List of cleaned, lowercase words extracted from the sentence
//     */
//    public static List<String> extractWords(String sentence) {
//        List<String> words = new ArrayList<>();
//        for (String word : sentence.split("\\s+")) {
//            if (!word.isEmpty()) {
//                words.add(word);
//            }
//        }
//        return words;
//    }
}