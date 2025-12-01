package UI.Elements;

import javax.swing.*;

import UI.Menus.PlayMenu;
import UI.Styles.*;
import java.awt.*;
import scrabble.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.HashMap;

public class GamePanel extends JPanel
{
    private Game game;
    private BoardPanel boardPanel;
    private JPanel profilePanel;
    private JPanel deckPanel;
    private JPanel turnPanel;
    private HashMap<Player, PlayerPanelGroup> playerPanelHash = new HashMap<>();
    private JButton endTurnButton;
    private JButton resignButton;
    private JButton saveButton;
    private JButton exitButton;
    private JButton beginTurnButton;
    private Player visiblePlayer;
    private HashSet<Dimension> uncheckedPanels = new HashSet<>();
    private HashMap<List<Dimension>, Boolean> currentWords = new HashMap<>();
    private boolean isFirstTurn = true;

    public GamePanel(Game game)
    {
        this.game = game;

        setLayout(new BorderLayout());
        setBackground(Colors.BACKGROUND_1);

        boardPanel = new BoardPanel(game.getBoard());
        boardPanel.setUI(new ScrabblePanelUI1());
        boardPanel.setBackground(Colors.BACKGROUND_1);
        boardPanel.setMaximumSize(new Dimension(400,400));

        profilePanel = new JPanel();
        profilePanel.setBackground(Colors.BACKGROUND_2);
        profilePanel.setLayout(new FlowLayout());
        profilePanel.setPreferredSize(new Dimension(125,0));

        JPanel eastPanel = new JPanel(new BorderLayout());
        eastPanel.setBackground(Colors.BACKGROUND_2);
        JPanel topRightPanel = new JPanel();
        topRightPanel.setBackground(getBackground());

        exitButton = new JButton("Exit");
        saveButton = new JButton("Save Game");
        topRightPanel.add(exitButton);
        topRightPanel.add(saveButton);

        turnPanel = new JPanel();
        turnPanel.setBackground(Colors.BACKGROUND_2);
        endTurnButton = new JButton("End Turn");
        endTurnButton.setVisible(false);
        beginTurnButton = new JButton("Begin Turn");
        beginTurnButton.setVisible(false);
        resignButton = new JButton("Resign");
        resignButton.setVisible(false);
        turnPanel.add(endTurnButton);
        turnPanel.add(beginTurnButton);
        turnPanel.add(resignButton);
        eastPanel.add(turnPanel, BorderLayout.SOUTH);
        eastPanel.add(topRightPanel, BorderLayout.NORTH);

        deckPanel = new JPanel();
        deckPanel.setBackground(Colors.BACKGROUND_3);

        //game setup
        connectEvents();
        for(Player p : game.getPlayers()) addPlayer(p);
        setTurn(game.getCurrentPlayer(), true);

        add(profilePanel, BorderLayout.WEST);
        add(boardPanel, BorderLayout.CENTER);
        add(eastPanel, BorderLayout.EAST);
        add(deckPanel, BorderLayout.SOUTH);
    }

    private void connectEvents()
    {
        game.addListener(new GameListener() 
        {
            public void onWordPlaced(String word, int row, int col, boolean horizontal, Player player)
            {
                int[] dir = {!horizontal ? 1 : 0,0};
                dir[1] = 1-dir[0];

                int x = row, y = col;

                for(int i=0; i<word.length(); i++)
                {
                    boardPanel.setSlot(x, y, new TilePanel(new Tile(word.charAt(i), game.getTileValue(word.charAt(i)))), false);
                    boardPanel.getSlot(x, y).setLocked(true);
                    x+=dir[0];
                    y+=dir[1];
                }
            }

            public void onScoreChanged(Player player) 
            {
                if(playerPanelHash.containsKey(player))
                    playerPanelHash.get(player).profile.updateScore();
            }

            public void onPlayerTilesChanged(Player player) 
            { updateDeckTiles(player); }

            public void onTurnChanged(Player currentPlayer) 
            { setTurn(currentPlayer, false); }

            public void onPlayerAdded(Player player) 
            { addPlayer(player); }
        });

        beginTurnButton.addActionListener((ActionEvent e) -> 
        {
            playerPanelHash.get(visiblePlayer).deck.setVisible(true);
            beginTurnButton.setVisible(false);
            endTurnButton.setVisible(true);
            resignButton.setVisible(true);
        });

        endTurnButton.addActionListener((ActionEvent e) -> 
        {
            for (List<Dimension> wordDims : currentWords.keySet()) 
            {
                String word = dimToStr(wordDims);
                game.placeWord(word, wordDims.get(0).width, wordDims.get(0).height, currentWords.get(wordDims));
            }

            uncheckedPanels.clear();
            endTurnButton.setEnabled(false);
        });

        saveButton.addActionListener((ActionEvent e) ->
        {
            Window w = SwingUtilities.getWindowAncestor(GamePanel.this);
			Frame owner = (w instanceof Frame) ? (Frame) w : null;
			FileDialog fd = new FileDialog(owner, "Open Game File", FileDialog.SAVE);
			fd.setVisible(true);
			String file = fd.getDirectory();
			if (file != null) {
				game.saveGame(file);
			}
        });

        boardPanel.addBoardListener((BoardEvent e) -> 
        {
            SlotPanel slot = boardPanel.getSlot(e.getX(), e.getY());
            if (slot.getPanel() != null) 
                uncheckedPanels.add(new Dimension(e.getX(), e.getY()));
            else uncheckedPanels.remove(new Dimension(e.getX(), e.getY()));

            endTurnButton.setEnabled(isValidTilePlacement());
        });

        exitButton.addActionListener((ActionEvent e) ->
        {
            
            if (JOptionPane.showConfirmDialog(this, "Are you sure you want to exit to title screen? Unsaved progress will be lost.") == JOptionPane.YES_OPTION)
            {
                Container ancestor = getParent();
                while (ancestor != null) {
                    if (ancestor.getLayout() instanceof CardLayout) {
                        ((CardLayout) ancestor.getLayout()).show(ancestor, "title");
                        break;
                    }
                    ancestor = ancestor.getParent();
                }
            }
        });

        resignButton.addActionListener((ActionEvent e) ->
        {
            
            if (JOptionPane.showConfirmDialog(this, "Are you sure you want to resign?") == JOptionPane.YES_OPTION)
            {
                game.resign(game.getCurrentPlayer());
            }
        });
    }

    private void updateDeckTiles(Player player)
    {
        PlayerDeckPanel deck = playerPanelHash.get(player).deck;
        List<Tile> tiles = player.getTiles();

        for(int i=0 ; i<tiles.size(); i++)
            if(deck.getSlot(i) == null || deck.getSlot(i).getTile().getLetter() != tiles.get(i).getLetter())
                deck.setSlot(i, new TilePanel(tiles.get(i)));
    }

    private void setTurn(Player plr, boolean isInit)
    {
        uncheckedPanels.clear();

        if(visiblePlayer != null)
        {
            playerPanelHash.get(visiblePlayer).deck.setVisible(false);
            playerPanelHash.get(visiblePlayer).profile.setSelected(false);
        }

        endTurnButton.setVisible(false);
        beginTurnButton.setVisible(true);
        playerPanelHash.get(plr).profile.setSelected(true);

        visiblePlayer = plr;

        isFirstTurn = isInit;
    }

    private void addPlayer(Player player)
    {
        if (playerPanelHash.containsKey(player)) playerPanelHash.remove(player);

        PlayerDeckPanel deck = new PlayerDeckPanel();
        deck.setPreferredSize(new Dimension(480, 60));

        PlayerProfilePanel profile = new PlayerProfilePanel(player);
        profile.setPreferredSize(new Dimension(125, 40));

        playerPanelHash.put(player, new PlayerPanelGroup(deck, profile));
        deck.setVisible(false);
        updateDeckTiles(player);
        deckPanel.add(deck);
        profilePanel.add(profile);
    }

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

    private boolean isValidTilePlacement()
    {
        currentWords.clear();
        if (uncheckedPanels.size() <= 1 && isFirstTurn) return false;

        HashMap<List<Dimension>, Boolean> words = new HashMap<>();
        boolean somethingOnStart = false;
        boolean allConnected = true;
        boolean connectedToExisting = false;
        boolean hasValidWords = true;
        Dictionary dict = game.getDictionary();
        boolean isVertical = true;
        boolean isHorizontal = true;
        int lastRow = -1;
        int lastCol = -1;

        for (Dimension dimension : uncheckedPanels)
        {
            int lowestRow = dimension.width;
            int lowestCol = dimension.height;

            if (dimension.width == 7 && dimension.height == 7)
                somethingOnStart = true;

            if(lastRow != -1 && lastRow != dimension.width)
                isHorizontal = false;
            if(lastCol != -1 && lastCol != dimension.height)
                isVertical = false;

            lastRow = dimension.width;
            lastCol = dimension.height;

            while(hasAdjacentPiece(new Dimension(lowestRow, dimension.height), new Dimension(-1, 0)))
                lowestRow--;
            while(hasAdjacentPiece(new Dimension(dimension.width, lowestCol), new Dimension(0, -1)))
                lowestCol--;

            List<Dimension> verticalWord = new ArrayList<>();
            Dimension currentDim = new Dimension(lowestRow, dimension.height);
            while(currentDim.width < 15 && boardPanel.getSlot(currentDim.width, currentDim.height).getPanel() != null)
            {
                if(boardPanel.getSlot(currentDim.width, currentDim.height).getPanel() != null && !uncheckedPanels.contains(new Dimension(currentDim.width, currentDim.height)))
                    connectedToExisting = true;

                verticalWord.add(new Dimension(currentDim.width, currentDim.height));
                currentDim.width++;
            }
            List<Dimension> horizontalWord = new ArrayList<>();
            currentDim = new Dimension(dimension.width, lowestCol);
            while(currentDim.height < 15 && boardPanel.getSlot(currentDim.width, currentDim.height).getPanel() != null)
            {
                if(boardPanel.getSlot(currentDim.width, currentDim.height).getPanel() != null && !uncheckedPanels.contains(new Dimension(currentDim.width, currentDim.height)))
                    connectedToExisting = true;

                horizontalWord.add(new Dimension(currentDim.width, currentDim.height));
                currentDim.height++;
            }

            if (verticalWord.size() <= 1 && horizontalWord.size() <= 1)
            {
                allConnected = false;
                break;
            }

            if (verticalWord.size() > 1)
            {
                String wordStr = dimToStr(verticalWord);
                if (!dict.isValidWord(wordStr))
                {
                    hasValidWords = false;
                    break;
                }
                else words.put(verticalWord, false);
            }

            if (horizontalWord.size() > 1)
            {
                String wordStr = dimToStr(horizontalWord);
                if (!dict.isValidWord(wordStr))
                {
                    hasValidWords = false;
                    break;
                }
                else words.put(horizontalWord, true);
            }
        }
        if(isFirstTurn && somethingOnStart) connectedToExisting = true;
        boolean ret = allConnected && connectedToExisting && hasValidWords && (isHorizontal || isVertical);
        if(ret) currentWords = words;
        return ret;
    }

    private boolean hasAdjacentPiece(Dimension d, Dimension dir)
    {
        int x = d.width + dir.width;
        int y = d.height + dir.height;

        if(x < 0 || x >= Board.SIZE || y < 0 || y >= Board.SIZE) return false;

        SlotPanel slot = boardPanel.getSlot(x, y);
        return slot.getPanel() != null;
    }

    private String dimToStr(List<Dimension> word)
    {
        StringBuilder sb = new StringBuilder();
        for(Dimension dim : word)
        {
            SlotPanel slot = boardPanel.getSlot(dim.width, dim.height);
            if(slot.getPanel() != null)
                sb.append(slot.getPanel().getTile().getLetter());
        }
        return sb.toString();
    }

    public JButton getEndTurnButton()
    { return endTurnButton; }
}