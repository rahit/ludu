package com.tahsinrahit.ludu;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class GameOptionSelectionPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private JPanel nameSelectionPanel;
	private JPanel courtSelectionPanel;
	private JTextField[] playersTextField = new JTextField[4];
	private JLabel[] playersLabel = new JLabel[4];
	private JButton startButton;
	private JRadioButton avengersRadioButton;
	private JRadioButton basicRadioButton;
	private ButtonGroup courtSelection;
	
	
	private LuduCourt luduCourt;
	private ArrayList<LuduPlayer>luduPlayers;
	private String boardBackground;
	

	private String[] avengersPlayerNames = {"Thor", "Captain America", "Iron Man", "Hulk"};
	private LuduCourt avengersCourt;
	private String[] justiceLeaguePlayerNames = {"Wolverine", "Batman", "Superman", "Green Lantern"};
	private LuduCourt justiceLeagueCourt;
	
	
	public GameOptionSelectionPanel() {
		this.luduPlayers = new ArrayList<LuduPlayer>(4);
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.nameSelectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 30));
		this.courtSelectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 0));
		
		this.initOptions();
		this.initTextFields();

		this.startButton = new JButton("Start");

		this.addComponents();
		
		this.setVisible(true);
	}
	

	private void addComponents() {
		this.nameSelectionPanel.add(this.playersLabel[1]);
		this.nameSelectionPanel.add(this.playersTextField[1]);
		this.nameSelectionPanel.add(this.playersLabel[2]);
		this.nameSelectionPanel.add(this.playersTextField[2]);
		this.nameSelectionPanel.add(this.playersLabel[0]);
		this.nameSelectionPanel.add(this.playersTextField[0]);
		this.nameSelectionPanel.add(this.playersLabel[3]);
		this.nameSelectionPanel.add(this.playersTextField[3]);
		this.courtSelectionPanel.add(this.avengersRadioButton);
		this.courtSelectionPanel.add(this.basicRadioButton);
		this.add(this.courtSelectionPanel);
		this.add(this.nameSelectionPanel);
		this.add(this.startButton);
	}


	private void initOptions() {		
		this.avengersCourt = new LuduCourt(0, "avengers", avengersPlayerNames);
		this.justiceLeagueCourt = new LuduCourt(0, "justice_league", justiceLeaguePlayerNames);
		this.avengersRadioButton = new JRadioButton("Avengers", new ImageIcon(this.getClass().getResource("resources/"+"avengers_thumb.png")), true);
		this.luduCourt = avengersCourt;
		this.basicRadioButton = new JRadioButton("Justice League", new ImageIcon(this.getClass().getResource("resources/"+"justice_league_thumb.png")));
		this.courtSelection = new ButtonGroup();
		this.courtSelection.add(this.avengersRadioButton);
		this.courtSelection.add(this.basicRadioButton);
		ActionListener optionSelectionListener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(GameOptionSelectionPanel.this.avengersRadioButton.isSelected()) {
					GameOptionSelectionPanel.this.luduCourt = GameOptionSelectionPanel.this.avengersCourt;
					GameOptionSelectionPanel.this.setTextFields();
				}
				else if (GameOptionSelectionPanel.this.basicRadioButton.isSelected()) {
					GameOptionSelectionPanel.this.luduCourt = GameOptionSelectionPanel.this.justiceLeagueCourt;
					GameOptionSelectionPanel.this.setTextFields();
				}
			}
		};
		this.avengersRadioButton.addActionListener(optionSelectionListener);
		this.basicRadioButton.addActionListener(optionSelectionListener);
	}


	protected void setTextFields() {
		this.playersTextField[0].setText(this.luduCourt.getPlayerName(0));
		this.playersTextField[1].setText(this.luduCourt.getPlayerName(1));
		this.playersTextField[2].setText(this.luduCourt.getPlayerName(2));
		this.playersTextField[3].setText(this.luduCourt.getPlayerName(3));
	}


	private void initTextFields() {
		this.playersTextField[0] = new JTextField(10);
		this.playersTextField[1] = new JTextField(10);
		this.playersTextField[2] = new JTextField(10);
		this.playersTextField[3] = new JTextField(10);

		this.playersTextField[0].setText(this.luduCourt.getPlayerName(0));
		this.playersTextField[1].setText(this.luduCourt.getPlayerName(1));
		this.playersTextField[2].setText(this.luduCourt.getPlayerName(2));
		this.playersTextField[3].setText(this.luduCourt.getPlayerName(3));
		
		this.playersLabel[0] = new JLabel("Player Name (yellow)");
		this.playersLabel[1] = new JLabel("Player Name (blue)");
		this.playersLabel[2] = new JLabel("Player Name (red)");
		this.playersLabel[3] = new JLabel("Player Name (green)");		
	}


	protected boolean isSetPlayers() {
		int playerCount = 0;
		for (int i = 0; i < this.playersTextField.length; i++) {
			if(!this.playersTextField[i].getText().isEmpty()) {
				playerCount++;
				switch (i) {
				case 0:
					this.luduPlayers.add(new LuduPlayer(Color.YELLOW, this.playersTextField[i].getText()));
					break;

				case 1:
					this.luduPlayers.add(new LuduPlayer(Color.BLUE, this.playersTextField[i].getText()));
					break;
					
				case 2:
					this.luduPlayers.add(new LuduPlayer(Color.RED, this.playersTextField[i].getText()));
					break;
					
				case 3:
					this.luduPlayers.add(new LuduPlayer(Color.GREEN, this.playersTextField[i].getText()));
					break;
					
				default:
					break;
				}
			}
			
		}
		if(playerCount > 1) {
			return true;
		}
		return false;
	}


	public JButton getStartButton() {
		return startButton;
	}


	public void setStartButton(JButton startButton) {
		this.startButton = startButton;
	}


	public ArrayList<LuduPlayer> getLuduPlayers() {
		return luduPlayers;
	}


	public void setLuduPlayers(ArrayList<LuduPlayer> luduPlayers) {
		this.luduPlayers = luduPlayers;
	}


	public String getBoardBackground() {
		return boardBackground;
	}


	public void setBoardBackground(String boardBackground) {
		this.boardBackground = boardBackground;
	}


	public LuduCourt getLuduCourt() {
		return luduCourt;
	}


	public void setLuduCourt(LuduCourt luduCourt) {
		this.luduCourt = luduCourt;
	}

	
	

}
