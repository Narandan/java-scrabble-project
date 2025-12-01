package scrabble;

public interface GameListener {

    

    // Player events
    void onPlayerAdded(Player player);
    void onPlayerTilesChanged(Player player);
    void onScoreChanged(Player player);
    void onPlayerRemoved(Player player);

    // Board events
    void onWordPlaced(String word, int row, int col, boolean horizontal, Player player);
    

    // Turn events
    void onTurnChanged(Player currentPlayer);
}
