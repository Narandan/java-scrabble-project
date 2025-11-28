package scrabble;

import java.util.ArrayList;
import java.util.List;

public class Board {
    public static final int SIZE = 15;
    private Tile[][] grid;
    private int[][] letterMultiplier;
    private int[][] wordMultiplier;

    public Board() {
        grid = new Tile[SIZE][SIZE];
        letterMultiplier = new int[SIZE][SIZE];
        wordMultiplier = new int[SIZE][SIZE];

        // Initialize all squares to normal
        for (int r = 0; r < SIZE; r++)
            for (int c = 0; c < SIZE; c++) {
                letterMultiplier[r][c] = 1;
                wordMultiplier[r][c] = 1;
            }

        // Premium squares (official example)
        int[][] TW = {{0,0},{0,7},{0,14},{7,0},{7,14},{14,0},{14,7},{14,14}};
        int[][] DW = {
            {1,1},{2,2},{3,3},{4,4},{10,10},{11,11},{12,12},{13,13},
            {1,13},{2,12},{3,11},{4,10},{10,4},{11,3},{12,2},{13,1},
            {7,7} // center
        };
        int[][] TL = {
            {1,5},{1,9},{5,1},{5,5},{5,9},{5,13},
            {9,1},{9,5},{9,9},{9,13},{13,5},{13,9}
        };
        int[][] DL = {
            {0,3},{0,11},{2,6},{2,8},{3,0},{3,7},{3,14},{6,2},{6,6},{6,8},{6,12},
            {7,3},{7,11},{8,2},{8,6},{8,8},{8,12},{11,0},{11,7},{11,14},{12,6},{12,8},{14,3},{14,11}
        };

        for (int[] pos : TW) wordMultiplier[pos[0]][pos[1]] = 3;
        for (int[] pos : DW) wordMultiplier[pos[0]][pos[1]] = 2;
        for (int[] pos : TL) letterMultiplier[pos[0]][pos[1]] = 3;
        for (int[] pos : DL) letterMultiplier[pos[0]][pos[1]] = 2;
    }

    // --- Place word with validation ---
    public boolean placeWord(Tile[] tiles, int row, int col, boolean horizontal) {
        if (!canPlaceWord(tiles, row, col, horizontal)) return false;

        for (int i = 0; i < tiles.length; i++) {
            int r = row + (horizontal ? 0 : i);
            int c = col + (horizontal ? i : 0);
            grid[r][c] = tiles[i];
        }

        return true;
    }

    // --- Check if a word can be placed ---
    public boolean canPlaceWord(Tile[] tiles, int row, int col, boolean horizontal) {
        if (horizontal && col + tiles.length > SIZE) return false;
        if (!horizontal && row + tiles.length > SIZE) return false;

        boolean touchesExisting = false;

        for (int i = 0; i < tiles.length; i++) {
            int r = row + (horizontal ? 0 : i);
            int c = col + (horizontal ? i : 0);

            Tile existing = grid[r][c];
            if (existing != null) {
                if (existing.getLetter() != tiles[i].getLetter()) return false;
                touchesExisting = true;
            } else {
                // Check adjacency to existing tiles
                if (hasAdjacentTile(r, c)) touchesExisting = true;
            }
        }

        // First move must cover center
        if (isBoardEmpty() && !coversCenter(row, col, horizontal, tiles.length)) return false;

        // Subsequent moves must touch existing tiles
        if (!isBoardEmpty() && !touchesExisting) return false;

        return true;
    }

    // --- Check if board is empty ---
    public boolean isBoardEmpty() {
        for (int r = 0; r < SIZE; r++)
            for (int c = 0; c < SIZE; c++)
                if (grid[r][c] != null) return false;
        return true;
    }

    // --- Check if move covers center ---
    private boolean coversCenter(int row, int col, boolean horizontal, int length) {
        for (int i = 0; i < length; i++) {
            int r = row + (horizontal ? 0 : i);
            int c = col + (horizontal ? i : 0);
            if (r == SIZE/2 && c == SIZE/2) return true;
        }
        return false;
    }

    // --- Check adjacency ---
    private boolean hasAdjacentTile(int row, int col) {
        int[] dr = {-1,0,1,0};
        int[] dc = {0,1,0,-1};
        for (int i = 0; i < 4; i++) {
            int nr = row + dr[i];
            int nc = col + dc[i];
            if (nr >=0 && nr < SIZE && nc >=0 && nc < SIZE && grid[nr][nc] != null) return true;
        }
        return false;
    }

    // --- Accessors ---
    public Tile getTile(int row, int col) { return grid[row][col]; }
    public int getLetterMultiplier(int row, int col) { return letterMultiplier[row][col]; }
    public int getWordMultiplier(int row, int col) { return wordMultiplier[row][col]; }

    // --- Get all tiles in row/column (helper for scoring cross-words) ---
    public List<Tile> getRow(int row) {
        List<Tile> list = new ArrayList<>();
        for (int c = 0; c < SIZE; c++) list.add(grid[row][c]);
        return list;
    }

    public List<Tile> getColumn(int col) {
        List<Tile> list = new ArrayList<>();
        for (int r = 0; r < SIZE; r++) list.add(grid[r][col]);
        return list;
    }
}
