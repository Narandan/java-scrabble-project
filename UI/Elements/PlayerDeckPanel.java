package UI.Elements;

import java.util.List;
import scrabble.Game;
import scrabble.Player;
import scrabble.Tile;
import scrabble.GameListener;

public class PlayerDeckPanel extends DeckPanel
{
    private Game game;
    private Player player;
    
    public PlayerDeckPanel(Game game, Player player)
    {
        super(false);

        this.game = game;
        this.player = player;

        refreshDeck();

        connectEvents();
    }

    private void connectEvents()
    {
        game.addListener(new GameListener()
        {
            public void onPlayerAdded(Player player) {}
            public void onPlayerTilesChanged(Player player) 
            { if (player == PlayerDeckPanel.this.player) refreshDeck(); }
            public void onScoreChanged(Player player) {}
            public void onPlayerRemoved(Player player) {}
            public void onGameOver(List<Player> finalRanking) {}
            public void onWordPlaced(String word, int row, int col, boolean horizontal, Player player) {}
            public void onTurnChanged(Player currentPlayer) {}
        });
    }

    private void refreshDeck()
    {
        List<Tile> tiles = player.getTiles();

        for(int i=0 ; i<tiles.size(); i++)
            if(getSlot(i) == null || getSlot(i).getTile() != tiles.get(i))
                setSlot(i, new TilePanel(tiles.get(i)));
    }
}
