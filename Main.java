import javax.swing.*;
import java.awt.*;
import UI.Menus.*;
import UI.Elements.*;

class Main
{
	public static JFrame window = new JFrame();
	
	static
	{
		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);	
		
		JPanel panel = new JPanel(new CardLayout());
		window.setContentPane(panel);
		
		CardJumpPanel startScreen = new GameMenu();
		panel.add((JPanel)startScreen, "title");
		panel.add(new PlayMenu(), "playmenu");
		
		startScreen.jumpLoad();
		
		window.setSize(600,600);
	}
	
	public static void main(String[] args)
	{
		window.setVisible(true);
	}
}
