package UI.Elements;

import scrabble.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.BorderFactory;

import UI.Styles.ScrabblePanelUI1;

public class PlayerDeckPanel extends JPanel
{
    private Player player;
    private SlotPanel[] slots;

    public PlayerDeckPanel(Player player)
    {
        this.player = player;

        setSize(new Dimension(480,60));

        slots = new SlotPanel[8];
        setLayout(new GridLayout(1,8, 3, 3));
        setUI(new ScrabblePanelUI1());
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setBackground(Color.DARK_GRAY);

        for (int i = 0; i < 8; i++)
        {
            SlotPanel piecePanel = new SlotPanel();
            piecePanel.setBackground(Color.LIGHT_GRAY);
            piecePanel.setSize(getSize());
            add(piecePanel);

            slots[i] = piecePanel;
        }

        connectPlayerEvents();
    }

    private void connectPlayerEvents()
    {
        /*requires Player object connection
        player.addPlayerListener(new PlayerListener()
        {
            public void tileAdded(PlayerEvent e)
            {
                setSlot(e.getIndex(), e.getTile());
            }

            public void tileRemoved(PlayerEvent e)
            {
                setSlot(e.getIndex(), null);
            }

            ...whatever other interface methods would be added for a player
        });*/
    }

    public void setSlot(int x, TilePanel piecePanel)
    {
        slots[x].setTilePanel(piecePanel);
    }
}
