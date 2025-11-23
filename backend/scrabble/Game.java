package scrabble;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game {
    private Board board;
    private TileBag tileBag;
    private Dictionary dictionary;
    private List<Player> players;
    private int currentPlayerIndex;
    private List<GameListener> listeners;

    public Game(Dictionary dictionary) {
        this.board = new Board();
        this.tileBag = new TileBag();
        this.dictionary = dictionary;
        this.players = new ArrayList<>();
        this.currentPlayerIndex = 0;
        this.listeners = new ArrayList<>();
    }

    // --- Listener Management ---
    public void addListener(GameListener listener) {
        listeners.add(listener);
    }

    public void removeListener(GameListener listener) {
        listeners.remove(listener);
    }

    // --- Event Notifiers ---
    private void notifyPlayerAdded(Player player) {
        for (GameListener l : listeners) {
            l.onPlayerAdded(player);
        }
    }

    private void notifyPlayerTilesChanged(Player player) {
        for (GameListener l : listeners) {
            l.onPlayerTilesChanged(player);
        }
    }

    private void notifyWordPlaced(String word, int row, int col, boolean horizontal, Player player) {
        for (GameListener l : listeners) {
            l.onWordPlaced(word, row, col, horizontal, player);
        }
    }

    private void notifyScoreChanged(Player player) {
        for (GameListener l : listeners) {
            l.onScoreChanged(player);
        }
    }

    private void notifyTurnChanged(Player currentPlayer) {
        for (GameListener l : listeners) {
            l.onTurnChanged(currentPlayer);
        }
    }

    // --- Player Management ---
    public void addPlayer(Player player) {
        players.add(player);

        // Draw starting tiles
        for (int i = 0; i < 7; i++) {
            Tile t = tileBag.drawTile();
            if (t != null) {
                player.addTile(t);
            }
        }

        // Notify UI
        notifyPlayerAdded(player);
        notifyPlayerTilesChanged(player);
    }

    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    // --- Turn Management ---
    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public void nextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        notifyTurnChanged(getCurrentPlayer());
    }

    // --- Word Placement Logic ---
    public boolean placeWord(String word, int row, int col, boolean horizontal) {
        Player player = getCurrentPlayer();
        word = word.toUpperCase();

        // Validate dictionary
        if (!dictionary.isValidWord(word)) {
            return false;
        }

        // Convert word to tiles
        Tile[] tilesToPlace = new Tile[word.length()];
        for (int i = 0; i < word.length(); i++) {
            tilesToPlace[i] = new Tile(word.charAt(i), 1); // placeholder scoring
        }

        // Try to place on board
        if (!board.placeWord(tilesToPlace, row, col, horizontal)) {
            return false;
        }

        // Notify placed word
        notifyWordPlaced(word, row, col, horizontal, player);

        // Example scoring logic
        player.addScore(word.length());
        notifyScoreChanged(player);

        // Remove tiles used
        for (char c : word.toCharArray()) {
            Tile toRemove = null;
            for (Tile t : player.getTiles()) {
                if (t.getLetter() == c) {
                    toRemove = t;
                    break;
                }
            }
            if (toRemove != null) {
                player.removeTile(toRemove);
            }
        }

        // Refill tiles
        while (player.getTiles().size() < 7) {
            Tile t = tileBag.drawTile();
            if (t == null) break;
            player.addTile(t);
        }
        notifyPlayerTilesChanged(player);

        // Move to next turn
        nextTurn();

        return true;
    }
}
