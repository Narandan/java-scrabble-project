package UI.Elements;

import java.awt.Color;
import javax.swing.*;
import UI.Styles.*;
import scrabble.Tile;

public class TilePanel extends JPanel{
    private Tile tile;
    private JLabel letterLabel;
    private JLabel valueLabel;
    //private LetterPieceMousePanel dragPanel;

    public TilePanel(Tile piece)
    {
        this.tile = piece;

        setLayout(null);
        setBackground(Color.GREEN);
        setUI(new LetterPieceUI1());

        letterLabel = new JLabel();
        letterLabel.setBackground(Color.YELLOW);
        add(letterLabel);

        valueLabel = new JLabel();
        valueLabel.setBackground(Color.CYAN);
        add(valueLabel);

        setTile(piece);
    }

    /*
    private class LetterPieceMousePanel extends TilePanel
    {
        public LetterPieceMousePanel(TilePanel original)
        {
            super(original.getTile());
            setSize(original.getSize());
        }
    }*/

    public void setTile(Tile piece)
    {
        this.tile = piece;
        updateLabels();
    }

    private void updateLabels()
    {
        if (tile != null)
        {
            letterLabel.setText(String.valueOf(tile.getLetter()));
            valueLabel.setText(Integer.toString(tile.getScore()));
        }
    }

    public Tile getTile()
    {
        return tile;
    }
}
