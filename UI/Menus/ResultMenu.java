package UI.Menus;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.BoxLayout;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Container;
import java.awt.Color;
import java.util.List;
import java.util.ArrayList;
import UI.Elements.CardJumpPanel;
import UI.Elements.CardJumpButton;
import UI.Elements.PlayerProfilePanel;
import UI.Styles.ScrabbleLabelUI2;
import UI.Styles.ScrabblePanelUI1;
import UI.Styles.Fonts;
import UI.Styles.ScrabbleButtonUI1;
import scrabble.Player;
import UI.Info.Colors;
import UI.Info.CardJumpNames;
import UI.Info.Strings;

public class ResultMenu extends CardJumpPanel {
    private JLabel winnerTitleLabel;
    private JLabel winnerNameLabel;
    private JPanel statsPanel;

    private static List<Color> colors = new ArrayList<>();

    static
    {
        colors.add(Colors.WORD_2);
        colors.add(Colors.WORD_3);
        colors.add(Colors.WORD_4);
    }

    public ResultMenu(Container parent, String jumpName)
    {
        super(parent, jumpName);

        setLayout(new BorderLayout(12,12));
        setBackground(Colors.BACKGROUND_1);
        setBorder(BorderFactory.createEmptyBorder(12,12,12,12));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.setBackground(getBackground());

        CardJumpButton backButton = new CardJumpButton(CardJumpNames.TITLEMENU);
        backButton.setUI(new ScrabbleButtonUI1());
        backButton.setBackground(Colors.BUTTON_2);
        backButton.setText(Strings.RESULTMENU_BACK_BUTTON_TEXT);

        topPanel.add(backButton, BorderLayout.WEST);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(getBackground());

        JPanel winnerPanel = new JPanel(new BorderLayout());
        winnerPanel.setOpaque(false);

        winnerTitleLabel = new JLabel(Strings.RESULTMENU_WINNER_TITLE);
        winnerTitleLabel.setUI(new ScrabbleLabelUI2());
        winnerTitleLabel.setFont(Fonts.SCRABBLE_FONT_1.deriveFont(Font.BOLD, 28f));
        winnerTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        winnerNameLabel = new JLabel(Strings.RESULTMENU_DEFAULT_WINNER_TEXT);
        winnerNameLabel.setUI(new ScrabbleLabelUI2());
        winnerNameLabel.setFont(Fonts.SCRABBLE_FONT_1.deriveFont(Font.PLAIN, 48f));
        winnerNameLabel.setForeground(Colors.WORD_2);
        winnerNameLabel.setHorizontalAlignment(SwingConstants.CENTER);

        winnerPanel.add(winnerTitleLabel, BorderLayout.NORTH);
        winnerPanel.add(winnerNameLabel, BorderLayout.CENTER);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);

        JLabel statsTitleLabel = new JLabel(Strings.PLAYMENU_PLAYERS_LABEL_TEXT);
        statsTitleLabel.setUI(new ScrabbleLabelUI2());
        statsTitleLabel.setFont(Fonts.SCRABBLE_FONT_1.deriveFont(Font.BOLD, 24f));
        statsTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        centerPanel.add(statsTitleLabel, BorderLayout.NORTH);

        statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        statsPanel.setUI(new ScrabblePanelUI1());
        statsPanel.setBackground(Colors.PROFILE_DEFAULT);

        JScrollPane scroll = new JScrollPane(statsPanel);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        centerPanel.add(statsTitleLabel, BorderLayout.NORTH);
        centerPanel.add(scroll, BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(winnerPanel, BorderLayout.NORTH);

        add(topPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
    }

    @SuppressWarnings("unchecked")
    public void jumpLoad(Object... args)
    {
        statsPanel.removeAll();

        if(args[0] instanceof List)
        {
            List<Player> players = (List<Player>) args[0];

            setWinner(players.get(0));
            rankPlayers(players);
        }
    }

    private void setWinner(Player player)
    {
        if(player != null) winnerNameLabel.setText(player.getName());
        else winnerNameLabel.setText(Strings.RESULTMENU_DEFAULT_WINNER_TEXT);
    }

    private void rankPlayers(List<Player> players)
    {
        for(int i=0; i<players.size(); i++)
        {
            PlayerProfilePanel panel = new PlayerProfilePanel(players.get(i), i < colors.size() ? colors.get(i) : null);
            statsPanel.add(panel);
        }
    }
}
