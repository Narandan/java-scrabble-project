package UI.Menus;

import javax.swing.JSlider;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.SwingUtilities;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Window;
import java.awt.Container;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import UI.Info.Colors;
import UI.Info.CardJumpNames;
import UI.Info.Strings;
import UI.Elements.CardJumpPanel;
import UI.Elements.CardJumpButton;
import UI.Styles.ScrabbleSliderUI1;
import UI.Styles.ScrabbleButtonUI1;
import UI.Styles.ScrabblePanelUI1;
import UI.Styles.ScrabbleTextFieldUI1;
import UI.Styles.ScrabbleLabelUI2;

public class PlayMenu extends CardJumpPanel {

	private JSlider slider;
	private CardJumpButton startButton;
	private JButton deselectLoadButton;
	private JLabel fileLabel;
	private File selectedFile = null;
	private JButton loadButton;
	private JPanel playersContainer;
	private List<PlayerEntry> playerEntries = new ArrayList<>();

	public PlayMenu(Container parent, String jumpName) {
		super(parent, jumpName);

		setLayout(new BorderLayout(12, 12));
		setBackground(Colors.BACKGROUND_1);
		setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.setOpaque(false);

		// Back button
		JPanel leftTopPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		leftTopPanel.setOpaque(false);

		CardJumpButton backButton = new CardJumpButton(CardJumpNames.TITLEMENU);
		backButton.setText(Strings.PLAYMENU_BACK_BUTTON_TEXT);
		backButton.setBackground(Colors.BUTTON_2);
		backButton.setUI(new ScrabbleButtonUI1());
		backButton.setPreferredSize(new Dimension(75, 36));

		leftTopPanel.add(backButton);

		// Load game panel
		JPanel rightTopPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		rightTopPanel.setOpaque(false);

		loadButton = new JButton(Strings.PLAYMENU_LOAD_BUTTON_TEXT);
		loadButton.setUI(new ScrabbleButtonUI1());
		loadButton.setPreferredSize(new Dimension(120, 36));
		loadButton.setBackground(Colors.BUTTON_5);

		deselectLoadButton = new JButton(Strings.PLAYMENU_DESELECT_LOAD_BUTTON_SYMBOL);
		deselectLoadButton.setUI(new ScrabbleButtonUI1());
		deselectLoadButton.setPreferredSize(new Dimension(36, 36));
		deselectLoadButton.setBackground(Colors.BUTTON_4);
		deselectLoadButton.setVisible(false);

		fileLabel = new JLabel(Strings.PLAYMENU_NO_FILE_SELECTED_TEXT);
		fileLabel.setUI(new ScrabbleLabelUI2());

		rightTopPanel.add(deselectLoadButton);
		rightTopPanel.add(fileLabel);
		rightTopPanel.add(loadButton);

		topPanel.add(rightTopPanel, BorderLayout.EAST);
		topPanel.add(leftTopPanel, BorderLayout.WEST);

		// Players scroll container
		playersContainer = new JPanel();
		playersContainer.setLayout(new BoxLayout(playersContainer, BoxLayout.Y_AXIS));
		playersContainer.setBackground(Colors.BACKGROUND_2);

		JScrollPane playersScroll = new JScrollPane(playersContainer);
		playersScroll.setBorder(BorderFactory.createLineBorder(Colors.BACKGROUND_3));
		playersScroll.getViewport().setBackground(Colors.BACKGROUND_2);

		// Bottom panel (slider + start button)
		JPanel bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.setOpaque(false);

		JPanel bottomLeftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
		bottomLeftPanel.setOpaque(false);

		slider = new JSlider(JSlider.HORIZONTAL, 2, 4, 2);
		slider.setUI(new ScrabbleSliderUI1());
		slider.setBackground(Colors.BACKGROUND_1);
		slider.setMajorTickSpacing(1);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.setPreferredSize(new Dimension(300, 50));

		JLabel playersLabel = new JLabel(Strings.PLAYMENU_PLAYERS_LABEL_TEXT);
		playersLabel.setUI(new ScrabbleLabelUI2());

		bottomLeftPanel.add(playersLabel);
		bottomLeftPanel.add(slider);

		JPanel bottomRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		bottomRightPanel.setOpaque(false);

		startButton = new CardJumpButton(CardJumpNames.GAMEMENU);
		startButton.setPreferredSize(new Dimension(120, 36));
		startButton.setText(Strings.PLAYMENU_START_BUTTON_TEXT);
		startButton.setUI(new ScrabbleButtonUI1());
		startButton.setBackground(Colors.BUTTON_1);

		bottomRightPanel.add(startButton);

		bottomPanel.add(bottomRightPanel, BorderLayout.CENTER);
		bottomPanel.add(bottomLeftPanel, BorderLayout.WEST);

		add(topPanel, BorderLayout.NORTH);
		add(playersScroll, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.SOUTH);

		updatePlayerEntries(slider.getValue());
		updateGameArgs();

		connectComponents();
	}

	private void connectComponents() {
		// Slider player count
		slider.addChangeListener(e -> {
			updatePlayerEntries(slider.getValue());
			updateGameArgs();
		});

		slider.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				slider.repaint();
			}
		});

		loadButton.addActionListener(e -> {
			Window window = SwingUtilities.getWindowAncestor(PlayMenu.this);
			Frame owner = (window instanceof Frame) ? (Frame) window : null;

			FileDialog dialog = new FileDialog(owner, Strings.PLAYMENU_FILE_DIALOG_TITLE, FileDialog.LOAD);
			dialog.setVisible(true);

			String dir = dialog.getDirectory();
			String file = dialog.getFile();

			if (file != null) {
				selectedFile = (dir != null) ? new File(dir, file) : new File(file);
				fileLabel.setText(selectedFile.getName());
				deselectLoadButton.setVisible(true);
				updateGameArgs();
			}
		});

		deselectLoadButton.addActionListener((ActionEvent e) -> {
			deselectFile();
			updateGameArgs();
		});
	}

	public void jumpLoad(Object... args) {
		slider.setValue(2);
		updatePlayerEntries(0);
		updatePlayerEntries(2);
		deselectFile();
		updateGameArgs();
	}

	private void deselectFile() {
		if (selectedFile != null) {
			fileLabel.setText(Strings.PLAYMENU_NO_FILE_SELECTED_TEXT);
			selectedFile = null;
			deselectLoadButton.setVisible(false);
		}
	}

	private void updateGameArgs() {
		List<PlayerInfo> infos = new ArrayList<>();
		for (PlayerEntry entry : playerEntries)
			infos.add(entry.getInfo());
		startButton.changeArgs(infos, selectedFile);
	}

	private void updatePlayerEntries(int count) {
		while (playerEntries.size() < count) {
			PlayerEntry entry = new PlayerEntry(playerEntries.size() + 1);
			playerEntries.add(entry);
			playersContainer.add(entry);
		}
		while (playerEntries.size() > count) {
			int last = playerEntries.size() - 1;
			PlayerEntry entry = playerEntries.remove(last);
			playersContainer.remove(entry);
		}
		playersContainer.revalidate();
	}

	// Cleaned PlayerInfo: no bot field
	public static class PlayerInfo {
		public String name;
		public PlayerInfo(String name) { this.name = name; }
	}

	private class PlayerEntry extends JPanel {
		private JTextField nameField;

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

			nameField = new JTextField(Strings.PLAYMENU_PLAYER_ENTRY_DEFAULT_NAME + index);
			nameField.setColumns(16);
			nameField.setBackground(Colors.BACKGROUND_1);
			nameField.setUI(new ScrabbleTextFieldUI1());
			nameField.addActionListener((ActionEvent e) -> updateGameArgs());

			JLabel order = new JLabel(String.valueOf(index) + ".");
			order.setUI(new ScrabbleLabelUI2());

			leftPanel.add(order);
			leftPanel.add(nameField);

			panel.add(leftPanel, BorderLayout.WEST);
			// Removed bot checkbox entirely
		}

		public PlayerInfo getInfo() {
			return new PlayerInfo(nameField.getText());
		}
	}
}
