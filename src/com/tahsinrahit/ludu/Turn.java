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
	
	private Player player;
	private Piece currentPiece;
	private boolean isPieceSelected = true;
	private int diceValue = 0;

	public Turn() {
		
	}

	public Turn(Player player) {
		this.player = player;
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
		pieces = this.player.getPieces();
		pieces[0].addMouseListener(this);
		pieces[1].addMouseListener(this);
		pieces[2].addMouseListener(this);
		pieces[3].addMouseListener(this);

		synchronized (this) {
			//System.out.println("waiting for TURN");
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//System.out.println("resumed TURN");
		}
		return this.currentPiece;
	}


	public void movePiece(int destinationIndex) {
		int temp = this.currentPiece.getPositionIndex();
		if(temp != -1) {
			PositionMap map = Board.getPostionMapByIndex(temp);
			map.removePieceFromPosition(this.currentPiece);
		}
		PositionMap map = Board.getPostionMapByIndex(destinationIndex);
		if(map.countPieceInthisPosition() > 0) {
			map.removeOpponentPieceFromPosition(this.currentPiece.getPieceColor());
		}
		map.addPieceToPosition(this.currentPiece);
		int x = map.getX(), y = map.getY();
		this.currentPiece.setPositionIndex(destinationIndex);
		this.currentPiece.setBounds(x, y, 32, 32);
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
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
}
