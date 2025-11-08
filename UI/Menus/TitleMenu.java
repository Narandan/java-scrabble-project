package UI.Menus;

import java.awt.Color;

import javax.swing.*;
import UI.Elements.CardJumpButton;
import UI.Elements.CardJumpPanel;
import UI.Styles.*;

public class TitleMenu extends JPanel implements CardJumpPanel
{	
	public TitleMenu()
	{
		BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		
		JLabel logo = new JLabel(new ImageIcon("UIAssets/Images/logo.png"));
		add(logo);
		
		CardJumpButton button = new CardJumpButton("playmenu");
		button.setSize(50,50);
		button.setText("Play");
		button.setBackground(Color.LIGHT_GRAY);
		button.setUI(new ScrabbleButtonUI1());
		add(button);

		JLabel testLabel = new JLabel();
		testLabel.setText("test jlabel");
		testLabel.setSize(200,50);
		testLabel.setBackground(Color.DARK_GRAY);
		testLabel.setUI(new ScrabbleLabelUI1());
		add(testLabel);
		
		setLayout(layout);
	}
}