package gameStates;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

import animateHandlers.AnimationHandler;
import main.GamePanel;
import mapData.Background;
import playerData.Item;
import playerData.Player;
import xmlManager.XmlManager;

public class DogRunState extends GameState implements Runnable{
	
	private long startTime;
	private long elapsedTime;
	
	private long begin;
	private long passed;
	private long wait;
	
	private boolean running;
	
	private Font font;
	private FontMetrics metrics;
	
	private boolean[] moveDirection = new boolean[4];
	private AnimationHandler animationHandler;
	
	private double mX;
	private double mY;
	private double x;
	private double y;
	private double dx;
	private double dy;
	private double ax;
	private double ay;
	
	private ArrayList<Item> items;
	private XmlManager itemDB;
	private int[] spawnableItems;
	
	Random rand = new Random();
	
	private int w;
	private int h;
	
	private double speed;
	private double hunger;
	private double numberHit;
	
	private final int gameTime = 20;
	private boolean start;
	
	private Image dog;
	
	private Thread t;
	
	public DogRunState(GameStateManager gm){
		this.gm = gm;
		itemDB = new XmlManager("Resources/XML_Files/InventoryDatabase.xml");
		running = false;
		
		t = new Thread(this, "DogRun");
		
		try{
			bg = new Background("/Background/DogRunBG.png", 1);
			bg.setVector(-4, 0);
			bg.setPosition(0, 0);
			bg.setSize(GamePanel.HEIGHT);
			
			font = new Font("Arial", Font.PLAIN, 12);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void init(Player p){
		this.p = p;
		speed = .15;
		hunger = -15;
		items = new ArrayList<Item>();
		w = GamePanel.HEIGHT/10;
		h = GamePanel.HEIGHT/10;
		
		x = GamePanel.WIDTH/10;
		y = GamePanel.HEIGHT/2 -w/2;
		mX = 2  + (.1) * (p.getDogStats(p.getCurrentDog())[Player.SPEED]-5) + p.getDogStats(p.getCurrentDog())[Player.STAMINA]/5;
		mY = 2  + (.25) * (p.getDogStats(p.getCurrentDog())[Player.SPEED]-5) + p.getDogStats(p.getCurrentDog())[Player.STAMINA]/8;
		ax = p.getDogStats(p.getCurrentDog())[Player.SPEED]/15;
		ay = p.getDogStats(p.getCurrentDog())[Player.SPEED]/20;
		dx = 0;
		dy = 0;
		
		animationHandler = new AnimationHandler(gm);
		
		wait = 0;
		
		for(int i = 0; i < 4; i++)
			moveDirection[i] = false;
		start = false;
		
		spawnableItems = p.getAllOfElement("spawnable");
		
		while(items.size() < 10){
			spawnItem();
		}
		
		
		try{
			if(p.getDogSprite(p.getCurrentDog()) != null)
				dog = p.getDogSprite(p.getCurrentDog());
			else System.out.println("NO DOG SPRITE");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void run(){
		while(running){
			begin = System.nanoTime();
			for(int i = 0; i < items.size(); i++){
				if(new Rectangle((int)x+5,(int)y+5,w-5,h-5).intersects(new Rectangle((int)(GamePanel.WIDTH - items.get(i).getX()),(int)items.get(i).getY(),w/2,h/2)))
					checkCollision(items.get(i), i);
			}
			passed = System.nanoTime() - begin;
			wait = (1000/60 - passed/1000 > 0)? 1000/60 - passed/1000: 0;
			try{
				Thread.sleep(wait);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	
	public void start(){
		numberHit = 0;
		t = new Thread(this, "DogRun");
		t.start();
		running = true;
		start = true;
	}
	
	public void update(){
		if(!start){
			start();
			startTime = System.nanoTime();
		}
		if(running){
			bg.update();
			move();
			elapsedTime = (System.nanoTime()-startTime)/1000000000;
			
			if(elapsedTime >= gameTime) finish();
		}
	}
	public void draw(java.awt.Graphics2D g){
		metrics = g.getFontMetrics(font);
		
		
		//DrawBg
		bg.draw(g);
		
		if(running){
			//Draw dog
			g.drawImage(dog, (int)x, (int)y, w, h, null);
			
			//drawTime
			g.setColor(Color.WHITE);
			g.fillRect(GamePanel.WIDTH/2 - GamePanel.WIDTH/10, GamePanel.HEIGHT/10, GamePanel.WIDTH/5, metrics.getHeight()*3/2);
			g.setColor(Color.BLACK);
			g.drawString("" + elapsedTime, GamePanel.WIDTH/2 - metrics.stringWidth("" + elapsedTime)/2, GamePanel.HEIGHT/10 + metrics.getHeight());
			
			//Draw items
			if(items.size() > 0){
				for(int i = 0; i < items.size(); i++)
					if(items.get(i).getX() > 0)
						g.drawImage(items.get(i).getSprite(), GamePanel.WIDTH - (int)items.get(i).getX(), (int)items.get(i).getY(), w*3/5, h*3/5, null);
			}
			
			//Draw animations
			animationHandler.drawFrame(g);
			
		}else{
			g.setColor(Color.WHITE);
			g.fillRect(GamePanel.WIDTH/10, GamePanel.HEIGHT/10, GamePanel.WIDTH*8/10, GamePanel.HEIGHT*8/10);
			g.setColor(Color.BLACK);
			g.drawString("Your dog is now tired!", GamePanel.WIDTH/2 - metrics.stringWidth("Your dog is now tired!"), GamePanel.HEIGHT/2);
			//Obstacles HIT
			g.drawString("Your dog hit: " + numberHit + " obstacles!!!" , GamePanel.WIDTH/2 - metrics.stringWidth("Your dog hit: " + numberHit + " obstacles!!!"), GamePanel.HEIGHT/2 + metrics.getHeight()*3/2);
			//Speed gained
			if(speed < 0)
				g.drawString("Your dog lost " + -((int)(speed*100.0))/100.0 + " speed", GamePanel.WIDTH/2 - metrics.stringWidth("Your dog lost " + -((int)(speed*100.0))/100.0 + " speed"), GamePanel.HEIGHT/2 + metrics.getHeight()*3);
			if(speed > 0)
				g.drawString("Your dog gained " + ((int)(speed*100.0))/100.0 + " speed", GamePanel.WIDTH/2 - metrics.stringWidth("Your dog lost " + ((int)(speed*100.0))/100.0 + " speed"), GamePanel.HEIGHT/2 + metrics.getHeight()*3);
			g.drawString("Hit enter to continue home", GamePanel.WIDTH/2 - metrics.stringWidth("Hit enter to continue home"), GamePanel.HEIGHT/2 + metrics.getHeight()*9/2);
		}
	}
	
	public void spawnItem(){
		int randID = rand.nextInt(spawnableItems.length);
		int randomW = -(1+rand.nextInt(GamePanel.WIDTH*3));
		double randomH = (rand.nextDouble()*(GamePanel.HEIGHT*4/5))+GamePanel.HEIGHT/10+50;
		double chance = rand.nextDouble();
		
		if(chance < .3){
			while(!itemDB.getElementByAttribute("type", "" + spawnableItems[randID]).equalsIgnoreCase("food")){
				randID = rand.nextInt(spawnableItems.length);
			}
			if(rand.nextDouble() > .3 * Double.parseDouble(itemDB.getElementByAttribute("spawn_rate", "" + spawnableItems[randID]))) return;
		}else if(chance < .7){
			while(!itemDB.getElementByAttribute("type", "" + spawnableItems[randID]).equalsIgnoreCase("obstacle")){
				randID = rand.nextInt(spawnableItems.length);
			}
			if(rand.nextDouble() > .7 * Double.parseDouble(itemDB.getElementByAttribute("spawn_rate", "" + spawnableItems[randID]))) return;
		}
		
		/*
		if(itemDB.getElementByAttribute("type", "" + spawnableItems[randID]).equalsIgnoreCase("food"))
			spawnChance = .3;
		else if(itemDB.getElementByAttribute("type", "" + spawnableItems[randID]).equalsIgnoreCase("obstacle"))
			spawnChance = .7;
		else spawnChance = 0;
		
		if(chance > chance * Double.parseDouble(itemDB.getElementByAttribute("spawn_rate", "" + spawnableItems[randID])))
			return;
		*/
		
		items.add(new Item("" + (spawnableItems[randID]), randomW, (int)randomH));
		System.out.println("Spawned!!! ID: " + items.get(items.size()-1).getID() + " at X: " + randomW);
	}
	
	public void addAnimation(int type, String text, double x, double y, Color c){
		animationHandler.setPos(x, y);
		animationHandler.setColor(c);
		animationHandler.setText(text);
		animationHandler.addAnimation(type);
	}
	
	public synchronized void checkCollision(Item item, int i){
		try{
			if(item.getInfo().equalsIgnoreCase("food")){
				p.addItemToInventory(item.getID());
				addAnimation(AnimationHandler.FADINGTEXT, "+1 " + item.getItemName(), x, y, Color.WHITE);
				System.out.println("GOT FOOD");
			}else if(item.getInfo().equalsIgnoreCase("obstacle")){
				speed -= .05;
				hunger -= 15;
				addAnimation(AnimationHandler.FADINGTEXT, "-Speed!!!", x, y, Color.RED);
				addAnimation(AnimationHandler.FADINGTEXT, "-15 Hunger!!!", x, y-metrics.getHeight()*3/2, Color.RED);
				numberHit++;
				System.out.println("GOT HIT");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		items.remove(i);
	}
	
	public void finish(){
		p.setDogStat(p.getCurrentDog(), Player.SPEED, speed);
		p.setDogStat(p.getCurrentDog(), Player.STAMINA, speed*3/5);
		p.setDogStat(p.getCurrentDog(), Player.HUNGER, hunger);
		gm.savePlayerData(p);
		running = false;
		animationHandler.finished();
		//gm.setState(GameStateManager.HOMESTATE, p);
		try {
			t.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//A - 0
	//W - 1
	//S - 2
	//D - 3
	
	public void move(){
		//Movement of items
		for(Item item: items)
			item.setPos(item.getX()+4, item.getY());
		
		//ACCELERATION WHILE MOVING
		//Left
		if(x >= GamePanel.WIDTH/10)
			if(moveDirection[0])
				dx -= ax;
		//Up
		if(y + h >= GamePanel.HEIGHT/5)
			if(moveDirection[1])
				dy -= ay;
		//Down
		if(y  <= GamePanel.HEIGHT*9/10 - h)
			if(moveDirection[2])
				dy += ay;
		//Right
		if(x <= GamePanel.WIDTH*9/10 - w)
			if(moveDirection[3])
				dx += ax;

		//ACCELERATIONS
				if(moveDirection[0]){
					if (x < GamePanel.WIDTH*9/10){
						if(dx > -1.5*mX) dx -= ax;
						if(dx < -1.5*mX) dx = -1.5*mX;
					}
				}
				if(moveDirection[3]){
					if (x < GamePanel.WIDTH*9/10){
						if(dx < mX) dx += ax;
					}
				}		

		//DECELERATIONS
		if((!moveDirection[0] && !moveDirection[3])){
			if(x > GamePanel.WIDTH/10){
				if(dx > -mX) dx -= ax;
				if(dx < -mX) dx += ax;
			}		
		}
		
		if(!moveDirection[1] && !moveDirection[2]){
			if(dy > 0) dy -= ay/2;
			else dy += ay/2;
		}
		
		//Fix boundries
		if(x > GamePanel.WIDTH*9/10){
			x = GamePanel.WIDTH*9/10;
			dx = 0;
		}
		if(x < GamePanel.WIDTH/10){
			x = GamePanel.WIDTH/10;
			dx = 0;
		}
		if(y > GamePanel.HEIGHT*9/10){
			y = GamePanel.HEIGHT*9/10;
			dy = 0;
		}
		if(y < GamePanel.HEIGHT/5 - h + 15){
			y = GamePanel.HEIGHT/5 - h + 15;
			dy = 0;
		}
			
		
		//VELOCITIES
		if(dx > mX) dx = mX;
		//if(dx < -mX) dx = -mX;
		if(dy > mY) dy = mY;
		if(dy < -mY) dy = -mY;
		
		x += dx;
		y += dy;
	}
	
	public void keyPressed(int k){
		if(k == KeyEvent.VK_A) moveDirection[0] = true;
		if(k == KeyEvent.VK_W) moveDirection[1] = true;
		if(k == KeyEvent.VK_S) moveDirection[2] = true;
		if(k == KeyEvent.VK_D) moveDirection[3] = true;
	}
	public void keyReleased(int k){
		if(k == KeyEvent.VK_A) moveDirection[0] = false;
		if(k == KeyEvent.VK_W) moveDirection[1] = false;
		if(k == KeyEvent.VK_S) moveDirection[2] = false;
		if(k == KeyEvent.VK_D) moveDirection[3] = false;
		if(k == KeyEvent.VK_ENTER && !running) gm.setState(GameStateManager.HOMESTATE, p);
	}
	public void mouseReleased(int x, int y){}
	public void mousePressed(int x, int y){}
}
