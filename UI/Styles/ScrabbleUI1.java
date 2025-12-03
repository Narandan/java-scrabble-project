package UI.Styles;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;

public interface ScrabbleUI1
{
    default void paintBackground(Graphics g, JComponent c)
    {
        Graphics2D g2d = (Graphics2D) g;
        Color gp = c.getBackground();
        g2d.setPaint(gp);
        int minSize = Math.min(c.getWidth(), c.getHeight());
        g2d.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), (int) (minSize*.15), (int) (minSize*.15));
    }

    default void install(JComponent c)
    {
        c.setOpaque(false);
        c.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        c.setForeground(Color.WHITE);
        c.setFont(Fonts.SCRABBLE_FONT_1);
    }   
}
