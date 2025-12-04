package UI.Elements;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import java.awt.Color;
import java.util.List;
import UI.Styles.ScrabblePanelUI1;
import UI.Styles.Fonts;
import scrabble.GameListener;
import scrabble.Player;
import scrabble.Game;
import UI.Info.Colors;

public class PlayerProfilePanel extends JPanel 
{
    private Player player;
    private Game game;
    private JLabel nameLabel;
    private JLabel scoreLabel;

    public PlayerProfilePanel(Game game, Player player)
    {
        this.player = player;
        this.game = game;

        setBackground(Colors.PROFILE_DEFAULT);
        setUI(new ScrabblePanelUI1());

        nameLabel = new JLabel();
        nameLabel.setText(player.getName());
        nameLabel.setFont(Fonts.SCRABBLE_FONT_1);
        nameLabel.setForeground(Color.WHITE);

        scoreLabel = new JLabel();
        scoreLabel.setFont(Fonts.SCRABBLE_FONT_1);
        scoreLabel.setForeground(Color.WHITE);
        
        updateScore();

        add(nameLabel);
        add(scoreLabel);

        connectEvents();
    }

    public PlayerProfilePanel(Player player, Color textColor)
    {
        this(null, player);

        if (textColor != null)
        {
            nameLabel.setForeground(textColor);
            scoreLabel.setForeground(textColor);
        }
    }

    private void connectEvents()
    {
        if (game == null) return;
        
        game.addListener(new GameListener()
        {
            public void onPlayerAdded(Player player) {}
            public void onPlayerTilesChanged(Player player) {}
            public void onScoreChanged(Player player) 
            { if (player == PlayerProfilePanel.this.player) updateScore(); }
            public void onPlayerRemoved(Player player) {}
            public void onGameOver(List<Player> finalRanking) {}
            public void onWordPlaced(String word, int row, int col, boolean horizontal, Player player) {}
            public void onTurnChanged(Player currentPlayer) {}
        });
    }

    public void setSelected(boolean toggle)
    {
        if(!toggle)
        {
            setBackground(Colors.PROFILE_DEFAULT);
            setBorder(BorderFactory.createEmptyBorder());
        }
        else
        {
            setBackground(Colors.PROFILE_SELECTED);
            setBorder(BorderFactory.createLineBorder(Colors.PROFILE_HIGHLIGHT, 2));
        }
    }

    public void updateScore()
    { scoreLabel.setText("| ".concat(String.valueOf(player.getScore()))); }
}
