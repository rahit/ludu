package com.tahsinrahit.ludu;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.security.SecureRandom;
import java.security.SecureRandomSpi;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class Turn implements MouseListener{
	
	private LuduPlayer luduPlayer;
	private Piece currentPiece;
	private boolean isPieceSelected = true;
	private int diceValue = 0;
	private Piece pieceToEliminate;

	public Turn() {
		
	}

	public Turn(LuduPlayer luduPlayer) {
		this.luduPlayer = luduPlayer;
		this.currentPiece = null;
	}

	public int rollDice() {
		this.diceValue = new SecureRandom().nextInt(7);
		while(diceValue == 0) {
			this.diceValue = new SecureRandom().nextInt(7);
		}
		return this.diceValue;
	}
	
	
	public Piece selectPiece() {
		this.isPieceSelected = false;
		Piece[] pieces = new Piece[4];
		pieces = this.luduPlayer.getPieces();
		pieces[0].addMouseListener(this);
		pieces[1].addMouseListener(this);
		pieces[2].addMouseListener(this);
		pieces[3].addMouseListener(this);

		synchronized (this) {
			System.out.println("waiting for TURN");
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("resumed TURN");
		}
		return this.currentPiece;
	}


	public void movePiece(int destinationIndex) {
		int currentIndex = this.currentPiece.getPositionIndex();
		if(currentIndex != -1) {
			PositionMap map = Board.getPostionMapByIndex(currentIndex);
			map.removePieceFromPosition(this.currentPiece);
		}
		PositionMap map = Board.getPostionMapByIndex(destinationIndex);
		if(map.countPieceInthisPosition() > 0) {
			//map.removeOpponentPieceFromPosition(this.currentPiece.getPieceColor());
		}
		map.addPieceToPosition(this.currentPiece);
		int x = map.getX(), y = map.getY();
		this.currentPiece.setPositionIndex(destinationIndex);
		this.currentPiece.setBounds(x, y, 32, 32);
	}


	public void eliminate(int destinationIndex) {
		PositionMap map = Board.getPostionMapByIndex(destinationIndex);
		Piece[] pieces = map.getOpponentPiecesInThisPosition(currentPiece.getPieceColor());
		int i = 0, y = 200;
		while(pieces[i] != null) {
			y += 50;
			pieces[i].addMouseListener(this);
			pieces[i].setBounds(Board.getWidth()-50, y, 30, 30);
			i++;
		}
		// TODO create condition to avoid elimination
		synchronized (this) {
			System.out.println("waiting to ELIMINATE");
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("resumed ELIMINATE");
		}
		//this.pieceToEliminate.setBounds(this.pieceToEliminate.getOriginX(), this.pieceToEliminate.getOriginY(), 30, 30);
		//this.pieceToEliminate.setPositionIndex(-1);
		//this.pieceToEliminate.setJournyCount(0);
		map.removePieceFromPosition(this.pieceToEliminate);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if(!this.isPieceSelected) {
			this.isPieceSelected = true;
			this.currentPiece = (Piece)e.getSource();
			synchronized (Turn.this) {
				Turn.this.notify();
			}
		}
		else {
			this.pieceToEliminate = (Piece)e.getSource();
			synchronized (Turn.this) {
				Turn.this.notify();
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		Piece piece = (Piece)e.getSource();
		if(piece.getPositionIndex() > 0 && piece.getPositionIndex() < 78) {
			PositionMap map = Board.getPostionMapByIndex(piece.getPositionIndex());
			Piece[] pieces = map.getPiecesInThisPosition();
			int i = 0, x = piece.getX(), y = piece.getY();
			while(pieces[i] != null) {
				if(pieces[i] != piece) {
					y += 33;
					pieces[i].setBounds(x, y, 30, 30);
				}
				i++;
			}
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {		
		Piece piece = (Piece)e.getSource();
		if(piece.getPositionIndex() > 0 && piece.getPositionIndex() < 78) {
			PositionMap map = Board.getPostionMapByIndex(piece.getPositionIndex());
			Piece[] pieces = map.getPiecesInThisPosition();
			int i = 0, x = piece.getX(), y = piece.getY();
			while(pieces[i] != null) {
				if(!pieces[i].equals(piece)) {
					pieces[i].setBounds(x, y, 30, 30);
				}
				i++;
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {		
	}

	@Override
	public void mouseReleased(MouseEvent e) {		
	}
	
	
}
