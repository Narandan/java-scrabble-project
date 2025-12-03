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
import java.util.List;
import UI.Elements.CardJumpPanel;
import UI.Elements.CardJumpButton;
import UI.Elements.PlayerProfilePanel;
import UI.Styles.ScrabbleLabelUI2;
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
        winnerNameLabel.setHorizontalAlignment(SwingConstants.CENTER);

        winnerPanel.add(winnerTitleLabel, BorderLayout.NORTH);
        winnerPanel.add(winnerNameLabel, BorderLayout.CENTER);

        statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        statsPanel.setOpaque(false);

        JScrollPane scroll = new JScrollPane(statsPanel);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        mainPanel.add(scroll, BorderLayout.CENTER);
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
        for(Player player : players)
        {
            PlayerProfilePanel panel = new PlayerProfilePanel(player);
            statsPanel.add(panel);
        }
    }
}
