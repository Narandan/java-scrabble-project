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
import UI.Styles.Fonts;
import UI.Styles.ScrabbleButtonUI1;
import UI.Styles.ScrabbleLabelUI2;
import UI.Info.Colors;
import UI.Info.Strings;
import scrabble.Tile;

public class BlankTileWindow extends JDialog
{
    private Character selectedLetter = null;
    private boolean confirmed = false;
    private int currentLetterIndex = 0;
    private Tile displayTile;
    
    private static final String[] LETTERS =
    {
        "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
        "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
        "U", "V", "W", "X", "Y", "Z"
    };
    
    private BlankTileWindow(Frame owner)
    {
        super(owner, Strings.BLANKTILEWINDOW_TITLE, true);

        setLayout(new BorderLayout(12, 12));
        getContentPane().setBackground(Colors.BACKGROUND_1);
        
        JLabel titleLabel = new JLabel(Strings.BLANKTILEWINDOW_LABEL);
        titleLabel.setUI(new ScrabbleLabelUI2());
        titleLabel.setFont(Fonts.SCRABBLE_FONT_1.deriveFont(Font.BOLD, 18f));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        add(titleLabel, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 12));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        
        JButton leftArrowButton = new JButton("<");
        leftArrowButton.setUI(new ScrabbleButtonUI1());
        leftArrowButton.setBackground(Colors.BUTTON_1);
        leftArrowButton.setPreferredSize(new Dimension(48, 60));
        leftArrowButton.setFont(Fonts.SCRABBLE_FONT_1.deriveFont(Font.BOLD, 20f));
        
        displayTile = new Tile();
        displayTile.assignLetter(LETTERS[currentLetterIndex].charAt(0));
        TilePanel tileDisplay = new TilePanel(displayTile);
        tileDisplay.setPreferredSize(new Dimension(80, 80));
        
        JButton rightArrowButton = new JButton(">");
        rightArrowButton.setUI(new ScrabbleButtonUI1());
        rightArrowButton.setBackground(Colors.BUTTON_1);
        rightArrowButton.setPreferredSize(new Dimension(48, 60));
        rightArrowButton.setFont(Fonts.SCRABBLE_FONT_1.deriveFont(Font.BOLD, 20f));
        
        updateTileDisplay(tileDisplay);
        
        leftArrowButton.addActionListener(e ->
        {
            currentLetterIndex = (currentLetterIndex - 1 + LETTERS.length) % LETTERS.length;
            updateTileDisplay(tileDisplay);
        });
        
        rightArrowButton.addActionListener(e ->
        {
            currentLetterIndex = (currentLetterIndex + 1) % LETTERS.length;
            updateTileDisplay(tileDisplay);
        });
        
        centerPanel.add(leftArrowButton);
        centerPanel.add(tileDisplay);
        centerPanel.add(rightArrowButton);
        
        add(centerPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        
        JButton confirmButton = new JButton(Strings.BLANKTILEWINDOW_CONFIRM_BUTTON_TEXT);
        confirmButton.setUI(new ScrabbleButtonUI1());
        confirmButton.setBackground(Colors.BUTTON_1);
        confirmButton.setPreferredSize(new Dimension(120, 36));
        confirmButton.addActionListener(e ->
        {
            selectedLetter = LETTERS[currentLetterIndex].charAt(0);
            confirmed = true;
            dispose();
        });
        
        JButton cancelButton = new JButton(Strings.BLANKTILEWINDOW_CANCEL_BUTTON_TEXT);
        cancelButton.setUI(new ScrabbleButtonUI1());
        cancelButton.setBackground(Colors.BUTTON_2);
        cancelButton.setPreferredSize(new Dimension(120, 36));
        cancelButton.addActionListener(e ->
        {
            selectedLetter = null;
            confirmed = false;
            dispose();
        });

        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
        
        pack();
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    
    private void updateTileDisplay(TilePanel tileDisplay)
    {
        String letter = LETTERS[currentLetterIndex];
        displayTile.assignLetter(letter.charAt(0));
        tileDisplay.repaint();
    }
    
    public static Character showBlankTileDialog(Component parent)
    {
        Window window = SwingUtilities.getWindowAncestor(parent);
        Frame owner = (window instanceof Frame) ? (Frame) window : null;
        
        BlankTileWindow dialog = new BlankTileWindow(owner);
        dialog.setVisible(true);
        
        return dialog.confirmed ? dialog.selectedLetter : null;
    }
}
