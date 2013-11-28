package com.tahsinrahit.ludu;

public class Ludu {

	private GameFrame mainGameFrame;
	private Thread gameFrameThread;
	
	public Ludu() {
		this.mainGameFrame = new GameFrame();
		this.gameFrameThread = new Thread(this.mainGameFrame);
		this.gameFrameThread.start();
	}
	
	public static void main(String[] args) {
		Ludu ludu = new Ludu();
		ludu.start();
	}
	
	

	public void start() {
		while (true) {
			Thread gameThread = new Thread(new GamePlay(this.mainGameFrame));
			gameThread.start();
			synchronized (this.mainGameFrame) {				
				try {
					Ludu.this.mainGameFrame.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			this.mainGameFrame.reInitiate();
		}
	}


}
