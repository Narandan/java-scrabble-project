package UI.Elements;

import java.awt.BorderLayout;
import javax.swing.*;
import UI.Styles.*;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class SlotPanel extends JPanel{
    private TilePanel panel;
    private boolean highlighted = false;
    private boolean isLocked = false;
    private List<SlotListener> listeners = new ArrayList<>();

    private Color originalColor;

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

    public TilePanel getPanel()
    {
        return panel;
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
            notifyTileAdded(this);
        }
        else
        {
            System.out.println("TilePanel set to null");
            notifyTileRemoved(this);
        }
    }

    public void addSlotListener(SlotListener listener)
    {
        listeners.add(listener);
    }

    private void notifyTileAdded(SlotPanel s)
    {
        for(SlotListener listener : listeners)
        {
            listener.tileAdded(new SlotEvent(s));
        }
    }

    private void notifyTileRemoved(SlotPanel s)
    {
        for(SlotListener listener : listeners)
        {
            listener.tileRemoved(new SlotEvent(s));
        }
    }

    public boolean isEmpty() {
        return this.panel == null;
    }

    public boolean isLocked() {
        return this.isLocked;
    }

    public void setLocked(boolean locked) {
        this.isLocked = locked;
    }

    public void setHighlight(boolean on) {
        if (this.highlighted == on) return;


        this.highlighted = on;
        if (on) {
            originalColor = getBackground();
            setBackground(UIManager.getColor("Panel.background").darker());
        } else {
            setBackground(originalColor);
        }
        repaint();
    }
}
