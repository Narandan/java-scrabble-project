package scrabble;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class Dictionary {
    private Set<String> words;

    public Dictionary(String filePath) {
        words = new HashSet<>();
        loadWords(filePath);
    }

    private void loadWords(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                words.add(line.toUpperCase());
            }
        } catch (IOException e) {
            System.err.println("Error loading dictionary: " + e.getMessage());
        }
    }

    public boolean isValidWord(String word) {
        return words.contains(word.toUpperCase());
    }

    public int size() { return words.size(); }
}
