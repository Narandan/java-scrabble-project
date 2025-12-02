package UI.Menus;

import java.awt.*;

import javax.swing.*;

import UI.Elements.*;
import UI.Styles.*;

public class TitleMenu extends CardJumpPanel
{	
	public TitleMenu(JComponent parent, String jumpName)
	{
		super(parent, jumpName);

		setLayout(new BorderLayout());

		setBackground(Colors.BACKGROUND_1);
		
		JLabel logo = new JLabel("SCRABBLE");
		logo.setFont(Fonts.SCRABBLE_FONT_1.deriveFont(Font.PLAIN, 150f));
		logo.setHorizontalAlignment(SwingConstants.CENTER);
		logo.setForeground(Color.WHITE);
		add(logo, BorderLayout.NORTH);

		JPanel centerPanel = new JPanel();
		centerPanel.setBackground(getBackground());
		
		CardJumpButton playButton = new CardJumpButton("playmenu");
		playButton.setPreferredSize(new Dimension(100,40));
		playButton.setBackground(Colors.BUTTON_3);
		playButton.setText("Play");
		playButton.setUI(new ScrabbleButtonUI1());
		playButton.setFont(Fonts.SCRABBLE_FONT_1.deriveFont(Font.PLAIN, 30f));
		playButton.setAlignmentX(CENTER_ALIGNMENT);
		centerPanel.add(playButton);

		CardJumpButton creditButton = new CardJumpButton("creditmenu");
		creditButton.setText("Credits");
		creditButton.setBackground(Colors.BUTTON_5);
		creditButton.setUI(new ScrabbleButtonUI1());
		creditButton.setAlignmentX(CENTER_ALIGNMENT);
		centerPanel.add(creditButton);

		add(centerPanel, BorderLayout.CENTER);
	}
}