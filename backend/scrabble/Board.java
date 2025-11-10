package scrabble;

public class Board {
    private Tile[][] board;
    public static final int SIZE = 15;

    public Board() {
        board = new Tile[SIZE][SIZE];
    }

    public boolean placeWord(Tile[] tiles, int row, int col, boolean horizontal) {
        // 1️⃣ Bounds check
        if (tiles == null || tiles.length == 0) return false;
        if (row < 0 || col < 0 || row >= SIZE || col >= SIZE) return false;

        // 2️⃣ Check if word fits within board
        if (horizontal && col + tiles.length > SIZE) return false;
        if (!horizontal && row + tiles.length > SIZE) return false;

        // 3️⃣ Check for conflicts (don’t overwrite existing tiles)
        for (int i = 0; i < tiles.length; i++) {
            int r = row + (horizontal ? 0 : i);
            int c = col + (horizontal ? i : 0);

            if (board[r][c] != null && board[r][c].getLetter() != tiles[i].getLetter()) {
                return false; // invalid overlap
            }
        }

        // 4️⃣ If valid, place tiles
        for (int i = 0; i < tiles.length; i++) {
            int r = row + (horizontal ? 0 : i);
            int c = col + (horizontal ? i : 0);
            board[r][c] = tiles[i];
        }

        return true; // successful placement
    }

    public Tile[][] getBoard() {
        return board;
    }

    public void printBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                System.out.print(board[i][j] != null ? board[i][j].getLetter() : "."); 
                System.out.print(" ");
            }
            System.out.println();
        }
    }
}
