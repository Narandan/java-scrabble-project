package scrabble;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {
    private Board board;
    private TileBag tileBag;
    private Dictionary dictionary;
    private List<Player> players;
    private int currentPlayerIndex;
    private List<GameListener> listeners;

    // Official Scrabble letter values
    private static final Map<Character, Integer> LETTER_VALUES = new HashMap<>();
    static {
        String one = "AEIOULNRST";
        for (char c : one.toCharArray()) LETTER_VALUES.put(c, 1);
        for (char c : "DG".toCharArray()) LETTER_VALUES.put(c, 2);
        for (char c : "BCMP".toCharArray()) LETTER_VALUES.put(c, 3);
        for (char c : "FHVWY".toCharArray()) LETTER_VALUES.put(c, 4);
        LETTER_VALUES.put('K', 5);
        for (char c : "JX".toCharArray()) LETTER_VALUES.put(c, 8);
        for (char c : "QZ".toCharArray()) LETTER_VALUES.put(c, 10);
    }

    public Game(Dictionary dictionary) {
        this.board = new Board();
        this.tileBag = new TileBag();
        this.dictionary = dictionary;
        this.players = new ArrayList<>();
        this.currentPlayerIndex = 0;
        this.listeners = new ArrayList<>();
    }

    // --- Listener Management ---
    public void addListener(GameListener listener) { listeners.add(listener); }
    public void removeListener(GameListener listener) { listeners.remove(listener); }

    // --- Event Notifiers ---
    private void notifyPlayerAdded(Player player) { for (GameListener l : listeners) l.onPlayerAdded(player); }
    private void notifyPlayerTilesChanged(Player player) { for (GameListener l : listeners) l.onPlayerTilesChanged(player); }
    private void notifyWordPlaced(String word, int row, int col, boolean horizontal, Player player) {
        for (GameListener l : listeners) l.onWordPlaced(word, row, col, horizontal, player);
    }
    private void notifyScoreChanged(Player player) { for (GameListener l : listeners) l.onScoreChanged(player); }
    private void notifyTurnChanged(Player currentPlayer) { for (GameListener l : listeners) l.onTurnChanged(currentPlayer); }

    // --- Player Management ---
    public void addPlayer(Player player) {
        players.add(player);

        // Draw starting tiles
        for (int i = 0; i < 7; i++) {
            Tile t = tileBag.drawTile();
            if (t != null) player.addTile(t);
        }

        notifyPlayerAdded(player);
        notifyPlayerTilesChanged(player);
    }

    public List<Player> getPlayers() { return Collections.unmodifiableList(players); }

    // --- Turn Management ---
    public Player getCurrentPlayer() { return players.get(currentPlayerIndex); }
    public void nextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        notifyTurnChanged(getCurrentPlayer());
    }

    // --- Word Placement Logic ---
    public boolean placeWord(String word, int row, int col, boolean horizontal) {
        Player player = getCurrentPlayer();
        word = word.toUpperCase();

        if (!dictionary.isValidWord(word)) return false;

        Tile[] tilesToPlace = new Tile[word.length()];
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            tilesToPlace[i] = new Tile(c, LETTER_VALUES.getOrDefault(c, 1));
        }

        if (!board.placeWord(tilesToPlace, row, col, horizontal)) return false;

        // --- Scoring with multipliers ---
        int wordMultiplierTotal = 1;
        int wordScore = 0;

        for (int i = 0; i < tilesToPlace.length; i++) {
            int r = row + (horizontal ? 0 : i);
            int c = col + (horizontal ? i : 0);

            Tile t = tilesToPlace[i];
            int letterScore = t.getValue() * board.getLetterMultiplier(r, c);
            wordScore += letterScore;

            wordMultiplierTotal *= board.getWordMultiplier(r, c);
        }

        wordScore *= wordMultiplierTotal;

        // --- Bingo bonus ---
        if (tilesToPlace.length == 7) wordScore += 50;

        player.addScore(wordScore);
        notifyScoreChanged(player);

        // Remove used tiles and refill
        for (char c : word.toCharArray()) {
            Tile toRemove = null;
            for (Tile t : player.getTiles()) {
                if (t.getLetter() == c) {
                    toRemove = t;
                    break;
                }
            }
            if (toRemove != null) player.removeTile(toRemove);
        }

        while (player.getTiles().size() < 7) {
            Tile t = tileBag.drawTile();
            if (t == null) break;
            player.addTile(t);
        }
        notifyPlayerTilesChanged(player);

        nextTurn();
        notifyWordPlaced(word, row, col, horizontal, player);
        return true;
    }
}
