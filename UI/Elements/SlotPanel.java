package UI.Elements;

import javax.swing.JPanel;
import javax.swing.UIManager;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import UI.Info.Colors;
import UI.Styles.Fonts;
import UI.Styles.ScrabblePanelUI1;

public class SlotPanel extends JPanel
{
    private TilePanel panel;
    private boolean highlighted = false;
    private boolean isLocked = false;
    private List<SlotListener> listeners = new ArrayList<>();
    private String label;
    private boolean unchecked = false;


    private Color originalColor;

    public SlotPanel()
    {
        setLayout(new BorderLayout());
        setUI(new ScrabblePanelUI1());
    }

    public SlotPanel(String label)
    {
        this();

        setLabel(label);
    }

    public SlotPanel(TilePanel panel)
    {
        this();

        setTilePanel(panel, false);
    }

    public TilePanel getPanel()
    { return panel; }

    public void setLabel(String label)
    {
        this.label = label;
        repaint();
    }

    public void setTilePanel(TilePanel panel, boolean unchecked)
    {
        if (this.panel != null) remove(this.panel);

        this.panel = panel;
        this.unchecked = unchecked;

        if (panel != null)
        {
            updateTileColor();
            add(panel, BorderLayout.CENTER);

            if (unchecked) notifyTileAdded(this);
        }
        else {
            if (unchecked) notifyTileRemoved(this);
        }

        revalidate();
        repaint();
    }



    public void addSlotListener(SlotListener listener)
    { listeners.add(listener); }

    private void notifyTileAdded(SlotPanel s)
    {
        for (SlotListener listener : listeners)
            listener.tileAdded(new SlotEvent(s));
    }

    private void notifyTileRemoved(SlotPanel s)
    {
        for (SlotListener listener : listeners)
            listener.tileRemoved(new SlotEvent(s));
    }

    public boolean isEmpty()
    { return this.panel == null; }

    public boolean isLocked()
    { return this.isLocked; }

    public void setLocked(boolean locked) 
    { 
        this.isLocked = locked; 

        updateTileColor();
    }

    private void updateTileColor()
    {
        if (this.panel != null)
            this.panel.setBackground(isLocked? Colors.TILE_LOCKED : Colors.TILE_UNLOCKED);
    }

    public void setHighlight(boolean toggle) 
    {
        if (this.highlighted == toggle) return;

        this.highlighted = toggle;
        if (toggle)
        {
            originalColor = getBackground();
            setBackground(UIManager.getColor("Panel.background").darker());
        }
        else setBackground(originalColor);

        repaint();
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        if (label != null)
        {
            Graphics2D g2d = (Graphics2D) g;

            int min = Math.min(getWidth(), getHeight());

            g2d.setFont(Fonts.SCRABBLE_FONT_1.deriveFont(min * 0.4f));

            g2d.drawString(label, getWidth()/2 - g2d.getFontMetrics().stringWidth(label)/2, getHeight()/2 + g2d.getFontMetrics().getAscent()/2 - 2);
        }
    }
    public boolean hasTile() {
        return this.panel != null;
    }

    public boolean isUnchecked() {
        return this.unchecked;
    }

    public void removeTilePanel() {
        if (this.panel != null) {
            remove(this.panel);
            this.panel = null;
            this.unchecked = false;
            revalidate();
            repaint();
        }
    }

}