package UI.Elements;

import javax.swing.JPanel;

public class CardJumpPanel extends JPanel implements Jumpable
{
    private String name;

    public CardJumpPanel(String name)
    {
        super();
        this.name = name;
    }

    public String getName()
    { return name; }
}
