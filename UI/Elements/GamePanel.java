package UI.Elements;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Container;
import java.awt.Window;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.HashMap;
import UI.Styles.ScrabblePanelUI1;
import UI.Styles.ScrabbleButtonUI1;
import scrabble.Game;
import scrabble.Player;
import scrabble.Tile;
import scrabble.Board;
import scrabble.Dictionary;
import scrabble.GameListener;
import UI.Info.Colors;
import UI.Info.CardJumpNames;
import UI.Info.Strings;

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
    private JButton exchangeButton;
    private JButton passButton;
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

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Colors.BACKGROUND_2);
        JPanel rightTopPanel = new JPanel();
        rightTopPanel.setBackground(rightPanel.getBackground());

        exitButton = new JButton(Strings.GAMEPANEL_EXIT_BUTTON_TEXT);
        exitButton.setUI(new ScrabbleButtonUI1());
        exitButton.setBackground(Colors.BUTTON_4);
        saveButton = new JButton(Strings.GAMEPANEL_SAVE_BUTTON_TEXT);
        saveButton.setUI(new ScrabbleButtonUI1());
        saveButton.setBackground(Colors.BUTTON_5);

        rightTopPanel.add(exitButton);
        rightTopPanel.add(saveButton);

        JPanel rightBottomPanel = new JPanel();
        rightBottomPanel.setBackground(rightPanel.getBackground());

        beginTurnButton = new JButton(Strings.GAMEPANEL_BEGIN_TURN_BUTTON_TEXT);
        beginTurnButton.setVisible(false);
        beginTurnButton.setUI(new ScrabbleButtonUI1());
        beginTurnButton.setBackground(Colors.BUTTON_1);
        beginTurnButton.setPreferredSize(new Dimension(145, 36));

        turnPanel = new JPanel();
        turnPanel.setBackground(rightPanel.getBackground());
        turnPanel.setVisible(false);

        endTurnButton = new JButton(Strings.GAMEPANEL_END_TURN_BUTTON_TEXT);
        endTurnButton.setUI(new ScrabbleButtonUI1());
        endTurnButton.setBackground(Colors.BUTTON_3);

        resignButton = new JButton(Strings.GAMEPANEL_RESIGN_BUTTON_TEXT);
        resignButton.setUI(new ScrabbleButtonUI1());
        resignButton.setBackground(Colors.BUTTON_4);

        exchangeButton = new JButton(Strings.GAMEPANEL_EXCHANGE_BUTTON_TEXT);
        exchangeButton.setUI(new ScrabbleButtonUI1());
        exchangeButton.setBackground(Colors.BUTTON_2);

        passButton = new JButton(Strings.GAMEPANEL_PASS_BUTTON_TEXT);
        passButton.setUI(new ScrabbleButtonUI1());
        passButton.setBackground(Colors.BUTTON_2);

        turnPanel.add(passButton);
        turnPanel.add(resignButton);
        turnPanel.add(endTurnButton);
        turnPanel.add(exchangeButton);

        turnPanel.setPreferredSize(new Dimension(145, turnPanel.getComponentCount() * 41 + 2));

        for(int i = 0; i < turnPanel.getComponentCount(); i++)
        {
            Component comp = turnPanel.getComponent(i);
            comp.setPreferredSize(new Dimension((int) turnPanel.getPreferredSize().getWidth(), comp.getPreferredSize().height));
        }

        rightBottomPanel.add(turnPanel);
        rightBottomPanel.add(beginTurnButton);

        rightPanel.add(rightBottomPanel, BorderLayout.SOUTH);
        rightPanel.add(rightTopPanel, BorderLayout.NORTH);

        deckPanel = new JPanel();
        deckPanel.setBackground(Colors.BACKGROUND_3);

        connectEvents();
        for(Player p : game.getPlayers()) addPlayer(p);
        setTurn(game.getCurrentPlayer(), true);

        add(profilePanel, BorderLayout.WEST);
        add(boardPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
        add(deckPanel, BorderLayout.SOUTH);

        SwingUtilities.invokeLater(() -> refreshBoardFromGame());
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
                    if (boardPanel.getSlot(x, y).getPanel() == null)
                        boardPanel.setSlot(x, y, new TilePanel(new Tile(word.charAt(i), game.getTileValue(word.charAt(i)))), false);
                    boardPanel.getSlot(x, y).setLocked(true);
                    x+=dir[0];
                    y+=dir[1];
                }
            }

            public void onScoreChanged(Player player) {}

            public void onPlayerTilesChanged(Player player) {}

            public void onTurnChanged(Player currentPlayer) 
            { setTurn(currentPlayer, false); }

            public void onPlayerAdded(Player player) 
            { addPlayer(player); }

            public void onPlayerRemoved(Player player)
            { removePlayer(player); }

            public void onGameOver(List<Player> finalRanking) { }
        });

        beginTurnButton.addActionListener((ActionEvent e) -> 
        {
            playerPanelHash.get(visiblePlayer).deck.setVisible(true);
            beginTurnButton.setVisible(false);
            turnPanel.setVisible(true);
        });

        endTurnButton.addActionListener((ActionEvent e) -> 
        {
            if(currentWords.size() > 0)
            {
                List<Dimension> wordDims = currentWords.entrySet().iterator().next().getKey();
                String word = dimToStr(wordDims);
                boolean success = game.placeWord(word, wordDims.get(0).width, wordDims.get(0).height, currentWords.get(wordDims));
                if (success)
                {
                    // Lock all tiles placed this turn (permanent now)
                    for (Dimension dim : uncheckedPanels)
                    {
                        SlotPanel slot = boardPanel.getSlot(dim.width, dim.height);
                        if (slot != null && slot.getPanel() != null)
                        {
                            slot.setLocked(true);   // 🔥 mark tile as permanent
                        }
                    }

                    uncheckedPanels.clear();
                    endTurnButton.setEnabled(false);
                }

                else
                {
                    JOptionPane.showMessageDialog(this, Strings.GAMEPANEL_INVALID_PLACEMENT_MESSAGE);
                }
            }
        });

        saveButton.addActionListener((ActionEvent e) ->
        {
            Window w = SwingUtilities.getWindowAncestor(GamePanel.this);
            Frame owner = (w instanceof Frame) ? (Frame) w : null;
            FileDialog fd = new FileDialog(owner, "Save Game", FileDialog.SAVE);
            fd.setVisible(true);

            String dir = fd.getDirectory();
            String file = fd.getFile();

            if (dir != null && file != null) {
                String fullPath = dir + file;
                game.saveGame(fullPath);
                JOptionPane.showMessageDialog(GamePanel.this, "Game saved to:\n" + fullPath);
            }
        });


        boardPanel.addBoardListener((BoardEvent e) -> 
        {
            SlotPanel slot = boardPanel.getSlot(e.getX(), e.getY());

            if (slot.getPanel() != null && slot.getPanel().getTile().getLetter() == '_')
            {
                Character chosenLetter = BlankTileWindow.showBlankTileDialog(GamePanel.this);
                if (chosenLetter != null)
                {
                    slot.getPanel().setTile(new Tile(chosenLetter, 0));
                    slot.repaint();
                }
                else
                {
                    boardPanel.setSlot(e.getX(), e.getY(), null, false);
                    playerPanelHash.get(visiblePlayer).deck.refreshDeck();
                }
                endTurnButton.setEnabled(isValidTilePlacement());
                return;
            }

            if (slot.getPanel() != null) 
                uncheckedPanels.add(new Dimension(e.getX(), e.getY()));
            else uncheckedPanels.remove(new Dimension(e.getX(), e.getY()));

            endTurnButton.setEnabled(isValidTilePlacement());
        });

        exitButton.addActionListener((ActionEvent e) ->
        {
            
            if (ConfirmWindow.showConfirmDialog(GamePanel.this, Strings.GAMEPANEL_EXIT_PROMPT) == ConfirmWindow.YES)
            {
                Container ancestor = getParent();
                while (ancestor != null) {
                    if (ancestor.getLayout() instanceof CardLayout) {
                        ((CardLayout) ancestor.getLayout()).show(ancestor, CardJumpNames.TITLEMENU);
                        break;
                    }
                    ancestor = ancestor.getParent();
                }
            }
        });

        passButton.addActionListener(e -> {
            boardPanel.clearTemporaryTiles();
            uncheckedPanels.clear();
            game.registerPass();
        });



        resignButton.addActionListener((ActionEvent e) ->
        {
            
            if (ConfirmWindow.showConfirmDialog(GamePanel.this, Strings.GAMEPANEL_RESIGN_PROMPT) == ConfirmWindow.YES)
            {
                game.resign(game.getCurrentPlayer());
            }
        });

        exchangeButton.addActionListener((ActionEvent e) ->
        {
            for(Dimension dim : uncheckedPanels)
            {
                boardPanel.setSlot(dim.width, dim.height, null, false);
            }

            playerPanelHash.get(visiblePlayer).deck.refreshDeck();

            List<Tile> tiles = game.getCurrentPlayer().getTiles();
            List<Boolean> exchangeNums = ExchangeWindow.showExchangeDialog(GamePanel.this, tiles);
            if (exchangeNums != null)
            {
                game.exchangeTile(visiblePlayer, exchangeNums);
            }
        });
    }

    private void setTurn(Player plr, boolean isInit)
    {
        uncheckedPanels.clear();

        if(visiblePlayer != null)
        {
            playerPanelHash.get(visiblePlayer).deck.setVisible(false);
            playerPanelHash.get(visiblePlayer).profile.setSelected(false);
        }

        turnPanel.setVisible(false);
        endTurnButton.setEnabled(false);
        beginTurnButton.setVisible(true);
        playerPanelHash.get(plr).profile.setSelected(true);

        visiblePlayer = plr;

        isFirstTurn = game.getBoard().isBoardEmpty();
    }

    private void addPlayer(Player player)
    {
        if (playerPanelHash.containsKey(player)) playerPanelHash.remove(player);

        PlayerDeckPanel deck = new PlayerDeckPanel(game, player);
        deck.setPreferredSize(new Dimension(480, 60));

        PlayerProfilePanel profile = new PlayerProfilePanel(game, player);
        profile.setPreferredSize(new Dimension(125, 40));

        playerPanelHash.put(player, new PlayerPanelGroup(deck, profile));
        deck.setVisible(false);
        deckPanel.add(deck);
        profilePanel.add(profile);
    }

    private void removePlayer(Player player)
    {
        if (playerPanelHash.containsKey(player)) 
        {
            visiblePlayer = visiblePlayer == player ? null : visiblePlayer;
            deckPanel.remove(playerPanelHash.get(player).deck);
            profilePanel.remove(playerPanelHash.get(player).profile);
            playerPanelHash.remove(player);
        }
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
        boolean connectedToExisting = false;
        Dictionary dict = game.getDictionary();
        boolean isVertical = true;
        boolean isHorizontal = true;
        boolean hasValidWords = true;
        boolean wordContainsAllUnchecked = false;
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
            int uncheckedTiles = 0;
            while(currentDim.width < 15 && boardPanel.getSlot(currentDim.width, currentDim.height).getPanel() != null)
            {
                if(!uncheckedPanels.contains(new Dimension(currentDim.width, currentDim.height)))
                {
                    connectedToExisting = true;
                }
                else uncheckedTiles++;

                verticalWord.add(new Dimension(currentDim.width, currentDim.height));
                currentDim.width++;
            }

            if (uncheckedTiles >= uncheckedPanels.size())
                wordContainsAllUnchecked = true;

            uncheckedTiles = 0;

            List<Dimension> horizontalWord = new ArrayList<>();
            currentDim = new Dimension(dimension.width, lowestCol);
            while(currentDim.height < 15 && boardPanel.getSlot(currentDim.width, currentDim.height).getPanel() != null)
            {
                if(!uncheckedPanels.contains(new Dimension(currentDim.width, currentDim.height)))
                {
                    connectedToExisting = true;
                }
                else uncheckedTiles++;

                horizontalWord.add(new Dimension(currentDim.width, currentDim.height));
                currentDim.height++;
            }

            if (uncheckedTiles >= uncheckedPanels.size())
                wordContainsAllUnchecked = true;

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

        boolean ret = ((isFirstTurn && somethingOnStart) || connectedToExisting) && (isHorizontal || isVertical) && hasValidWords && wordContainsAllUnchecked;
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

    private void refreshBoardFromGame() {
        Board b = game.getBoard();

        for (int r = 0; r < 15; r++) {
            for (int c = 0; c < 15; c++) {
                Tile t = b.getTile(r, c);
                if (t != null) {
                    TilePanel tilePanel = new TilePanel(t);
                    boardPanel.setSlot(r, c, tilePanel, false);
                    boardPanel.getSlot(r, c).setLocked(true);
                }
            }
        }
    }


    public JButton getEndTurnButton()
    { return endTurnButton; }
}