package UI.Styles;

import javax.swing.JComponent;
import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.plaf.basic.BasicCheckBoxUI;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import UI.Info.Colors;

public class ScrabbleCheckBoxUI1 extends BasicCheckBoxUI implements ScrabbleUI1
{
    public void installUI(JComponent c)
	{
        super.installUI(c);
		install(c);
        
        AbstractButton checkBox = (AbstractButton) c;
        checkBox.setIcon(new Icon()
		{
			private int size = 16;
			
			public void paintIcon(Component comp, Graphics g, int x, int y)
			{
				Graphics2D g2d = (Graphics2D) g;
                
				g2d.setColor(Colors.BACKGROUND_2);
				g2d.fillRoundRect(x, y, 16, 16, 4, 4);
				g2d.setColor(Colors.BACKGROUND_3);
				g2d.setStroke(new BasicStroke(1.5f));
				g2d.drawRoundRect(x, y, 16, 16, 4, 4);

				if (((AbstractButton) comp).isSelected())
				{
					g2d.setColor(comp.getForeground());
					g2d.setStroke(new BasicStroke(2.0f));
					g2d.drawLine(x + 4, y + 8, x + 7, y + 11);
					g2d.drawLine(x + 7, y + 11, x + 13, y + 5);
				}
			}

			public int getIconWidth()
			{ return size; }

			public int getIconHeight()
			{ return size; }
		});
    }
}
