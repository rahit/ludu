package com.tahsinrahit.ludu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class LuduMenuBar extends JMenuBar {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JMenu luduMenu;
	private JMenu aboutMenu;
	
	private JMenuItem newGameMenuItem;
	private JMenuItem exitGameMenuItem;
	private JMenuItem aboutLuduMenuItem;

	private GameFrame gameFrame;
	public LuduMenuBar(GameFrame gameFrame) {
		this.gameFrame = gameFrame;
		this.luduMenu = new JMenu("Ludu");
		this.aboutMenu = new JMenu("About");
		
		this.newGameMenuItem = new JMenuItem("New");
		this.exitGameMenuItem = new JMenuItem("Exit");
		this.aboutLuduMenuItem = new JMenuItem("About");
		
		this.luduMenu.add(this.newGameMenuItem);
		this.luduMenu.add(this.exitGameMenuItem);
		this.aboutMenu.add(this.aboutLuduMenuItem);
		
		this.add(this.luduMenu);
		this.add(this.aboutMenu);

		
		this.initListeners();
	}

	private void initListeners() {
		this.newGameMenuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				synchronized (LuduMenuBar.this.gameFrame) {
					LuduMenuBar.this.gameFrame.notify();
				}
			}
		});

		this.exitGameMenuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				LuduMenuBar.this.gameFrame.dispose();
				System.exit(ABORT);
			}
		});

		this.aboutLuduMenuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(null, "Copyright (c) Ludu 2013 \nDeveloper: K.M. Tahsin Hassan Rahit");
			}
		});
	}

	
	
	
}
