import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.awt.CardLayout;
import UI.Menus.*;
import UI.Elements.Jumpable;
import UI.Info.CardJumpNames;

class Main
{
	public static JFrame window = new JFrame();
	
	static
	{
		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);	
		
		JPanel panel = new JPanel(new CardLayout());
		window.setContentPane(panel);
		
		Jumpable startScreen = new TitleMenu(panel, CardJumpNames.TITLEMENU);
		new CreditMenu(panel, CardJumpNames.CREDITMENU);
		new PlayMenu(panel, CardJumpNames.PLAYMENU);
		new GameMenu(panel, CardJumpNames.GAMEMENU);
		new ResultMenu(panel, CardJumpNames.RESULTMENU);
		
		startScreen.jumpLoad();
		
		window.setSize(600,600);
	}
	
	public static void main(String[] args)
	{
		window.setVisible(true);
	}
}
