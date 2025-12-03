package UI.Styles;

import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicTextFieldUI;

public class ScrabbleTextFieldUI1 extends BasicTextFieldUI implements ScrabbleUI1{    
    public void installUI(JComponent c) {
        super.installUI(c);
        install(c);

        JTextField field = (JTextField) c;
        field.setCaretColor(field.getForeground());
    }
}