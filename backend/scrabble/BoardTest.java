package scrabble;

public class BoardTest {
    public static void main(String[] args) {
        Board board = new Board();
        Tile[] word1 = { new Tile('C', 3), new Tile('A', 1), new Tile('T', 1) };
        Tile[] word2 = { new Tile('A', 1), new Tile('R', 1) };

        System.out.println(board.placeWord(word1, 7, 7, true));  // true
        System.out.println(board.placeWord(word2, 7, 8, false)); // true (shares 'A')
        System.out.println(board.placeWord(word2, 7, 9, false)); // false (conflict)
        board.printBoard();
    }
}
