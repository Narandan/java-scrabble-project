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

    // Prepare tiles to place with official letter values
    Tile[] tilesToPlace = new Tile[word.length()];
    for (int i = 0; i < word.length(); i++) {
        char c = word.charAt(i);
        tilesToPlace[i] = new Tile(c, LETTER_VALUES.getOrDefault(c, 1));
    }

    // Check if placement is valid on board
    if (!board.canPlaceWord(tilesToPlace, row, col, horizontal)) return false;

    // --- Collect all words formed ---
    List<String> wordsFormed = new ArrayList<>();
    List<Integer> wordScores = new ArrayList<>();

    // Place temporarily to compute perpendicular words
    for (int i = 0; i < tilesToPlace.length; i++) {
        int r = row + (horizontal ? 0 : i);
        int c = col + (horizontal ? i : 0);
        if (board.getTile(r, c) == null) grid[r][c] = tilesToPlace[i];
    }

    // Main word score calculation
    int mainWordScore = calculateWordScore(tilesToPlace, row, col, horizontal);
    wordsFormed.add(word);
    wordScores.add(mainWordScore);

    // Check perpendicular words
    for (int i = 0; i < tilesToPlace.length; i++) {
        int r = row + (horizontal ? 0 : i);
        int c = col + (horizontal ? i : 0);
        if (board.getTile(r, c) == tilesToPlace[i]) { // new tile
            String perpWord;
            int perpScore;
            if (horizontal) {
                perpWord = getPerpendicularWord(r, c, false);
                perpScore = calculatePerpendicularScore(r, c, false);
            } else {
                perpWord = getPerpendicularWord(r, c, true);
                perpScore = calculatePerpendicularScore(r, c, true);
            }
            if (perpWord.length() > 1) { // only count if forms word
                if (!dictionary.isValidWord(perpWord)) {
                    // Undo temporary placement
                    for (int j = 0; j < tilesToPlace.length; j++) {
                        int rr = row + (horizontal ? 0 : j);
                        int cc = col + (horizontal ? j : 0);
                        if (board.getTile(rr, cc) == tilesToPlace[j]) grid[rr][cc] = null;
                    }
                    return false;
                }
                wordsFormed.add(perpWord);
                wordScores.add(perpScore);
            }
        }
    }

    // Place word officially
    board.placeWord(tilesToPlace, row, col, horizontal);

    // Total score
    int totalScore = wordScores.stream().mapToInt(Integer::intValue).sum();

    // Bingo bonus
    if (tilesToPlace.length == 7) totalScore += 50;

    player.addScore(totalScore);
    notifyScoreChanged(player);

    // Remove used tiles from player and refill
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

// --- Helpers for perpendicular words ---
private String getPerpendicularWord(int row, int col, boolean horizontal) {
    StringBuilder sb = new StringBuilder();
    int r = row, c = col;

    // Move to start of word
    while (r > 0 && c > 0 && board.getTile(horizontal ? r-1 : r, horizontal ? c : c-1) != null) {
        r -= horizontal ? 1 : 0;
        c -= horizontal ? 0 : 1;
    }

    // Collect letters
    while (r < Board.SIZE && c < Board.SIZE && board.getTile(r, c) != null) {
        sb.append(board.getTile(r, c).getLetter());
        r += horizontal ? 1 : 0;
        c += horizontal ? 0 : 1;
    }

    return sb.toString();
}

private int calculateWordScore(Tile[] tiles, int row, int col, boolean horizontal) {
    int wordMultiplierTotal = 1;
    int wordScore = 0;

    for (int i = 0; i < tiles.length; i++) {
        int r = row + (horizontal ? 0 : i);
        int c = col + (horizontal ? i : 0);
        Tile t = tiles[i];
        int letterScore = t.getValue() * board.getLetterMultiplier(r, c);
        wordScore += letterScore;
        wordMultiplierTotal *= board.getWordMultiplier(r, c);
    }

    return wordScore * wordMultiplierTotal;
}

private int calculatePerpendicularScore(int row, int col, boolean horizontal) {
    StringBuilder sb = new StringBuilder();
    int r = row, c = col;

    // Move to start of word
    while (r > 0 && c > 0 && board.getTile(horizontal ? r-1 : r, horizontal ? c : c-1) != null) {
        r -= horizontal ? 1 : 0;
        c -= horizontal ? 0 : 1;
    }

    int wordMultiplierTotal = 1;
    int wordScore = 0;

    while (r < Board.SIZE && c < Board.SIZE && board.getTile(r, c) != null) {
        Tile t = board.getTile(r, c);
        wordScore += t.getValue() * board.getLetterMultiplier(r, c);
        wordMultiplierTotal *= board.getWordMultiplier(r, c);
        r += horizontal ? 1 : 0;
        c += horizontal ? 0 : 1;
    }

    return wordScore * wordMultiplierTotal;
}

}
