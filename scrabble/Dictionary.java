package scrabble;

import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Dictionary {
    private Set<String> words;

    public Dictionary(String filePath) {
        words = new HashSet<>();
        loadWords(filePath);
    }

    private void loadWords(String filePath) {
        words = new HashSet<>();

        // Try the provided path first
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim().toUpperCase();
                if (!line.isEmpty()) {
                    words.add(line);
                }
            }
            return; // success
        } catch (IOException e) {
            System.err.println("Error loading dictionary file: " + filePath + " (" + e.getMessage() + ")");
            System.err.println("Attempting fallback dictionary path: words.txt");
        }

        // Fallback attempt: look in project root
        try (BufferedReader br = new BufferedReader(new FileReader("words.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim().toUpperCase();
                if (!line.isEmpty()) {
                    words.add(line);
                }
            }
            System.out.println("Loaded dictionary from fallback path: words.txt");
        } catch (IOException e) {
            System.err.println("FATAL ERROR: Could not load dictionary from any location.");
        }
    }

    public boolean isValidWord(String word) {
        return words.contains(word.toUpperCase());
    }

    public int size() { 
        return words.size(); 
    }

    public List<String> getAllWords() {
        return new ArrayList<>(words); 
    }

}
