package UI.Elements;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import UI.Info.Colors;
import UI.Styles.ScrabblePanelUI1;
import UI.Styles.SquareGridLayout;

public class DeckPanel extends JPanel {
    protected SlotPanel[] slots;

    public DeckPanel(boolean locked)
    {
        slots = new SlotPanel[8];
        setLayout(new SquareGridLayout(1, 7, 4, 4));
        setUI(new ScrabblePanelUI1());
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setBackground(Colors.BACKGROUND_4);

        for (int i = 0; i < 7; i++)
        {
            SlotPanel piecePanel = new SlotPanel();
            piecePanel.setLocked(locked);
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
