/**
 * 
 */
package com.tahsinrahit.ludu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.beans.Transient;

import javax.swing.JPanel;

/**
 * @author Tahsin Hassan Rahit <tahsin.rahit@gmail.com>
 *
 */
public class Piece extends JPanel{

	private final int DIAMETRE = 28;
	private int pieceId;
	private Color pieceColor;
	private int journyCount = 0;
	private int positionIndex = -1;
	private int originX = 0;
	private int originY = 0;
	private int x = 0;
	private int y = 0;
	/*
	 * positionIndex:
	 * -1 = Not active
	 * 1 = active on board
	 * 100 = In home
	 */
	private int state = -1;
	
	/**
	 * 
	 */
	public Piece() {
		super();
	}
	
	/**
	 * @param pieceColor
	 */
	public Piece(int pieceId, Color pieceColor) {
		super();
		this.pieceId = pieceId;
		this.pieceColor = pieceColor;
		this.positionIndex = -1;
		this.journyCount = 0;
		this.setBackground(Color.ORANGE);
	}



	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.BLACK);
		//g.fillOval(this.x-1, this.y-1, this.DIAMETRE+2, this.DIAMETRE+2);
		g.drawLine((int)((this.x+DIAMETRE)/2), this.y, (int)((this.x+DIAMETRE)/2), this.y+DIAMETRE);
		g.setColor(this.pieceColor);
		g.fillOval(this.x, this.y, this.DIAMETRE, this.DIAMETRE);
		//g.draw3DRect(this.x-1, this.y-1, this.DIAMETRE+2, this.DIAMETRE+2, true);
		g.setColor(Color.BLACK);
		g.drawLine((int)((this.x+DIAMETRE)/2), this.y, (int)((this.x+DIAMETRE)/2), this.y+DIAMETRE);
		g.drawLine(this.x, (int)((this.y+DIAMETRE)/2), this.x+DIAMETRE, (int)((this.y+DIAMETRE)/2));
	}

	public Color getPieceColor() {
		return pieceColor;
	}
	
	public int getPieceId() {
		return this.pieceId;
	}

	public int getJournyCount() {
		return journyCount;
	}

	public void setJournyCount(int journyCount) {
		this.journyCount = journyCount;
	}

	public void setPieceColor(Color pieceColor) {
		this.pieceColor = pieceColor;
	}
	

	public void setPiecePosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	@Transient
	public Dimension getMinimumSize() {
		return new Dimension(32, 32);
	}

	@Override
	@Transient
	public Dimension getPreferredSize() {
		return this.getMinimumSize();
	}

	public int getPositionIndex() {
		return positionIndex;
	}

	public void setPositionIndex(int positionIndex) {
		this.positionIndex = positionIndex;
	}

	public void setOriginPosition(int x, int y) {
		this.originX = x;
		this.originY = y;
	}

	public int getOriginX() {
		return originX;
	}

	public int getOriginY() {
		return originY;
	}
	
}
