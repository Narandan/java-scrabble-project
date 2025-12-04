package UI.Elements;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.BorderFactory;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Window;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.ArrayList;
import scrabble.Tile;
import UI.Styles.Fonts;
import UI.Styles.ScrabbleButtonUI1;
import UI.Styles.ScrabbleLabelUI2;
import UI.Info.Colors;
import UI.Info.Strings;

public class ExchangeWindow extends JDialog
{
    private List<Boolean> selectedTiles;
    private boolean confirmed = false;
    
    private ExchangeWindow(Frame owner, List<Tile> tiles)
    {
        super(owner, Strings.EXCHANGEWINDOW_TITLE, true);

        selectedTiles = new ArrayList<>(tiles.size());
        for (int i = 0; i < tiles.size(); i++)
            selectedTiles.add(false);
        
        setLayout(new BorderLayout(12, 12));
        getContentPane().setBackground(Colors.BACKGROUND_1);
        
        JLabel titleLabel = new JLabel(Strings.EXCHANGEWINDOW_LABEL);
        titleLabel.setUI(new ScrabbleLabelUI2());
        titleLabel.setFont(Fonts.SCRABBLE_FONT_1.deriveFont(Font.BOLD, 18f));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        add(titleLabel, BorderLayout.NORTH);
        
        JPanel tilesPanel = new JPanel();
        tilesPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 8, 8));
        tilesPanel.setOpaque(false);
        tilesPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        
        for (int i = 0; i < tiles.size(); i++)
        {
            final int index = i;
            final Tile tile = tiles.get(i);
            
            TilePanel tilePanel = new TilePanel(tile);
            tilePanel.setPreferredSize(new Dimension(60, 60));
            
            tilePanel.addMouseListener(new MouseAdapter()
            {
                private boolean pressed = false;

                public void mousePressed(MouseEvent e)
                {
                    if (pressed) return;
                    if (e.getButton() != MouseEvent.BUTTON1) return;
                    
                    pressed = true;
                }

                public void mouseReleased(MouseEvent e) 
                {
                    if (!pressed) return;

                    pressed = false;

                    selectedTiles.set(index, !selectedTiles.get(index));

                    tilePanel.setBorder(BorderFactory.createLineBorder(
                        selectedTiles.get(index) ? Colors.EXCHANGE_SELECTED : Colors.EXCHANGE_HIGHLIGHT, 2));
                }
                
                public void mouseEntered(MouseEvent e)
                { tilePanel.setBorder(BorderFactory.createLineBorder(Colors.EXCHANGE_HIGHLIGHT, 2)); }
                
                public void mouseExited(MouseEvent e)
                { 
                    if (!selectedTiles.get(index))
                        tilePanel.setBorder(null);
                    else
                        tilePanel.setBorder(BorderFactory.createLineBorder(Colors.EXCHANGE_SELECTED, 2));
                }
            });
            
            tilesPanel.add(tilePanel);
        }

        JButton confirmButton = new JButton(Strings.EXCHANGEWINDOW_CONFIRM_BUTTON_TEXT);
        confirmButton.setUI(new ScrabbleButtonUI1());
        confirmButton.setBackground(Colors.BUTTON_1);
        confirmButton.setPreferredSize(new Dimension(120, 36));

        confirmButton.addActionListener(e -> 
        {
            for (int i = 0; i < selectedTiles.size(); i++)
            {
                if (selectedTiles.get(i))
                {
                    confirmed = true;
                    break;
                }
            }
            dispose();
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        
        JButton cancelButton = new JButton(Strings.EXCHANGEWINDOW_CANCEL_BUTTON_TEXT);
        cancelButton.setUI(new ScrabbleButtonUI1());
        cancelButton.setBackground(Colors.BUTTON_2);
        cancelButton.setPreferredSize(new Dimension(120, 36));
        cancelButton.addActionListener(e -> 
        {
            confirmed = false;
            dispose();
        });

        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
        add(tilesPanel, BorderLayout.CENTER);
        
        pack();
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    
    public static List<Boolean> showExchangeDialog(Component parent, List<Tile> tiles) {
        Window window = SwingUtilities.getWindowAncestor(parent);
        Frame owner = (window instanceof Frame) ? (Frame) window : null;
        
        ExchangeWindow dialog = new ExchangeWindow(owner, tiles);
        dialog.setVisible(true);
        
        return dialog.confirmed ? dialog.selectedTiles : null;
    }
}
