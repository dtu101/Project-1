package gameStates;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Image;

import javax.imageio.ImageIO;

import main.GamePanel;
import mapData.Background;
import playerData.Player;

public class DogManagement extends GameState{
	
	private int currentDog;
	private int currentTab;
	
	private int stage;
	
	private int viewInc;
	private int selectedItem;
	private boolean viewUp;
	private boolean viewDown;
	private double viewY;
	
	private int finalY;
	
	private String[] tabNames = {
			"Activity",
			"Trophies",
			"Stats",
			"Bio."
	};
	
	private boolean displayError;
	private String error;
	
	private String[][] data;
	
	private Font font;
	private FontMetrics metrics;
	
	private Image itemIcon;
	private String info;
	private double hunger;
	private double gold;
	
	public DogManagement(GameStateManager gm){
		this.gm = gm;
		
		try{
			bg = new Background("/Background/menubg.jpeg", 1);
			//bg = new Background("/Background/dogMwpbg.jpg", 1);
			bg.setVector(-.5, 0);
			bg.setPosition(0, 0);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void init(Player p){
		this.p = p;
		stage = 0;
		currentDog = p.getCurrentDog();
		currentTab = 0;
		
		finalY = 0;
		info = "";
		selectedItem = -1;
	}
	
	public void update(){
		if(viewUp || viewDown)
			scroll();
		bg.update();
	}
	public void draw(java.awt.Graphics2D g){
		font = new Font("Arial", Font.PLAIN, GamePanel.FONTSIZE*3/5);
		metrics = g.getFontMetrics(font);
		g.setFont(font);
		
		//draw BG
		bg.draw(g);
		
		//Draw Box
		g.setColor(Color.WHITE);
		g.fillRect(GamePanel.WIDTH/20, GamePanel.HEIGHT/20, GamePanel.WIDTH*9/10, GamePanel.HEIGHT*7/10 + metrics.getHeight()*3/2);
	
		//Draw Hunger Meter
		if(p.getDogStats(p.getCurrentDog())[Player.HUNGER] > 100){
			g.setColor(Color.GREEN);
			g.fillRect(GamePanel.WIDTH/10, GamePanel.HEIGHT/4+GamePanel.HEIGHT*3/20 + metrics.getHeight()*2, GamePanel.WIDTH/4, metrics.getHeight()*3/2);
			g.setColor(Color.ORANGE);
			g.fillRect(GamePanel.WIDTH/10, GamePanel.HEIGHT/4+GamePanel.HEIGHT*3/20 + metrics.getHeight()*2, (int)(((p.getDogStats(p.getCurrentDog())[Player.HUNGER]-100)/100 * GamePanel.WIDTH/4)), metrics.getHeight()*3/2);
		}else{
			g.setColor(Color.GREEN);
			g.fillRect(GamePanel.WIDTH/10, GamePanel.HEIGHT/4+GamePanel.HEIGHT*3/20 + metrics.getHeight()*2, (int)(p.getDogStats(p.getCurrentDog())[Player.HUNGER]/100 * GamePanel.WIDTH/4), metrics.getHeight()*3/2);
		}
		
		g.setColor(Color.WHITE);
		for(int i = 1; i < 10; i++){
			g.drawLine(GamePanel.WIDTH/10 + i * GamePanel.WIDTH/40, GamePanel.HEIGHT/4+GamePanel.HEIGHT*3/20+metrics.getHeight()*2, GamePanel.WIDTH/10 + i * GamePanel.WIDTH/40, GamePanel.HEIGHT/4+GamePanel.HEIGHT*3/20+metrics.getHeight()*2 + metrics.getHeight()*3/2);
		}
		
		g.setColor(Color.BLACK);
		g.drawString("Hunger: " + p.getDogStats(p.getCurrentDog())[Player.HUNGER], GamePanel.WIDTH/10, GamePanel.HEIGHT/4 + GamePanel.HEIGHT*3/20 + metrics.getHeight()*3/2);
		g.drawRect(GamePanel.WIDTH/10, GamePanel.HEIGHT/4+GamePanel.HEIGHT*3/20 + metrics.getHeight()*2, GamePanel.WIDTH/4, metrics.getHeight()*3/2);
		
		//Draw go back
		g.setColor(Color.WHITE);
		g.fillRect(GamePanel.WIDTH/20, GamePanel.HEIGHT*9/10 - metrics.getHeight()/2, metrics.stringWidth("Go Back")*2, metrics.getHeight()*3/2);
		g.setColor(Color.BLACK);
		g.drawString("Go Back", GamePanel.WIDTH/20 + metrics.stringWidth("Go Back")/2, GamePanel.HEIGHT*9/10 + metrics.getHeight()/2);
		
		switch(stage){
		case 0:
			//Draw Portrait
			g.setColor(Color.BLACK);
			g.drawImage(p.getDogPortrait(currentDog), GamePanel.WIDTH/10, GamePanel.HEIGHT/10, GamePanel.HEIGHT/4, GamePanel.HEIGHT/4, null);
			g.drawRect(GamePanel.WIDTH/10, GamePanel.HEIGHT/10, GamePanel.HEIGHT/4, GamePanel.HEIGHT/4);
			g.drawString("Name: " + p.getDogName(currentDog), GamePanel.WIDTH/10, GamePanel.HEIGHT/4 + GamePanel.HEIGHT/10 + metrics.getHeight()*3/2);
			
			//Draw Tabs
			for(int i = 0; i < tabNames.length; i++){
				g.drawRect(GamePanel.WIDTH - GamePanel.WIDTH/20 - formatTabs(tabNames[i],i)*3/2, 
						GamePanel.HEIGHT*3/4-metrics.getHeight()*3/2, 
						metrics.stringWidth(tabNames[i])*3/2, 
						metrics.getHeight()*3/2);
				g.drawString(tabNames[i], 
						GamePanel.WIDTH - GamePanel.WIDTH/20 - formatTabs(tabNames[i],i)*3/2 + metrics.stringWidth(tabNames[i])/4, 
						GamePanel.HEIGHT*7/10 + metrics.getHeight()*3/2);
			}
			
			//Tabs
			switch(currentTab){
			//Bio.
			case 0:
				g.drawString("Gender: " + p.getDogGender(currentDog), GamePanel.WIDTH*2/10 + GamePanel.HEIGHT/4, GamePanel.HEIGHT/10);
				g.drawString("Age: " + p.getDogAge(currentDog), GamePanel.WIDTH*2/10 + GamePanel.HEIGHT/4, GamePanel.HEIGHT/10 + metrics.getHeight()*3/2);
				g.drawString("Origin: " + p.getDogBio(currentDog, "origin"), GamePanel.WIDTH*2/10+GamePanel.HEIGHT/4, GamePanel.HEIGHT/10+metrics.getHeight()*3);
				break;
			//Stats	
			case 1:
				for(int i = 0; i < p.getDogStatNames(currentDog).length - 1; i++)
					g.drawString(p.getDogStatNames(currentDog)[i] + ": " + (double)Math.round(p.getDogStats(currentDog)[i]*10)/10, GamePanel.WIDTH*2/10 + GamePanel.HEIGHT/4, (GamePanel.HEIGHT/10 + (i)*metrics.getHeight()*3/2));
				break;
			//Trophies
			case 2:
			//Activities
			case 3:
			}
			
			//Draw Feed
			g.setColor(Color.WHITE);
			g.fillRect(GamePanel.WIDTH*19/20-metrics.stringWidth("Feed " + p.getDogName(p.getCurrentDog()))*2, GamePanel.HEIGHT*9/10-metrics.getHeight()/2, metrics.stringWidth("Feed " + p.getDogName(p.getCurrentDog()))*2, metrics.getHeight()*3/2);
			g.setColor(Color.BLACK);
			g.drawString("Feed " + p.getDogName(p.getCurrentDog()), GamePanel.WIDTH*19/20 - metrics.stringWidth("Feed " + p.getDogName(p.getCurrentDog()))*3/2, GamePanel.HEIGHT*9/10 + metrics.getHeight()/2);
			break;
		//Feed menu
		case 1:
			
			//Draw Feed Menu
			
			//DrawFood
			g.setColor(Color.BLACK);
			if(itemIcon != null)
				g.drawImage(itemIcon, GamePanel.WIDTH/10, GamePanel.HEIGHT/10, GamePanel.HEIGHT/4, GamePanel.HEIGHT/4, null);
			g.drawRect(GamePanel.WIDTH/10, GamePanel.HEIGHT/10, GamePanel.HEIGHT/4, GamePanel.HEIGHT/4);
			
			//Draw food inventory
			for(int i = 0; i <  ((GamePanel.HEIGHT/2)/(metrics.getHeight())); i++){
				//Draw Box
				if(viewInc + i == selectedItem)
					g.drawRect(GamePanel.WIDTH*9/20, metrics.getHeight() * (i) + (int)viewY, GamePanel.WIDTH*9/20, metrics.getHeight() *5/4);
				//Draw item names
				if(viewInc + i < p.getInventorySize()){
					//g.drawString(p.getInventoryItemName(i+viewInc), GamePanel.WIDTH*9/20 + 5, GamePanel.HEIGHT/10 + (i+2)*(metrics.getHeight()) + (int)viewY);
					g.drawString(p.getInventoryItemName(i+viewInc), GamePanel.WIDTH*9/20 + 5, metrics.getHeight() * (i+1) + (int)viewY);
				}else
					g.drawString("-------------------------------------------------------", GamePanel.WIDTH*9/20, metrics.getHeight() * (i+1) + (int)viewY);
			}
			
			//Text boundaries
			g.fillRect(GamePanel.WIDTH*9/20, 
					GamePanel.HEIGHT/10 - metrics.getHeight() - 5, GamePanel.WIDTH*9/20, metrics.getHeight()*2);
			g.fillRect(GamePanel.WIDTH*9/20, 
					GamePanel.HEIGHT*3/5+metrics.getHeight() + 5, GamePanel.WIDTH*9/20, metrics.getHeight()*2);
			
			
			//Draw food info
			//p.getInventorySize() > 0 && selectedItem < p.getInventorySize() && 
			if(info != "" && selectedItem >= 0){
				g.drawString(p.getInventoryItemName(selectedItem) + ":", GamePanel.WIDTH/10, GamePanel.HEIGHT*2/5 + metrics.getHeight()*9/2);
				formattText(g, info, GamePanel.WIDTH/10, GamePanel.HEIGHT*2/5 + metrics.getHeight()*11/2);
				g.drawString("G: " + gold, GamePanel.WIDTH/10, finalY);
				g.drawString("H: " + hunger, GamePanel.WIDTH/5, finalY);
			}
			
			//Eat button
			g.setColor(Color.WHITE);
			g.fillRect(GamePanel.WIDTH*9/10 - metrics.stringWidth("Eat"), GamePanel.HEIGHT*9/10 - metrics.getHeight()/2, metrics.stringWidth("Eat")*2, metrics.getHeight()*3/2);
			g.setColor(Color.BLACK);
			g.drawString("Eat", GamePanel.WIDTH*9/10 - metrics.stringWidth("Eat")/2, GamePanel.HEIGHT*9/10 + metrics.getHeight()/2);
			break;
		}
		
		//Draw error message
		if(displayError)
			g.drawString(error, GamePanel.WIDTH/10, GamePanel.HEIGHT*2/5 + metrics.getHeight()*9/2);
	}
	
	private void formattText(java.awt.Graphics2D g, String info, int x, int y){
		String s;
		if(info != null) s = info;
		else {
			System.out.println("FORMAT TEXT: INFO ERROR");
			return;
		}
		
		//Initialize temp to be start to first space
		String temp = s.substring(0, s.indexOf(' ') + 1);

		//add another word to temp and subtract from s if a space exists
		while(metrics.stringWidth(temp) < GamePanel.WIDTH/4 && s.indexOf(' ') != -1){
			s = s.substring(s.indexOf(' ') + 1);
			temp += s.substring(0, s.indexOf(' ') + 1);
		}
		
		g.drawString(temp, x, y);
		
		//Do the same with the next line
		if(metrics.stringWidth(s) > GamePanel.WIDTH/4){
			formattText(g, s, x, y + metrics.getHeight());
		}
		//Or finish it manually
		else {
			g.drawString(s, x, y + metrics.getHeight());
			finalY = y + metrics.getHeight()*2;
		}
		
	}
	
	private int formatTabs(String s, int i){
		if(i <= 0)
			return metrics.stringWidth(tabNames[i]);
		return metrics.stringWidth(tabNames[i]) + formatTabs(tabNames[i], i-1);
	}
	
	private void scroll(){
		if(viewDown){
			if(viewInc < p.getInventorySize()){ 
				viewY -= 1;
			}
		}	
		if(viewUp) {
			if(viewInc > 0 || viewY < GamePanel.HEIGHT/10 + (metrics.getHeight())){
				viewY += 1;
				if(viewY >= GamePanel.HEIGHT/10 + (metrics.getHeight()) && viewInc != 0){
					viewY = GamePanel.HEIGHT/10;
					viewInc--;
				}
			}
		}
		
		//Scroll down
		if(viewY < (GamePanel.HEIGHT/10 + (metrics.getHeight())) -metrics.getHeight()){
			if(viewInc < p.getInventorySize()){
				viewInc += 1;
				viewY = GamePanel.HEIGHT/10 + (metrics.getHeight());
			}
		}
		
		//Scroll up
		if(viewY > GamePanel.HEIGHT/10 + (metrics.getHeight()) + metrics.getHeight()){
			if(viewInc > 0){
				viewInc -= 1;
				viewY = GamePanel.HEIGHT/10 + (metrics.getHeight());
			}
		}
	}
	
	public void updateData(){
		itemIcon = p.getItemIcon(selectedItem);
		info = p.getInventoryItemInfo("info", selectedItem);
		hunger = Double.parseDouble(p.getInventoryItemInfo("hunger_value", selectedItem));
		gold = Double.parseDouble(p.getInventoryItemInfo("gold_value", selectedItem));
	}
	
	public void keyPressed(int k){}
	public void keyReleased(int k){}
	public void mouseReleased(int x, int y){
		//go back button
		if( x >= GamePanel.WIDTH/20 && x <= GamePanel.WIDTH/20 + 2*metrics.stringWidth("Go Back") &&
				y >= GamePanel.HEIGHT*9/10 - metrics.getHeight()/2 && y <= GamePanel.HEIGHT*9/10 - metrics.getHeight()/2 + metrics.getHeight()*3/2){
			switch(stage){
			case 0:
				p.setCurrentDog(currentDog);
				gm.savePlayerData(p);
				gm.setState(GameStateManager.HOMESTATE, p);
				break;
			case 1:
				stage = 0;
				break;
			}
		}
		
		switch(stage){
		case 0:
			//Feed button
			if( x >= GamePanel.WIDTH*19/20-metrics.stringWidth("Feed " + p.getDogName(p.getCurrentDog()))*2 && x <= GamePanel.WIDTH*19/20-metrics.stringWidth("Feed " + p.getDogName(p.getCurrentDog()))*2 + metrics.stringWidth("Feed " + p.getDogName(p.getCurrentDog()))*2&&
					y >= GamePanel.HEIGHT*9/10 - metrics.getHeight()/2 && y <= GamePanel.HEIGHT*9/10 - metrics.getHeight()/2 + metrics.getHeight()*3/2){
				p.setCurrentDog(currentDog);
				gm.savePlayerData(p);
				stage = 1;
				selectedItem = 0;
				viewInc = 0;
				viewY = GamePanel.HEIGHT/10 + (metrics.getHeight());
				updateData();
			}
				
			//pressing tabs
			for(int i = 0; i < tabNames.length; i++){
				if(x >= GamePanel.WIDTH - GamePanel.WIDTH/20 - formatTabs(tabNames[i],i)*3/2 &&
						x <= GamePanel.WIDTH - GamePanel.WIDTH/20 - formatTabs(tabNames[i],i)*3/2 + metrics.stringWidth(tabNames[i])*3/2 &&
						y >= GamePanel.HEIGHT*3/4-metrics.getHeight()*3/2 &&
						y <= GamePanel.HEIGHT*3/4-metrics.getHeight()*3/2 + metrics.getHeight()*3/2){
					currentTab = tabNames.length - i - 1;
				}
			}
			break;
		case 1:
			//Feeding
			viewUp = false;
			viewDown = false;
			break;
		}
	}
	public void mousePressed(int x, int y){
		displayError = false;
		//Feeding
		switch(stage){
		case 1:
			//Move inventory up or down
			if(x >= GamePanel.WIDTH*9/20 && x <= GamePanel.WIDTH*9/10 &&
				y >= GamePanel.HEIGHT/10 - metrics.getHeight() && y <= GamePanel.HEIGHT/10 + metrics.getHeight())
				viewDown = true;
			else if(x >= GamePanel.WIDTH*9/20 && x <= GamePanel.WIDTH*9/10 &&
					y >= GamePanel.HEIGHT*3/5+metrics.getHeight() + 5 && y <= GamePanel.HEIGHT*3/5 + metrics.getHeight()*3)
				viewUp = true;
			//Move click on food item
			for(int i = 0; i < ((GamePanel.HEIGHT/2)/(metrics.getHeight())); i++){
				if(x >= GamePanel.WIDTH*9/20 && x <= GamePanel.WIDTH*9/10 &&
						y >= (metrics.getHeight() * (i) + (int)viewY) &&
						y <= (metrics.getHeight() * (i+1) + (int)viewY)){
					selectedItem = viewInc + i;
					updateData();
				}
			}
			//Click on feed
			//GamePanel.WIDTH*9/10 - metrics.stringWidth("Eat"), GamePanel.HEIGHT*9/10 - metrics.getHeight()/2, metrics.stringWidth("Eat")*2, metrics.getHeight()*3/2
			if(x >= GamePanel.WIDTH*9/10 - metrics.stringWidth("Eat") && x <= GamePanel.WIDTH*9/10 - metrics.stringWidth("Eat") + metrics.stringWidth("Eat")*2 &&
					y >= GamePanel.HEIGHT*9/10 - metrics.getHeight()/2 && y <= GamePanel.HEIGHT*9/10 - metrics.getHeight()/2 + metrics.getHeight()*3/2){
				if(p.getInventorySize() > 0 && selectedItem >= 0){
					if(p.getDogStats(p.getCurrentDog())[Player.HUNGER]<200){
						p.feedDog(selectedItem);
						selectedItem = 0;
						updateData();
					}else{
						selectedItem = -1;
						displayError = true;
						error = p.getDogName(p.getCurrentDog()) + " is full!!!";
					}
				}
			}
			break;
		}
	}
}
