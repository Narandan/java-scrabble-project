package UI.Menus;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.LayoutManager;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;
import scrabble.Game;
import scrabble.Player;
import scrabble.AIPlayer;
import scrabble.GameListener;
import scrabble.Dictionary;
import UI.Elements.CardJumpPanel;
import UI.Elements.Jumpable;
import UI.Elements.GamePanel;
import UI.Info.CardJumpNames;


public class GameMenu extends CardJumpPanel
{
    private GamePanel gamePanel;
    private Thread gameThread;
    private final Object lock = new Object();
    private volatile boolean endTurnClicked = false;

    public GameMenu(Container parent, String jumpName)
    {
        super(parent, jumpName);

        setLayout(new BorderLayout());
    }

    public void jumpLoad(Object... args)
    {
        Game game = new Game(new Dictionary("words.txt"));

        if (args.length >= 2 && args[1] != null)
        { game.loadGame(((File) args[1]).getAbsolutePath()); }
        
        if(game.getPlayers().size() <= 0)
        {
            @SuppressWarnings("unchecked")//temp for no warning on List downcast
            List<PlayMenu.PlayerInfo> infos = (List<PlayMenu.PlayerInfo>) args[0];
            for(int i=0; i < (int) infos.size(); i++)
            {
                Player plr = infos.get(i).isBot ? new /*AI*/Player(infos.get(i).name) : new Player(infos.get(i).name);
                game.addPlayer(plr);
            }
        }

        if(gamePanel != null)
        { remove(gamePanel); }

        gamePanel = new GamePanel(game);

        gamePanel.getEndTurnButton().addActionListener((ActionEvent e) ->
        {
            synchronized (lock)
            {
                endTurnClicked = true;
                lock.notifyAll();
            }
        });

        game.addListener(new GameListener()
        {
            public void onPlayerRemoved(Player player) 
            {
                synchronized (lock)
                {
                    endTurnClicked = true;
                    lock.notifyAll();
                }
            }

            public void onGameOver(List<Player> finalRanking)
            { showResults(finalRanking); }

            public void onWordPlaced(String word, int row, int col, boolean horizontal, Player player) {}
            public void onTurnChanged(Player currentPlayer) {}
            public void onPlayerAdded(Player player) {}
            public void onPlayerTilesChanged(Player player) {}
            public void onScoreChanged(Player player) {}
        });

        gameThread = new Thread(() ->
        {
            while (!game.isGameOver())
            {
                try
                {
                    synchronized (lock)
                    { while (!endTurnClicked) lock.wait(); }

                    endTurnClicked = false;
                }
                catch (InterruptedException ie)
                { ie.printStackTrace(); }
            }
        });
        gameThread.setName("GameLoop");
        gameThread.start();

        add(gamePanel, BorderLayout.CENTER);
    }

    private void showResults(List<Player> rankings)
    {
       Container ancestor = getParent();
        Container cardContainer = null;
        while (ancestor != null)
        {
            LayoutManager lm = ancestor.getLayout();
            if (lm instanceof CardLayout && cardContainer == null)
                cardContainer = ancestor;

            ancestor = ancestor.getParent();
        }

        if (cardContainer != null)
        {
            LayoutManager layout = cardContainer.getLayout();
            for (Component comp : cardContainer.getComponents())
            {
                if (comp instanceof CardJumpPanel)
                {
                    CardJumpPanel panel = (CardJumpPanel) comp;
                    if (panel.getName().equals(CardJumpNames.RESULTMENU))
                        ((Jumpable) panel).jumpLoad(rankings);
                }
            }

            ((CardLayout) layout).show(cardContainer, CardJumpNames.RESULTMENU);
        }
    }
}
