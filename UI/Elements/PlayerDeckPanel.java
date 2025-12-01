package UI.Elements;

import javax.swing.*;
import UI.Styles.*;

public class PlayerDeckPanel extends JPanel
{
    private SlotPanel[] slots;

    public PlayerDeckPanel()
    {
        slots = new SlotPanel[8];
        setLayout(new SquareGridLayout(1, 7, 4, 4));
        setUI(new ScrabblePanelUI1());
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setBackground(Colors.BACKGROUND_4);

        for (int i = 0; i < 7; i++)
        {
            SlotPanel piecePanel = new SlotPanel();
            piecePanel.setBackground(Colors.SLOT_DECK);
            piecePanel.setSize(getSize());
            add(piecePanel);

            slots[i] = piecePanel;
        }
    }

    public TilePanel getSlot(int x)
    { return slots[x].getPanel(); }

    public void setSlot(int x, TilePanel piecePanel)
    { slots[x].setTilePanel(piecePanel, true); }
}
