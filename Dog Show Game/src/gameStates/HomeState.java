package gameStates;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;

import main.GamePanel;
import mapData.Background;
import playerData.Player;

public class HomeState extends GameState{

	private Background bg;
	
	private Font font = new Font("Arial", Font.PLAIN, GamePanel.FONTSIZE);
	private FontMetrics metrics;
	
	private int currentDog = 0;
	private int stage;
	
	private Image portrait;
	
	private String errorMessage;
	private boolean displayMessage;
	
	public HomeState (GameStateManager gm){
		this.gm = gm;
		
		try{
			bg = new Background("/Background/menubg.jpeg", 1);
			bg.setPosition(0, 0);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void init(Player p) {
		this.p = p;
		stage = 0;
		errorMessage = "";
		displayMessage = false;
		currentDog = p.getCurrentDog();
		try{
			portrait = p.getDogPortrait(currentDog);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	
	public void update() {
		bg.update();
	}

	
	public void draw(Graphics2D g) {
		g.setFont(font);
		metrics = g.getFontMetrics(font);
		
		//Draw background
		bg.draw(g);
		
		switch(stage){
		case 0:
			//Draw Menu Items
			//Dog portraits
			g.drawImage(portrait, GamePanel.WIDTH/10, GamePanel.HEIGHT/10, GamePanel.HEIGHT*3/10,GamePanel.HEIGHT*3/10, null);
			g.drawRect(GamePanel.WIDTH/10, GamePanel.HEIGHT/10, GamePanel.HEIGHT*3/10, GamePanel.HEIGHT*3/10);
			
			g.setColor(Color.WHITE);
			g.fillRect(GamePanel.WIDTH/10, GamePanel.HEIGHT*2/5+10, GamePanel.WIDTH*3/10, metrics.getHeight()*3/2);
			g.setColor(Color.BLACK);
			if(p != null)
				g.drawString(p.getDogName(currentDog), GamePanel.WIDTH/10 + 10, GamePanel.HEIGHT*2/5+10 + metrics.getHeight());
			
			//Training Games
			g.setColor(Color.WHITE);
			g.fillRect(GamePanel.WIDTH*6/10, GamePanel.HEIGHT/10, GamePanel.HEIGHT*3/10, GamePanel.HEIGHT*3/10);
			g.setColor(Color.BLACK);
			g.drawString("TRAIN", GamePanel.WIDTH*6/10+GamePanel.HEIGHT*3/20-metrics.stringWidth("TRAIN")/2, GamePanel.HEIGHT/4+metrics.getHeight()/2);
			
			//Go to shop
			g.setColor(Color.WHITE);
			g.fillRect(GamePanel.WIDTH/10, GamePanel.HEIGHT/2, GamePanel.HEIGHT/4, GamePanel.HEIGHT/4);
			g.setColor(Color.BLACK);
			g.drawString("SHOP", GamePanel.WIDTH/10+GamePanel.WIDTH/8-metrics.stringWidth("SHOP")/2, GamePanel.HEIGHT*5/8+metrics.getHeight()/2);
			
			//Save & Menu
			g.setColor(Color.BLACK);
			g.drawString("Save and Quit", 10, GamePanel.HEIGHT - 25 - metrics.getHeight()/2);
			g.drawRect(5, GamePanel.HEIGHT - 25 - metrics.getHeight()*3/2, metrics.stringWidth("Save and Quit") + 10, metrics.getHeight() *3/2);
			break;
		//TRAINING OPTIONS
		case 1:
			//Dog run
			g.setColor(Color.WHITE);
			g.fillRect(GamePanel.WIDTH/10, GamePanel.HEIGHT/10, GamePanel.HEIGHT*3/10, GamePanel.HEIGHT*3/10);
			g.setColor(Color.BLACK);
			g.drawString("DOG RUN", GamePanel.WIDTH/10 + GamePanel.HEIGHT*3/20 - metrics.stringWidth("DOG RUN")/2, GamePanel.HEIGHT*5/20+metrics.getHeight()/2);
			if(displayMessage) g.drawString(errorMessage, GamePanel.WIDTH/2 - metrics.stringWidth(errorMessage)/2, GamePanel.HEIGHT/2+metrics.getHeight()/2);
			
			//Go Back
			g.setColor(Color.BLACK);
			g.drawString("Go back", 10, GamePanel.HEIGHT - 25-metrics.getHeight()/2);
			g.drawRect(5, GamePanel.HEIGHT - 25 - metrics.getHeight()*3/2, metrics.stringWidth("Go back") + 10, metrics.getHeight() *3/2);
		}
	}

	public void select(int x){
		p.setCurrentDog(currentDog);
		switch(x){
		case 0:
			gm.savePlayerData(p);
			gm.setState(GameStateManager.MENUSTATE, p);
			break;
		case 1:
			gm.setState(GameStateManager.DOGMANAGEMENT, p);
			break;
		case 2:
			stage = 1;
			break;
		case 3:
			gm.setState(GameStateManager.DOGRUN, p);
			break;
		case 4:
			stage = 0;
			break;
		case 5:
			gm.setState(GameStateManager.SHOP, p);
			break;
		}
	}
	
	public void keyPressed(int k) {}

	
	public void keyReleased(int k) {}

	
	public void mouseReleased(int x, int y) {
		displayMessage = false;
		switch(stage){
		case 0:
			//DogManagement Button
			if(x >= GamePanel.WIDTH/10 && x <= GamePanel.WIDTH*4/10 &&
					y >= GamePanel.HEIGHT/10 && y <= GamePanel.HEIGHT*4/10){
				select(1);
			}
			
			//TRAIN BUTTON
			if(x >= GamePanel.WIDTH*6/10 && x <= GamePanel.WIDTH*9/10 &&
					y >= GamePanel.HEIGHT/10 && y <= GamePanel.HEIGHT*4/10){
				select(2);
			}
			
			//Shop Button
			if(x >= GamePanel.WIDTH/10 && x <= GamePanel.HEIGHT*7/2 &&
					y >= GamePanel.HEIGHT/2 && y <= GamePanel.HEIGHT*3/4){
				select(5);
			}
			
			//SAVE AND QUIT BUTTON
			if(x >= 5 && x <= 5 + metrics.stringWidth("Save and Quit") + 10 &&
					y >= GamePanel.HEIGHT - 25 - metrics.getHeight()*3/2&& y <= GamePanel.HEIGHT - 25){
				select(0);
			}
			break;
		case 1:
			//GO BACK
			if(x >= 5 && x <= 5 + metrics.stringWidth("Go back") + 10 &&
					y >= GamePanel.HEIGHT - 25 - metrics.getHeight()*3/2&& y <= GamePanel.HEIGHT - 25){
				select(4);
			}
			//DOG RUN
			if(x >= GamePanel.WIDTH/10 && x <= GamePanel.WIDTH*4/10 &&
			y >= GamePanel.HEIGHT/10 && y <= GamePanel.HEIGHT*4/10){
				if(p.getDogStats(p.getCurrentDog())[Player.HUNGER] >= 15) select(3);
				else{
					errorMessage = p.getDogName(p.getCurrentDog()) + " is hungry!!!";
					displayMessage = true;
				}
			}
		}
	}

	
	public void mousePressed(int x, int y) {}

}
