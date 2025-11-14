package scrabble;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TileBag {
    private List<Tile> tiles;

    public TileBag() {
        tiles = new ArrayList<>();
        // Add letter distribution here (example A=9, B=2, etc.)
        for (char c = 'A'; c <= 'Z'; c++) {
            tiles.add(new Tile(c, 1)); // All letters value 1 for now
        }
        Collections.shuffle(tiles);
    }

    public Tile drawTile() {
        if (tiles.isEmpty()) return null;
        return tiles.remove(0);
    }

    public int remainingTiles() { return tiles.size(); }
}
