package scrabble;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private List<Tile> tiles;
    private int score;

    public Player(String name) {
        this.name = name;
        this.tiles = new ArrayList<>();
        this.score = 0;
    }

    public String getName() { return name; }
    public List<Tile> getTiles() { return tiles; }
    public int getScore() { return score; }

    public void addTile(Tile tile) { tiles.add(tile); }
    public void removeTile(Tile tile) { tiles.remove(tile); }

    public void addScore(int points) { score += points; }

    public void setScore(int s) { this.score = s; }

}

