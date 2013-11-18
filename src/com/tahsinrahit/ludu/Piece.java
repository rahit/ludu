/**
 * 
 */
package com.tahsinrahit.ludu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.beans.Transient;
import java.io.ObjectInputStream.GetField;

import javax.swing.JPanel;

/**
 * @author Tahsin Hassan Rahit <tahsin.rahit@gmail.com>
 *
 */
public class Piece extends JPanel {

	private final int DIAMETRE = 28;
	private int pieceId;
	private Color pieceColor;
	private int originX;
	private int originY;
	private int x = 0;
	private int y = 0;
	/*
	 * States:
	 * -1 = Not active
	 * 1 = active on board
	 * 0 = In home
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
		this.state = -1;
		this.initOriginPosition();
	}



	private void initOriginPosition() {
		this.x = this.originX;
		this.y = this.originY;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.BLACK);
		g.fillOval(this.x-1, this.y-1, this.DIAMETRE+2, this.DIAMETRE+2);
		g.setColor(this.pieceColor);
		g.fillOval(this.x, this.y, this.DIAMETRE, this.DIAMETRE);
	}

	public Color getPieceColor() {
		return pieceColor;
	}
	
	public int getPieceId() {
		return this.pieceId;
	}


	public void setPieceColor(Color pieceColor) {
		this.pieceColor = pieceColor;
	}
	

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
		super.repaint();
	}


	public void setOriginPosition(int originX, int originY) {
		this.originX = originX;
		this.originY = originY;
		super.repaint();
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
	
}
