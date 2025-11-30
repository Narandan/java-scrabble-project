package scrabble;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Arrays;

public class GameMenu {

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Dictionary dict = new Dictionary("words.txt");
        Game game = new Game(dict);

        System.out.println("Welcome to Scrabble!");

        // Ask for number of human players
        int numPlayers = 0;
        while (numPlayers < 2 || numPlayers > 4) {
            System.out.print("How many players? (2–4): ");
            try {
                numPlayers = Integer.parseInt(scanner.nextLine().trim());
                if (numPlayers < 2 || numPlayers > 4) {
                    System.out.println("Please enter a number between 2 and 4.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Try again.");
            }
        }

        // Add human players
        for (int i = 1; i <= numPlayers; i++) {
            System.out.print("Enter Player " + i + " name: ");
            String name = scanner.nextLine().trim();
            game.addPlayer(new Player(name));
        }

        System.out.println("\n" + numPlayers + " players added. Starting game...");

        game.startGame();

        boolean running = true;
        while (running && !game.isGameOver()) {
            Player current = game.getCurrentPlayer();
            System.out.println("\nCurrent Player: " + current.getName());
            printBoard(game);
            printPlayerTiles(current);

            System.out.println("1. Place a word");
            System.out.println("2. View scores");
            System.out.println("3. Pass turn");
            System.out.println("4. Exchange tiles");
            System.out.println("5. Resign");
            System.out.println("6. Save game");
            System.out.println("7. Load game");
            System.out.println("8. Exit");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("Enter word: ");
                    String word = scanner.nextLine().toUpperCase();
                    System.out.print("Enter row (0-" + (Board.SIZE - 1) + "): ");
                    int row = Integer.parseInt(scanner.nextLine());
                    System.out.print("Enter col (0-" + (Board.SIZE - 1) + "): ");
                    int col = Integer.parseInt(scanner.nextLine());
                    System.out.print("Horizontal? (y/n): ");
                    boolean horizontal = scanner.nextLine().trim().toLowerCase().equals("y");

                    if (game.placeWord(word, row, col, horizontal)) {
                        System.out.println("Word placed successfully!");
                    } else {
                        System.out.println("Word placement failed!");
                    }
                    break;

                case "2":
                    System.out.println("\nScores:");
                    for (Player p : game.getPlayers()) {
                        System.out.println(p.getName() + ": " + p.getScore());
                    }
                    break;

                case "3":
                    // Pass turn
                    System.out.println(current.getName() + " passes the turn.");
                    game.nextTurn();
                    break;

                case "4":
                    // Exchange tiles
                    if (game.getTileBag().remainingTiles() < 7) {
                        System.out.println("Not enough tiles left in the bag to exchange.");
                        break;
                    }

                    Player p = current;

                    // Show current rack with indices
                    System.out.println("Your tiles:");
                    for (int i = 0; i < p.getTiles().size(); i++) {
                        System.out.println(i + ": " + p.getTiles().get(i).getLetter());
                    }

                    System.out.print("Enter tile indices to exchange (e.g. '0 3 6'), or leave blank to cancel: ");
                    String input = scanner.nextLine().trim();
                    if (input.isEmpty()) {
                        System.out.println("Exchange cancelled.");
                        break;
                    }

                    String[] parts = input.split("\\s+");
                    List<Tile> toExchange = new ArrayList<>();

                    try {
                        for (String s : parts) {
                            int index = Integer.parseInt(s);
                            if (index < 0 || index >= p.getTiles().size()) {
                                System.out.println("Invalid index: " + index);
                                toExchange.clear();
                                break;
                            }
                            toExchange.add(p.getTiles().get(index));
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Exchange cancelled.");
                        break;
                    }

                    if (toExchange.isEmpty()) {
                        System.out.println("No valid tiles selected.");
                        break;
                    }

                    // Remove tiles and return to bag
                    for (Tile t : toExchange) {
                        p.removeTile(t);
                        game.getTileBag().returnTile(t);
                    }

                    // Draw new tiles
                    while (p.getTiles().size() < 7) {
                        Tile newTile = game.getTileBag().drawTile();
                        if (newTile == null) break;
                        p.addTile(newTile);
                    }

                    System.out.println("Tiles exchanged.");
                    game.nextTurn();
                    break;

                case "5":
                    // Player resigns
                    System.out.println(current.getName() + " resigns!");
                    game.resign(current);
                    break;

                case "6":
                    System.out.print("Enter filename to save: ");
                    String saveFile = scanner.nextLine();
                    game.saveGame(saveFile);
                    break;

                case "7":
                    System.out.print("Enter filename to load: ");
                    String loadFile = scanner.nextLine();
                    game.loadGame(loadFile);
                    break;

                case "8":
                    // Exit game
                    running = false;
                    System.out.println("Exiting game...");
                    break;


                default:
                    System.out.println("Invalid option.");
            }
        }

    }

    private static void printBoard(Game game) {
        System.out.println("\nBoard:");
        Board board = game.getBoard();
        for (int r = 0; r < Board.SIZE; r++) {
            for (int c = 0; c < Board.SIZE; c++) {
                Tile t = board.getTile(r, c);

                if (t == null) {
                    System.out.print(". ");
                } else if (t.getValue() == 0) {
                    // This is a blank tile → show lowercase
                    System.out.print(Character.toLowerCase(t.getLetter()) + " ");
                } else {
                    System.out.print(t.getLetter() + " ");
                }
            }
            System.out.println();
        }
    }


    private static void printPlayerTiles(Player player) {
        System.out.print(player.getName() + "'s tiles: ");
        for (Tile t : player.getTiles()) {
            System.out.print(t.getLetter() + " ");
        }
        System.out.println();
    }
}
