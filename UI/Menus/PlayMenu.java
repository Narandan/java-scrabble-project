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
	private JButton deselectLoadButton;
	private JLabel fileLabel;
	private File selectedFile = null;
	private JPanel playersContainer;
	private JScrollPane playersScroll;
	private List<PlayerEntry> playerEntries = new ArrayList<>();

	@Override
	public void jumpLoad(Object... args) 
	{
		slider.setValue(2);
		updatePlayerEntries(0);
		updatePlayerEntries(2);

		deselectFile();

		updateGameArgs();
	}

	public PlayMenu(JComponent parent, String jumpName) {
		super(parent, jumpName);

		setLayout(new BorderLayout(12, 12));
		setBackground(Colors.BACKGROUND_1);
		setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

		JPanel top = new JPanel(new BorderLayout());
		top.setOpaque(false);

		CardJumpButton backButton = new CardJumpButton("titlemenu");
		backButton.setText("Back");
		backButton.setBackground(Colors.BUTTON_2);
		backButton.setUI(new UI.Styles.ScrabbleButtonUI1());
		backButton.setPreferredSize(new Dimension(75, 36));
		JPanel leftTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
		leftTop.setOpaque(false);
		leftTop.add(backButton);
		top.add(leftTop, BorderLayout.WEST);

		JPanel rightTop = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		rightTop.setOpaque(false);
		JButton loadButton = new JButton("Load Game");
		loadButton.setUI(new UI.Styles.ScrabbleButtonUI1());
		loadButton.setPreferredSize(new Dimension(120, 36));
		loadButton.setBackground(Colors.BUTTON_5);

		deselectLoadButton = new JButton("X");
		deselectLoadButton.setUI(new ScrabbleButtonUI1());
		deselectLoadButton.setPreferredSize(new Dimension(36, 36));
		deselectLoadButton.setBackground(Colors.BUTTON_4);
		deselectLoadButton.setVisible(false);

		fileLabel = new JLabel("No file selected");
		fileLabel.setForeground(Color.WHITE);
		fileLabel.setFont(fileLabel.getFont().deriveFont(Font.PLAIN, 12f));
		rightTop.add(deselectLoadButton);
		rightTop.add(fileLabel);
		rightTop.add(loadButton);
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
		slider.setUI(new ScrabbleSliderUI1());
		slider.setBackground(Colors.BACKGROUND_1);
		slider.setMajorTickSpacing(1);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.setPreferredSize(new Dimension(300, 50));
		sliderPanel.add(slider);

		bottom.add(sliderPanel, BorderLayout.WEST);

		startButton = new CardJumpButton("gamemenu");
		startButton.setPreferredSize(new Dimension(120, 36));
		startButton.setText("Start Game");
		startButton.setUI(new ScrabbleButtonUI1());
		startButton.setBackground(Colors.BUTTON_1);
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

		loadButton.addActionListener(e -> {
			Window w = SwingUtilities.getWindowAncestor(PlayMenu.this);
			Frame owner = (w instanceof Frame) ? (Frame) w : null;
			FileDialog fd = new FileDialog(owner, "Open Game File", FileDialog.LOAD);
			fd.setVisible(true);
			String dir = fd.getDirectory();
			String file = fd.getFile();
			if (file != null) {
				selectedFile = (dir != null) ? new File(dir, file) : new File(file);
				fileLabel.setText(selectedFile.getName());
				deselectLoadButton.setVisible(true);
				updateGameArgs();
			}
		});

		deselectLoadButton.addActionListener((ActionEvent e) ->
		{
			deselectFile();
			updateGameArgs();
		});
	}

	private void deselectFile()
	{
		if(selectedFile != null)
		{
			fileLabel.setText("No file selected");//change string to static final
			selectedFile = null;
			deselectLoadButton.setVisible(false);
		}
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

			JPanel panel = new JPanel(new BorderLayout());
			panel.setUI(new ScrabblePanelUI1());
			panel.setBackground(Colors.BACKGROUND_3);
			add(panel);

			JPanel leftPanel = new JPanel();
			leftPanel.setBackground(panel.getBackground());
			
			nameField = new JTextField("Player " + index);
			nameField.setColumns(16);
			nameField.setBackground(Color.GRAY);
			nameField.setUI(new ScrabbleTextFieldUI1());
			nameField.addActionListener( (ActionEvent e) ->
			{
				PlayMenu.this.updateGameArgs();
			});

			botBox = new JCheckBox("Bot");
			botBox.setOpaque(false);
			botBox.setUI(new ScrabbleCheckBoxUI1());
			botBox.addActionListener( (ActionEvent e) ->
			{
				PlayMenu.this.updateGameArgs();
			});

			JLabel order = new JLabel(String.valueOf(index) + ".");
			order.setForeground(Color.WHITE);
			panel.add(leftPanel, BorderLayout.WEST);
			leftPanel.add(order);
			leftPanel.add(nameField);
			panel.add(botBox, BorderLayout.EAST);
		}

		public PlayerInfo getInfo() {
			return new PlayerInfo(nameField.getText(), botBox.isSelected());
		}
	}
}