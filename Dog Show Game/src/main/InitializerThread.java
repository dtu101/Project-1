package main;

import gameStates.GameStateManager;
import playerData.Player;

public class InitializerThread implements Runnable{
	
	private GameStateManager gm;
	private Player p;
	
	public InitializerThread(GameStateManager gm){
		this.gm = gm;
	}
	
	public void run(){
		long x = System.nanoTime()/1000000;
		try{
			gm.initGameState();
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("**********************************************");
		System.out.println("LOAD STATE TIME: " + (System.nanoTime()/1000000-x) + " ms");
		gm.doneLoading();
		if(gm.getCurrentState() == 1) gm.startGame();
	}
}
