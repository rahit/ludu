package com.tahsinrahit.ludu;

import java.awt.Color;

public class LuduPlayer {
	
	private Color playerColor;
	private int playerId;
	private Piece[] pieces = new Piece[4];

	public LuduPlayer() {
		// TODO Auto-generated constructor stub
	}
	
	public LuduPlayer(Color color) {
		Board.initpieceOrigin();
		if(color == Color.YELLOW) {
			this.playerId = 0;
			this.initiateYellow();
		}
		else if (color == Color.BLUE) {
			this.playerId = 1;
			this.initiateBlue();
		}
		else if (color == Color.RED) {
			this.playerId = 2;
			this.initiateRed();
		}
		else {
			this.playerId = 3;
			this.initiateGreen();
		}
		this.playerColor = color;
	}


	public void initiateYellow() {
		int[][][] originPosition = Board.getPieceOrigin();
		for (int i = 0; i < 4; i++) {
			pieces[i] = new Piece(i, Color.YELLOW);
			pieces[i].setOriginPosition(originPosition[0][i][0], originPosition[0][i][1]);
			pieces[i].setBounds(originPosition[0][i][0], originPosition[0][i][1], 30, 30);
			pieces[i].setPiecePosition(originPosition[0][i][0], originPosition[0][i][1]);
			pieces[i].setOpaque(true);
		}
	}
	
	private void initiateBlue() {
		int[][][] originPosition = Board.getPieceOrigin();
		for (int i = 0; i < 4; i++) {
			pieces[i] = new Piece(i, Color.BLUE);
			pieces[i].setOriginPosition(originPosition[1][i][0], originPosition[1][i][1]);
			pieces[i].setBounds(originPosition[1][i][0], originPosition[1][i][1], 30, 30);
			pieces[i].setPiecePosition(originPosition[1][i][0], originPosition[1][i][1]);
			pieces[i].setOpaque(true);
		}
		
	}

	private void initiateRed() {
		int[][][] originPosition = Board.getPieceOrigin();
		for (int i = 0; i < 4; i++) {
			pieces[i] = new Piece(i, Color.RED);
			pieces[i].setOriginPosition(originPosition[2][i][0], originPosition[2][i][1]);
			pieces[i].setBounds(originPosition[2][i][0], originPosition[2][i][1], 30, 30);
			pieces[i].setPiecePosition(originPosition[2][i][0], originPosition[2][i][1]);
			pieces[i].setOpaque(true);
		}		
	}

	private void initiateGreen() {
		int[][][] originPosition = Board.getPieceOrigin();
		for (int i = 0; i < 4; i++) {
			pieces[i] = new Piece(i, Color.GREEN);
			pieces[i].setOriginPosition(originPosition[3][i][0], originPosition[3][i][1]);
			pieces[i].setBounds(originPosition[3][i][0], originPosition[3][i][1], 30, 30);
			pieces[i].setPiecePosition(originPosition[3][i][0], originPosition[3][i][1]);
			pieces[i].setOpaque(true);
		}		
		
	}
	
	public boolean isMoveAvailable(int diceValue) {
		for (int i = 0; i < this.pieces.length; i++) {
			int currentPosition = this.pieces[i].getPositionIndex();
			if( 
					(currentPosition != -1 && currentPosition < 80) ||
					(currentPosition == -1 && diceValue == 6) ) {
					return true;
				}
		}
		return false;		
	}

	public Piece[] getPieces() {
		return pieces;
	}

	public Piece getPiece(int index) {
		return pieces[index];
	}

	public int getPlayerId() {
		return playerId;
	}

	public Color getPlayerColor() {
		return playerColor;
	}
	
	

}
