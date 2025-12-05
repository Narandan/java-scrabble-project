package UI.Elements;

import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import java.awt.Color;
import java.util.List;
import java.awt.BorderLayout;
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
        setLayout(new BorderLayout());
        setUI(new ScrabblePanelUI1());

        nameLabel = new JLabel();
        nameLabel.setText(player.getName());
        nameLabel.setFont(Fonts.SCRABBLE_FONT_1);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        scoreLabel = new JLabel();
        scoreLabel.setFont(Fonts.SCRABBLE_FONT_1);
        scoreLabel.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        
        updateScore();

        add(nameLabel, BorderLayout.CENTER);
        add(scoreLabel, BorderLayout.EAST);

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
