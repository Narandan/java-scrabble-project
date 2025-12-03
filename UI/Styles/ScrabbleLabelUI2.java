package UI.Styles;

import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicLabelUI;
import java.awt.Color;
import java.awt.Graphics;

public class ScrabbleLabelUI2 extends BasicLabelUI {
    public void installUI(JComponent c)
    {
        super.installUI(c);
        
        c.setForeground(Color.WHITE);
        c.setFont(Fonts.SCRABBLE_FONT_1);
    }

    public void paint(Graphics g, JComponent c)
    {
        super.paint(g, c);
    }
}
