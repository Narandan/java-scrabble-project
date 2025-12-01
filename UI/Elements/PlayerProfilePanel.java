package UI.Elements;

import javax.swing.*;
import UI.Styles.*;
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
    { scoreLabel.setText(String.valueOf(player.getScore())); }
}
