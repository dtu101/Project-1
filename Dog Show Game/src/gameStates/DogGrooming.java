package gameStates;

import java.awt.Font;
import java.awt.FontMetrics;

import mapData.Background;
import playerData.Player;

public class DogGrooming extends GameState{

	private Font font;
	private FontMetrics metrics;
	
	public DogGrooming(GameStateManager gm){
		this.gm = gm;
		try{
			bg = new Background("/Background/menubg.jpeg", 1);
			bg.setPosition(0, 0);
			
			font = new Font("Arial", Font.PLAIN, 12);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void init(Player p){
		this.p = p;
		try{
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void update(){}
	public void draw(java.awt.Graphics2D g){}
	public void keyPressed(int k){}
	public void keyReleased(int k){}
	public void mouseReleased(int x, int y){}
	public void mousePressed(int x, int y){}
	
}
