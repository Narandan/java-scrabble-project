package scrabble;

import java.util.List;

public class TestGame {
    public static void main(String[] args) {
        // Load your dictionary
        Dictionary dict = new Dictionary("words.txt");
        Game game = new Game(dict);

        // Create players
        Player p1 = new Player("Alice");
        Player p2 = new Player("Bob");

        // Add players to the game
        game.addPlayer(p1);
        game.addPlayer(p2);

        // Print starting tiles
        System.out.println("Starting tiles:");
        printPlayerTiles(game.getPlayers());

        // Attempt a valid word (letters must be in Alice's rack)
        String validWord = findWordFromRack(p1);
        System.out.println("\nAlice tries to place '" + validWord + "' at (7,7) horizontally:");
        if (game.placeWord(validWord, 7, 7, true)) {
            System.out.println("Word placed successfully!");
        } else {
            System.out.println("Word placement failed!");
        }

        // Attempt an invalid word (letters Alice doesn't have)
        String invalidWord = "ZZZZ";
        System.out.println("\nAlice tries to place '" + invalidWord + "' at (7,8) vertically:");
        if (game.placeWord(invalidWord, 7, 8, false)) {
            System.out.println("Word placed successfully!");
        } else {
            System.out.println("Word placement failed (as expected)!");
        }

        // Print scores
        System.out.println("\nScores:");
        for (Player p : game.getPlayers()) {
            System.out.println(p.getName() + ": " + p.getScore());
        }

        // Print remaining tiles
        System.out.println("\nRemaining tiles:");
        printPlayerTiles(game.getPlayers());
    }

    private static void printPlayerTiles(List<Player> players) {
        for (Player p : players) {
            System.out.print(p.getName() + ": ");
            for (Tile t : p.getTiles()) {
                System.out.print(t.getLetter() + " ");
            }
            System.out.println();
        }
    }

    // Helper to pick a valid word that can be formed from player's tiles
    private static String findWordFromRack(Player player) {
        // Just return first 3 letters in rack as a "word" for testing
        StringBuilder sb = new StringBuilder();
        List<Tile> tiles = player.getTiles();
        for (int i = 0; i < Math.min(3, tiles.size()); i++) {
            sb.append(tiles.get(i).getLetter());
        }
        return sb.toString();
    }
}
