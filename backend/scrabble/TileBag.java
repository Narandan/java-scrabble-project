package scrabble;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TileBag {
    private List<Tile> tiles;

    public TileBag() {
        tiles = new ArrayList<>();

        // Official Scrabble tile distribution (without blanks for now)
        addTiles('A', 9, 1);
        addTiles('B', 2, 3);
        addTiles('C', 2, 3);
        addTiles('D', 4, 2);
        addTiles('E', 12, 1);
        addTiles('F', 2, 4);
        addTiles('G', 3, 2);
        addTiles('H', 2, 4);
        addTiles('I', 9, 1);
        addTiles('J', 1, 8);
        addTiles('K', 1, 5);
        addTiles('L', 4, 1);
        addTiles('M', 2, 3);
        addTiles('N', 6, 1);
        addTiles('O', 8, 1);
        addTiles('P', 2, 3);
        addTiles('Q', 1, 10);
        addTiles('R', 6, 1);
        addTiles('S', 4, 1);
        addTiles('T', 6, 1);
        addTiles('U', 4, 1);
        addTiles('V', 2, 4);
        addTiles('W', 2, 4);
        addTiles('X', 1, 8);
        addTiles('Y', 2, 4);
        addTiles('Z', 1, 10);

        // We will add blanks in a later step.

        Collections.shuffle(tiles);
    }

    private void addTiles(char letter, int count, int value) {
        for (int i = 0; i < count; i++) {
            tiles.add(new Tile(letter, value));
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

    // For save/load support
    public List<Tile> getTiles() {
        return tiles;
    }

    public void clear() {
        tiles.clear();
    }
}
