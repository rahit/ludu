package com.tahsinrahit.ludu;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.security.SecureRandom;
import java.security.SecureRandomSpi;
import java.util.Random;

import javax.swing.JLabel;
import javax.swing.JPanel;


public class Turn implements MouseListener{
	
	private Player player;
	private Piece currentPiece;
	private boolean isPieceSelected = true;

	public Turn() {
		
	}

	public Turn(Player player) {
		this.player = player;
		this.currentPiece = new Piece();
	}

	public int rollDice() {
		int diceValue = new SecureRandom().nextInt(7);
		while(diceValue == 0) {
			diceValue = new SecureRandom().nextInt(7);
		}
		return diceValue;
	}
	
	
	public Piece selectPiece() {
		this.isPieceSelected = false;
		Piece[] pieces = new Piece[4];
		pieces = this.player.getPieces();
		pieces[0].addMouseListener(this);
		pieces[1].addMouseListener(this);
		pieces[2].addMouseListener(this);
		pieces[3].addMouseListener(this);
		return this.currentPiece;
	}


	public void movePiece() {
		this.currentPiece.setBounds(300, 370, 32, 32);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(!this.isPieceSelected) {
			this.isPieceSelected = true;
			this.currentPiece = (Piece)e.getSource();
			movePiece();
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
