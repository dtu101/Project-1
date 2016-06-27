package gameStates;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.KeyEvent;

import main.GamePanel;
import mapData.Background;
import playerData.Player;

public class MenuState extends GameState {

	private Background bg;

	private int currentChoice;
	private int stage;
	private String[][] options = {
		{"Start", "Help", "Quit"},
		{"New Game", "Load Game", "Go Back"},
		{"Go Back", "OK"},
		{"Go Back", "OK"},
		{"Go Back"}
	};
	private String[] saves = new String[3];
	private int selectedSave;
	
	private boolean isCap;
	
	private Color titleColor;
	private Font titleFont;
	
	private Font font;
	private FontMetrics metrics;
	
	public MenuState(GameStateManager gm){
		this.gm = gm;
		
		try{
			bg = new Background("/Background/menubg.jpeg", 1);
			bg.setVector(-.5, 0);
			bg.setPosition(0, 0);
			
			titleColor = new Color(128, 0 , 0);
			titleFont = new Font("Century Gothic", Font.PLAIN, GamePanel.FONTSIZE*3/2);
			
			font = new Font("Arial", Font.PLAIN, GamePanel.FONTSIZE);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public void init(Player p) {
		stage = 0;
		
		currentChoice = 0;
		selectedSave = 3;
		
		isCap = false;
		
		long temp = System.nanoTime();
		System.out.println("LOADING PLAYER DATA");
		for(int i = 0; i < 3; i++){
			if(gm.loadPlayerData(i) != null)
				saves[i] = "Save " + i + ": " + gm.loadPlayerData(i).getName();
			else saves[i] = "Save " + i + ": EMPTY";
		}
		try{
			font = new Font("Arial", Font.PLAIN, GamePanel.FONTSIZE);
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("LOAD COMPLETE!!! Time: " + (System.nanoTime()-temp)/1000000 + "ms");
	}

	public void init(){
		currentChoice = 0;
		selectedSave = 3;
		
		isCap = false;
	}
	
	public void update() {
		bg.update();
	}

	
	public void draw(Graphics2D g) {
		metrics = g.getFontMetrics(font);
		g.setStroke(new BasicStroke(4f));
		
		//drawBG
		bg.draw(g);
		
		//draw title
		g.setColor(titleColor);
		g.setFont(titleFont);
		g.drawString("Dog Show", 
				GamePanel.WIDTH/2-g.getFontMetrics(titleFont).stringWidth("Dog Show")/2, 
				GamePanel.HEIGHT*1/4);
		
		//draw options
		g.setFont(font);
		for(int i = 0; i < options[stage].length; i++){
			//Select Color
			if(i == currentChoice){
				g.setColor(Color.BLACK);
			}else{
				g.setColor(Color.RED);
			}
			
			//Draw Options
			if(stage < 2){
				g.drawRect(GamePanel.WIDTH/2-g.getFontMetrics(font).stringWidth(options[stage][i])/2 - 10, 
						GamePanel.HEIGHT/2+i*GamePanel.HEIGHT/10 - metrics.getHeight(),
						metrics.stringWidth(options[stage][i])+ 20,
						metrics.getHeight()*3/2);
				g.drawString(options[stage][i],  
						GamePanel.WIDTH/2-g.getFontMetrics(font).stringWidth(options[stage][i])/2, 
						GamePanel.HEIGHT/2+i*GamePanel.HEIGHT/10);
			}
		}
		if(stage == 2 || stage == 3){
			for(int i = 0; i < 2; i++){
				if(currentChoice == i) g.setColor(Color.BLACK);
				else g.setColor(Color.RED);
				g.drawRect(GamePanel.WIDTH/10 - 10 + i * (GamePanel.WIDTH*9/10 - g.getFontMetrics(font).stringWidth(options[2][1])-GamePanel.WIDTH/10),
						GamePanel.HEIGHT*9/10 - metrics.getHeight(), 
						metrics.stringWidth(options[stage][i]) + 20, 
						metrics.getHeight()*3/2);
			}
			if(currentChoice == 0) g.setColor(Color.BLACK);
			else g.setColor(Color.RED);
			
			g.drawString(options[2][0], 
					GamePanel.WIDTH/10, 
					GamePanel.HEIGHT*9/10);
			
			if(currentChoice == 1) g.setColor(Color.BLACK);
			else g.setColor(Color.RED);
		
			g.drawString(options[2][1], 
					GamePanel.WIDTH*9/10 - g.getFontMetrics(font).stringWidth(options[2][1]),
					GamePanel.HEIGHT*9/10);
			
			g.setColor(Color.RED);
			
			//draw game slots
			for(int i = 0; i < 3; i ++){
				if(selectedSave == i && stage == 2) g.setColor(Color.BLACK);
				g.drawRect(GamePanel.WIDTH*3/10, GamePanel.HEIGHT/2 + i * GamePanel.HEIGHT/10 - metrics.getHeight(), GamePanel.WIDTH*2/5, metrics.getHeight()*3/2);
				if(saves[i] != null)
					g.drawString(saves[i], GamePanel.WIDTH*3/10+10, GamePanel.HEIGHT/2 + i * GamePanel.HEIGHT/10);
				g.setColor(Color.RED);
			}
		}
		
		if(stage == 4){
			g.setColor(Color.BLACK);
			
			g.drawString(options[3][0], 
					GamePanel.WIDTH/10,
					GamePanel.HEIGHT*9/10);
		}
	}

	public void type(char c){
		if(saves[selectedSave].length() != 8 && c == 8) saves[selectedSave] = saves[selectedSave].substring(0, saves[selectedSave].length()-1);
		if(saves[selectedSave].length() > 25) return;
		if(c == 10){
			currentChoice = 1;
			select();
			return;
		}
		if(c >= 32 && c <= 137){
			if(isCap) saves[selectedSave] += c;
			else saves[selectedSave] += Character.toLowerCase(c);
		}
	}
	
	/*
	 * Stage = 0
	 * 	Menu
	 * Stage = 1
	 * 	New/Load
	 * Stage = 2
	 * 	New
	 * Stage = 3
	 * 	Old
	 * Stage = 4
	 * 	Help
	*/
	private void select(){
		switch(stage){
			//Menu
			case 0: 
				switch(currentChoice){
				case 0: 
					stage = 1;
					break;
				case 1:
					stage = 4;
					currentChoice = 0;
					break;
				case 2: 
					System.exit(0);
					break;
				}
				break;
			// New-Load Game
			case 1: 
				switch(currentChoice){
				case 0:
					stage = 2;
					selectedSave = 1;
					break;
				case 1:
					stage = 3;
					currentChoice = 0;
					break;
				case 2:
					stage = 0;
					currentChoice = 0;
					break;
				}
				break;
			//New Game
			case 2:
				if(selectedSave!= 3) currentChoice = 1;
				switch(currentChoice){
				case 0:
					stage = 1;
					break;
				case 1:	
					initNewPlayer(saves[selectedSave].substring(8), selectedSave);
					gm.setState(GameStateManager.CHOOSEDOG, p);
					break;
				}
				break;
			//Load Game
			case 3:
				stage = 1;
				break;
			case 4:
				stage = 0;
				break;
		}
		init();
	}
	
	public void initNewPlayer(String s, int i){
		System.out.println("NEW PLAYER INITIALIZED");
		p = new Player(s, i);
	}
	
	public void keyPressed(int k) {
		
		if(k == KeyEvent.VK_SHIFT) isCap = true;
		if(stage == 2){
			type((char)k);
			return;
		}
		if(stage < 2){
			if(k==KeyEvent.VK_W || k==KeyEvent.VK_UP) currentChoice--;
			if(k==KeyEvent.VK_S || k==KeyEvent.VK_DOWN) currentChoice++;
		}else if(stage == 2 || stage == 3){
			if(k==KeyEvent.VK_A || k==KeyEvent.VK_LEFT) currentChoice--;
			if(k==KeyEvent.VK_D || k==KeyEvent.VK_RIGHT) currentChoice++;
		}
		if(currentChoice == -1) currentChoice = options[stage].length-1;
		if(currentChoice == options[stage].length) currentChoice = 0;
	}

	
	public void keyReleased(int k) {
		if(k == KeyEvent.VK_SHIFT)
			isCap = false;
	}
	
	public void mouseReleased(int x, int y){
		//Boundries for options
		for(int i = 0; i < options[stage].length; i++){
			if(stage == 0 || stage == 1){
				for(int j = 0; j < options[stage].length; j++){
					if(x >= GamePanel.WIDTH/2-metrics.stringWidth(options[stage][i])/2 - 10 &&
							x <= GamePanel.WIDTH/2-metrics.stringWidth(options[stage][i])/2 - 10 + metrics.stringWidth(options[stage][i] + 20) &&
							y >= GamePanel.HEIGHT/2 + j * GamePanel.HEIGHT/10 - metrics.getHeight() &&
							y <= GamePanel.HEIGHT/2 + j * GamePanel.HEIGHT/10 - metrics.getHeight() + metrics.getHeight()*3/2){
						currentChoice = j;
						select();
						return;
					}
				}
			}
			if(stage == 2 || stage == 3){
				for (int j = 0; j < 2; j++){
					if(x >= GamePanel.WIDTH/10 - 10 + j * (GamePanel.WIDTH*9/10 - metrics.stringWidth(options[stage][i])-GamePanel.WIDTH/10) && 
							x <= GamePanel.WIDTH/10 - 10 + j * (GamePanel.WIDTH*9/10 - metrics.stringWidth(options[stage][i])-GamePanel.WIDTH/10) + metrics.stringWidth(options[stage][i]) + 20 && 
							y >= GamePanel.HEIGHT*9/10 - metrics.getHeight() && 
							y <= GamePanel.HEIGHT*9/10 + metrics.getHeight() * 2){
						currentChoice = j;
						select();
						return;
					}
				}
			}else if(stage == 4){
				if(x >= GamePanel.WIDTH/10-10 &&
						x <= GamePanel.WIDTH/10 + metrics.stringWidth(options[stage][0]) + 10 &&
						y >= GamePanel.HEIGHT*9/10 - metrics.getHeight()*3/2 &&
						y <= GamePanel.HEIGHT*9/10 + metrics.getHeight()/2)
					select();
				return;
			}
		}
		//Boundries for loading
		if(stage == 2 || stage == 3){
			for(int i = 0; i < 3; i++){
				if(x >= GamePanel.WIDTH*3/10 && x <= GamePanel.WIDTH*3/10 + GamePanel.WIDTH*2/5 &&
						y >= GamePanel.HEIGHT/2 + i * GamePanel.HEIGHT/10 - metrics.getHeight() && y <= GamePanel.HEIGHT/2 + i * GamePanel.HEIGHT/10 - metrics.getHeight() + metrics.getHeight()*3/2){
					if(stage == 2){
						selectedSave = i;
						break;
					}
					if(stage == 3){
						p = null;
						if(gm.loadPlayerData(i) != null)
							p = gm.loadPlayerData(i);
						if(p != null){
							p.updatePlayer();
							gm.setState(GameStateManager.HOMESTATE, p);
						}else{
							System.out.println("NULL ERROR");
							break;
						}
					}
				}else selectedSave = 3;
			}
		}
	}
	
	public void mousePressed(int x, int y){}
	
	
}
