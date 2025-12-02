package UI.Menus;

import UI.Elements.*;
import UI.Styles.*;

import javax.swing.*;
import java.awt.*;

public class CreditMenu extends CardJumpPanel {
    private JLabel titleLabel;
    private JLabel namesLabel;

    public void jumpLoad(Object... args) {
        
    }

    public CreditMenu(JComponent parent, String jumpName)
    {
        super(parent, jumpName);

        setLayout(new BorderLayout(12,12));
        setBackground(Colors.BACKGROUND_1);
        setBorder(BorderFactory.createEmptyBorder(12,12,12,12));

        // Top bar: back button (left) + title (center)
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);

        CardJumpButton backButton = new CardJumpButton("titlemenu");
        backButton.setText("Back");
		backButton.setBackground(Colors.BUTTON_2);
        backButton.setUI(new UI.Styles.ScrabbleButtonUI1());
        backButton.setPreferredSize(new Dimension(100, 36));
        JPanel leftTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftTop.setOpaque(false);
        leftTop.add(backButton);
        topBar.add(leftTop, BorderLayout.WEST);

        titleLabel = new JLabel("Credits:");
        titleLabel.setFont(Fonts.SCRABBLE_FONT_1.deriveFont(Font.BOLD, 28f));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        add(topBar, BorderLayout.NORTH);

        namesLabel = new JLabel("Tyler Keller and Narandan Miller");
        namesLabel.setFont(Fonts.SCRABBLE_FONT_1.deriveFont(Font.PLAIN, 16f));
        namesLabel.setForeground(Color.WHITE);
        namesLabel.setHorizontalAlignment(SwingConstants.CENTER);
        namesLabel.setVerticalAlignment(SwingConstants.TOP);

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.PAGE_AXIS));
        center.setOpaque(false);

        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        namesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        center.add(titleLabel);
        center.add(namesLabel);
        add(center, BorderLayout.CENTER);
    }
}