package gameStates;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import dog.*;
import main.GamePanel;
import mapData.Background;
import playerData.Player;

public class DogSelectionState extends GameState{
	
	private Background bg;
	private Dog[] dog = new Dog[4];
	private int xbound;
	private int ybound;
	private int x2bound;
	
	private int currentChoice;
	private int current;

	private Font font;
	private FontMetrics metric;
	
	private String[] options = {
			"Go Back to Menu",
			"Ok"
	};
	private String[] stats = {
			"Speed",
			"Beauty",
			"Intelligence",
			"Obedience",
			"Stamina",
			"Happiness"
	};
	
	private String name;
	private boolean nameFocused;
	private boolean isCap;
	
	public DogSelectionState(GameStateManager gm){
		this.gm = gm;
		
		xbound = (GamePanel.WIDTH/10 + 10);
		ybound = (GamePanel.HEIGHT/2 + 16);
		x2bound = GamePanel.HEIGHT/10-20;
		
		try{
			bg = new Background("/Background/dogwpbg.jpg", 1);
			bg.setVector(-.5, 0);
			bg.setPosition(0, 0);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void init(Player p){
		this.p = p;
		name = "";
		currentChoice = 0;
		current = 0;
		dog[0] = new Dog("Golden_Retriever");
		dog[1] = new Dog("Panda_Dog");
		dog[2] = new Dog("Panda_DogTu");
		dog[3] = new Dog("Husky");
		
		try{
			font = new Font("Arial", Font.PLAIN, GamePanel.FONTSIZE*3/4);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void update(){
		bg.update();
		
	}
	public void draw(Graphics2D g){
		
		g.setStroke(new BasicStroke(3f));
		metric = g.getFontMetrics(font);
		g.setFont(font);
		
		//Draw background
		bg.draw(g);
		
		//Draw Options
		if(currentChoice == 0) g.setColor(Color.BLACK);
		else g.setColor(Color.RED);
		g.drawRect(GamePanel.WIDTH/10 - 10, 
				GamePanel.HEIGHT * 9/10 - metric.getHeight(), 
				metric.stringWidth(options[0]) + 20,
				metric.getHeight()*3/2);
		g.drawString(options[0], GamePanel.WIDTH/10,
				GamePanel.HEIGHT*9/10);
		if(currentChoice == 1) g.setColor(Color.BLACK);
		else g.setColor(Color.RED);
		g.drawRect(GamePanel.WIDTH*9/10 - 10, 
				GamePanel.HEIGHT * 9/10 - metric.getHeight(), 
				metric.stringWidth(options[1]) + 20, 
				metric.getHeight()*3/2);
		g.drawString(options[1], GamePanel.WIDTH*9/10,
				GamePanel.WIDTH*9/10);
		
		//Draw namespace
		if(nameFocused) g.setColor(Color.BLACK);
		else g.setColor(Color.RED);
		//Draw box
		g.drawRect(GamePanel.WIDTH/10 + metric.stringWidth(options[0]) + (GamePanel.WIDTH*9/10-GamePanel.WIDTH/10)/10 - 20, 
				GamePanel.HEIGHT*9/10 - (metric.getHeight()),
				GamePanel.WIDTH*8/10 - GamePanel.WIDTH/10 - metric.stringWidth(options[0]),
				metric.getHeight()*3/2);
		//draw word name
		g.drawString("Name:",
				GamePanel.WIDTH/10 + metric.stringWidth(options[0]) + (GamePanel.WIDTH*9/10-GamePanel.WIDTH/10)/10 + 5, 
				GamePanel.HEIGHT*9/10);
		//draw typed name
		g.drawString(name, 
				GamePanel.WIDTH/10 + metric.stringWidth(options[0]) + (GamePanel.WIDTH*9/10-GamePanel.WIDTH/10)/10 + 10 + metric.stringWidth("Name:"),
				GamePanel.HEIGHT*9/10);
			
		g.setColor(Color.BLACK);
		//Draw dog selections
			//Dog portrait
		if(current < dog.length && dog[0] != null && dog[1] != null){
			g.drawImage(dog[current].getPortrait(),GamePanel.WIDTH/20, GamePanel.HEIGHT/20, GamePanel.WIDTH*2/5, GamePanel.HEIGHT*2/5, null);
				//Dog Stats
			g.drawRect(GamePanel.WIDTH*19/20 - GamePanel.WIDTH*2/5, GamePanel.HEIGHT/20, GamePanel.WIDTH*2/5, GamePanel.HEIGHT*2/5);
			for(int i = 0; i < dog[current].getStats().length-1; i++){
				g.drawString(stats[i], GamePanel.WIDTH*19/20 - GamePanel.WIDTH*2/5 + 10, (GamePanel.HEIGHT/20 + metric.getHeight()) + (metric.getHeight()*3/2) * i);
				g.drawString("" + dog[current].getStats()[i], GamePanel.WIDTH*19/20 - GamePanel.WIDTH*1/5 + 10, (GamePanel.HEIGHT/20 + metric.getHeight()) + (metric.getHeight()*3/2) * i);
			}
			
				//Draw Starter dogs
			g.drawRect(GamePanel.WIDTH/10, GamePanel.HEIGHT/2, GamePanel.WIDTH*8/10, GamePanel.HEIGHT*3/10);
			for(int j = 0; j < 2; j++){
				for(int x = 0; x < 5; x++){
					if(x + j * 5 < dog.length){
						g.drawImage(dog[x+j*5].getPortrait(), 
								(GamePanel.WIDTH/10 + 10) + x * (GamePanel.WIDTH/10 + 14), 
								(GamePanel.HEIGHT/2 + 16) + j * (GamePanel.HEIGHT*2/10 - 10),
								GamePanel.HEIGHT/10-20, 
								GamePanel.HEIGHT/10-20, 
								null);					
					}
				}
			}
		}
		
	}
	
	public void type(char c){
		//System.out.println((int)c);
		if(name.length() != 0 && c == 8) name = name.substring(0, name.length()-1);
		if(name.length() > 25) return;
		if(c == 10){
			currentChoice = 1;
			nameFocused = false;
			return;
		}
		if(c >= 32 && c <= 137){
			if(isCap) name += c;
			else name += Character.toLowerCase(c);
		}
	}
	
	public void select(){
		switch(currentChoice){
		case 0: gm.setState(GameStateManager.MENUSTATE, null);
			break;
		case 1:
			dog[current].setName(name);
			p.addDog(dog[current]);
			System.out.println("Dog added");
			gm.setState(GameStateManager.HOMESTATE, p);
			break;
		}
	}
	
	public void keyPressed(int k){
		if(k==KeyEvent.VK_SHIFT) isCap = true;
		
		if(!nameFocused){
			if(k==KeyEvent.VK_A || k==KeyEvent.VK_LEFT) currentChoice--;
			if(k==KeyEvent.VK_D || k==KeyEvent.VK_RIGHT) currentChoice++;
			if(currentChoice == -1) currentChoice = options.length-1;
			if(currentChoice == options.length) currentChoice = 0;
		}else{
			type((char)k);
		}
		
		if(k==KeyEvent.VK_ENTER){
			select();
		}
	}
	public void keyReleased(int k){
		if(k == KeyEvent.VK_SHIFT) isCap = false;
	}
	
	public void mouseReleased(int x, int y){
		int xpos;
		int ypos;
		int x2pos;
		int y2pos;
		
		//click on name box
		xpos = GamePanel.WIDTH/10 + metric.stringWidth(options[0]) + (GamePanel.WIDTH*9/10-GamePanel.WIDTH/10)/10 - 20;
		ypos = GamePanel.HEIGHT*9/10 - (metric.getHeight());
		x2pos = GamePanel.WIDTH/10 + metric.stringWidth(options[0]) + (GamePanel.WIDTH*9/10-GamePanel.WIDTH/10)/10 - 20 + GamePanel.WIDTH*8/10 - GamePanel.WIDTH/10 - metric.stringWidth(options[0]);
		y2pos = ypos + metric.getHeight()*3/2;
		if(x > xpos && x < x2pos
				&& y > ypos && y < y2pos){
			nameFocused = true;
			currentChoice = 3;
		}else{
			nameFocused = false;
		}
		
		//Click on a dog
		for(int i = 0; i < 5; i++){
			for(int j = 0; j < 2; j++){
				if(x > xbound + i * (GamePanel.WIDTH/10 + 14) && x < xbound + i * (GamePanel.WIDTH/10 + 14) + x2bound
						&& y > ybound + j * (GamePanel.HEIGHT*2/10-10) && y < ybound + j * (GamePanel.HEIGHT*2/10-10) + x2bound){
					current = i + j*5;
					break;
				}
			}
		}
		
		//Click on options
		for(int i = 0; i < options.length; i++){
			if(x >= GamePanel.WIDTH/10 - 10 + i * (GamePanel.WIDTH * 9/10 - metric.stringWidth(options[0])/2) &&
					x <= GamePanel.WIDTH/10 - 10 + metric.stringWidth(options[0]) + i * (GamePanel.WIDTH * 9/10 - metric.stringWidth(options[0])/2) &&
					y >= GamePanel.HEIGHT * 9/10 - metric.getHeight() &&
					y <= GamePanel.HEIGHT * 9/10 + metric.getHeight()){
				currentChoice = i;
				select();
			}
		}
		
	}
	
	public void mousePressed(int x, int y){}
}
