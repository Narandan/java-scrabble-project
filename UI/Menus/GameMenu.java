package UI.Menus;

import scrabble.*;
import UI.Elements.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;

public class GameMenu extends CardJumpPanel
{
    private GamePanel gamePanel;
    private Thread gameThread;
    private final Object turnLock = new Object();
    private volatile boolean endTurnClicked = false;

    public void jumpLoad(Object... args)
    {
        Game game = new Game(new Dictionary("backend/words.txt"));

        if(args.length >= 2 && args[1] != null)
            game.loadGame(((File) args[1]).getAbsolutePath());
        else
        {
            List<PlayMenu.PlayerInfo> infos = (List<PlayMenu.PlayerInfo>) args[0];
            for(int i=0; i < (int) infos.size(); i++)
            {
                Player plr = infos.get(i).isBot ? new AIPlayer(infos.get(i).name) : new Player(infos.get(i).name);
                game.addPlayer(plr);
            }
        }

        if(gamePanel != null)
        {
            remove(gamePanel);
        }

        gamePanel = new GamePanel(game);

        gamePanel.getEndTurnButton().addActionListener((ActionEvent e) ->
        {
            synchronized (turnLock)
            {
                endTurnClicked = true;
                turnLock.notifyAll();
                System.out.println("notified thread");
            }
        });

        gameThread = new Thread(() ->
        {
            System.out.println("Game thread started.");
            while (true)
            {
                try
                {
                    synchronized (turnLock)
                    {
                        while (!endTurnClicked)
                        {
                            turnLock.wait();
                        }
                    }
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

        add(gamePanel, BorderLayout.CENTER);
    }

    public GameMenu(String jumpName)
    {
        super(jumpName);

        setLayout(new BorderLayout());
    }
}
