package scrabble;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TileBag {
    private final List<Tile> tiles;

    public TileBag() {
        tiles = new ArrayList<>();

        // Official English Scrabble distribution

        // 2 blanks (0 points)
        addBlanks(2);

        addTiles('A', 1, 9);
        addTiles('B', 3, 2);
        addTiles('C', 3, 2);
        addTiles('D', 2, 4);
        addTiles('E', 1, 12);
        addTiles('F', 4, 2);
        addTiles('G', 2, 3);
        addTiles('H', 4, 2);
        addTiles('I', 1, 9);
        addTiles('J', 8, 1);
        addTiles('K', 5, 1);
        addTiles('L', 1, 4);
        addTiles('M', 3, 2);
        addTiles('N', 1, 6);
        addTiles('O', 1, 8);
        addTiles('P', 3, 2);
        addTiles('Q', 10, 1);
        addTiles('R', 1, 6);
        addTiles('S', 1, 4);
        addTiles('T', 1, 6);
        addTiles('U', 1, 4);
        addTiles('V', 4, 2);
        addTiles('W', 4, 2);
        addTiles('X', 8, 1);
        addTiles('Y', 4, 2);
        addTiles('Z', 10, 1);

        Collections.shuffle(tiles);
    }

    private void addTiles(char letter, int value, int count) {
        for (int i = 0; i < count; i++) {
            tiles.add(new Tile(letter, value));
        }
    }

    private void addBlanks(int count) {
        for (int i = 0; i < count; i++) {
            tiles.add(new Tile());  // blank constructor
        }
    }

    public Tile drawTile() {
        if (tiles.isEmpty()) return null;
        return tiles.remove(0);
    }

    public void returnTile(Tile t) {
        tiles.add(t);
    }

    public int remainingTiles() {
        return tiles.size();
    }
}
