package UI.Menus;

import scrabble.*;
import UI.Elements.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;
import javax.swing.*;

public class GameMenu extends CardJumpPanel
{
    private GamePanel gamePanel;
    private Thread gameThread;
    private final Object turnLock = new Object();
    private volatile boolean endTurnClicked = false;

    public void jumpLoad(Object... args)
    {
        Game game = new Game(new Dictionary("words.txt"));

        if(args.length >= 2 && args[1] != null)
        {
            game.loadGame(((File) args[1]).getAbsolutePath());
        }
        
        if(game.getPlayers().size() <= 0)
        {
            @SuppressWarnings("unchecked")//temp for no warning on List downcast
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
            }
        });

        game.addListener(new GameListener()
        {
            public void onPlayerAdded(Player player) {}

            public void onPlayerTilesChanged(Player player) {}
            public void onScoreChanged(Player player) {}
            public void onPlayerRemoved(Player player) 
            {
                synchronized (turnLock)
                {
                    endTurnClicked = true;
                    turnLock.notifyAll();
                }
            }

            public void onWordPlaced(String word, int row, int col, boolean horizontal, Player player) {}
            
            public void onTurnChanged(Player currentPlayer) {}
        });

        gameThread = new Thread(() ->
        {
            System.out.println("Game thread started.");
            while (!game.isGameOver())
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

            showResults();
        });
        gameThread.setName("GameLoop");
        gameThread.start();

        add(gamePanel, BorderLayout.CENTER);
    }

    private void showResults()
    {
        System.out.println("RESULT TIME");
        Container ancestor = getParent();
        while (ancestor != null)
        {
            if (ancestor.getLayout() instanceof CardLayout)
            {
                System.out.println("FOUND IT");
                ((CardLayout) ancestor.getLayout()).show(ancestor, "resultmenu");
                break;
            }
            ancestor = ancestor.getParent();
        }
    }

    public GameMenu(JComponent parent, String jumpName)
    {
        super(parent, jumpName);

        setLayout(new BorderLayout());
    }
}
