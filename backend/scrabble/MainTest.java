package scrabble;

import java.util.Scanner;

public class MainTest {
    public static void main(String[] args) {
        Board board = new Board();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to Scrabble Backend Test!");
        board.printBoard();

        while (true) {
            System.out.println("\nEnter a word to place (or 'exit' to quit):");
            String inputWord = scanner.nextLine().toUpperCase();
            if (inputWord.equals("EXIT")) break;

            Tile[] tiles = new Tile[inputWord.length()];
            for (int i = 0; i < inputWord.length(); i++) {
                // Using simple value = 1 for now
                tiles[i] = new Tile(inputWord.charAt(i), 1);
            }

            System.out.println("Enter starting row (0-14):");
            int row = scanner.nextInt();
            System.out.println("Enter starting column (0-14):");
            int col = scanner.nextInt();
            System.out.println("Horizontal? (true/false):");
            boolean horizontal = scanner.nextBoolean();
            scanner.nextLine(); // consume leftover newline

            boolean success = board.placeWord(tiles, row, col, horizontal);
            if (success) {
                System.out.println("Word placed successfully!");
            } else {
                System.out.println("Invalid placement! Try again.");
            }

            board.printBoard();
        }

        System.out.println("Exiting test. Goodbye!");
        scanner.close();
    }
}
