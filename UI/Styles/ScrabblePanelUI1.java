package UI.Styles;

import javax.swing.*;
import java.awt.*;
import javax.swing.plaf.PanelUI;

public class ScrabblePanelUI1 extends PanelUI
{
    public void installUI(JComponent c)
    {
        super.installUI(c);
        
        JPanel panel = (JPanel) c;
        panel.setOpaque(false);
    }

    public void paint(Graphics g, JComponent c)
    {
        paintBackground(g, c);
        super.paint(g, c);
    }

    private void paintBackground(Graphics g, JComponent c)
    {
        Graphics2D g2d = (Graphics2D) g;
        Color gp = c.getBackground();
        g2d.setPaint(gp);
        int minSize = Math.min(c.getWidth(), c.getHeight());
        g2d.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), (int) (minSize*.15), (int) (minSize*.15));
    }
}