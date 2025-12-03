package UI.Elements;

import javax.swing.JPanel;
import java.awt.Container;
import java.awt.CardLayout;

public class CardJumpPanel extends JPanel implements Jumpable
{
    private String name;

    public CardJumpPanel(Container parent, String name)
    {
        super();
        this.name = name;

        if(parent.getLayout() instanceof CardLayout) parent.add(this, name);
        else throw new WrongParentLayoutException("CardJumpPanel parent must have CardLayout.");
    }

    public String getName()
    { return name; }

    public static class WrongParentLayoutException extends RuntimeException
    {
        public WrongParentLayoutException(String msg)
        { super(msg); }
    }
}
