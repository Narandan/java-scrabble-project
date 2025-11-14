package scrabble;

public class Board {
    private Tile[][] grid;
    public static final int SIZE = 15;

    public Board() {
        grid = new Tile[SIZE][SIZE];
    }

    public boolean placeWord(Tile[] tiles, int row, int col, boolean horizontal) {
        if (horizontal && col + tiles.length > SIZE) return false;
        if (!horizontal && row + tiles.length > SIZE) return false;

        for (int i = 0; i < tiles.length; i++) {
            if (horizontal) grid[row][col + i] = tiles[i];
            else grid[row + i][col] = tiles[i];
        }
        return true;
    }

    public Tile getTile(int row, int col) {
        return grid[row][col];
    }
}
