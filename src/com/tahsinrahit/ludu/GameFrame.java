package com.tahsinrahit.ludu;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;

public class GameFrame extends JFrame implements Runnable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String TITLE = "Ludu by Rahit";
    private static final int WIDTH = 700;
    private static final int HEIGHT = 700;
    
	private LuduMenuBar luduMenuBar;
	
	private JLayeredPane lPane;
    
	public GameFrame() {
		super(GameFrame.TITLE);
		this.luduMenuBar = new LuduMenuBar(this);
		this.setMinimumSize(new Dimension(GameFrame.WIDTH, GameFrame.HEIGHT));
		this.setPreferredSize(new Dimension(GameFrame.WIDTH, GameFrame.HEIGHT));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setJMenuBar(this.luduMenuBar);
	}
	
	public void reInitiate() {
		this.remove(this.lPane);
		this.luduMenuBar = new LuduMenuBar(this);
		this.setMinimumSize(new Dimension(GameFrame.WIDTH, GameFrame.HEIGHT));
		this.setPreferredSize(new Dimension(GameFrame.WIDTH, GameFrame.HEIGHT));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setJMenuBar(this.luduMenuBar);
		this.repaint();
	}
	
	@Override
	public void run() {
		this.setVisible(true);
	}	

	public int getWidth() {
		return GameFrame.WIDTH;
	}
	
	public int getHeight() {
		return GameFrame.HEIGHT;
	}

	public JLayeredPane getlPane() {
		return lPane;
	}

	public void setlPane(JLayeredPane lPane) {
		this.lPane = lPane;
	}


	
	
}
