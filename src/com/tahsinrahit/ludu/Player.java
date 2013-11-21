package com.tahsinrahit.ludu;

import java.awt.Color;

public class Player {
	
	private Color playerColor;
	private int playerId;
	private Piece[] pieces = new Piece[4];

	public Player() {
		// TODO Auto-generated constructor stub
	}
	
	public Player(Color color) {
		Board.initpieceOrigin();
		if(color == Color.YELLOW) {
			this.playerId = 0;
			this.initiateYellow();
		}
		else if (color == Color.GREEN) {
			this.playerId = 1;
			this.initiateGreen();
		}
		else if (color == Color.RED) {
			this.playerId = 2;
			this.initiateRed();
		}
		else {
			this.playerId = 3;
			this.initiateBlue();
		}
	}


	public void initiateYellow() {
		int[][][] originPosition = Board.getPieceOrigin();
		for (int i = 0; i < 4; i++) {
			pieces[i] = new Piece(i, Color.YELLOW);
			pieces[i].setOriginPosition(originPosition[0][i][0], originPosition[0][i][1]);
			pieces[i].setBounds(originPosition[0][i][0], originPosition[0][i][1], 30, 30);
			pieces[i].setOpaque(true);
		}
	}
	
	private void initiateBlue() {
		int[][][] originPosition = Board.getPieceOrigin();
		for (int i = 0; i < 4; i++) {
			pieces[i] = new Piece(i, Color.BLUE);
			pieces[i].setOriginPosition(originPosition[1][i][0], originPosition[1][i][1]);
			pieces[i].setBounds(originPosition[1][i][0], originPosition[1][i][1], 30, 30);
			pieces[i].setOpaque(true);
		}
		
	}

	private void initiateRed() {
		int[][][] originPosition = Board.getPieceOrigin();
		for (int i = 0; i < 4; i++) {
			pieces[i] = new Piece(i, Color.RED);
			pieces[i].setOriginPosition(originPosition[2][i][0], originPosition[2][i][1]);
			pieces[i].setBounds(originPosition[2][i][0], originPosition[2][i][1], 30, 30);
			pieces[i].setOpaque(true);
		}		
	}

	private void initiateGreen() {
		int[][][] originPosition = Board.getPieceOrigin();
		for (int i = 0; i < 4; i++) {
			pieces[i] = new Piece(i, Color.GREEN);
			pieces[i].setOriginPosition(originPosition[3][i][0], originPosition[3][i][1]);
			pieces[i].setBounds(originPosition[3][i][0], originPosition[3][i][1], 30, 30);
			pieces[i].setOpaque(true);
		}		
		
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

}
