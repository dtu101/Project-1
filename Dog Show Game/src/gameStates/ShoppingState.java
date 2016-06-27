package gameStates;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import main.GamePanel;
import mapData.Background;
import playerData.Player;

public class ShoppingState extends GameState{
	
	private int stage;
	
	private Font font;
	private FontMetrics metrics;
	
	
	public ShoppingState(GameStateManager gm){
		this.gm = gm;
		try{
			bg = new Background("/Background/menubg.jpeg", 1);
			
			font = new Font("Arial", Font.PLAIN, 16);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	
	public void init(Player p) {
		stage = 0;
		this.p = p;
	}

	
	public void update() {}

	
	public void draw(Graphics2D g) {
		metrics = g.getFontMetrics(font);
		//draw background
		bg.draw(g);
		
		//draw buttons
		switch(stage){
		//Buy or Sell
		case 0:
			g.setColor(Color.WHITE);
			//BUY
			g.fillRect(GamePanel.WIDTH/10, GamePanel.HEIGHT*7/20, GamePanel.WIDTH*3/10, GamePanel.WIDTH*3/10 + metrics.getHeight()/2);
			//SELL
			g.fillRect(GamePanel.WIDTH*6/10, GamePanel.HEIGHT*7/20, GamePanel.WIDTH*3/10, GamePanel.WIDTH*3/10);
			g.setColor(Color.BLACK);
			g.drawString("BUY", GamePanel.WIDTH/4 - metrics.stringWidth("BUY")/2, GamePanel.HEIGHT*7/20+metrics.getHeight()*3/2);
			g.drawString("SELL", GamePanel.WIDTH*3/4 - metrics.stringWidth("SELL")/2, GamePanel.HEIGHT*7/20+metrics.getHeight()*3/2);
			
			//Go Back
			g.setColor(Color.BLACK);
			g.drawString("Go back", GamePanel.WIDTH/20, GamePanel.HEIGHT-metrics.getHeight()*3/2);
			g.drawRect(GamePanel.WIDTH/20-metrics.stringWidth("Go back")/2, GamePanel.HEIGHT - metrics.getHeight()*3, metrics.stringWidth("Go back")*5/2, metrics.getHeight()*2);
			break;
		}
	}
	
	public void keyPressed(int k) {}

	
	public void keyReleased(int k) {}
	
	public void mouseReleased(int x, int y) {
		switch(stage){
		case 0:
			//Buy Button
			if(x >= GamePanel.WIDTH/10 && x <= GamePanel.WIDTH*2/5 && y >= GamePanel.HEIGHT*7/20 && y <= GamePanel.HEIGHT*7/20 + GamePanel.WIDTH*3/10)
				stage = 1;
		
			//Go Back Button
			if(x >= GamePanel.WIDTH/20-metrics.stringWidth("Go back")/2 && x <= GamePanel.WIDTH/20+metrics.stringWidth("Go back")*2 && y >= GamePanel.HEIGHT - metrics.getHeight()*5/2 && y <= GamePanel.HEIGHT - metrics.getHeight()/2)
				gm.setState(GameStateManager.HOMESTATE, p);
			break;
		}
	}

	
	public void mousePressed(int x, int y) {}
}
