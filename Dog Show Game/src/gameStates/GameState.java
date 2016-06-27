package gameStates;

import mapData.Background;
import playerData.Player;

public abstract class GameState {
	
	protected Player p;
	protected GameStateManager gm;
	protected Background bg;
	
	public abstract void init(Player p);
	public abstract void update();
	public abstract void draw(java.awt.Graphics2D g);
	public abstract void keyPressed(int k);
	public abstract void keyReleased(int k);
	public abstract void mouseReleased(int x, int y);
	public abstract void mousePressed(int x, int y);
	
}
