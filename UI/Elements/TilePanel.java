package UI.Elements;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.*;
import UI.Styles.*;
import scrabble.Tile;
import javax.swing.SwingUtilities;

public class TilePanel extends JPanel{
    private Tile tile;
    private JLabel letterLabel;
    private JLabel valueLabel;
    private TileMousePanel dragWindow;
    private Point pressOffset;
    private SlotPanel currentHoverSlot = null;

    public TilePanel(Tile piece)
    {
        this.tile = piece;

        setBackground(Color.GREEN);
        setUI(new LetterPieceUI1());

        letterLabel = new JLabel();
        letterLabel.setBackground(Color.YELLOW);
        add(letterLabel);

        valueLabel = new JLabel();
        valueLabel.setBackground(Color.CYAN);
        add(valueLabel);

        setTile(piece);

        // Mouse listeners for dragging
        MouseAdapter pressListener = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                SlotPanel parent = (SlotPanel) TilePanel.this.getParent();
                if (parent.isLocked() || parent == null) return;

                Window owner = SwingUtilities.getWindowAncestor(TilePanel.this);
                Dimension size = TilePanel.this.getSize();
                dragWindow = new TileMousePanel(owner, tile, size);

                Point screenPt = e.getLocationOnScreen();
                // record offset so tile doesn't jump
                pressOffset = e.getPoint();
                dragWindow.setLocation(screenPt.x - pressOffset.x, screenPt.y - pressOffset.y);
                dragWindow.setVisible(true);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (dragWindow != null) {
                    dragWindow.setVisible(false);
                    dragWindow.dispose();
                    dragWindow = null;
                    
                    // handle drop on slot
                    if (currentHoverSlot != null) {
                        // place this tile in the target slot
                        SlotPanel parent = (SlotPanel) TilePanel.this.getParent();
                        parent.setTilePanel(currentHoverSlot.getPanel());
                        currentHoverSlot.setTilePanel(TilePanel.this);
                        currentHoverSlot.setHighlight(false);

                        parent.repaint();
                        currentHoverSlot.repaint();
                    } else if (currentHoverSlot != null) {
                        // clear highlight if not dropping
                        currentHoverSlot.setHighlight(false);
                    }
                    
                    currentHoverSlot = null;
                }
            }
        };

        addMouseListener(pressListener);

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (dragWindow != null) {
                    Point screenPt = e.getLocationOnScreen();
                    dragWindow.setLocation(screenPt.x - pressOffset.x, screenPt.y - pressOffset.y);

                    // hit-test for SlotPanel under cursor
                    Window owner = SwingUtilities.getWindowAncestor(TilePanel.this);
                    if (owner != null) {
                        Point winLoc = owner.getLocationOnScreen();
                        int cx = screenPt.x - winLoc.x;
                        int cy = screenPt.y - winLoc.y;
                        java.awt.Component comp = SwingUtilities.getDeepestComponentAt(owner, cx, cy);
                        SlotPanel slot = null;
                        if (comp != null) {
                            while(comp != owner && !(comp instanceof SlotPanel)) {
                                comp = comp.getParent();
                            }
                            if (comp instanceof SlotPanel) slot = (SlotPanel) comp;
                        }

                        //System.out.println(slot);
                        currentHoverSlot = slot;
                        if (slot != null && slot.isEmpty()) {
                            if (currentHoverSlot != slot) {
                                if (currentHoverSlot != null) currentHoverSlot.setHighlight(false);
                                currentHoverSlot.setHighlight(true);
                            }
                        } else {
                            if (currentHoverSlot != null) {
                                currentHoverSlot.setHighlight(false);
                            }
                        }
                    }
                }
            }
        });
    }

    public void setTile(Tile piece)
    {
        this.tile = piece;
        updateLabels();
    }

    private void updateLabels()
    {
        if (tile != null)
        {
            letterLabel.setText(String.valueOf(tile.getLetter()));
            valueLabel.setText(Integer.toString(tile.getScore()));
        } else {
            letterLabel.setText("");
            valueLabel.setText("");
        }
    }

    public Tile getTile()
    {
        return tile;
    }
}
