import javax.swing.*;
import java.awt.*;
import UI.Menus.*;
import UI.Elements.*;

class Main
{
	public static JFrame window = new JFrame();

	private static String titleName = "titlemenu";
	private static String playMenuName = "playmenu";
	private static String gameMenuName = "gamemenu";
	
	static
	{
		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);	
		
		JPanel panel = new JPanel(new CardLayout());
		window.setContentPane(panel);
		
		Jumpable startScreen = new TitleMenu(titleName);
		panel.add((JPanel)startScreen, "title");
		panel.add(new PlayMenu(playMenuName), "playmenu");
		panel.add(new GameMenu(gameMenuName), "gamemenu");
		
		startScreen.jumpLoad();
		
		window.setSize(600,600);
	}
	
	public static void main(String[] args)
	{
		window.setVisible(true);
	}
}
