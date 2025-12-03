package UI.Menus;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Container;
import UI.Elements.CardJumpPanel;
import UI.Elements.CardJumpButton;
import UI.Styles.ScrabbleLabelUI2;
import UI.Styles.ScrabbleButtonUI1;
import UI.Styles.Fonts;
import UI.Info.Colors;
import UI.Info.Strings;
import UI.Info.CardJumpNames;

public class TitleMenu extends CardJumpPanel
{	
	public TitleMenu(Container parent, String jumpName)
	{
		super(parent, jumpName);

		setLayout(new BorderLayout());
		setBackground(Colors.BACKGROUND_1);
		
		JLabel logo = new JLabel(Strings.TITLEMENU_TITLE);
		logo.setUI(new ScrabbleLabelUI2());
		logo.setFont(logo.getFont().deriveFont(Font.PLAIN, 150f));
		logo.setHorizontalAlignment(SwingConstants.CENTER);

		JPanel centerPanel = new JPanel();
		centerPanel.setBackground(getBackground());
		
		CardJumpButton playButton = new CardJumpButton(CardJumpNames.PLAYMENU);
		playButton.setPreferredSize(new Dimension(100,40));
		playButton.setBackground(Colors.BUTTON_3);
		playButton.setText(Strings.TITLEMENU_PLAY_BUTTON_TEXT);
		playButton.setUI(new ScrabbleButtonUI1());
		playButton.setFont(Fonts.SCRABBLE_FONT_1.deriveFont(Font.PLAIN, 30f));
		playButton.setAlignmentX(CENTER_ALIGNMENT);

		CardJumpButton creditButton = new CardJumpButton(CardJumpNames.CREDITMENU);
		creditButton.setText(Strings.TITLEMENU_CREDITS_BUTTON_TEXT);
		creditButton.setBackground(Colors.BUTTON_5);
		creditButton.setUI(new ScrabbleButtonUI1());
		creditButton.setAlignmentX(CENTER_ALIGNMENT);

		centerPanel.add(playButton);
		centerPanel.add(creditButton);

		add(logo, BorderLayout.NORTH);
		add(centerPanel, BorderLayout.CENTER);
	}
}