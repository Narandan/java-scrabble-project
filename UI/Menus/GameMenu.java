package UI.Menus;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import scrabble.*;
import UI.Elements.*;

public class GameMenu extends JPanel implements CardJumpPanel
{
        public GameMenu()
        {
            setLayout(new BorderLayout());

            Game game = new Game("words");
            game.addPlayer(new Player("bob"));
            game.addPlayer(new Player("simon"));

            GamePanel gamePanel = new GamePanel(game);

            add(gamePanel, BorderLayout.CENTER);
            System.out.println("setup");

            /*
            while(true)
            {
                //wait for begin turn

                //wait for end turn
                game.nextTurn();
            }*/
        }
}
