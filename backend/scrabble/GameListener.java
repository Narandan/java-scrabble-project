package scrabble;

public interface GameListener {
    void onPlayerTilesChanged(Player player);
    void onWordPlaced(String word, int row, int col, boolean horizontal, Player player);
    void onScoreChanged(Player player);
    void onTurnChanged(Player currentPlayer);
}
