package com.tahsinrahit.ludu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.security.SecureRandom;


public class Turn {
	private GamePlay gamePlay;
	private LuduPlayer luduPlayer;
	private Piece currentPiece;
	private boolean isPieceSelected = true;
	private int diceValue = 0;
	private Piece pieceToEliminate;
	
	private MouseListener selectionListener;
	private MouseListener selectionOptionListener;
	private MouseListener eleminationListener;
	
	public Turn() {
		
	}

	public Turn(GamePlay gamePlay) {
		this.gamePlay = gamePlay;
		this.luduPlayer = this.gamePlay.getCurrentPlayer();
		this.currentPiece = null;
	}

	public int rollDice() {
		this.diceValue = new SecureRandom().nextInt(7);
		while(diceValue == 0) {
			this.diceValue = new SecureRandom().nextInt(7);
		}
		if(this.diceValue == 6) {
			this.gamePlay.setRollAgain(true);
		}
		return this.diceValue;
	}
	
	
	public Piece selectPiece() {
		this.isPieceSelected = false;
		this.selectionListener = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				Piece piece = (Piece)e.getSource();		
				if(isAvailableToSelect(piece)) {
					Turn.this.pickPiece(piece);
				}
			}
		};
		Piece[] pieces = new Piece[4];
		pieces = this.luduPlayer.getPieces();
		for (int i = 0; i < pieces.length; i++) {
			pieces[i].addMouseListener(this.selectionListener);
		}
		this.initOptionSelectionListener();
		synchronized (this) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return this.currentPiece;
	}


	protected void pickPiece(Piece piece) {
		if(!this.isPieceSelected) {
			this.isPieceSelected = true;
			this.currentPiece = piece;
			Piece[] pieces = new Piece[4];
			pieces = Turn.this.luduPlayer.getPieces();	
			for (int i = 0; i < pieces.length; i++) {
				pieces[i].removeMouseListener(this.selectionListener);
			}
			this.removeOptionSelectionListener();
			synchronized (this) {
				this.notify();
			}
		}
	}

	protected void removeOptionSelectionListener() {
		LuduPlayer[] luduPlayers = this.gamePlay.getBoard().getLuduPlayers(); 
		for (int i = 0; i < 4; i++) {
			if(luduPlayers[i] != null && luduPlayers[i] != this.luduPlayer) {
				Piece[] tempPieces = luduPlayers[i].getPieces();
				for (int j = 0; j < tempPieces.length; j++) {
					if(tempPieces[j].getPositionIndex() >= 0 && tempPieces[j].getPositionIndex() < 76) {
						tempPieces[j].removeMouseListener(this.selectionOptionListener);
					}
				}
			}
		}
	}

	private void initOptionSelectionListener() {
		this.selectionOptionListener = new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				Piece piece = (Piece)e.getSource();		
				PositionMap map = Board.getPostionMapByIndex(piece.getPositionIndex());
				Piece[] pieces = map.getPiecesInThisPosition();
				int i = 0, x = Board.getPostionMapByIndex(piece.getPositionIndex()).getX(), y = Board.getPostionMapByIndex(piece.getPositionIndex()).getY();
				while(pieces[i] != null) {
					pieces[i].setPiecePosition(x, y);
					if(!Turn.this.isPieceSelected && pieces[i].getPieceColor() == Turn.this.luduPlayer.getPlayerColor()) {
						Turn.this.pickPiece(pieces[i]);
					}
					i++;
				}
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				super.mouseEntered(e);
				Piece piece = (Piece)e.getSource();		
				PositionMap map = Board.getPostionMapByIndex(piece.getPositionIndex());
				Piece[] pieces = map.getPiecesInThisPosition();
				int i = 0, x = Board.getPostionMapByIndex(piece.getPositionIndex()).getX(), y = Board.getPostionMapByIndex(piece.getPositionIndex()).getY();
				while(pieces[i] != null) {
					if(pieces[i] != piece) {
						y += 31;
						pieces[i].setPiecePosition(x, y);
						Turn.this.gamePlay.getBoard().getStatus().setText("Click to select your piece in this position");
					}
					i++;
				}
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				super.mouseExited(e);
				Piece piece = (Piece)e.getSource();
				if(piece.getPositionIndex() >= 0 && piece.getPositionIndex() < 76) {
					PositionMap map = Board.getPostionMapByIndex(piece.getPositionIndex());
					Piece[] pieces = map.getPiecesInThisPosition();
					int i = 0, x = Board.getPostionMapByIndex(piece.getPositionIndex()).getX(), y = Board.getPostionMapByIndex(piece.getPositionIndex()).getY();
					while(pieces[i] != null) {
						pieces[i].setPiecePosition(x, y);
						i++;
					}
				}
				Turn.this.gamePlay.getBoard().getStatus().setText(Turn.this.luduPlayer.getPlayerName() + "'s turn");
			}
		};
		
		LuduPlayer[] luduPlayers = this.gamePlay.getBoard().getLuduPlayers(); 
		for (int i = 0; i < 4; i++) {
			if(luduPlayers[i] != null && luduPlayers[i] != this.luduPlayer) {
				Piece[] tempPieces = luduPlayers[i].getPieces();
				for (int j = 0; j < tempPieces.length; j++) {
					if(tempPieces[j].getPositionIndex() >= 0 && tempPieces[j].getPositionIndex() < 76) {
						tempPieces[j].addMouseListener(this.selectionOptionListener);
					}
				}
			}
		}
	}

	public void movePiece(int destinationIndex) {
		int currentIndex = this.currentPiece.getPositionIndex();
		if(currentIndex != -1) {
			PositionMap map = Board.getPostionMapByIndex(currentIndex);
			map.removePieceFromPosition(this.currentPiece);
		}
		PositionMap map = Board.getPostionMapByIndex(destinationIndex);
		map.addPieceToPosition(this.currentPiece);
		int x = map.getX(), y = map.getY();
		this.currentPiece.setPositionIndex(destinationIndex);
		this.currentPiece.setPiecePosition(x, y);
	}


	public void eliminate(final int destinationIndex) {
		this.gamePlay.getBoard().getStatus().setText("You've got a chance to eliminate 1 piece of your opponent");
		final PositionMap map = Board.getPostionMapByIndex(destinationIndex);
		Piece[] pieces = map.getOpponentPiecesInThisPosition(currentPiece.getPieceColor());
		this.eleminationListener = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				Piece piece = (Piece)e.getSource();		
				Turn.this.pieceToEliminate = piece;
				Turn.this.removeEliminateListener(destinationIndex);
			}
		};
		int i = 0, y = 200;
		while(pieces[i] != null) {
			y += 50;
			pieces[i].addMouseListener(this.eleminationListener);
			pieces[i].setPiecePosition(Board.getWidth()-50, y);
			i++;
		}		
		this.gamePlay.getBoard().getDoNotEliminate().setVisible(true);
		this.gamePlay.getBoard().getDoNotEliminate().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Turn.this.pieceToEliminate = null;
				Turn.this.removeEliminateListener(destinationIndex);
			}
		});
		synchronized (this) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(this.pieceToEliminate != null) {
			map.removePieceFromPosition(this.pieceToEliminate);
			this.pieceToEliminate.setPiecePosition(this.pieceToEliminate.getOriginX(), this.pieceToEliminate.getOriginY());
			this.pieceToEliminate.setPositionIndex(-1);
			this.pieceToEliminate.setJournyCount(0);
			this.gamePlay.setRollAgain(true);
		}
		this.gamePlay.getBoard().getDoNotEliminate().setVisible(false);
		this.gamePlay.getBoard().getStatus().setText(this.luduPlayer.getPlayerName() + "'s turn");
	}



	protected void removeEliminateListener(final int destinationIndex) {
		final PositionMap map = Board.getPostionMapByIndex(destinationIndex);
		Piece[] pieces = map.getOpponentPiecesInThisPosition(currentPiece.getPieceColor());
		int i = 0;
		while(pieces[i] != null) {
			pieces[i].setPiecePosition(map.getX(), map.getY());
			pieces[i].removeMouseListener(this.eleminationListener);
			i++;
		}
		synchronized (this) {
			this.notify();
		}
	}

	private boolean isAvailableToSelect(Piece piece) {
		int currentPosition = piece.getPositionIndex();
		if( 
			(currentPosition != -1 && currentPosition < 76) ||
			(currentPosition == -1 && this.diceValue == 6) ) {
			return true;
		}
		return false;
	}

	
	public boolean isAvailableToSelect(Piece piece, int destinationIndex) {
		int currentPosition = piece.getPositionIndex();
		if( 
			(currentPosition != -1 && currentPosition < 76 && (destinationIndex >= 0 && destinationIndex < 76) ) ||
			(currentPosition == -1 && this.diceValue == 6) ) {
			return true;
		}
		return false;
	}
	
}
