package scrabble;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
    private void notifyPlayerRemoved(Player player) {
        for (GameListener l : listeners)
            l.onPlayerRemoved(player);
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
        nextTurn(1);
    }

    public void nextTurn(int i) {
        currentPlayerIndex = (currentPlayerIndex + i) % players.size();
        notifyTurnChanged(getCurrentPlayer());
    }

    public Board getBoard() {
        return board;
    }

    public Dictionary getDictionary() {
        return dictionary;
    }


    public TileBag getTileBag() {
        return tileBag;
    }


    // --- Word Placement Logic ---
    public boolean placeWord(String word, int row, int col, boolean horizontal) {
        Player player = getCurrentPlayer();
        word = word.toUpperCase();

        // 1) Check dictionary
        // Scrabble rule: main word must be at least 2 letters
        if (word.length() < 2) return false;

        // Check main word dictionary validity
        if (!dictionary.isValidWord(word)) return false;


        // 2) Build tilesToPlace and track which come from the rack
        Tile[] tilesToPlace = new Tile[word.length()];
        boolean[] isNew = new boolean[word.length()];
        List<Tile> usedFromRack = new ArrayList<>();

        for (int i = 0; i < word.length(); i++) {
            int r = row + (horizontal ? 0 : i);
            int c = col + (horizontal ? i : 0);

            // quick bounds check (safety)
            if (r < 0 || r >= Board.SIZE || c < 0 || c >= Board.SIZE) {
                return false;
            }

            Tile boardTile = board.getTile(r, c);
            char needed = word.charAt(i);

            if (boardTile != null) {
                // must match existing letter
                if (Character.toUpperCase(boardTile.getLetter()) != needed) {
                    return false;
                }
                tilesToPlace[i] = boardTile;
                isNew[i] = false;
            } else {
                // need a tile from the rack: try exact letter first (non-blank)
                Tile chosen = null;
                for (Tile t : player.getTiles()) {
                    if (!t.isBlank() &&
                        !usedFromRack.contains(t) &&
                        t.getLetter() == needed) {
                        chosen = t;
                        break;
                    }
                }

                // if none, try a blank tile
                if (chosen == null) {
                    for (Tile t : player.getTiles()) {
                        if (t.isBlank() && !usedFromRack.contains(t)) {
                            chosen = t;
                            break;
                        }
                    }
                }

                if (chosen == null) {
                    // can't supply this letter
                    return false;
                }

                // If it's a blank, assign the letter it represents
                if (chosen.isBlank()) {
                    chosen.assignLetter(needed);
                }

                tilesToPlace[i] = chosen;
                isNew[i] = true;
                usedFromRack.add(chosen);
            }
        }

        // Rule: must place at least one new tile
        boolean placedNewTile = false;
        for (boolean b : isNew) if (b) { placedNewTile = true; break; }
        if (!placedNewTile) return false;


        // 3) Check board geometry (center, adjacency, overlap) using the final letters
        if (!board.canPlaceWord(tilesToPlace, row, col, horizontal)) return false;

        // clear multipliers only where new tiles were placed
        // this code was resetting multipliers before the score was calculated making it all 1s
        /*
        for (int i = 0; i < tilesToPlace.length; i++) {
            if (isNew[i]) {
                int r = row + (horizontal ? 0 : i);
                int c = col + (horizontal ? i : 0);
                board.clearMultipliersAt(r, c);
            }
        }
        */


        // 4) CROSS-WORD VALIDATION & SCORING (same logic, now using correct letters)
        int crossScore = 0;

        for (int i = 0; i < tilesToPlace.length; i++) {
            if (!isNew[i]) continue; // only tiles placed this turn can form new cross-words

            int r = row + (horizontal ? 0 : i);
            int c = col + (horizontal ? i : 0);

            if (horizontal) {
                // main word is horizontal → cross-words are vertical
                boolean hasVertical =
                    (r > 0 && board.getTile(r - 1, c) != null) ||
                    (r < Board.SIZE - 1 && board.getTile(r + 1, c) != null);

                if (hasVertical) {
                    // build vertical word
                    StringBuilder sb = new StringBuilder();

                    // up
                    int rr = r - 1;
                    while (rr >= 0 && board.getTile(rr, c) != null) {
                        sb.insert(0, board.getTile(rr, c).getLetter());
                        rr--;
                    }

                    // center (the newly placed tile)
                    sb.append(tilesToPlace[i].getLetter());

                    // down
                    rr = r + 1;
                    while (rr < Board.SIZE && board.getTile(rr, c) != null) {
                        sb.append(board.getTile(rr, c).getLetter());
                        rr++;
                    }

                    String crossWord = sb.toString();

                    // Skip 1-letter cross-words
                    if (crossWord.length() >= 2) {
                        if (!dictionary.isValidWord(crossWord)) return false;
                    }


                    // score cross-word
                    int base = 0;
                    int wordMult = 1;

                    // up (existing)
                    rr = r - 1;
                    while (rr >= 0 && board.getTile(rr, c) != null) {
                        base += board.getTile(rr, c).getValue();
                        rr--;
                    }

                    // center (new tile with multipliers)
                    int letterScore = tilesToPlace[i].getValue(); // 0 if blank
                    if (tilesToPlace[i].isBlank()) {
                        // blanks never get letter multiplier
                        base += 0;
                    } else {
                        letterScore *= board.getLetterMultiplier(r, c);
                        base += letterScore;
                    }
                    wordMult *= board.getWordMultiplier(r, c);

                    // down (existing)
                    rr = r + 1;
                    while (rr < Board.SIZE && board.getTile(rr, c) != null) {
                        base += board.getTile(rr, c).getValue();
                        rr++;
                    }

                    crossScore += base * wordMult;
                }
            } else {
                // main word is vertical → cross-words are horizontal
                boolean hasHorizontal =
                    (c > 0 && board.getTile(r, c - 1) != null) ||
                    (c < Board.SIZE - 1 && board.getTile(r, c + 1) != null);

                if (hasHorizontal) {
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

                    // Skip 1-letter cross-words
                    if (crossWord.length() >= 2) {
                        if (!dictionary.isValidWord(crossWord)) return false;
                    }


                    int base = 0;
                    int wordMult = 1;

                    // left (existing)
                    cc = c - 1;
                    while (cc >= 0 && board.getTile(r, cc) != null) {
                        base += board.getTile(r, cc).getValue();
                        cc--;
                    }

                    // center
                    int letterScore = tilesToPlace[i].getValue();
                    if (tilesToPlace[i].isBlank()) {
                        base += 0;
                    } else {
                        letterScore *= board.getLetterMultiplier(r, c);
                        base += letterScore;
                    }
                    wordMult *= board.getWordMultiplier(r, c);

                    // right (existing)
                    cc = c + 1;
                    while (cc < Board.SIZE && board.getTile(r, cc) != null) {
                        base += board.getTile(r, cc).getValue();
                        cc++;
                    }

                    crossScore += base * wordMult;
                }
            }
        }

        // 5) MAIN WORD SCORING
        int mainBase = 0;
        int mainWordMultiplier = 1;

        for (int i = 0; i < tilesToPlace.length; i++) {
            int rPos = row + (horizontal ? 0 : i);
            int cPos = col + (horizontal ? i : 0);

            int letterScore = tilesToPlace[i].getValue(); // 0 for blanks

            if (isNew[i]) {
                if (tilesToPlace[i].isBlank()) {
                    // no letter multiplier for blanks
                    // but word multiplier still applies
                    mainWordMultiplier *= board.getWordMultiplier(rPos, cPos);
                } else {
                    letterScore *= board.getLetterMultiplier(rPos, cPos);
                    mainWordMultiplier *= board.getWordMultiplier(rPos, cPos);
                }
            }

            mainBase += letterScore;
        }

        int totalScore = mainBase * mainWordMultiplier + crossScore;

        // Bingo if player used 7 tiles from rack
        if (usedFromRack.size() == 7) {
            totalScore += 50;
        }

        // 6) COMMIT MOVE

        // place on board
        if (!board.placeWord(tilesToPlace, row, col, horizontal)) return false;

        // remove used tiles from rack
        for (Tile t : usedFromRack) {
            player.removeTile(t);
        }

        // add score and notify
        player.addScore(totalScore);
        notifyScoreChanged(player);

        // refill rack
        while (player.getTiles().size() < 7) {
            Tile t = tileBag.drawTile();
            if (t == null) break;
            player.addTile(t);
        }
        notifyPlayerTilesChanged(player);

        // ENDGAME RULE: tile bag empty AND player has no tiles left
        if (player.getTiles().isEmpty() && tileBag.remainingTiles() == 0) {
            endGame();
            return true;
        }


        // notify + next turn
        notifyWordPlaced(word, row, col, horizontal, player);

        // A legal move resets the pass counter
        consecutivePasses = 0;

        nextTurn();
        return true;
    }


    public void resign(Player p) {

        int index = players.indexOf(p);
        boolean wasCurrent = (index == currentPlayerIndex);

        p.setScore(0);
        players.remove(p);

        notifyPlayerRemoved(p);

        // Last player left will immediatetly win
        if (players.size() == 1) {
            endGame();
            return;
        }

        consecutivePasses = 0;

        // Now move to next player if the current one resigned
        if (wasCurrent)
            nextTurn(0);
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

        // 8. Notify UI (GamePanel) that game is over
        notifyGameOver(sorted);
    }

    public static int letterValue(char ch) {
        ch = Character.toUpperCase(ch);
        return LETTER_VALUES.getOrDefault(ch, 0);
    }


    public void registerPass() {
        consecutivePasses++;

        // If every player has passed twice in a row -> end game
        if (consecutivePasses >= players.size() * 2) {
            System.out.println("\nAll players passed twice → no moves left.");
            endGame();
            return;
        }

        nextTurn();
    }

    // Helper: checks adjacency like in Board.canPlaceWord
    private boolean touchesExisting(Tile[] tiles, int row, int col, boolean horizontal) {
        for (int i = 0; i < tiles.length; i++) {
            int r = row + (horizontal ? 0 : i);
            int c = col + (horizontal ? i : 0);

            if (board.getTile(r, c) != null) return true;

            int[][] adj = {{1,0},{-1,0},{0,1},{0,-1}};
            for (int[] d : adj) {
                int rr = r + d[0], cc = c + d[1];
                if (rr>=0 && rr<Board.SIZE && cc>=0 && cc<Board.SIZE) {
                    if (board.getTile(rr,cc) != null) return true;
                }
            }
        }
        return false;
    }

    public void exchangeTile(Player p, List<Boolean> exchangeMask)
    {
        if (exchangeMask != null)
        {
            for (int i = 0; i < exchangeMask.size(); i++)
            { if (exchangeMask.get(i) == true) exchangeSingleTile(p, i); }

            notifyPlayerTilesChanged(p);

            nextTurn();
        }
    }

    private void exchangeSingleTile(Player p, int index)
    {
        List<Tile> playerTiles = p.getTiles();
        Tile oldTile = playerTiles.get(index);
        playerTiles.set(index, tileBag.drawTile());
        tileBag.returnTile(oldTile);
    }

    /**
     * Check whether a player has ANY legal move left according to Scrabble rules.
     * If the tile bag is not empty, exchanging is possible, so moves exist.
     * If the bag is empty, the player must be able to place a valid word somewhere.
     */
    public boolean hasAnyMove(Player p) {

        // If the bag still has tiles → player can exchange → a move exists
        if (tileBag.remainingTiles() > 0)
            return true;

        // Build rack letter counts + blanks
        Map<Character, Integer> rackCounts = new HashMap<>();
        int blankCount = 0;

        for (Tile t : p.getTiles()) {
            if (t.isBlank()) {
                blankCount++;
            } else {
                rackCounts.merge(t.getLetter(), 1, Integer::sum);
            }
        }

        // Try every dictionary word
        for (String word : dictionary.getAllWords()) {
            String upper = word.toUpperCase();

            // Skip impossible sizes
            if (upper.length() == 0 || upper.length() > p.getTiles().size())
                continue;

            // Quick rack feasibility check
            if (!canFormWordFromRack(upper, rackCounts, blankCount))
                continue;

            // Try placing this word EVERYWHERE on the board
            for (int r = 0; r < Board.SIZE; r++) {
                for (int c = 0; c < Board.SIZE; c++) {

                    if (trySimulatedPlacement(upper, r, c, true)) return true;  // horizontal
                    if (trySimulatedPlacement(upper, r, c, false)) return true; // vertical
                }
            }
        }

        // No possible word anywhere
        return false;
    }


    /**
     * Check if the rack (counts + blanks) can make the given word.
     */
    private boolean canFormWordFromRack(String word,
                                        Map<Character, Integer> counts,
                                        int blanks) {
        int neededBlanks = 0;

        for (char ch : word.toCharArray()) {
            int have = counts.getOrDefault(ch, 0);
            if (have > 0) {
                counts.put(ch, have - 1);
            } else {
                neededBlanks++;
                if (neededBlanks > blanks) {
                    // Restore counts before returning
                    for (char c : word.toCharArray())
                        counts.put(c, counts.getOrDefault(c, 0) + 1);
                    return false;
                }
            }
        }

        // Restore counts
        for (char c : word.toCharArray())
            counts.put(c, counts.getOrDefault(c, 0) + 1);

        return true;
    }

    /**
     * Simulate placing a word WITHOUT modifying game state.
     * Must obey:
     *  - bounds
     *  - matching existing tiles
     *  - at least one new tile
     *  - adjacency rules (must touch existing tiles unless board empty)
     *  - first move must cover center
     */
    private boolean trySimulatedPlacement(String word, int row, int col, boolean horizontal) {

        Tile[] tiles = new Tile[word.length()];
        boolean[] isNew = new boolean[word.length()];
        boolean placedNewTile = false;

        for (int i = 0; i < word.length(); i++) {
            int r = row + (horizontal ? 0 : i);
            int c = col + (horizontal ? i : 0);

            if (r < 0 || r >= Board.SIZE || c < 0 || c >= Board.SIZE)
                return false;

            Tile existing = board.getTile(r, c);
            char needed = word.charAt(i);

            if (existing != null) {
                if (Character.toUpperCase(existing.getLetter()) != needed)
                    return false;

                tiles[i] = existing;
                isNew[i] = false;
            } else {
                tiles[i] = new Tile(needed, 0); // phantom tile
                isNew[i] = true;
                placedNewTile = true;
            }
        }

        // Must place at least one new tile
        if (!placedNewTile)
            return false;

        // Use board logic to enforce adjacency & overlap
        if (!board.canPlaceWord(tiles, row, col, horizontal))
            return false;

        return true;
    }

    public int getTileValue(char ch) {
        ch = Character.toUpperCase(ch);
        return LETTER_VALUES.getOrDefault(ch, 0);
    }

    private void notifyGameOver(List<Player> ranking) {
        for (GameListener l : listeners)
            l.onGameOver(ranking);
    }



    // Temporary placeholder so UI compiles
    public void saveGame(String filename) {
        System.out.println("saveGame() not implemented.");
    }

    public void loadGame(String filename) {
        System.out.println("loadGame() not implemented.");
    }



}
