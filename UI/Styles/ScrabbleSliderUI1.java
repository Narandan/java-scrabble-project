package UI.Styles;

import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import UI.Info.Colors;

public class ScrabbleSliderUI1 extends BasicSliderUI 
{
    public void installUI(JComponent c)
    { super.installUI(c); }

    public void paint(Graphics g, JComponent c)
    { super.paint(g, c); }

    public void paintTrack(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;
        Rectangle rect = trackRect;
        g2d.setColor(Colors.SCROLLBAR_TRACK);
        g2d.fillRoundRect(rect.x, rect.y+rect.height/2-rect.height/8, rect.width, rect.height/4, 5,5);
    }

    public void paintLabels(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Colors.WORD_1);

        int min = slider.getMinimum();
        int max = slider.getMaximum();
        int majorSpacing = slider.getMajorTickSpacing();
        int trackTop = trackRect.y + trackRect.height / 2;
        
        for (int i = min; i <= max; i += majorSpacing) {
            int x = xPositionForValue(i);
            g2d.drawString(String.valueOf(i), x-3, trackTop + 20);
        }
    }

    public void paintThumb(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;
        Rectangle thumbBounds = thumbRect;
        g2d.setColor(Colors.SCROLLBAR_THUMB);
        g2d.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height, 8, 8);
    }

    public void paintTicks(Graphics g)
    { paintMajorTicksForHorizSlider(g); }

    public void paintMajorTicksForHorizSlider(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Colors.SCROLLBAR_TICK);
        
        int min = slider.getMinimum();
        int max = slider.getMaximum();
        int majorSpacing = slider.getMajorTickSpacing();
        int trackTop = trackRect.y + trackRect.height / 2;
        
        for (int i = min; i <= max; i += majorSpacing)
        {
            int x = xPositionForValue(i);
            g2d.fillRoundRect(x-2, trackTop-5, 4, 10, 2, 2);
        }
    }

    public void paintFocus(Graphics g) { }
}