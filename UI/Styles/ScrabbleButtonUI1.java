package UI.Styles;

import javax.swing.*;
import java.awt.*;

import javax.swing.plaf.basic.BasicButtonUI;

public class ScrabbleButtonUI1 extends BasicButtonUI
{
    private static final Font font = Fonts.SCRABBLE_FONT_1;

    public void installUI(JComponent c)
    {
        super.installUI(c);
        
        AbstractButton button = (AbstractButton) c;
        button.setOpaque(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
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
        g2d.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 15, 15);
    }
}