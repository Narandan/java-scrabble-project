package scrabble;

import java.util.ArrayList;
import java.util.List;

public class AIPlayer extends Player {

    public AIPlayer(String name) {
        super(name);
    }

    /**
     * Chooses a word from allWords that can be formed using the AI's tiles.
     * Returns null if no valid word can be formed.
     */
    public String chooseWord(List<String> allWords, List<Tile> rack) {
        for (String word : allWords) {
            if (canFormWord(word, rack)) {
                return word;
            }
        }
        return null; // no word possible
    }

    /**
     * Checks if the word can be formed from the given rack.
     */
    private boolean canFormWord(String word, List<Tile> rack) {
        List<Character> available = new ArrayList<>();
        for (Tile t : rack) available.add(t.getLetter());

        for (char c : word.toCharArray()) {
            if (available.contains(c)) {
                available.remove((Character) c);
            } else {
                return false; // letter not available
            }
        }
        return true;
    }
}
