package gameStates;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.KeyEvent;

import main.GamePanel;
import mapData.Background;
import playerData.Player;

public class LoadingState extends GameState{
	
	private int currentState;
	
	private Font font;
	private FontMetrics metrics;
	
	public LoadingState(GameStateManager gm){
		this.gm = gm;
		
		try{
			bg = new Background("/Background/menubg.jpeg", 1);
			bg.setPosition(0, 0);
			bg.setVector(-.5, 0);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void init(Player p){
		currentState = gm.getCurrentState();
	}
	public void update(){
		bg.update();
	}
	public void draw(java.awt.Graphics2D g){
		font = new Font("Arial", Font.PLAIN, GamePanel.FONTSIZE*3/5);
		metrics = g.getFontMetrics(font);
		bg.draw(g);

		switch(currentState){
		//DRAW INSTRUCTIONS FOR DOG RUN
		case 1:
			g.setColor(Color.BLACK);
			g.drawString("LOADING", GamePanel.WIDTH/2 - metrics.stringWidth("LOADING")/2, GamePanel.HEIGHT/2 + metrics.getHeight());
			break;
		case GameStateManager.DOGRUN:
			g.setColor(Color.WHITE);
			g.fillRect(GamePanel.WIDTH/10, GamePanel.HEIGHT/10, GamePanel.WIDTH*4/5, GamePanel.HEIGHT*4/5);
			g.setColor(Color.BLACK);
			g.drawString("You are taking your dog for a run in the park", GamePanel.WIDTH/2 - metrics.stringWidth("You are taking your dog for a run in the park")/2, GamePanel.HEIGHT * 3/10);
			g.drawString("WASD to move", GamePanel.WIDTH/2 - metrics.stringWidth("WASD to move")/2, GamePanel.HEIGHT *2/5);
			g.drawString("Try to collect food and avoid obstacles", GamePanel.WIDTH/2 - metrics.stringWidth("Try to collect food and avoid obstacles")/2, GamePanel.HEIGHT/2);
			g.setColor(Color.RED);
			if(!gm.stillLoading())
				g.drawString("PRESS ENTER TO BEGIN", GamePanel.WIDTH/2 - metrics.stringWidth("PRESS ENTER TO BEGIN")/2, GamePanel.HEIGHT * 7/10);
			else
				g.drawString("LOADING", GamePanel.WIDTH/2 - metrics.stringWidth("LOADING")/2, GamePanel.HEIGHT * 7/10);
			break;
		}
	}
	public void keyPressed(int k){
		if(!gm.stillLoading())
			if(k == KeyEvent.VK_ENTER){
				gm.startGame();
			}
	}
	public void keyReleased(int k){}
	public void mouseReleased(int x, int y){}
	public void mousePressed(int x, int y){}

}
