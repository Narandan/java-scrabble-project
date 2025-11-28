package UI.Elements;

import javax.swing.*;

import UI.Styles.ScrabblePanelUI1;
import scrabble.*;
import java.awt.Color;

public class PlayerProfilePanel extends JPanel 
{
    private Player player;
    private JLabel nameLabel;
    private JLabel scoreLabel;

    public PlayerProfilePanel(Player player)
    {
        this.player = player;

        setBackground(Color.MAGENTA);
        setUI(new ScrabblePanelUI1());

        nameLabel = new JLabel();
        nameLabel.setText(player.getName());

        scoreLabel = new JLabel();
        updateScore();

        add(nameLabel);
        add(scoreLabel);
    }

    public void updateScore()
    {
        scoreLabel.setText(String.valueOf(player.getScore()));
    }
}
