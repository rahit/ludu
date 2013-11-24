package com.tahsinrahit.ludu;

import java.awt.Color;

public class PositionMap {
	private int id;
	private int x;
	private int y;
	private Piece[] piecesInThisPosition = new Piece[16]; 
	
	public PositionMap() {
		// TODO Auto-generated constructor stub
	}

	public PositionMap(int id, int x, int y) {
		super();
		this.id = id;
		this.x = x;
		this.y = y;
	}
	
	public int countPieceInthisPosition() {
		int i = 0;
		while (this.piecesInThisPosition[i] != null) {
			i++;			
		}
		return i;
	}
	
	public void addPieceToPosition(Piece piece) {
		int i = 0;
		while (this.piecesInThisPosition[i] != null) {
			i++;			
		}
		this.piecesInThisPosition[i] = piece;
	}

	
	public void removePieceFromPosition(Piece piece) {
		int i = 0, position = 0;
		while (this.piecesInThisPosition[i] != null) {
			if(this.piecesInThisPosition[i] == piece) {
				position = i;
			}
			i++;			
		}
		this.piecesInThisPosition[position].setBounds(
				this.piecesInThisPosition[position].getOriginX(), 
				this.piecesInThisPosition[position].getOriginY(), 
				30, 30);
		this.piecesInThisPosition[position].setPositionIndex(-1);
		this.piecesInThisPosition[position].setJournyCount(0);
		this.piecesInThisPosition[position] = this.piecesInThisPosition[i-1] ;
		this.piecesInThisPosition[i-1] = null ;
	}
	
	public void removeOpponentPieceFromPosition(Color color) {
		int i = 0, position = 0;
		while (this.piecesInThisPosition[i] != null) {
			if(this.piecesInThisPosition[i].getPieceColor() != color) {
				position = i;
			}
			i++;			
		}
		this.piecesInThisPosition[position].setBounds(
				this.piecesInThisPosition[position].getOriginX(), 
				this.piecesInThisPosition[position].getOriginY(), 
				30, 30);
		this.piecesInThisPosition[position].setPositionIndex(-1);
		this.piecesInThisPosition[position].setJournyCount(0);
		this.piecesInThisPosition[position] = this.piecesInThisPosition[i-1] ;
		this.piecesInThisPosition[i-1] = null ;
	}

	public Piece[] getOpponentPiecesInThisPosition(Color color) {
		Piece[] pieces = new Piece[12];
		int i = 0, j = 0;
		while (this.piecesInThisPosition[i] != null) {
			if(this.piecesInThisPosition[i].getPieceColor() != color) {
				pieces[j++] = this.piecesInThisPosition[i];
			}
			i++;			
		}
		return pieces;
	}
	
	public int getId() {
		return id;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Piece[] getPiecesInThisPosition() {
		return piecesInThisPosition;
	}

	public void setPiecesInThisPosition(Piece[] piecesInThisPosition) {
		this.piecesInThisPosition = piecesInThisPosition;
	}
	
	
	

}
