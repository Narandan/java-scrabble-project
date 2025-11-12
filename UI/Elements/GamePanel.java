package UI.Elements;

import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import scrabble.*;
import java.awt.Dimension;

public class GamePanel extends JPanel
{
    private Game game;
    private BoardPanel boardPanel;
    private HashMap<Player, PlayerDeckPanel> playerPanels = new HashMap<>();

    public GamePanel(Game game)
    {
        this.game = game;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        boardPanel = new BoardPanel(game.getBoard());
        boardPanel.setMaximumSize(new Dimension(400,400));
        connectGameEvents();

        //test statement
        boardPanel.setSlot(0,0, new TilePanel(new Tile('a', 5)));

        add(boardPanel);
    }

    private void connectGameEvents()
    {
        /*requires Game object connection
        game.addGameListener(new GameListener()
        {
            public void playerAdded(GameEvent e)
            {
                addPlayer(e.getPlayer());
            }

            public void changedTurn(GameEvent e)
            {
                playerPanels.get(e.getPlayer()).setVisible(false);
                playerPanels.get(game.getCurrentPlayer()).setVisible(true);
            }
        });*/
    }

    public void addPlayer(Player player)
    {
        if (playerPanels.containsKey(player))
        {
            playerPanels.remove(player);
        }

        PlayerDeckPanel playerPanel = new PlayerDeckPanel(player);
        playerPanel.setMaximumSize(new Dimension(480, 60));

        playerPanels.put(player, playerPanel);
        playerPanel.setVisible(player == game.getCurrentPlayer());
        add(playerPanel);
    }
}
