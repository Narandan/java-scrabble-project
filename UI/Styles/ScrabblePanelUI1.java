package UI.Styles;

import javax.swing.JComponent;
import javax.swing.plaf.PanelUI;
import java.awt.Graphics;

public class ScrabblePanelUI1 extends PanelUI implements ScrabbleUI1
{
    public void installUI(JComponent c)
    {
        super.installUI(c);
        install(c);
        c.setBorder(null);
    }

    public void paint(Graphics g, JComponent c)
    {
        paintBackground(g, c);
        super.paint(g, c);
    }
}