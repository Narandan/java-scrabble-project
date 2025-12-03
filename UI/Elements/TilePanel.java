package UI.Elements;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.JWindow;
import javax.swing.JComponent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import UI.Info.Colors;
import UI.Styles.Fonts;
import UI.Styles.ScrabblePanelUI1;
import scrabble.Tile;

public class TilePanel extends JPanel
{
    private Tile tile;
    private TileMousePanel dragWindow;
    private Point pressOffset;
    private SlotPanel currentHoverSlot = null;

    public TilePanel(Tile piece)
    {
        tile = piece;

        setBackground(Colors.TILE_UNLOCKED);
        setUI(new ScrabblePanelUI1());
        setFont(Fonts.SCRABBLE_FONT_1);

        setTile(piece);

        connectMouse();
    }

    private void connectMouse()
    {
        addMouseListener(new MouseAdapter()
        {
            private boolean pressed = false;

            public void mousePressed(MouseEvent e)
            {
                if (pressed) return;
                if (getParent() instanceof SlotPanel == false) return;
                if (((SlotPanel) getParent()).isLocked()) return;
                if (e.getButton() != MouseEvent.BUTTON1) return;

                pressed = true;

                Window owner = SwingUtilities.getWindowAncestor(TilePanel.this);
                Dimension size = TilePanel.this.getSize();
                dragWindow = new TileMousePanel(owner, tile, size);

                Point screenPt = e.getLocationOnScreen();
                pressOffset = e.getPoint();
                dragWindow.setLocation(screenPt.x - pressOffset.x, screenPt.y - pressOffset.y);
                dragWindow.setVisible(true);
            }

            public void mouseReleased(MouseEvent e)
            {
                if (e.getButton() != MouseEvent.BUTTON1) return;
                if (dragWindow == null) return;

                dragWindow.setVisible(false);
                dragWindow.dispose();
                dragWindow = null;
                
                if (currentHoverSlot != null && !currentHoverSlot.isLocked())
                {
                    SlotPanel parent = (SlotPanel) TilePanel.this.getParent();
                    parent.setTilePanel(currentHoverSlot.getPanel(), true);
                    currentHoverSlot.setTilePanel(TilePanel.this, true);
                    currentHoverSlot.setHighlight(false);

                    parent.repaint();
                    currentHoverSlot.repaint();
                    revalidate();
                } 
                else if (currentHoverSlot != null) 
                    currentHoverSlot.setHighlight(false);
                
                pressed = false;
                currentHoverSlot = null;
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e)
            {
                if (dragWindow == null) return;
                Point screenPt = e.getLocationOnScreen();
                dragWindow.setLocation(screenPt.x - pressOffset.x, screenPt.y - pressOffset.y);

                Window owner = SwingUtilities.getWindowAncestor(TilePanel.this);
                if (owner != null)
                {
                    Point winLoc = owner.getLocationOnScreen();
                    int cx = screenPt.x - winLoc.x;
                    int cy = screenPt.y - winLoc.y;
                    java.awt.Component comp = SwingUtilities.getDeepestComponentAt(owner, cx, cy);
                    SlotPanel slot = null;
                    if (comp != null)
                    {
                        while(comp != owner && !(comp instanceof SlotPanel))
                            comp = comp.getParent();

                        if (comp instanceof SlotPanel) slot = (SlotPanel) comp;
                    }

                    currentHoverSlot = slot;
                    if (slot != null && slot.isEmpty())
                    {
                        if (currentHoverSlot != slot)
                        {
                            if (currentHoverSlot != null) currentHoverSlot.setHighlight(false);
                            
                            currentHoverSlot.setHighlight(true);
                        }
                    } 
                    //else requires brackets to prevent else if ambiguity when reading
                    else { if (currentHoverSlot != null) currentHoverSlot.setHighlight(false); }
                }
            }
        });
    }

    public void setTile(Tile piece)
    {
        tile = piece;
        repaint();
    }

    public Tile getTile()
    { return tile; }

    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        drawTilePanel((Graphics2D) g, this, tile);
    }

    private static class TileMousePanel extends JWindow
    {
        public TileMousePanel(Window owner, Tile tile, Dimension size)
        {
            super(owner);

            JPanel panel = new JPanel()
            {
                protected void paintComponent(Graphics g)
                {
                    super.paintComponent(g);
                    drawTilePanel((Graphics2D) g, this, tile);
                }
            };

            panel.setFont(Fonts.SCRABBLE_FONT_1);
            panel.setBackground(Colors.TILE_UNLOCKED);
            panel.setPreferredSize(size);
            setBackground(new Color(0,0,0,0));
            setOpacity(0.8f);
            panel.setUI(new ScrabblePanelUI1());

            getContentPane().add(panel);

            setAlwaysOnTop(true);
            setFocusableWindowState(false);
            pack();
        }
    }

    private static void drawTilePanel(Graphics2D g2d, JComponent c, Tile tile)
    {
        if(tile == null) return;

        int w = c.getWidth();
        int h = c.getHeight();
        int min = Math.max(0, Math.min(w, h));

        String letter = String.valueOf(tile.getLetter());
        String value = Integer.toString(tile.getValue());

        float letterSize = min * 0.55f;
        Font letterFont = c.getFont().deriveFont(Font.PLAIN, letterSize);
        g2d.setFont(letterFont);
        FontMetrics lf = g2d.getFontMetrics();
        int lx = (w - lf.stringWidth(letter)) / 2;
        int ly = (h - lf.getHeight()) / 2 + lf.getAscent();
        g2d.setColor(Color.BLACK);
        g2d.drawString(letter, lx, ly);

        float valueSize = Math.min(1100f, min * 0.25f);
        Font valueFont = c.getFont().deriveFont(Font.PLAIN, valueSize);
        g2d.setFont(valueFont);
        FontMetrics vf = g2d.getFontMetrics();
        int margin = Math.max(2, (int) (min * 0.06f));
        int vx = w - margin - vf.stringWidth(value);
        int vy = h - margin - vf.getDescent();
        g2d.drawString(value, vx, vy);
    }
}