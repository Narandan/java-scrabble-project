package UI.Menus;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Component;
import java.awt.Container;
import UI.Info.Colors;
import UI.Info.Strings;
import UI.Info.CardJumpNames;
import UI.Elements.CardJumpPanel;
import UI.Elements.CardJumpButton;
import UI.Styles.Fonts;
import UI.Styles.ScrabbleLabelUI2;

public class CreditMenu extends CardJumpPanel {
    public CreditMenu(Container parent, String jumpName)
    {
        super(parent, jumpName);

        setLayout(new BorderLayout(12,12));
        setBackground(Colors.BACKGROUND_1);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JPanel leftTopPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftTopPanel.setOpaque(false);

        CardJumpButton backButton = new CardJumpButton(CardJumpNames.TITLEMENU);
        backButton.setText(Strings.CREDITMENU_BACK_BUTTON_TEXT);
		backButton.setBackground(Colors.BUTTON_2);
        backButton.setUI(new UI.Styles.ScrabbleButtonUI1());
        backButton.setPreferredSize(new Dimension(100, 36));

        leftTopPanel.add(backButton);

        topPanel.add(leftTopPanel, BorderLayout.WEST);

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.PAGE_AXIS));
        center.setOpaque(false);

        JLabel titleLabel = new JLabel(Strings.CREDITMENU_TITLE);
        titleLabel.setUI(new ScrabbleLabelUI2());
        titleLabel.setFont(Fonts.SCRABBLE_FONT_1.deriveFont(Font.BOLD, 28f));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel namesLabel = new JLabel(Strings.CREDITMENU_NAMES);
        namesLabel.setUI(new ScrabbleLabelUI2());
        namesLabel.setFont(Fonts.SCRABBLE_FONT_1.deriveFont(Font.PLAIN, 16f));
        namesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);        

        center.add(titleLabel);
        center.add(namesLabel);
        
        add(topPanel, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
    }
}