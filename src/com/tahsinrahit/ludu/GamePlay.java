package com.tahsinrahit.ludu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;


public class GamePlay implements Runnable{
	private LuduCourt luduCourt;
	private GameFrame mainGameFrame;
	
	private static boolean isGameFinished = false;
	private boolean isProcessRunning = false;
	private boolean isRollAgain;
	private LuduPlayer currentPlayer;
	private Board board;
	private ArrayList<LuduPlayer>luduPlayers;
	private ArrayList<LuduPlayer>winners;
	private Turn turn;
	private int diceValue = 0;
	
	private GameOptionSelectionPanel gameOptionSelectionPanel;
	
	public GamePlay() {
		// TODO Auto-generated constructor stub
	}
	
	public GamePlay(GameFrame mainGameFrame) {
		this.mainGameFrame = mainGameFrame;
		GamePlay.isGameFinished = false;
		this.isProcessRunning = false;
		this.luduPlayers = new ArrayList<LuduPlayer>(4);
		this.selectGameOption();
		this.board = new Board(this.mainGameFrame, this.luduCourt);
		new Thread(new GameMusic("halo_theme.mp3", true)).start();
		Board.initpieceOrigin();        
		this.board.setPlayers(this.luduPlayers);
		this.currentPlayer = this.luduPlayers.get(0);
		this.winners = new ArrayList<LuduPlayer>(4);
	}


	private void selectGameOption() {
		this.gameOptionSelectionPanel = new GameOptionSelectionPanel();
		this.mainGameFrame.add(this.gameOptionSelectionPanel);
		this.mainGameFrame.pack();
		this.gameOptionSelectionPanel.getStartButton().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(GamePlay.this.gameOptionSelectionPanel.isSetPlayers()) {
					GamePlay.this.luduPlayers = GamePlay.this.gameOptionSelectionPanel.getLuduPlayers();
					GamePlay.this.luduCourt = GamePlay.this.gameOptionSelectionPanel.getLuduCourt();
					GamePlay.this.gameOptionSelectionPanel.setVisible(false);
					synchronized (GamePlay.this) {
						GamePlay.this.notify();
					}
				}
			}
		});

		synchronized (this) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}	
	}

	
	

	public void run() {
		int i = 0;
		while (!GamePlay.isGameFinished) {
			this.isRollAgain = false;
			this.currentPlayer = this.luduPlayers.get(i);
			this.turn = new Turn(this);
			this.board.getStatus().setText(this.currentPlayer.getPlayerName() + "'s turn");
			ActionListener rollActionListener = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(!GamePlay.this.isProcessRunning) {
						GamePlay.this.roll();
					}
					GamePlay.this.board.getDice().removeActionListener(this);
				}
			};
			this.board.getDice().addActionListener(rollActionListener);
			synchronized (this) {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}	
			this.board.getDice().removeActionListener(rollActionListener);
			if(this.isMoveAvailable()) {
				this.move();
			}
			if(this.isPlayerFinished()) {
				this.removePlayerFromList();
			}
			if(this.luduPlayers.size() == 1) {
				this.winners.add(this.luduPlayers.get(0));
				GamePlay.isGameFinished = true;
			}
			if(!this.isRollAgain) {
				i++;
				if(i >= this.luduPlayers.size()) {
					i = 0;
				}
			}
			GamePlay.this.isProcessRunning = false;
		}
		this.announceWinner();
	}

	private void announceWinner() {
		JDialog dialog = new JDialog(this.mainGameFrame, "We have a winner...", true);
		dialog.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		dialog.setMinimumSize(new Dimension(this.mainGameFrame.getHeight(), this.mainGameFrame.getHeight()));	
		dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);	
		dialog.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				super.windowClosed(e);
				synchronized (GamePlay.this.mainGameFrame) {
					GamePlay.this.mainGameFrame.notify();
				}
			}
		});
		
		JPanel championPanel = new JPanel();
		JPanel textPanel = new JPanel();
		
		textPanel.setLayout(new  FlowLayout(FlowLayout.CENTER, 100, 0));
		
		JLabel champion = new JLabel(new ImageIcon(this.getClass().getResource("resources/"+this.luduCourt.getCourtFileName()+"/"+this.winners.get(0).getPlayerId()+".png")));
		championPanel.add(champion);
		
		JLabel championLabel = new JLabel("Congratulations " + this.winners.get(0).getPlayerName());
		championLabel.setFont(new Font("Arial", Font.BOLD, 30));

		textPanel.add(championLabel);
		
		dialog.add(championPanel);
		dialog.add(textPanel);
		dialog.setVisible(true);
	}

	private boolean isMoveAvailable() {
		Piece[] pieces = this.currentPlayer.getPieces();
		for (int i = 0; i < pieces.length; i++) {
			int destinationIndex = this.calculateNextPosition(pieces[i], this.diceValue);
			if(this.turn.isAvailableToSelect(pieces[i], destinationIndex)) {
				return true;
			}
		}
		return false;
	}

	private void removePlayerFromList() {
		this.winners.add(this.currentPlayer);
		this.luduPlayers.remove(this.currentPlayer);
	}

	private boolean isPlayerFinished() {
		Piece[] pieces = this.currentPlayer.getPieces();
		for (int i = 0; i < pieces.length; i++) {
			if(pieces[i].getJournyCount() < 57 ) {
				return false;
			}
		}
		return true;
	}

	private boolean isEliminationAvailable(Piece currentPiece, int destinationIndex) {
		if(destinationIndex == 0 || destinationIndex == 13 || destinationIndex == 26 || destinationIndex == 39) {
			return false;
		}
		PositionMap map = Board.getPostionMapByIndex(destinationIndex);
		Piece[] pieces = map.getOpponentPiecesInThisPosition(currentPiece.getPieceColor());
		if(pieces[0] != null) {
			return true;
		}
		return false;
	}
	
	
	protected void roll() {
		this.isProcessRunning = true;
		GameMusic rollSound = new GameMusic("dice.mp3");
		new Thread(rollSound).start();
		this.diceValue = this.turn.rollDice();
		this.board.setDice(Integer.toString(this.diceValue));
		synchronized (GamePlay.this) {
			this.notify();
		}
	}

	private void move() {
		Piece currentPiece;
		int destinationIndex;
		do {
			currentPiece = this.turn.selectPiece();
			destinationIndex =  this.calculateNextPosition(currentPiece, this.diceValue);
		} while (!this.turn.isAvailableToSelect(currentPiece, destinationIndex));
		if(this.isEliminationAvailable(currentPiece, destinationIndex)) {
			this.board.getStatus().setText("You've got a chance to eliminate 1 piece of your opponent");
			this.turn.eliminate(destinationIndex);
		}
		int tempDiceValue = (currentPiece.getPositionIndex() == -1) ? 1 : this.diceValue;
		while(tempDiceValue > 0) {
			new Thread(new GameMusic("move.mp3")).start();
			this.turn.movePiece(this.calculateNextPosition(currentPiece, 1, true));
			tempDiceValue--;
			try {
				Thread.sleep(200);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}

	
	private int calculateNextPosition(Piece currentPiece, int positionToAdvance, boolean countJourny) {
		int tempJournyCount = currentPiece.getJournyCount()+positionToAdvance;
		int nextPosition = this.calculateNextPosition(currentPiece, positionToAdvance);
		if(countJourny) {
			currentPiece.setJournyCount(tempJournyCount);
		}
		return nextPosition;
	}

	private int calculateNextPosition(Piece currentPiece, int positionToAdvance) {
		int currentPosition = currentPiece.getPositionIndex();
		int tempJournyCount = currentPiece.getJournyCount()+positionToAdvance;
		if(currentPosition == -1) {
			// initial turn
			if (currentPiece.getPieceColor() == Color.YELLOW) {
				currentPosition = 0;
			} 
			else if(currentPiece.getPieceColor() == Color.BLUE) {
				currentPosition = 13;
			}
			else if(currentPiece.getPieceColor() == Color.RED) {
				currentPosition = 26;
			}
			else if(currentPiece.getPieceColor() == Color.GREEN) {
				currentPosition = 39;
			}
		}
		else if (currentPosition + positionToAdvance >= 52 && tempJournyCount < 51) {
			// Passing array bound for main line
			currentPosition = positionToAdvance - (52 - currentPosition);
		}
		else if(currentPosition < 52 && tempJournyCount > 51) {
			// Attempting to enter Home Line
			if (currentPiece.getPieceColor() == Color.YELLOW) {
				currentPosition = 51 + (positionToAdvance - (50 - currentPosition));
			} 
			else if(currentPiece.getPieceColor() == Color.BLUE) {
				currentPosition = 57 + (positionToAdvance - (11 - currentPosition));
			}
			else if(currentPiece.getPieceColor() == Color.RED) {
				currentPosition = 63 + (positionToAdvance - (24 - currentPosition));
			}
			else if(currentPiece.getPieceColor() == Color.GREEN) {
				currentPosition = 69 + (positionToAdvance - (37 - currentPosition));
			}
			System.out.println(currentPosition);
		}
		else {
			currentPosition += positionToAdvance;
		}
		if(tempJournyCount == 57) {
			// At HOME
			if (currentPiece.getPieceColor() == Color.YELLOW) {
				currentPosition = 57;
			} 
			else if(currentPiece.getPieceColor() == Color.BLUE) {
				currentPosition = 63;
			}
			else if(currentPiece.getPieceColor() == Color.RED) {
				currentPosition = 69;
			}
			else if(currentPiece.getPieceColor() == Color.GREEN) {
				currentPosition = 75;
			}
		}
		if(tempJournyCount > 57) {
			currentPosition = 100;
		}
		return currentPosition;
	}

	public Board getBoard() {
		return board;
	}

	public boolean isProcessRunning() {
		return isProcessRunning;
	}

	public void setProcessRunning(boolean isProcessRunning) {
		this.isProcessRunning = isProcessRunning;
	}

	public LuduPlayer getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(LuduPlayer currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public ArrayList<LuduPlayer> getLuduPlayers() {
		return luduPlayers;
	}

	public void setLuduPlayers(ArrayList<LuduPlayer> luduPlayers) {
		this.luduPlayers = luduPlayers;
	}

	public Turn getTurn() {
		return turn;
	}

	public void setTurn(Turn turn) {
		this.turn = turn;
	}

	public int getDiceValue() {
		return diceValue;
	}

	public void setDiceValue(int diceValue) {
		this.diceValue = diceValue;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public boolean isRollAgain() {
		return isRollAgain;
	}

	public void setRollAgain(boolean isRollAgain) {
		this.isRollAgain = isRollAgain;
	}
	
}
