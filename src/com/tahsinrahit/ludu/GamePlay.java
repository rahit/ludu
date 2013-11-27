package com.tahsinrahit.ludu;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Vector;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import javazoom.jl.player.Player;

public class GamePlay{
	private final String diceRollSoundFileName = "dice.mp3";
	
	private static boolean isGameFinished = false;
	private boolean isProcessRunning = false;
	private boolean isRollAgain;
	private LuduPlayer currentPlayer;
	private Board board = new Board();
	private ArrayList<LuduPlayer>luduPlayers = new ArrayList<LuduPlayer>();
	private Turn turn;
	private int diceValue = 0;
	
	public GamePlay() {
		super();
		GamePlay.isGameFinished = false;
		this.isProcessRunning = false;
		this.board = new Board();
		//new Thread(new GameMusic("mystic_river.mp3")).start();
		Board.initpieceOrigin();        
		this.luduPlayers.add(new LuduPlayer(Color.YELLOW));
		this.luduPlayers.add(new LuduPlayer(Color.BLUE));
		this.luduPlayers.add(new LuduPlayer(Color.RED));
		this.luduPlayers.add(new LuduPlayer(Color.GREEN));
		this.board.setPlayers(this.luduPlayers);
		this.currentPlayer = this.luduPlayers.get(0);
        board.showFrame();
	}

	public void run() {
		int i = 0;
		while (!isGameFinished) {
			this.isRollAgain = false;
			this.currentPlayer = this.luduPlayers.get(i);
			this.turn = new Turn(this);
			this.board.getRollButton().addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(!GamePlay.this.isProcessRunning) {
						GamePlay.this.roll();
					}
					GamePlay.this.board.getRollButton().removeActionListener(this);
				}
			});
			synchronized (this) {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}	
			this.board.getRollButton().addActionListener(null);
			if(this.currentPlayer.isMoveAvailable(this.diceValue)) {
				this.move();
			}
			if(!this.isRollAgain) {
				i++;
				if(i >= this.luduPlayers.size()) {
					i = 0;
				}
			}
			GamePlay.this.isProcessRunning = false;
		}

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
			//currentPosition += positionToAdvance;
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
