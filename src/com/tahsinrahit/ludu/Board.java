/**
 * 
 */
package com.tahsinrahit.ludu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JTextPane;

/**
 * @author Tahsin Hassan Rahit <tahsin.rahit@gmail.com>
 *
 */
public class Board {

    private static final int WIDTH = 700;
    private static final int HEIGHT = 700;
    private static final String TITLE = "Ludu by Rahit";
    private static PositionMap[] postionMap = new PositionMap[76];
    private static int[][][] pieceOrigin = new int[4][4][2];							// [color][pieceId][0 or 1] (0 for X, 1 for Y)
    
	private JLayeredPane lpane = new JLayeredPane();
	private JButton rollButton;
	private JButton doNotEliminate;
	private JLabel dice;
	private JFrame frame = new JFrame(TITLE);
	private LuduPlayer[] luduPlayers = new LuduPlayer[4];
	private Turn turn;
	
	/**
	 * 
	 */
	public Board() {
		this.frame.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setResizable(false);
		this.frame.add(lpane, BorderLayout.CENTER);
		this.lpane.setBounds(0, 0, WIDTH, HEIGHT);
		
		JLabel courtLabel = new JLabel(new ImageIcon(this.getClass().getResource("resources/avengers_ludu_court.png")));
		courtLabel.setBounds(-90, -90, WIDTH, HEIGHT);
		this.lpane.add(courtLabel, new Integer(0), 0);

        
		this.rollButton = new JButton("ROLL");
		this.rollButton.setBounds(0, HEIGHT-100, WIDTH, 50);
		Image img = null;
		try {
			img = ImageIO.read(getClass().getResource("resources/dice.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.rollButton.setIcon(new ImageIcon(img));
		this.lpane.add(rollButton, new Integer(1), 0);	
		this.doNotEliminate = new JButton("Show Mercy");
		this.doNotEliminate.setBounds(Board.WIDTH-150, 150, 120, 30);
		this.doNotEliminate.setVisible(false);
		this.lpane.add(doNotEliminate, new Integer(1), -1);	
		
		this.dice = new JLabel();
		this.dice.setFont(new Font("Arial", Font.BOLD, 50));
		dice.setBounds(WIDTH-100, 0, 50, 50);
		this.lpane.add(dice, new Integer(1), 0);

		this.initPositionMap();
		Board.initpieceOrigin();
		
        frame.pack();
        

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
	
	public void showFrame() {
        frame.setVisible(true);
	}
	
	public void setDice(String text) {
		this.dice.setText(text);
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


	public static int getWidth() {
		return WIDTH;
	}


	public static int getHeight() {
		return HEIGHT;
	}


	public JButton getRollButton() {
		return rollButton;
	}


	public void setPlayers(ArrayList<LuduPlayer> luduPlayers) {
		int i = 0;
		for (Iterator iterator = luduPlayers.iterator(); iterator.hasNext();) {
			LuduPlayer luduPlayer = (LuduPlayer) iterator.next();
			this.luduPlayers[i] = luduPlayer;
			i++;
		}		
		this.setPlayersPiece();
	}


	public JLayeredPane getLpane() {
		return lpane;
	}


	public LuduPlayer[] getLuduPlayers() {
		return luduPlayers;
	}


	public JFrame getFrame() {
		return frame;
	}


	public JButton getDoNotEliminate() {
		return doNotEliminate;
	}

	

}
