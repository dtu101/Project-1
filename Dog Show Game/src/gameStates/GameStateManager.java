package gameStates;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;

import animateHandlers.AnimationHandler;
import main.GamePanel;
import main.InitializerThread;
import playerData.Player;
import playerData.SaveData;

public class GameStateManager {

	private ArrayList<GameState> gameStates;
	
	private int currentState;
	
	private SaveData saveManager;

	public static final int MENUSTATE = 0;
	public static final int CHOOSEDOG = 1;
	public static final int HOMESTATE = 2;
	public static final int DOGMANAGEMENT = 3;
	public static final int SHOP = 4;
	public static final int DOGRUN = 5;
	public static final int LOADINGSTATE = 6;
	
	private boolean draw;
	private boolean updateGame;
	private boolean isLoading;
	
	private Runnable run;
	private Player p;
	
	private Font font;
	
	public GameStateManager(){
		gameStates = new ArrayList<GameState>();
		
		currentState = MENUSTATE;
		gameStates.add(new MenuState(this));
		gameStates.add(new DogSelectionState(this));
		gameStates.add(new HomeState(this));
		gameStates.add(new DogManagement(this));
		gameStates.add(new ShoppingState(this));
		gameStates.add(new DogRunState(this));
		gameStates.add(new LoadingState(this));
		
		saveManager = new SaveData();
		
		try{
			run = new InitializerThread(this);
			font = new Font("Arial", Font.BOLD, GamePanel.FONTSIZE/2);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void initGameState(){
		gameStates.get(currentState).init(p);
	}
	
	public void setState(int state, Player p){
		this.p = p;
		currentState = state;
		draw = false;
		updateGame = false;
		isLoading = true;
		
		Thread thread;
		try{
			
			thread = new Thread(run, "Init");
			thread.start();
		
		}catch(Exception e){
			e.printStackTrace();
		}
		if(currentState != DOGRUN && currentState != CHOOSEDOG){
			isLoading = false;
			startGame();
		}
	}
	
	public void update(){
		if(updateGame) gameStates.get(currentState).update();
		else gameStates.get(LOADINGSTATE).update();
	}
	
	public void draw(Graphics2D g){
		long tempTime = System.currentTimeMillis();
		if(draw) gameStates.get(currentState).draw(g);
		else {
			gameStates.get(LOADINGSTATE).draw(g);
			gameStates.get(LOADINGSTATE).init(null);
		}
		//FPS
		g.setColor(Color.BLACK);
		g.setFont(font);
		long x = System.currentTimeMillis()-tempTime;
		if(x != 0)g.drawString((1000/(System.currentTimeMillis()-tempTime)) + "fps", GamePanel.WIDTH-150, 75);
	}
	
	public void remove(AnimationHandler a){
		GamePanel.animations.remove(a);
	}
	
	public void drawAnimation(Graphics2D g){
		for(int i = 0; i < GamePanel.animations.size(); i++){
			GamePanel.animations.get(i).drawFrame(g);
		}
	}
	
	
	public void mouseReleased(int x, int y){
		if(updateGame) gameStates.get(currentState).mouseReleased(x, y);
	}
	
	public void mousePressed(int x, int y){
		if(updateGame) gameStates.get(currentState).mousePressed(x, y);
	}
	
	public void keyPressed(int k){
		if(updateGame) gameStates.get(currentState).keyPressed(k);
		else gameStates.get(LOADINGSTATE).keyPressed(k);
		
	}
	
	public void keyReleased(int k){
		if(updateGame) gameStates.get(currentState).keyReleased(k);
	}
	
	public void doneLoading(){
		isLoading = false;
	}
	
	public boolean stillLoading(){
		return isLoading;
	}
	
	public int getCurrentState(){
		return currentState;
	}
	
	public void startGame(){
		draw = true;
		updateGame = true;
	}
	
	public void savePlayerData(Player p){
		saveManager.saveGame(p);
	}
	
	public Player loadPlayerData(int i){
		Player p = null;
		
		p = saveManager.loadGame(i);
		if(p!=null)
			p.initAllDogs();
		else System.out.println("Player data is null");
		return p;
	}
}
