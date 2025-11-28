package UI.Elements;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JPanel;

import UI.Styles.SquareGridLayout;
import scrabble.*;

import java.util.ArrayList;
import java.util.List;

public class BoardPanel extends JPanel
{
    private SlotPanel[][] slots;
    private List<BoardListener> listeners = new ArrayList<>();

    private static int size = Board.SIZE;
    

    public BoardPanel()
    {
        slots = new SlotPanel[size][size];
        setLayout(new SquareGridLayout());

        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {
                final int x = i;
                final int y = j;

                SlotPanel piecePanel = new SlotPanel();
                piecePanel.addSlotListener(new SlotListener() {
                    public void tileAdded(SlotEvent s) {
                        notifyBoardUpdated(x, y);
                    }

                    public void tileRemoved(SlotEvent s) {
                        System.out.println("tile removed at (" + x + ", " + y + ")");
                        notifyBoardUpdated(x, y);
                    }
                });
                piecePanel.setBackground(Color.LIGHT_GRAY);
                add(piecePanel);

                slots[i][j] = piecePanel;
            }
        }

        connectBoardEvents();
    }

    private void notifyBoardUpdated(int x, int y)
    {
        for (BoardListener listener : listeners)
        {
            listener.boardUpdated(new BoardEvent(x, y));
        }
    }

    public void addBoardListener(BoardListener listener)
    {
        listeners.add(listener);
    }   

    private void connectBoardEvents()
    {
        /*requires Board object connection
        board.addBoardListener(new BoardListener() {
            {
                //this one could be something like placedword
                public void placedTile(BoardEvent e)
                {
                    setSlot(e.getX(), e.getY(), new TilePanel(e.getTile()));
                }
            }
        })*/
    }

    public void setSlot(int x, int y, TilePanel piecePanel)
    {
        slots[x][y].setTilePanel(piecePanel);
    }
    
    public SlotPanel getSlot(int x, int y)
    {
        return slots[x][y];
    }
}