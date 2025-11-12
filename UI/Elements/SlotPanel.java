package UI.Elements;

import java.awt.BorderLayout;
import javax.swing.*;
import UI.Styles.*;

public class SlotPanel extends JPanel{
    private TilePanel panel;

    public SlotPanel()
    {
        setLayout(new BorderLayout());
        setUI(new ScrabblePanelUI1());
    }

    public SlotPanel(TilePanel panel)
    {
        this();
        setTilePanel(panel);
    }

    public void setTilePanel(TilePanel panel)
    {
        if(this.panel != null)
        {
            remove(this.panel);
        }

        this.panel = panel;
        if (panel != null)
        {
            add(panel, BorderLayout.CENTER);
        }
    }
}
