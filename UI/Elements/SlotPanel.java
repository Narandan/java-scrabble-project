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

    // These two fields must ALWAYS stay in sync
    private boolean isLocked = false;     // permanent tile (previous turns)
    private boolean unchecked = false;    // temporary tile (this turn only)

    private List<SlotListener> listeners = new ArrayList<>();
    private String label;

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
    { 
        return panel; 
    }

    public void setLabel(String label)
    {
        this.label = label;
        repaint();
    }

    /**
     * Place or remove a tile in this slot.
     * `unchecked = true` means tile was placed THIS TURN (temporary).
     * `unchecked = false` means permanent tile or UI refresh.
     */
    public void setTilePanel(TilePanel panel, boolean unchecked)
    {
        // Prevent modifying locked tiles from previous turns
        if (this.isLocked) {
            return;
        }

        if (this.panel != null) {
            remove(this.panel);
        }

        this.panel = panel;
        this.unchecked = unchecked;

        if (panel != null)
        {
            updateTileColor();
            add(panel, BorderLayout.CENTER);

            if (unchecked) notifyTileAdded(this);
        }
        else
        {
            if (unchecked) notifyTileRemoved(this);
        }

        revalidate();
        repaint();
    }

    public void addSlotListener(SlotListener listener)
    { 
        listeners.add(listener); 
    }

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
    { 
        return this.panel == null; 
    }

    public boolean isLocked()
    { 
        return this.isLocked; 
    }

    /**
     * Lock tile so it cannot be removed or replaced.
     * Used at end of turn.
     */
    public void setLocked(boolean locked) 
    { 
        this.isLocked = locked; 

        if (locked) {
            this.unchecked = false;   // locked tiles are NEVER temporary
        }

        updateTileColor();
    }

    private void updateTileColor()
    {
        if (this.panel != null)
            this.panel.setBackground(isLocked ? Colors.TILE_LOCKED : Colors.TILE_UNLOCKED);
    }

    public boolean hasTile() {
        return this.panel != null;
    }

    public boolean isUnchecked() {
        return this.unchecked;
    }

    /**
     * Remove ONLY temporary tiles.
     * Locked tiles from previous turns must never be removed.
     */
    public void removeTilePanel() {

        // Prevent deleting permanent tiles
        if (isLocked) return;

        if (this.panel != null) {
            remove(this.panel);
            this.panel = null;
            this.unchecked = false;
            revalidate();
            repaint();
        }
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

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        if (label != null)
        {
            Graphics2D g2d = (Graphics2D) g;
            int min = Math.min(getWidth(), getHeight());

            g2d.setFont(Fonts.SCRABBLE_FONT_1.deriveFont(min * 0.4f));
            g2d.drawString(label,
                getWidth() / 2 - g2d.getFontMetrics().stringWidth(label) / 2,
                getHeight() / 2 + g2d.getFontMetrics().getAscent() / 2 - 2);
        }
    }
}
