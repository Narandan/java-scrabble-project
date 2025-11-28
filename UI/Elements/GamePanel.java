package UI.Elements;

import java.util.HashMap;

import javax.swing.*;
import java.awt.*;
import scrabble.*;
import java.awt.event.*;
import java.util.List;
import java.util.HashSet;

public class GamePanel extends JPanel
{
    private Game game;
    private BoardPanel boardPanel;
    private JPanel profilePanel;
    private JPanel deckPanel;
    private JPanel turnPanel;
    private HashMap<Player, PlayerPanelGroup> playerPanelHash = new HashMap<>();
    private JButton endTurnButton;
    private JButton beginTurnButton;
    private Player visiblePlayer;
    private HashSet<Dimension> uncheckedPanels = new HashSet<>();

    private class PlayerPanelGroup
    {
        public PlayerDeckPanel deck;
        public PlayerProfilePanel profile;
        public PlayerPanelGroup(PlayerDeckPanel deck, PlayerProfilePanel profile)
        {
            this.deck = deck;
            this.profile = profile;
        }
    }

    public GamePanel(Game game)
    {
        this.game = game;

        setLayout(new BorderLayout());

        boardPanel = new BoardPanel();
        boardPanel.setMaximumSize(new Dimension(400,400));

        profilePanel = new JPanel();
        profilePanel.setLayout(new FlowLayout());
        profilePanel.setPreferredSize(new Dimension(125,0));

        turnPanel = new JPanel();
        endTurnButton = new JButton("End Turn");
        endTurnButton.setVisible(false);
        beginTurnButton = new JButton("Begin Turn");
        beginTurnButton.setVisible(false);
        turnPanel.add(endTurnButton);
        turnPanel.add(beginTurnButton);

        deckPanel = new JPanel();

        connectEvents();

        List<Player> players = game.getPlayers();

        for(Player p : players)
        {
            addPlayer(p);
        }

        setTurn(game.getCurrentPlayer());

        add(profilePanel, BorderLayout.WEST);
        add(boardPanel, BorderLayout.CENTER);
        add(turnPanel, BorderLayout.EAST);
        add(deckPanel, BorderLayout.SOUTH);
    }

    private void connectEvents()
    {
        //mouse listener for slots
        

        game.addListener(new GameListener() {
            public void onPlayerTilesChanged(Player player) 
            {
                updateDeckTiles(player);
            }
            public void onWordPlaced(String word, int row, int col, boolean horizontal, Player player) 
            {
                int[] dir = {horizontal ? 1 : 0,0};
                dir[1] = (dir[0]+1)%2;

                int x = row, y = col;

                for(int i=0; i<word.length(); i++)
                {
                    boardPanel.setSlot(x, y, new TilePanel(new Tile(word.charAt(i), 4)));
                    x+=dir[0];
                    y+=dir[1];
                }
            }
            public void onScoreChanged(Player player) 
            {
                if(playerPanelHash.containsKey(player))
                {
                    playerPanelHash.get(player).profile.updateScore();
                }
            }
            public void onTurnChanged(Player currentPlayer){
                setTurn(currentPlayer);
            }

            public void onPlayerAdded(Player player){
                addPlayer(player);
            }
        });

        beginTurnButton.addActionListener((ActionEvent e) -> {
            playerPanelHash.get(visiblePlayer).deck.setVisible(true);
            beginTurnButton.setVisible(false);
            endTurnButton.setVisible(true);
        });

        boardPanel.addBoardListener((BoardEvent e) -> {
            SlotPanel slot = boardPanel.getSlot(e.getX(), e.getY());
            if (slot.getPanel() != null) 
            {
                uncheckedPanels.add(new Dimension(e.getX(), e.getY()));
            }
            else
            {
                uncheckedPanels.remove(new Dimension(e.getX(), e.getY()));
            }

            endTurnButton.setEnabled(isValidTilePlacement());
        });
    }

    private void updateDeckTiles(Player player)
    {
        PlayerDeckPanel deck = playerPanelHash.get(player).deck;
        java.util.List<Tile> tiles = player.getTiles();

        for(int i=0 ; i<tiles.size(); i++)
        {
            if(deck.getSlot(i) == null || deck.getSlot(i).getTile().getLetter() != tiles.get(i).getLetter())
            {
                deck.setSlot(i, new TilePanel(tiles.get(i)));
            }
        }
    }

    private void setTurn(Player plr)
    {
        if(visiblePlayer != null)
        {
            playerPanelHash.get(visiblePlayer).deck.setVisible(false);
        }

        endTurnButton.setVisible(false);
        beginTurnButton.setVisible(true);

        visiblePlayer = plr;
    }

    public void addPlayer(Player player)
    {
        if (playerPanelHash.containsKey(player))
        {
            playerPanelHash.remove(player);
        }

        PlayerDeckPanel deck = new PlayerDeckPanel(player);
        deck.setPreferredSize(new Dimension(480, 60));

        PlayerProfilePanel profile = new PlayerProfilePanel(player);
        profile.setPreferredSize(new Dimension(125, 40));

        playerPanelHash.put(player, new PlayerPanelGroup(deck, profile));
        deck.setVisible(false);
        updateDeckTiles(player);
        deckPanel.add(deck);
        profilePanel.add(profile);
    }

    public JButton getEndTurnButton()
    {
        return endTurnButton;
    }

    private boolean isValidTilePlacement()
    {
        if (uncheckedPanels.size() <= 1)
        {
            return false;
        }

        boolean isHorizontal = true;
        boolean isVertical = true;
        int lastRow = -1;
        int lastCol = -1;
        int lowestRow = Integer.MAX_VALUE;
        int lowestCol = Integer.MAX_VALUE;

        for (Dimension dimension : uncheckedPanels)
        {
            if(lastRow != -1 && dimension.width != lastRow)
            {
                isHorizontal = false;
            }
            if(dimension.width < lowestRow)
            {
                lowestRow = dimension.width;
            }

            if(lastCol != -1 && dimension.height != lastCol)
            {
                isVertical = false;
            }

            if(dimension.height < lowestCol)
            {
                lowestCol = dimension.height;
            }

            lastRow = dimension.width;
            lastCol = dimension.height;
        }
        int uncheckedCount = 0;
        SlotPanel currentSlot = null;
        int i = 0;

        if(!isHorizontal && !isVertical)
        {
            return false;
        }

        while((currentSlot == null || currentSlot.getPanel() != null))
        {
            int x = isHorizontal ? lastRow : lowestRow + i;
            int y = isVertical ? lastCol : lowestCol + i;
            if(x >= Board.SIZE || y >= Board.SIZE)
            {
                break;
            }
            currentSlot = boardPanel.getSlot(x, y);
            if(currentSlot.getPanel() != null)
            {
                if(uncheckedPanels.contains(new Dimension(x, y)))
                {
                    uncheckedCount++;
                }
            }
            else
            {
                break;
            }
            i++;
        }

        return uncheckedCount == uncheckedPanels.size();
    }

    /*
    private void sortUncheckedPanels(List<int[]> positions, boolean isHorizontal)
    {
        // Create a list of panel-position pairs
        List<SlotPanelWithPosition> panelPositions = new ArrayList<>();
        for (int i = 0; i < uncheckedPanels.size(); i++)
        {
            SlotPanel panel = uncheckedPanels.get(i);
            int[] pos = boardPanel.getSlotPosition(panel);
            panelPositions.add(new SlotPanelWithPosition(panel, pos));
        }

        // Sort based on orientation
        if (isHorizontal)
        {
            // Sort by column index (ascending)
            panelPositions.sort((a, b) -> Integer.compare(a.position[1], b.position[1]));
        }
        else
        {
            // Sort by row index (ascending)
            panelPositions.sort((a, b) -> Integer.compare(a.position[0], b.position[0]));
        }

        // Rebuild uncheckedPanels in sorted order
        uncheckedPanels.clear();
        for (SlotPanelWithPosition pp : panelPositions)
        {
            uncheckedPanels.add(pp.panel);
        }
    }*/
}