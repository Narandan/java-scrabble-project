package scrabble;

public class Tile {

    private final boolean isBlank;     // true if this tile is a blank tile
    private char assignedLetter;       // letter the blank represents (A–Z)
    private final char letter;         // original tile letter (for normal tiles)
    private final int value;           // always 0 for blanks

    // Constructor for regular tiles
    public Tile(char letter, int value) {
        this.isBlank = false;
        this.letter = Character.toUpperCase(letter);
        this.value = value;
        this.assignedLetter = this.letter; // same as normal
    }

    // Constructor for blank tiles (no letter yet)
    public Tile() {
        this.isBlank = true;
        this.letter = '_';      // internal marker
        this.value = 0;         // blank tiles always score 0
        this.assignedLetter = '_'; // not yet chosen
    }

    public boolean isBlank() {
        return isBlank;
    }

    public void assignLetter(char c) {
        if (isBlank) {
            this.assignedLetter = Character.toUpperCase(c);
        }
    }

    // For game logic: for blanks use assignedLetter, otherwise original letter
    public char getLetter() {
        return isBlank ? assignedLetter : letter;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        if (isBlank) {
            return "_" + "(" + assignedLetter + ")";
        }
        return letter + "(" + value + ")";
    }
}
