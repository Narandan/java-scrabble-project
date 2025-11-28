package UI.Menus;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import scrabble.*;
import UI.Elements.*;
import java.awt.event.ActionEvent;

public class GameMenu extends JPanel implements CardJumpPanel
{
    private Thread gameThread;
    private final Object turnLock = new Object();
    private volatile boolean endTurnClicked = false;

    public GameMenu()
    {
        setLayout(new BorderLayout());

        Game game = new Game(new Dictionary("words.txt"));

        game.addPlayer(new Player("Jerry"));
        game.addPlayer(new Player("Elaine"));
        game.addPlayer(new Player("George"));
        game.addPlayer(new Player("Kramer"));

        GamePanel gamePanel = new GamePanel(game);

        add(gamePanel, BorderLayout.CENTER);

        gamePanel.getEndTurnButton().addActionListener((ActionEvent e) ->
        {
            synchronized (turnLock)
            {
                endTurnClicked = true;
                turnLock.notifyAll();
                System.out.println("notified thread");
            }
        });

        // Create and start a thread that waits for end turn button
        gameThread = new Thread(() ->
        {
            System.out.println("Game thread started.");
            while (true)
            {
                try
                {
                    synchronized (turnLock)
                    {
                        System.out.println("Waiting for turn to end...");
                        while (!endTurnClicked)
                        {
                            turnLock.wait();
                        }
                    }
                    System.out.println("Turn ended.");
                    game.nextTurn();
                    endTurnClicked = false;
                }
                catch (InterruptedException ie)
                {
                    ie.printStackTrace();
                }
            }
        });
        gameThread.setName("GameLoop");
        gameThread.start();
    }
}
