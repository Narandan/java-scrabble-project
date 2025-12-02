package UI.Styles;

import javax.swing.*;
import javax.swing.plaf.basic.BasicCheckBoxUI;
import java.awt.*;

public class ScrabbleCheckBoxUI1 extends BasicCheckBoxUI {
    private static final Font font = Fonts.SCRABBLE_FONT_1;
    
    public void installUI(JComponent c) {
        super.installUI(c);
        
        AbstractButton checkBox = (AbstractButton) c;
        checkBox.setOpaque(false);
        checkBox.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        checkBox.setForeground(Color.WHITE);
        c.setFont(font);

        checkBox.setIcon(new Icon() {
			private final int size = 16;
			@Override
			public void paintIcon(Component c, Graphics g, int x, int y) {
				Graphics2D g2d = (Graphics2D) g.create();
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
				g2d.setColor(Colors.BACKGROUND_2);
				g2d.fillRoundRect(x, y, size, size, 4, 4);
				g2d.setColor(Colors.BACKGROUND_3);
				g2d.setStroke(new BasicStroke(1.5f));
				g2d.drawRoundRect(x, y, size, size, 4, 4);

				if (c instanceof AbstractButton && ((AbstractButton) c).isSelected()) {
					g2d.setColor(Color.WHITE);
					g2d.setStroke(new BasicStroke(2.0f));
					g2d.drawLine(x + 4, y + 8, x + 7, y + 11);
					g2d.drawLine(x + 7, y + 11, x + 13, y + 5);
				}

				g2d.dispose();
			}

			@Override
			public int getIconWidth() {
				return size;
			}

			@Override
			public int getIconHeight() {
				return size;
			}
		});
    }
}
