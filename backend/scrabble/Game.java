package scrabble;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class Game {
    private Board board;
    private TileBag tileBag;
    private Dictionary dictionary;
    private List<Player> players;
    private int currentPlayerIndex;
    private List<GameListener> listeners;
    private boolean gameOver = false;
    private int consecutivePasses = 0;  


    // Official Scrabble letter values
    private static final Map<Character, Integer> LETTER_VALUES = new HashMap<>();
    static {
        String one = "AEIOULNRST";
        for (char c : one.toCharArray()) LETTER_VALUES.put(c, 1);
        for (char c : "DG".toCharArray()) LETTER_VALUES.put(c, 2);
        for (char c : "BCMP".toCharArray()) LETTER_VALUES.put(c, 3);
        for (char c : "FHVWY".toCharArray()) LETTER_VALUES.put(c, 4);
        LETTER_VALUES.put('K', 5);
        for (char c : "JX".toCharArray()) LETTER_VALUES.put(c, 8);
        for (char c : "QZ".toCharArray()) LETTER_VALUES.put(c, 10);
    }

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
        for (GameListener l : listeners) l.onPlayerAdded(player); 
    }
    private void notifyPlayerTilesChanged(Player player) { 
        for (GameListener l : listeners) l.onPlayerTilesChanged(player); 
    }
    private void notifyWordPlaced(String word, int row, int col, boolean horizontal, Player player) {
        for (GameListener l : listeners) l.onWordPlaced(word, row, col, horizontal, player);
    }
    private void notifyScoreChanged(Player player) { 
        for (GameListener l : listeners) l.onScoreChanged(player); 
    }
    private void notifyTurnChanged(Player currentPlayer) { 
        for (GameListener l : listeners) l.onTurnChanged(currentPlayer); 
    }

    // --- Player Management ---
    public void addPlayer(Player player) {
        players.add(player);

        // Draw starting tiles
        for (int i = 0; i < 7; i++) {
            Tile t = tileBag.drawTile();
            if (t != null) player.addTile(t);
        }
        
        notifyPlayerAdded(player);
        notifyPlayerTilesChanged(player);
    }

    public void startGame() {
        notifyTurnChanged(getCurrentPlayer());
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

    public Board getBoard() {
    return board;
    }

    public TileBag getTileBag() {
        return tileBag;
    }


    // --- Word Placement Logic ---
    public boolean placeWord(String word, int row, int col, boolean horizontal) {
    Player player = getCurrentPlayer();
    word = word.toUpperCase();

    // Prevent out-of-bounds access BEFORE inspecting board tiles
    if (horizontal && col + word.length() > Board.SIZE) return false;
    if (!horizontal && row + word.length() > Board.SIZE) return false;

    // Validate that the word is in the dictionary
    if (!dictionary.isValidWord(word)) return false;

    // --- Check rack availability ---
    Map<Character, Integer> rackCount = new HashMap<>();
    for (Tile t : player.getTiles()) {
    // EXACT MATCH (normal tile)
        if (t.getLetter() == letter && !usedFromRack.contains(t)) {
            chosen = t;
            isBlankTile = false;
            break;
        }
        // BLANK TILE ('?')
        if (t.getLetter() == '?' && !usedFromRack.contains(t)) {
            chosen = t;
            isBlankTile = true;
            break;
        }
    }

    if (isBlankTile) {
        System.out.print("Blank tile used! Enter the letter it should represent: ");
        char assigned = Character.toUpperCase(scanner.nextLine().trim().charAt(0));

        // Create a NEW tile only for placement (keeping rack state clean)
        Tile placed = new Tile(assigned, 0);
        tilesToPlace[i] = placed;
        isBlank[i] = true;

        usedFromRack.add(chosen); // this is the '?' tile from rack
    } else {
        tilesToPlace[i] = chosen;
    }



    // Check letters needed vs. what’s on rack
    for (int i = 0; i < word.length(); i++) {
        char c = word.charAt(i);
        Tile boardTile = board.getTile(row + (horizontal ? 0 : i), col + (horizontal ? i : 0));
        if (boardTile == null) {
            if (!rackCount.containsKey(c) || rackCount.get(c) == 0) return false;
            rackCount.put(c, rackCount.get(c) - 1);
        }
    }

    // --- Build tilesToPlace[] + isNew[] ---
    Tile[] tilesToPlace = new Tile[word.length()];
    boolean[] isNew = new boolean[word.length()];
    boolean[] isBlank = new boolean[word.length()];
    List<Tile> usedFromRack = new ArrayList<>();

    for (int i = 0; i < word.length(); i++) {
        int r = row + (horizontal ? 0 : i);
        int c = col + (horizontal ? i : 0);

        Tile boardTile = board.getTile(r, c);

        if (boardTile == null) {
            // Find tile from rack
            Tile chosen = null;
            char letter = word.charAt(i);

            for (Tile t : player.getTiles()) {
                if (t.getLetter() == letter && !usedFromRack.contains(t)) {
                    chosen = t;
                    break;
                }
            }

            if (chosen == null) return false;
            tilesToPlace[i] = chosen;
            isNew[i] = true;
            usedFromRack.add(chosen);

        } else {
            tilesToPlace[i] = boardTile;
            isNew[i] = false;
        }
    }

    // --- Validate geometry + center + adjacency rules ---
    if (!board.canPlaceWord(tilesToPlace, row, col, horizontal)) return false;

    // --- CROSS WORD VALIDATION & SCORING ---
    int crossScore = 0;

    for (int i = 0; i < tilesToPlace.length; i++) {
        if (!isNew[i]) continue; // only new tiles make cross-words

        int r = row + (horizontal ? 0 : i);
        int c = col + (horizontal ? i : 0);

        // horizontal main → vertical cross
        if (horizontal) {
            boolean hasVertical =
                (r > 0 && board.getTile(r - 1, c) != null) ||
                (r < Board.SIZE - 1 && board.getTile(r + 1, c) != null);

            if (hasVertical) {
                // Build vertical word
                StringBuilder sb = new StringBuilder();

                // walk upward
                int rr = r - 1;
                while (rr >= 0 && board.getTile(rr, c) != null) {
                    sb.insert(0, board.getTile(rr, c).getLetter());
                    rr--;
                }

                // center letter
                sb.append(tilesToPlace[i].getLetter());

                // walk downward
                rr = r + 1;
                while (rr < Board.SIZE && board.getTile(rr, c) != null) {
                    sb.append(board.getTile(rr, c).getLetter());
                    rr++;
                }

                String crossWord = sb.toString();
                if (!dictionary.isValidWord(crossWord)) return false;

                // Score this cross-word
                int base = 0;
                int wordMult = 1;

                // Up (existing tiles)
                rr = r - 1;
                while (rr >= 0 && board.getTile(rr, c) != null) {
                    base += board.getTile(rr, c).getValue();
                    rr--;
                }

                // center tile
                int letterScore = tilesToPlace[i].getValue();
                letterScore *= board.getLetterMultiplier(r, c);
                base += letterScore;
                wordMult *= board.getWordMultiplier(r, c);

                // Down (existing tiles)
                rr = r + 1;
                while (rr < Board.SIZE && board.getTile(rr, c) != null) {
                    base += board.getTile(rr, c).getValue();
                    rr++;
                }

                crossScore += base * wordMult;
            }
        }

        // vertical main → horizontal cross
        else {
            boolean hasHorizontal =
                (c > 0 && board.getTile(r, c - 1) != null) ||
                (c < Board.SIZE - 1 && board.getTile(r, c + 1) != null);

            if (hasHorizontal) {
                // Build horizontal cross word
                StringBuilder sb = new StringBuilder();

                // left
                int cc = c - 1;
                while (cc >= 0 && board.getTile(r, cc) != null) {
                    sb.insert(0, board.getTile(r, cc).getLetter());
                    cc--;
                }

                // center
                sb.append(tilesToPlace[i].getLetter());

                // right
                cc = c + 1;
                while (cc < Board.SIZE && board.getTile(r, cc) != null) {
                    sb.append(board.getTile(r, cc).getLetter());
                    cc++;
                }

                String crossWord = sb.toString();
                if (!dictionary.isValidWord(crossWord)) return false;

                // Score cross-word
                int base = 0;
                int wordMult = 1;

                cc = c - 1;
                while (cc >= 0 && board.getTile(r, cc) != null) {
                    base += board.getTile(r, cc).getValue();
                    cc--;
                }

                int letterScore = tilesToPlace[i].getValue();
                letterScore *= board.getLetterMultiplier(r, c);
                base += letterScore;
                wordMult *= board.getWordMultiplier(r, c);

                cc = c + 1;
                while (cc < Board.SIZE && board.getTile(r, cc) != null) {
                    base += board.getTile(r, cc).getValue();
                    cc++;
                }

                crossScore += base * wordMult;
            }
        }
    }

    // --- MAIN WORD SCORING ---
    int mainBase = 0;
    int mainWordMultiplier = 1;

    for (int i = 0; i < tilesToPlace.length; i++) {
        int rPos = row + (horizontal ? 0 : i);
        int cPos = col + (horizontal ? i : 0);

        int letterScore;

        if (isBlank[i]) {
            // Blank tiles score 0 and do NOT get letter multipliers
            letterScore = 0;
        } else {
            // Normal tile scoring
            letterScore = t.getValue();
            if (isNew[i]) {
                letterScore *= board.getLetterMultiplier(rPos, cPos);
                mainWordMultiplier *= board.getWordMultiplier(rPos, cPos);
            }
        }

mainBase += letterScore;

    }

    int totalScore = mainBase * mainWordMultiplier + crossScore;

    // Bingo: 7 tiles used from rack
    if (usedFromRack.size() == 7) totalScore += 50;

    // --- Commit move ---
    if (!board.placeWord(tilesToPlace, row, col, horizontal)) return false;

    // remove used tiles
    for (Tile t : usedFromRack) {
        player.removeTile(t);
    }

    // update score
    player.addScore(totalScore);
    notifyScoreChanged(player);

    // refill rack
    while (player.getTiles().size() < 7) {
        Tile t = tileBag.drawTile();
        if (t == null) break;
        player.addTile(t);
    }
    notifyPlayerTilesChanged(player);

    // notify + turn
    notifyWordPlaced(word, row, col, horizontal, player);
    nextTurn();
    return true;
    }

    public void resign(Player p) {
    // Set score to zero
    p.setScore(0);

    // Remove from player list
    int index = players.indexOf(p);
    players.remove(p);

    // Adjust turn index safely
    if (index <= currentPlayerIndex && currentPlayerIndex > 0) {
        currentPlayerIndex--;
    }

    // If only one player left → immediate win
    if (players.size() == 1) {
        endGame();
        return;
    }

    // Reset pass counter
    consecutivePasses = 0;

    // If there are players left, continue to the next player's turn
    nextTurn();
    }

    public boolean isGameOver() {
        return gameOver;
    }

    private void endGame() {
        gameOver = true;

        System.out.println("\n--- GAME OVER ---");

        // 1. Check if any player used all tiles
        Player finisher = null;
        for (Player p : players) {
            if (p.getTiles().isEmpty()) {
                finisher = p;
                break;
            }
        }

        // 2. Calculate leftover tile penalties
        Map<Player, Integer> leftoverValues = new HashMap<>();
        int sumOfAllLeftovers = 0;

        for (Player p : players) {
            int penalty = 0;
            for (Tile t : p.getTiles()) {
                penalty += t.getValue();
            }
            leftoverValues.put(p, penalty);
            sumOfAllLeftovers += penalty;
        }

        // 3. Subtract leftover tile values from everyone
        for (Player p : players) {
            int penalty = leftoverValues.get(p);
            p.addScore(-penalty);
        }

        // 4. If one player finished, they get the total bonus
        if (finisher != null) {
            finisher.addScore(sumOfAllLeftovers);
        }

        // 5. Sort players by descending score
        List<Player> sorted = new ArrayList<>(players);
        sorted.sort((a, b) -> Integer.compare(b.getScore(), a.getScore()));

        // 6. Announce winner
        Player winner = sorted.get(0);
        System.out.println("\n" + winner.getName() + " wins!");

        // 7. Display final sorted scoreboard
        System.out.println("\nFinal Scores:");
        for (Player p : sorted) {
            System.out.println(p.getName() + ": " + p.getScore());
        }

        System.out.println("\nThank you for playing!");
    }


    public void saveGame(String filename) {
        try (PrintWriter out = new PrintWriter(new FileWriter(filename))) {

            out.println("TURN:" + currentPlayerIndex);
            out.println("PASSES:" + consecutivePasses);

            // --- Save Players ---
            for (Player p : players) {
                out.println("PLAYER:" + p.getName());
                out.println("SCORE:" + p.getScore());

                // rack tiles
                StringBuilder sb = new StringBuilder();
                for (Tile t : p.getTiles()) {
                    sb.append(t.getLetter()).append(",");
                }
                out.println("RACK:" + sb.toString());
            }

            // --- Save Board ---
            out.println("BOARD:");
            for (int r = 0; r < Board.SIZE; r++) {
                for (int c = 0; c < Board.SIZE; c++) {
                    Tile t = board.getTile(r, c);
                    out.print((t == null ? "." : t.getLetter()));
                }
                out.println();
            }

            // --- Save Tile Bag ---
            out.println("BAG:");
            for (Tile t : tileBag.getTiles()) {
                out.print(t.getLetter());
            }
            out.println();

            System.out.println("Game saved to " + filename);

        } catch (Exception e) {
            System.out.println("Error saving game: " + e.getMessage());
        }
    }

    public void loadGame(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {

            String line;

            // Clear current state
            players.clear();

            // --- Read turn ---
            line = br.readLine();
            currentPlayerIndex = Integer.parseInt(line.split(":")[1]);

            // --- Read passes ---
            line = br.readLine();
            consecutivePasses = Integer.parseInt(line.split(":")[1]);

            // --- Read players ---
            while ((line = br.readLine()) != null && line.startsWith("PLAYER:")) {

                String name = line.split(":")[1];
                Player p = new Player(name);

                // Score
                line = br.readLine();
                int score = Integer.parseInt(line.split(":")[1]);
                p.setScore(score);

                // Rack
                line = br.readLine(); // RACK:letters
                String rackPart = line.split(":")[1];
                if (!rackPart.isEmpty()) {
                    String[] letters = rackPart.split(",");
                    for (String l : letters) {
                        if (!l.isEmpty()) {
                            char ch = l.charAt(0);
                            p.addTile(new Tile(ch, getTileValue(ch))); // value corrected automatically in game
                        }
                    }
                }

                players.add(p);
            }

            // --- Read board ---
            if (!line.equals("BOARD:")) {
                // already read it in the loop above
                while (!line.equals("BOARD:")) {
                    line = br.readLine();
                }
            }

            for (int r = 0; r < Board.SIZE; r++) {
                line = br.readLine();
                for (int c = 0; c < Board.SIZE; c++) {
                    char ch = line.charAt(c);
                    if (ch != '.') {
                        board.placeTile(r, c, new Tile(ch, getTileValue(ch)));
                    } else {
                        board.placeTile(r, c, null);
                    }
                }
            }

            // --- Read tile bag ---
            line = br.readLine(); // should be BAG:
            line = br.readLine(); // the actual bag line

            tileBag.clear();
            for (char ch : line.toCharArray()) {
                tileBag.returnTile(new Tile(ch, getTileValue(ch)));
            }

            System.out.println("Game loaded from " + filename);

        } catch (Exception e) {
            System.out.println("Error loading game: " + e.getMessage());
        }
    }

    private int getTileValue(char letter) {
        return LETTER_VALUES.getOrDefault(letter, 0);
    }


}
