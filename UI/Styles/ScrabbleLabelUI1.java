package UI.Styles;

import javax.swing.*;
import java.awt.*;

import javax.swing.plaf.basic.BasicLabelUI;

public class ScrabbleLabelUI1 extends BasicLabelUI
{
    private static final Font font = Fonts.SCRABBLE_FONT_1;

    public void installUI(JComponent c)
    {
        super.installUI(c);
        
        JLabel label = (JLabel) c;
        label.setOpaque(false);
        label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        c.setFont(font);
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