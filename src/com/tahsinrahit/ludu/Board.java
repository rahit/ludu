/**
 * 
 */
package com.tahsinrahit.ludu;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

/**
 * @author Tahsin Hassan Rahit <tahsin.rahit@gmail.com>
 *
 */
public class Board {

    private static final int WIDTH = 700;
    private static final int HEIGHT = 700;
    private static PositionMap[] postionMap = new PositionMap[76];
    private static int[][][] pieceOrigin = new int[4][4][2];							// [color][pieceId][0 or 1] (0 for X, 1 for Y)
    private static LuduCourt luduCourt;
        
    private GameFrame gameFrame;
	private JLayeredPane lpane;
	private JButton doNotEliminate;
	private JButton dice;
	private JLabel status;
	private LuduPlayer[] luduPlayers = new LuduPlayer[4];
	

	public Board() {
		// TODO Auto-generated constructor stub
	}
	
	public Board(GameFrame gameFrame, LuduCourt luduCourt) {
		this.gameFrame = gameFrame;
		this.lpane = new JLayeredPane();
		this.gameFrame.setlPane(this.lpane);
		this.gameFrame.add(lpane, BorderLayout.CENTER);
		this.lpane.setBounds(0, 0, WIDTH, HEIGHT);
		
		this.initCourt(luduCourt);
		this.initDice();
		this.initDoNotEliminateButton();
		this.initStatus();
		this.initPositionMap();
		Board.initpieceOrigin();
		
        this.gameFrame.pack();       

	}

	private void initStatus() {
		this.status = new JLabel();
		this.status.setFont(new Font("Arial", Font.BOLD, 20));
		this.status.setBounds(50, Board.HEIGHT-200, Board.WIDTH-50, 150);
		this.lpane.add(this.status, new Integer(1), 0);
	}

	private void initDoNotEliminateButton() {
		this.doNotEliminate = new JButton("Show Mercy");
		this.doNotEliminate.setBounds(Board.WIDTH-150, 150, 120, 30);
		this.doNotEliminate.setVisible(false);
		this.lpane.add(doNotEliminate, new Integer(1), -1);	
	}

	private void initDice() {
		this.dice = new JButton();
		//this.dice.setFont(new Font("Arial", Font.BOLD, 50));
		this.dice.setBounds(WIDTH-150, 0, 120, 120);
		Image img = null;
		try {
			img = ImageIO.read(getClass().getResource("resources/dice.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.dice.setIcon(new ImageIcon(img));
		this.lpane.add(dice, new Integer(1), 0);
	}


	private void initCourt(LuduCourt luduCourt) {		
		Board.luduCourt = luduCourt;
		JLabel courtLabel = new JLabel(new ImageIcon(this.getClass().getResource("resources/"+Board.luduCourt.getCourtFileName()+".png")));
		courtLabel.setBounds(-90, -90, WIDTH, HEIGHT);
		this.lpane.add(courtLabel, new Integer(0), 0);
	}

	private void initPositionMap() {
		int x = 211, y = 447;
		for (int i = 0; i < 52; i++) {
			Board.postionMap[i] = new PositionMap(i, x, y);
			// Setting X coordinates of the blocks
			if( (i >= 12 && i <= 17) || i == 23 || i == 24 || (i >= 30 && i < 36) ) {
				x += 34;
			}
			else if((i >= 4 && i < 10) || i == 49 || i == 50 || (i >= 38 && i <= 43)) {
				x -= 34;
			}
			// Setting Y coordinates of the blocks
			if(i <= 4 || i == 10 || i == 11 || (i >= 17 && i < 23)) {
				y -= 34;
			}
			else if((i >= 25 && i <= 30) || i == 36 || i == 37 || i == 51 || (i >= 43 && i < 49)){
				y += 34;
			}	
		}
		// setting Home lane
		x = 211; y = 447;
		for (int i = 52; i < Board.postionMap.length; i++) {
			if(i >= 52 && i < 58 ) {
				y = (i == 52) ? 447 : y - 34;
				x = 211 + (34*1);
			}	
			else if(i >= 58 && i < 64) {
				y = 447 - (34*6);
				x = (i == 58) ? (211-(34*5)) : x + 34;
			}
			else if(i >= 64 && i < 70){
				y = (i == 64) ? (447-(34*12)) : y + 34;
				x = 211 + (34*1);
			}
			else if(i >= 70) {
				y = 447 - (34*6);
				x = (i == 70) ? (211+(34*7)) : x - 34;
			}
			Board.postionMap[i] = new PositionMap(i, x, y);
		}
	}
	
	
	public static void initpieceOrigin() {
		int offsetX = 0, offsetY = 0, tempX = 65, tempY = 425, tempDist = 55;
		// Color Yellow
		pieceOrigin[0][0][0] = offsetX + tempX;
		pieceOrigin[0][0][1] = offsetY + tempY;
		pieceOrigin[0][1][0] = offsetX + tempX;
		pieceOrigin[0][1][1] = offsetY + tempY - tempDist;
		pieceOrigin[0][2][0] = offsetX + tempX + tempDist;
		pieceOrigin[0][2][1] = offsetY + tempY - tempDist;
		pieceOrigin[0][3][0] = offsetX + tempX + tempDist;
		pieceOrigin[0][3][1] = offsetY + tempY;
		// Color Blue
		offsetY = -310;
		pieceOrigin[1][0][0] = offsetX + tempX;
		pieceOrigin[1][0][1] = offsetY + tempY;
		pieceOrigin[1][1][0] = offsetX + tempX;
		pieceOrigin[1][1][1] = offsetY + tempY - tempDist;
		pieceOrigin[1][2][0] = offsetX + tempX + tempDist;
		pieceOrigin[1][2][1] = offsetY + tempY - tempDist;
		pieceOrigin[1][3][0] = offsetX + tempX + tempDist;
		pieceOrigin[1][3][1] = offsetY + tempY;
		// Color Red
		offsetX = 310;
		pieceOrigin[2][0][0] = offsetX + tempX;
		pieceOrigin[2][0][1] = offsetY + tempY;
		pieceOrigin[2][1][0] = offsetX + tempX;
		pieceOrigin[2][1][1] = offsetY + tempY - tempDist;
		pieceOrigin[2][2][0] = offsetX + tempX + tempDist;
		pieceOrigin[2][2][1] = offsetY + tempY - tempDist;
		pieceOrigin[2][3][0] = offsetX + tempX + tempDist;
		pieceOrigin[2][3][1] = offsetY + tempY;
		// Color Green
		offsetY = 0;
		offsetX = 310;
		pieceOrigin[3][0][0] = offsetX + tempX;
		pieceOrigin[3][0][1] = offsetY + tempY;
		pieceOrigin[3][1][0] = offsetX + tempX;
		pieceOrigin[3][1][1] = offsetY + tempY - tempDist;
		pieceOrigin[3][2][0] = offsetX + tempX + tempDist;
		pieceOrigin[3][2][1] = offsetY + tempY - tempDist;
		pieceOrigin[3][3][0] = offsetX + tempX + tempDist;
		pieceOrigin[3][3][1] = offsetY + tempY;
	}


	public void setPlayersPiece() {
		for (int i = 0; i < this.luduPlayers.length; i++) {
			if(this.luduPlayers[i] != null) {
				Piece[] pieces = new Piece[4];
				pieces = this.luduPlayers[i].getPieces();
		        this.lpane.add(pieces[0], new Integer(2), -1);
		        this.lpane.add(pieces[1], new Integer(2), -1);
		        this.lpane.add(pieces[2], new Integer(2), -1);
		        this.lpane.add(pieces[3], new Integer(2), -1);
			}
		}
	}
	
	public void setDice(String text) {
		Image img = null;
		try {
			img = ImageIO.read(getClass().getResource("resources/dice/"+text+".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.dice.setIcon(new ImageIcon(img));
	}

	public static PositionMap[] getPostionMap() {
		return postionMap;
	}

	public static PositionMap getPostionMapByIndex(int i) {
		return postionMap[i];
	}


	public static int[][][] getPieceOrigin() {
		return pieceOrigin;
	}


	public LuduPlayer[] getPlayers() {
		return luduPlayers;
	}


	public void setPlayers(LuduPlayer[] players) {
		this.luduPlayers = players;
		this.setPlayersPiece();
	}


	public void setPlayers(ArrayList<LuduPlayer> luduPlayers) {
		int i = 0;
		for (Iterator<LuduPlayer> iterator = luduPlayers.iterator(); iterator.hasNext();) {
			LuduPlayer luduPlayer = (LuduPlayer) iterator.next();
			this.luduPlayers[i] = luduPlayer;
			i++;
		}		
		this.setPlayersPiece();
	}


	public JLayeredPane getLpane() {
		return lpane;
	}


	public static int getWidth() {
		return Board.WIDTH;
	}


	public static int getHeight() {
		return Board.HEIGHT;
	}


	public LuduPlayer[] getLuduPlayers() {
		return luduPlayers;
	}

	
	public GameFrame getGameFrame() {
		return gameFrame;
	}

	public JButton getDoNotEliminate() {
		return doNotEliminate;
	}

	public JButton getDice() {
		return dice;
	}

	public void setDice(JButton dice) {
		this.dice = dice;
	}

	public JLabel getStatus() {
		return status;
	}
	

}
