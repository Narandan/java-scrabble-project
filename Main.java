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
	private static String resultMenuName = "resultmenu";
	private static String creditMenuName = "creditmenu";
	
	static
	{
		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);	
		
		JPanel panel = new JPanel(new CardLayout());
		window.setContentPane(panel);
		
		Jumpable startScreen = new TitleMenu(panel, titleName);
		new CreditMenu(panel, creditMenuName);
		new PlayMenu(panel, playMenuName);
		new GameMenu(panel, gameMenuName);
		new ResultMenu(panel, resultMenuName);
		
		startScreen.jumpLoad();
		
		window.setSize(600,600);
	}
	
	public static void main(String[] args)
	{
		window.setVisible(true);
	}
}
