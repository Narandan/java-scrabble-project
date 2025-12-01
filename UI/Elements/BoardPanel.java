package UI.Elements;

import java.awt.*;
import javax.swing.*;
import UI.Styles.*;
import scrabble.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class BoardPanel extends JPanel
{
    private SlotPanel[][] slots;
    private List<BoardListener> listeners = new ArrayList<>();
    
    private static HashMap<Integer, Color> specialSquares = new HashMap<>();

    private static int size = Board.SIZE;
    
    static
    {
        specialSquares.put(7, Colors.SLOT_TRIPLE_WORD); // Triple Word
        specialSquares.put(6, Colors.SLOT_DOUBLE_WORD); // Double Word
        specialSquares.put(3, Colors.SLOT_TRIPLE_LETTER); // Triple Letter
        specialSquares.put(2, Colors.SLOT_DOUBLE_LETTER); // Double Letter
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
                    piecePanel.setBackground(specialSquares.get(letterMultiplier > wordMultiplier % 4? letterMultiplier : wordMultiplier));
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
}