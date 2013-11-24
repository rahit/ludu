package com.tahsinrahit.ludu;

import java.io.BufferedInputStream;

import javazoom.jl.player.Player;

public class GameMusic implements Runnable{
	
	private String fileName;

	public GameMusic() {
		// TODO Auto-generated constructor stub
	}

	public GameMusic(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public void run() {
		try {
			BufferedInputStream bis = new BufferedInputStream(this.getClass().getResourceAsStream("resources/" + this.fileName));  
            Player Player = new Player(bis);  
            Player.play();  
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	


}
