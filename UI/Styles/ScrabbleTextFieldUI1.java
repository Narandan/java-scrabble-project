package UI.Styles;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTextFieldUI;

import java.awt.*;

public class ScrabbleTextFieldUI1 extends BasicTextFieldUI {
    private static final Font font = Fonts.SCRABBLE_FONT_1;
    
    public void installUI(JComponent c) {
        super.installUI(c);
        
        JTextField textArea = (JTextField) c;
        textArea.setOpaque(false);
        textArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        textArea.setForeground(Color.WHITE);
        c.setFont(font);

        textArea.setCaretColor(textArea.getForeground());
    }
}