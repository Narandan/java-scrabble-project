package scrabble;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private Board board;
    private TileBag tileBag;
    private Dictionary dictionary;
    private List<Player> players;
    private int currentPlayerIndex;

    public Game(String dictionaryFile) {
        board = new Board();
        tileBag = new TileBag();
        dictionary = new Dictionary(dictionaryFile);
        players = new ArrayList<>();
        currentPlayerIndex = 0;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public void nextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    // TODO: implement move validation, scoring, and game end conditions
    // Place a word for the current player
    public boolean placeWord(String word, int row, int col, boolean horizontal) {
        Player player = getCurrentPlayer();

        // Check if word is valid
        if (!dictionary.isValidWord(word)) {
            System.out.println("Invalid word: " + word);
            return false;
        }

        // Convert player's tiles to Tile objects (simplified, assuming player has all letters)
        Tile[] tilesToPlace = new Tile[word.length()];
        for (int i = 0; i < word.length(); i++) {
            tilesToPlace[i] = new Tile(word.charAt(i), getLetterScore(word.charAt(i)));
        }

        // Attempt to place on board
        if (board.placeWord(tilesToPlace, row, col, horizontal)) {
            int score = calculateScore(word); // TODO: implement proper scoring
            player.addScore(score);
            System.out.println(player.getName() + " played '" + word + "' for " + score + " points!");
            nextTurn();
            return true;
        } else {
            System.out.println("Cannot place word at that location.");
            return false;
        }
    }

    private int getLetterScore(char letter) {
        // Simple example scoring (later update with real Scrabble scores)
        letter = Character.toUpperCase(letter);
        switch (letter) {
            case 'A': case 'E': case 'I': case 'O': case 'U': case 'L': case 'N': case 'S': case 'T': case 'R':
                return 1;
            case 'D': case 'G':
                return 2;
            case 'B': case 'C': case 'M': case 'P':
                return 3;
            case 'F': case 'H': case 'V': case 'W': case 'Y':
                return 4;
            case 'K':
                return 5;
            case 'J': case 'X':
                return 8;
            case 'Q': case 'Z':
                return 10;
            default:
                return 0;
        }
    }

    public Board getBoard() {
        return board;
    }

    private int calculateScore(String word) {
        int score = 0;
        for (char c : word.toCharArray()) {
            score += getLetterScore(c);
        }
        return score;
    }
}
