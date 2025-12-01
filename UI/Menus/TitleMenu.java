package UI.Menus;

import java.awt.Color;

import javax.swing.*;
import javax.swing.plaf.ButtonUI;

import UI.Elements.*;
import UI.Styles.*;

public class TitleMenu extends CardJumpPanel
{	
	public TitleMenu(String jumpName)
	{
		super(jumpName);
		
		BoxLayout layout = new BoxLayout(this, BoxLayout.PAGE_AXIS);

		setBackground(Colors.BACKGROUND_1);
		
		JLabel logo = new JLabel(new ImageIcon("UI/Assets/Images/logo.png"));
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