package com.tahsinrahit.ludu;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GamePlay{

	private static boolean isGameFinished = false;
	private Player currentPlayer;
	private Board board = new Board();
	private Player[] players = new Player[4];
	private Turn turn;
	
	
	public GamePlay() {
		super();
		this.isGameFinished = false;
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

	public void play() {
		while (!isGameFinished) {
			board.getRollButton().addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					this.turn = new Turn(this.players[0]);
					int diceValue = turn.rollDice();
					board.setDice(Integer.toString(diceValue));
					Piece piece = turn.selectPiece();
				}
			});
		}
	}

}
