package UI.Elements;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JPanel;
import scrabble.*;

public class BoardPanel extends JPanel
{
    private Board board;
    private SlotPanel[][] slots;

    public BoardPanel(Board board)
    {
        this.board = board;
        int boardSize = board.getBoard().length;
        slots = new SlotPanel[boardSize][boardSize];
        setLayout(new GridLayout(boardSize,boardSize, 3, 3));

        for (int i = 0; i < boardSize; i++)
        {
            for (int j = 0; j < boardSize; j++)
            {
                SlotPanel piecePanel = new SlotPanel();
                piecePanel.setBackground(Color.LIGHT_GRAY);
                add(piecePanel);

                slots[i][j] = piecePanel;
            }
        }

        connectBoardEvents();
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
}