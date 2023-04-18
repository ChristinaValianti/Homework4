package hw4;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The SemanticDescriptor class provides methods for building and comparing semantic descriptors
 * of words based on their context in sentences.
 */
public class SemanticDescriptor {
    /**
     * Builds semantic descriptors for all unique words in the given list of sentences.
     *
     * @param sentences List of lists of words, where each inner list represents a sentence
     * @return HashMap with unique words as keys and their semantic descriptors as values
     */
    public static HashMap<String, HashMap<String, Integer>> buildSemanticDescriptors(List<List<String>> sentences) {
        HashMap<String, HashMap<String, Integer>> semanticDescriptors = new HashMap<>();

        // Iterate through each sentence in the list of sentences.
        for (List<String> sentence : sentences) {
            // Iterate through each word in the sentence.
            for (String word1 : sentence) {
                // If the semanticDescriptors map does not contain the current word, add it with an empty HashMap as its value.
                if (!semanticDescriptors.containsKey(word1)) {
                    semanticDescriptors.put(word1, new HashMap<>());
                }

                HashMap<String, Integer> wordDescriptor = semanticDescriptors.get(word1);

                // Iterate through the other words in the sentence and update the word descriptor.
                for (String word2 : sentence) {
                    if (!word1.equals(word2)) {
                        wordDescriptor.put(word2, wordDescriptor.getOrDefault(word2, 0) + 1);
                    }
                }
            }
        }

        return semanticDescriptors;
    }

    /**
     * Computes the cosine similarity between two semantic descriptors.
     *
     * @param vec1 First semantic descriptor
     * @param vec2 Second semantic descriptor
     * @return The cosine similarity between the two semantic descriptors
     */
    public static double cosineSimilarity(Map<String, Integer> vec1, Map<String, Integer> vec2) {
        double dotProduct = 0.0;
        // Calculate dot product for shared keys.
        for (String x : vec1.keySet()) {
            if (vec2.containsKey(x)) {
                dotProduct += vec1.get(x) * vec2.get(x);
            }
        }
        return dotProduct / (norm(vec1) * norm(vec2));
    }

    /**
     * Computes the Euclidean norm of a semantic descriptor.
     *
     * @param vec The semantic descriptor
     * @return The Euclidean norm of the semantic descriptor
     */
    private static double norm(Map<String, Integer> vec) {
        double sumOfSquares = 0.0;
        // Calculate the sum of squares for each value.
        for (int x : vec.values()) {
            sumOfSquares += x * x;
        }
        return Math.sqrt(sumOfSquares);
    }

    /**
     * Finds the most similar word to the given target word among a list of choices based on their
     * semantic descriptors.
     *
     * @param word                 The target word to find the most similar word for
     * @param choices              A list of words to compare against the target word
     * @param semantic_descriptors The semantic descriptors for all words
     * @return The most similar word to the target word among the choices, or an empty string if no
     * valid similarity can be computed
     */
    public static String mostSimilarWord(String word, List<String> choices, HashMap<String, HashMap<String, Integer>> semantic_descriptors) {

        double max_similarity = -1;
        String most_similar = "";

        Map<String, Integer> word_descriptor = semantic_descriptors.get(word);

        // If the target word's semantic descriptor is not found, return the first word from the choices as the most similar word.
        if (word_descriptor == null) {
            return choices.get(0);
        }
        // Iterate through the list of choices and compute the cosine similarity with the target word.
        for (String choice : choices) {
            Map<String, Integer> choice_descriptor = semantic_descriptors.get(choice);
            if (choice_descriptor != null) {
                double similarity = cosineSimilarity(word_descriptor, choice_descriptor);
                // Update the most similar word if the current similarity is greater than the previous maximum similarity.
                if (similarity > max_similarity) {
                    max_similarity = similarity;
                    most_similar = choice;
                }
            }
        }

        return most_similar;
    }
}
