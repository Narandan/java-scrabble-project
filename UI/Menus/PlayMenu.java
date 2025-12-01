package UI.Menus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import UI.Elements.*;
import UI.Styles.*;

public class PlayMenu extends CardJumpPanel {
	private JSlider slider;
	private CardJumpButton startButton;
	private File selectedFile = null;
	private JPanel playersContainer;
	private JScrollPane playersScroll;
	private List<PlayerEntry> playerEntries = new ArrayList<>();

	@Override
	public void jumpLoad(Object... args) 
	{
		//reset everything
	}

	public PlayMenu(String jumpName) {
		super(jumpName);

		setLayout(new BorderLayout(12, 12));
		setBackground(Colors.BACKGROUND_1);
		setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

		JPanel top = new JPanel(new BorderLayout());
		top.setOpaque(false);

		CardJumpButton backButton = new CardJumpButton("title");
		backButton.setText("Back");
		backButton.setUI(new UI.Styles.ScrabbleButtonUI1());
		backButton.setPreferredSize(new Dimension(100, 36));
		JPanel leftTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
		leftTop.setOpaque(false);
		leftTop.add(backButton);
		top.add(leftTop, BorderLayout.WEST);

		JPanel rightTop = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		rightTop.setOpaque(false);
		JButton loadBtn = new JButton("Load Game");
		loadBtn.setUI(new UI.Styles.ScrabbleButtonUI1());
		loadBtn.setPreferredSize(new Dimension(120, 36));
		JLabel fileLabel = new JLabel("No file selected");
		fileLabel.setForeground(Color.WHITE);
		fileLabel.setFont(fileLabel.getFont().deriveFont(Font.PLAIN, 12f));
		rightTop.add(loadBtn);
		rightTop.add(fileLabel);
		top.add(rightTop, BorderLayout.EAST);

		add(top, BorderLayout.NORTH);

		playersContainer = new JPanel();
		playersContainer.setLayout(new BoxLayout(playersContainer, BoxLayout.Y_AXIS));
		playersContainer.setBackground(Colors.BACKGROUND_2);
		playersScroll = new JScrollPane(playersContainer);
		playersScroll.setBorder(BorderFactory.createLineBorder(Colors.BACKGROUND_3));
		playersScroll.getViewport().setBackground(Colors.BACKGROUND_2);
		add(playersScroll, BorderLayout.CENTER);

		JPanel bottom = new JPanel(new BorderLayout());
		bottom.setOpaque(false);

		JPanel sliderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
		sliderPanel.setOpaque(false);
		JLabel playersLabel = new JLabel("Players:");
		playersLabel.setForeground(Color.WHITE);
		sliderPanel.add(playersLabel);

		slider = new JSlider(JSlider.HORIZONTAL, 2, 8, 2);
		slider.setMajorTickSpacing(1);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.setPreferredSize(new Dimension(300, 50));
		sliderPanel.add(slider);

		bottom.add(sliderPanel, BorderLayout.WEST);

		startButton = new CardJumpButton("gamemenu");
		JPanel startWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		startWrap.setOpaque(false);
		startWrap.add(startButton);
		bottom.add(startWrap, BorderLayout.CENTER);

		add(bottom, BorderLayout.SOUTH);

		updatePlayerEntries(slider.getValue());
		updateGameArgs();

		slider.addChangeListener(e -> {
			int cnt = slider.getValue();
			updatePlayerEntries(cnt);
			updateGameArgs();
		});

		loadBtn.addActionListener(e -> {
			Window w = SwingUtilities.getWindowAncestor(PlayMenu.this);
			Frame owner = (w instanceof Frame) ? (Frame) w : null;
			FileDialog fd = new FileDialog(owner, "Open Game File", FileDialog.LOAD);
			fd.setVisible(true);
			String dir = fd.getDirectory();
			String file = fd.getFile();
			if (file != null) {
				selectedFile = (dir != null) ? new File(dir, file) : new File(file);
				fileLabel.setText(selectedFile.getName());
				updateGameArgs();
			}
		});
	}

	private void updateGameArgs()
	{
		List<PlayerInfo> infos = new ArrayList<>();

		for(PlayerEntry entry : playerEntries)
			infos.add(entry.getInfo());

		startButton.changeArgs(infos, selectedFile);
	}

	private void updatePlayerEntries(int count) {
		while (playerEntries.size() < count) {
			PlayerEntry pe = new PlayerEntry(playerEntries.size() + 1);
			playerEntries.add(pe);
			playersContainer.add(pe);
		}
		while (playerEntries.size() > count) {
			int last = playerEntries.size() - 1;
			PlayerEntry pe = playerEntries.remove(last);
			playersContainer.remove(pe);
		}
		playersContainer.revalidate();
		playersContainer.repaint();
	}

	public static class PlayerInfo {
		public String name;
		public boolean isBot;
		public PlayerInfo(String name, boolean isBot) {
			this.name = name;
			this.isBot = isBot;
		}
	}

	private class PlayerEntry extends JPanel {
		private JTextField nameField;
		private JCheckBox botBox;
		public PlayerEntry(int index) {
			setLayout(new FlowLayout(FlowLayout.LEFT, 8, 6));
			setOpaque(true);
			setBackground(Colors.BACKGROUND_2);
			setBorder(BorderFactory.createLineBorder(Colors.BACKGROUND_3));

			nameField = new JTextField("Player " + index);
			nameField.setColumns(16);
			nameField.addActionListener( (ActionEvent e) ->
			{
				PlayMenu.this.updateGameArgs();
			});

			botBox = new JCheckBox("Bot");
			botBox.setOpaque(false);
			botBox.addActionListener( (ActionEvent e) ->
			{
				PlayMenu.this.updateGameArgs();
			});

			JLabel order = new JLabel(String.valueOf(index) + ".");
			order.setForeground(Color.WHITE);
			add(order);
			add(nameField);
			add(botBox);
		}

		public PlayerInfo getInfo() {
			return new PlayerInfo(nameField.getText(), botBox.isSelected());
		}
	}
}