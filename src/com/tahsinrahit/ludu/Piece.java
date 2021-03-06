/**
 * 
 */
package com.tahsinrahit.ludu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.beans.Transient;

import javax.swing.JButton;

/**
 * @author Tahsin Hassan Rahit <tahsin.rahit@gmail.com>
 *
 */
public class Piece extends JButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final int DIAMETRE = 28;
	private int pieceId;
	private Color pieceColor;
	private int journyCount;
	private int positionIndex = -1;
	private int originX = 0;
	private int originY = 0;
	private int currentX;
	private int currentY;
	private int x;
	private int y;
	/*
	 * positionIndex:
	 * -1 = Not active
	 * 1 = active on board
	 * 100 = In home
	 */
	
	public Piece() {
		super();
		this.x = 0;
		this.y = 0;
		this.currentX = 0;
		this.currentY = 0;
		this.journyCount = 0;
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
		this.x = 0;
		this.y = 0;
		this.currentX = 0;
		this.currentY = 0;
		this.setBackground(pieceColor.darker());
	}

	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(this.pieceColor);
		g.fillOval(0, 0, this.DIAMETRE, this.DIAMETRE);
		g.setColor(Color.BLACK);
		g.drawLine((int)((this.DIAMETRE)/2), 0, (int)((this.DIAMETRE)/2), this.DIAMETRE);
		g.drawLine(0, (int)((this.DIAMETRE)/2), this.DIAMETRE, (int)((this.DIAMETRE)/2));
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
		this.currentX = x;
		this.currentY = y;
		this.setLocation(this.x, this.y);
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
		switch (positionIndex) {
		case -1:
			this.currentX = this.originX;
			this.currentY = this.originY;
			break;

		default:
			PositionMap map = Board.getPostionMapByIndex(positionIndex);
			this.currentX = map.getX();
			this.currentY = map.getY();
			break;
		}
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

	public int getCurrentX() {
		return currentX;
	}

	public void setCurrentX(int currentX) {
		this.currentX = currentX;
	}

	public int getCurrentY() {
		return currentY;
	}

	public void setCurrentY(int currentY) {
		this.currentY = currentY;
	}
	
}
