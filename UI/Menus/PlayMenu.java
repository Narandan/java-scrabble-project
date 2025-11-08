package UI.Menus;

import javax.swing.*;
import UI.Elements.CardJumpButton;
import UI.Elements.CardJumpPanel;

public class PlayMenu extends JPanel implements CardJumpPanel
{
	private JSlider slider;
	
	
	public void jumpLoad()
	{
		slider.setValue(2);
	}
	
	public PlayMenu()
	{
		BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		
		CardJumpButton backButton = new CardJumpButton("title");
		backButton.setSize(50,50);
		backButton.setText("Back");
		add(backButton);
		
		slider = new JSlider(JSlider.HORIZONTAL, 2, 4, 2);
		slider.setMajorTickSpacing(1);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		add(slider);
		
		JButton startButton = new JButton();
		startButton.setSize(50,50);
		startButton.setText("StartGame");
		add(startButton);
		
		setLayout(layout);
	}
}
