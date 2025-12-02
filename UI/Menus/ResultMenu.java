package UI.Menus;

import UI.Elements.*;
import UI.Styles.*;

import javax.swing.*;
import java.awt.*;

public class ResultMenu extends CardJumpPanel {
    private JLabel winnerTitleLabel;
    private JLabel winnerNameLabel;
    private JPanel statsPanel;

    public void jumpLoad(Object... args)
    { }

    public ResultMenu(JComponent parent, String jumpName)
    {
        super(parent, jumpName);

        setLayout(new BorderLayout(12,12));
        setBackground(Colors.BACKGROUND_1);
        setBorder(BorderFactory.createEmptyBorder(12,12,12,12));

        JPanel winnerPanel = new JPanel(new BorderLayout());
        winnerPanel.setOpaque(false);

        winnerTitleLabel = new JLabel("Winner");
        winnerTitleLabel.setFont(Fonts.SCRABBLE_FONT_1.deriveFont(Font.BOLD, 28f));
        winnerTitleLabel.setForeground(Color.WHITE);
        winnerTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        winnerPanel.add(winnerTitleLabel, BorderLayout.NORTH);

        winnerNameLabel = new JLabel("TBD");
        winnerNameLabel.setFont(Fonts.SCRABBLE_FONT_1.deriveFont(Font.PLAIN, 48f));
        winnerNameLabel.setForeground(Color.WHITE);
        winnerNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        winnerPanel.add(winnerNameLabel, BorderLayout.CENTER);

        add(winnerPanel, BorderLayout.NORTH);

        // Stats area (center)
        statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        statsPanel.setOpaque(false);

        // Example placeholder stat rows (formatted labels)
        statsPanel.add(createStatRow("Score:", "--"));
        statsPanel.add(Box.createRigidArea(new Dimension(0,8)));
        statsPanel.add(createStatRow("Words Formed:", "--"));
        statsPanel.add(Box.createRigidArea(new Dimension(0,8)));
        statsPanel.add(createStatRow("Tiles Used:", "--"));

        JScrollPane scroll = new JScrollPane(statsPanel);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        add(scroll, BorderLayout.CENTER);

        // Footer note
        JLabel note = new JLabel("Match summary and detailed stats will appear here.");
        note.setForeground(Color.WHITE);
        note.setFont(Fonts.SCRABBLE_FONT_1.deriveFont(Font.PLAIN, 12f));
        note.setHorizontalAlignment(SwingConstants.CENTER);
        add(note, BorderLayout.SOUTH);

        System.out.println("setup result menu");
    }

    private JPanel createStatRow(String label, String value) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        JLabel left = new JLabel(label);
        left.setForeground(Color.WHITE);
        left.setFont(Fonts.SCRABBLE_FONT_1.deriveFont(Font.PLAIN, 16f));
        JLabel right = new JLabel(value);
        right.setForeground(Color.WHITE);
        right.setFont(Fonts.SCRABBLE_FONT_1.deriveFont(Font.BOLD, 16f));
        row.add(left, BorderLayout.WEST);
        row.add(right, BorderLayout.EAST);
        return row;
    }
}
