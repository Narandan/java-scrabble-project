package UI.Elements;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Window;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;
import UI.Styles.*;
import scrabble.Tile;

public class TileMousePanel extends JWindow {
    public TileMousePanel(Window owner, Tile tile, Dimension size) {
        super(owner);

        JPanel panel = new JPanel();
        panel.setBackground(Color.GREEN);
        panel.setPreferredSize(size);
        panel.setUI(new LetterPieceUI1());

        JLabel letterLabel = new JLabel(String.valueOf(tile.getLetter()));
        JLabel valueLabel = new JLabel(Integer.toString(tile.getScore()));

        panel.add(letterLabel);
        panel.add(valueLabel);

        getContentPane().add(panel);

        setAlwaysOnTop(true);
        setFocusableWindowState(false);
        pack();
    }
}
