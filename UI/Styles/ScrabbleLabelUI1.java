package UI.Styles;

import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicLabelUI;
import java.awt.Graphics;

public class ScrabbleLabelUI1 extends BasicLabelUI implements ScrabbleUI1
{
    public void installUI(JComponent c)
    {
        super.installUI(c);
        install(c);
    }

    public void paint(Graphics g, JComponent c)
    {
        paintBackground(g, c);
        super.paint(g, c);
    }
}