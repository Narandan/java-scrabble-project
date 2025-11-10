package scrabble;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TileBag {
    private List<Tile> tiles;

    public TileBag() {
        tiles = new ArrayList<>();
        // TODO: initialize tiles with correct letter distribution
        // Example: tiles.add(new Tile('A', 1));
        Collections.shuffle(tiles);
    }

    public Tile drawTile() {
        if (tiles.isEmpty()) return null;
        return tiles.remove(0);
    }

    public int remainingTiles() {
        return tiles.size();
    }
}
