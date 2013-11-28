package com.tahsinrahit.ludu;

import java.io.BufferedInputStream;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class GameMusic implements Runnable{
	
	private String fileName;
	private boolean repeat;
	private Player player;

	public GameMusic() {
		// TODO Auto-generated constructor stub
	}

	public GameMusic(String fileName) {
		this(fileName, false);
	}

	public GameMusic(String fileName, boolean repeat) {
		this.fileName = fileName;
		this.repeat = repeat;
	}
	@Override
	public void run() {
		if(this.repeat) {
			do {
				this.play();
			} while (player.isComplete());
		}
		else {
			this.play();
		}
	}
	
	
	public void play() {
		BufferedInputStream bis = new BufferedInputStream(this.getClass().getResourceAsStream("resources/" + this.fileName));  
		try {
			this.player = new Player(bis);
			this.player.play();
		} catch (JavaLayerException e) {
			e.printStackTrace();
		}
	}
	


}
