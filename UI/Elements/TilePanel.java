package UI.Elements;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.*;
import javax.swing.*;
import UI.Styles.*;
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
        setUI(new LetterPieceUI1());
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

        addMouseMotionListener(new MouseMotionAdapter()
        {
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
    {
        return tile;
    }

    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        try {

            int w = getWidth();
            int h = getHeight();
            int min = Math.max(1, Math.min(w, h));

            if (tile == null) return;

            String letter = String.valueOf(tile.getLetter());
            String value = Integer.toString(tile.getValue());

            float letterSize = Math.max(10f, min * 0.55f);
            Font letterFont = getFont().deriveFont(Font.PLAIN, letterSize);
            g2.setFont(letterFont);
            FontMetrics lf = g2.getFontMetrics();
            int lx = (w - lf.stringWidth(letter)) / 2;
            int ly = (h - lf.getHeight()) / 2 + lf.getAscent();
            g2.setColor(java.awt.Color.BLACK); //give a color
            g2.drawString(letter, lx, ly);

            float valueSize = Math.max(8f, min * 0.20f);
            java.awt.Font valueFont = getFont().deriveFont(java.awt.Font.PLAIN, valueSize);
            g2.setFont(valueFont);
            java.awt.FontMetrics vf = g2.getFontMetrics();
            int margin = Math.max(2, (int) (min * 0.06f));
            int vx = w - margin - vf.stringWidth(value);
            int vy = h - margin - vf.getDescent();
            g2.drawString(value, vx, vy);
        }
        finally
        {
            g2.dispose();
        }
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
                    setFont(Fonts.SCRABBLE_FONT_1);
                    super.paintComponent(g);
                    if (tile == null) return;
                    Graphics2D g2 = (Graphics2D) g;
                    try
                    {
                        int w = getWidth();
                        int h = getHeight();
                        int min = Math.max(1, Math.min(w, h));

                        String letter = String.valueOf(tile.getLetter());
                        String value = Integer.toString(tile.getValue());

                        float letterSize = Math.max(10f, min * 0.55f);
                        Font letterFont = getFont().deriveFont(Font.PLAIN, letterSize);
                        g2.setFont(letterFont);
                        FontMetrics lf = g2.getFontMetrics();
                        int lx = (w - lf.stringWidth(letter)) / 2;
                        int ly = (h - lf.getHeight()) / 2 + lf.getAscent();
                        g2.setColor(Color.BLACK);
                        g2.drawString(letter, lx, ly);

                        float valueSize = Math.max(8f, min * 0.20f);
                        Font valueFont = getFont().deriveFont(Font.PLAIN, valueSize);
                        g2.setFont(valueFont);
                        FontMetrics vf = g2.getFontMetrics();
                        int margin = Math.max(2, (int) (min * 0.06f));
                        int vx = w - margin - vf.stringWidth(value);
                        int vy = h - margin - vf.getDescent();
                        g2.drawString(value, vx, vy);
                    }
                    finally
                    {
                        g2.dispose();
                    }
                }
            };

            panel.setBackground(Colors.TILE_UNLOCKED);
            panel.setPreferredSize(size);
            setBackground(new Color(0,0,0,0));
            setOpacity(0.8f);
            panel.setUI(new LetterPieceUI1());

            getContentPane().add(panel);

            setAlwaysOnTop(true);
            setFocusableWindowState(false);
            pack();
        }
    }
}
