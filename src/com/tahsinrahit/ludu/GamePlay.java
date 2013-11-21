package com.tahsinrahit.ludu;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GamePlay{

	private static boolean isGameFinished = false;
	private boolean isProcessRunning = false;
	private Player currentPlayer;
	private Board board = new Board();
	private Player[] players = new Player[4];
	private Turn turn;
	private int diceValue = 0;
	
	public GamePlay() {
		super();
		this.isGameFinished = false;
		this.isProcessRunning = false;
		this.board = new Board();
		board.initpieceOrigin();        
        this.players[0] = new Player(Color.YELLOW);
        this.players[1] = new Player(Color.BLUE);
        this.players[2] = new Player(Color.RED);
        this.players[3] = new Player(Color.GREEN);	
        board.setPlayers(players);
		this.currentPlayer = this.players[0];
		board.showFrame();
	}

	public void run() {
		int i = 0;
		while (!isGameFinished) {
			this.turn = new Turn(GamePlay.this.players[i]);
			board.getRollButton().addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					if(!GamePlay.this.isProcessRunning) {
						GamePlay.this.isProcessRunning = true;
						GamePlay.this.diceValue = GamePlay.this.turn.rollDice();
						GamePlay.this.board.setDice(Integer.toString(GamePlay.this.diceValue));
						synchronized (GamePlay.this) {
							GamePlay.this.notify();
						}
					}
				}
			});
			synchronized (this) {
				//System.out.println("waiting for ROLL");
				try {
					wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//System.out.println("resumed ROLL");
			}	
			this.board.getRollButton().addActionListener(null);
			Piece currentPiece = this.turn.selectPiece();
			this.turn.movePiece(this.calculateNextPosition(currentPiece));
			if(this.diceValue != 6) {
				i++;
			}
			if(i > 3) {
				i = 0;
			}
			GamePlay.this.isProcessRunning = false;
		}

	}

	private int calculateNextPosition(Piece currentPiece) {
		int currentPosition = currentPiece.getPositionIndex();
		int tempJournyCount = currentPiece.getJournyCount()+this.diceValue;
		
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
			currentPosition += this.diceValue;
		}
		else if (currentPosition + this.diceValue >= 52 && tempJournyCount < 50) {
			// Passing array bound for main line
			currentPosition = this.diceValue - (52 - currentPosition);
		}
		else if(currentPosition < 52 && tempJournyCount > 50) {
			// At Home Line
			if (currentPiece.getPieceColor() == Color.YELLOW) {
				currentPosition = 52 + (this.diceValue - (50 - currentPosition)) - 2;
			} 
			else if(currentPiece.getPieceColor() == Color.BLUE) {
				currentPosition = 58 + (this.diceValue - (11 - currentPosition)) - 2;
			}
			else if(currentPiece.getPieceColor() == Color.RED) {
				currentPosition = 64 + (this.diceValue - (24 - currentPosition)) - 2;
			}
			else if(currentPiece.getPieceColor() == Color.GREEN) {
				currentPosition = 70 + (this.diceValue - (37 - currentPosition)) - 2;
			}
		}
		else {
			currentPosition += this.diceValue;
		}
		if(tempJournyCount > 55) {
			currentPosition = 57;
		}
		currentPiece.setJournyCount(tempJournyCount);
		return currentPosition;
	}
	
}
