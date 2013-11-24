package com.tahsinrahit.ludu;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

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
	private LuduPlayer currentPlayer;
	private Board board = new Board();
	private LuduPlayer[] luduPlayers = new LuduPlayer[4];
	private Turn turn;
	private int diceValue = 0;
	
	public GamePlay() {
		super();
		this.isGameFinished = false;
		this.isProcessRunning = false;
		this.board = new Board();
		//new Thread(new GameMusic("mystic_river.mp3")).start();
		board.initpieceOrigin();        
        this.luduPlayers[0] = new LuduPlayer(Color.YELLOW);
        this.luduPlayers[1] = new LuduPlayer(Color.BLUE);
        this.luduPlayers[2] = new LuduPlayer(Color.RED);
        this.luduPlayers[3] = new LuduPlayer(Color.GREEN);	
        board.setPlayers(luduPlayers);
		this.currentPlayer = this.luduPlayers[0];
		board.showFrame();
	}

	public void run() {
		int i = 0;
		while (!isGameFinished) {
			this.turn = new Turn(GamePlay.this.luduPlayers[i]);
			board.getRollButton().addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(!GamePlay.this.isProcessRunning) {
						GamePlay.this.roll();
					}
				}
			});
			synchronized (this) {
				//System.out.println("waiting for ROLL");
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//System.out.println("resumed ROLL");
			}	
			this.board.getRollButton().addActionListener(null);
			this.move();
			if(this.diceValue != 6) {
				i++;
			}
			if(i > 3) {
				i = 0;
			}
			GamePlay.this.isProcessRunning = false;
		}

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
		Piece currentPiece = this.turn.selectPiece();
		int destinationIndex = this.calculateNextPosition(currentPiece, this.diceValue);
		if(this.isEliminationAvailable(currentPiece, destinationIndex)) {
			this.turn.eliminate(destinationIndex);
		}
		int tempDiceValue = this.diceValue;
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

	private boolean isEliminationAvailable(Piece currentPiece, int destinationIndex) {
		PositionMap map = Board.getPostionMapByIndex(destinationIndex);
		Piece[] pieces = map.getOpponentPiecesInThisPosition(currentPiece.getPieceColor());
		if(pieces[0] != null) {
			return true;
		}
		return false;
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
		System.out.println(currentPosition);
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
			currentPosition += positionToAdvance;
		}
		else if (currentPosition + positionToAdvance >= 52 && tempJournyCount < 50) {
			// Passing array bound for main line
			currentPosition = positionToAdvance - (52 - currentPosition);
		}
		else if(currentPosition < 52 && tempJournyCount > 50) {
			// At Home Line
			if (currentPiece.getPieceColor() == Color.YELLOW) {
				currentPosition = 52 + (positionToAdvance - (50 - currentPosition)) - 2;
			} 
			else if(currentPiece.getPieceColor() == Color.BLUE) {
				currentPosition = 58 + (positionToAdvance - (11 - currentPosition)) - 2;
			}
			else if(currentPiece.getPieceColor() == Color.RED) {
				currentPosition = 64 + (positionToAdvance - (24 - currentPosition)) - 2;
			}
			else if(currentPiece.getPieceColor() == Color.GREEN) {
				currentPosition = 70 + (positionToAdvance - (37 - currentPosition)) - 2;
			}
		}
		else {
			currentPosition += positionToAdvance;
		}
		if(tempJournyCount > 55) {
			currentPosition = 57;
		}
		return currentPosition;
	}
	
}
