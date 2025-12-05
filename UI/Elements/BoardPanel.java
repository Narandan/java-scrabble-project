package UI.Elements;

import javax.swing.JPanel;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import UI.Styles.SquareGridLayout;
import scrabble.Board;
import UI.Info.Colors;

public class BoardPanel extends JPanel
{
    private SlotPanel[][] slots;
    private List<BoardListener> listeners = new ArrayList<>();
    
    private static HashMap<Integer, MultiplierInfo> specialSquares = new HashMap<>();

    private static int size = Board.SIZE;
    
    static
    {
        specialSquares.put(7, new MultiplierInfo(Colors.SLOT_TRIPLE_WORD, "TW")); // Triple Word
        specialSquares.put(6, new MultiplierInfo(Colors.SLOT_DOUBLE_WORD, "DW")); // Double Word
        specialSquares.put(3, new MultiplierInfo(Colors.SLOT_TRIPLE_LETTER, "TL")); // Triple Letter
        specialSquares.put(2, new MultiplierInfo(Colors.SLOT_DOUBLE_LETTER, "DL")); // Double Letter
    }

    public BoardPanel(Board board)
    {
        slots = new SlotPanel[size][size];
        setLayout(new SquareGridLayout(15, 15, 2, 2));

        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {
                final int x = i;
                final int y = j;

                SlotPanel piecePanel = new SlotPanel();
                
                if(board.getTile(i, j) != null)
                    piecePanel.setTilePanel(new TilePanel(board.getTile(i, j)), false);

                piecePanel.addSlotListener(new SlotListener()
                {
                    public void tileAdded(SlotEvent s)
                    { notifyBoardUpdated(x, y); }

                    public void tileRemoved(SlotEvent s) 
                    { notifyBoardUpdated(x, y); }
                });

                int letterMultiplier = board.getLetterMultiplier(i, j);
                int wordMultiplier = board.getWordMultiplier(i, j)+4;

                if(letterMultiplier > 1 || wordMultiplier % 4 > 1)
                {
                    MultiplierInfo info = specialSquares.get(letterMultiplier > wordMultiplier % 4? letterMultiplier : wordMultiplier);
                    piecePanel.setBackground(info.color);
                    piecePanel.setLabel(info.slotLabel);
                }
                else piecePanel.setBackground(Colors.SLOT_NORMAL);

                slots[i][j] = piecePanel;
                add(piecePanel);
            }
        }
    }

    private void notifyBoardUpdated(int x, int y)
    {
        for (BoardListener listener : listeners)
            listener.boardUpdated(new BoardEvent(x, y));
    }

    public void addBoardListener(BoardListener listener)
    { listeners.add(listener); }   

    public void setSlot(int x, int y, TilePanel piecePanel, boolean unchecked)
    { slots[x][y].setTilePanel(piecePanel, unchecked); }
    
    public SlotPanel getSlot(int x, int y)
    { return slots[x][y]; }

    private static class MultiplierInfo
    {
        public Color color;
        public String slotLabel;

        public MultiplierInfo(Color color, String slotLabel)
        {
            this.color = color;
            this.slotLabel = slotLabel;
        }
    }

    public void clearTemporaryTiles() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                SlotPanel slot = slots[i][j];

                // A temporary tile is one that was not confirmed yet ("unchecked")
                if (slot.hasTile() && slot.isUnchecked()) {
                    slot.removeTilePanel();  // clears tile visually
                }
            }
        }
    }

}