package UI.Elements;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Window;
import java.awt.Font;

import UI.Styles.Fonts;
import UI.Styles.ScrabbleButtonUI1;
import UI.Styles.ScrabbleLabelUI2;
import UI.Info.Colors;

public class ConfirmWindow extends JDialog
{
    private int result = NO;

    public static final int YES = 1;
    public static final int NO = 0;

    private ConfirmWindow(Frame parent, String msg)
    {
        super(parent, "Confirm", true);

        setLayout(new BorderLayout(12, 12));
        getContentPane().setBackground(Colors.BACKGROUND_1);

        JLabel messageLabel = new JLabel(msg, SwingConstants.CENTER);
        messageLabel.setUI(new ScrabbleLabelUI2());
        messageLabel.setFont(Fonts.SCRABBLE_FONT_1.deriveFont(Font.PLAIN, 16f));
        messageLabel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        add(messageLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 12, 12, 12));

        JButton yesButton = new JButton("Yes");
        yesButton.setUI(new ScrabbleButtonUI1());
        yesButton.setBackground(Colors.BUTTON_1);
        yesButton.setPreferredSize(new Dimension(120, 36));
        yesButton.addActionListener(e -> {
            result = YES;
            dispose();
        });

        JButton noButton = new JButton("No");
        noButton.setUI(new ScrabbleButtonUI1());
        noButton.setBackground(Colors.BUTTON_2);
        noButton.setPreferredSize(new Dimension(120, 36));
        noButton.addActionListener(e -> {
            result = NO;
            dispose();
        });

        buttonPanel.add(yesButton);
        buttonPanel.add(noButton);

        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public static int showConfirmDialog(Component parent, String msg)
    {
        Window window = SwingUtilities.getWindowAncestor(parent);
        Frame owner = (window instanceof Frame) ? (Frame) window : null;
        
        ConfirmWindow dialog = new ConfirmWindow(owner, msg);
        dialog.setVisible(true);
        
        return dialog.result;
    }
}