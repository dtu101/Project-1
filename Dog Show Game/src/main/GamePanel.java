package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

import animateHandlers.AnimationHandler;
import gameStates.GameStateManager;

public class GamePanel extends JPanel implements Runnable, KeyListener, MouseListener {
	
	//dimensions
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 1280;
	public static final int SCALE = 1;
	public static final int FONTSIZE = 48;
	
	//game thread
	private Thread thread;
	private boolean running;
	private int FPS = 60;
	private long targetTime = 1000/FPS;
	
	private int fps;
	private int frames;
	private int updateTime;
	private int drawTime;
	private int animationTime;
	private int screenTime;
	
	long start;
	long elapsed;
	long wait;
	
	//image
	private BufferedImage image;
	private Graphics2D g;
	
	//gameState manager
	private GameStateManager game;
	
	///Animation Threads
	public static ArrayList<AnimationHandler> animations;
	
	public GamePanel(){
		super();
		animations = new ArrayList<AnimationHandler>();
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setFocusable(true);
		requestFocus();
	}
	
	public void addNotify(){
		super.addNotify();
		if(thread == null){
			thread = new Thread(this);
			addKeyListener(this);
			addMouseListener(this);
			thread.start();
		}
	}
	
	private void init(){
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
		running = true;
		
		frames = 0;
		
		game = new GameStateManager();
		game.setState(0, null);
	}
	
	public void run(){
		init();
		
		//game loop
		while(running){
			
			start = System.nanoTime();
			
			update();
			if(frames == 9)
				updateTime = (int) (System.nanoTime() - start);
			
			draw();
			if(frames == 9)
				drawTime = (int) (System.nanoTime()-start-updateTime);
			
			//drawAnimation();
			if(frames == 9)
				animationTime = (int) (System.nanoTime()-start-drawTime);
			
			drawToScreen();
			if(frames == 9)
				screenTime = (int)(System.nanoTime()-start-animationTime);
			elapsed = System.nanoTime() - start;
			
			wait = targetTime - elapsed/1000000;
			
			if(wait < 0)
				wait = 0;
			
			try{
				Thread.sleep(wait);
			}catch(Exception e){
				e.printStackTrace();
			}
			
			updateFPS();
		}
	}
	
	private void updateFPS(){
		frames ++;
		if(frames >= 10){
			fps = (int)(1000/(elapsed/1000000 + wait));
			frames = 0;
		}
	}
	
	private synchronized void update(){ game.update();}
	private synchronized void draw(){game.draw(g);}
	private synchronized void drawAnimation(){game.drawAnimation(g);}
	private synchronized void drawToScreen(){
		Graphics g2 = getGraphics();
		g2.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
		
		/*//draw FPS
		g2.setFont(new Font("Arial", Font.BOLD, 18));
		g2.setColor(Color.WHITE);
		g2.fillRect(GamePanel.WIDTH-125, 0, 125, 125);
		g2.setColor((fps < 60)?Color.RED:Color.BLACK);
		g2.drawString("FPS: " + fps, GamePanel.WIDTH*SCALE-100, 50);
		g2.setColor((updateTime/1000000 > 16)?Color.RED:Color.BLACK);
		g2.drawString("UD: " + updateTime/1000000, GamePanel.WIDTH*SCALE-100, 65);
		g2.setColor((drawTime/1000000 > 16)?Color.RED:Color.BLACK);
		g2.drawString("DT: " + drawTime/1000000, GamePanel.WIDTH*SCALE-100, 80);
		g2.setColor((animationTime/1000000 > 16)?Color.RED:Color.BLACK);
		g2.drawString("DA: " + animationTime/1000000, GamePanel.WIDTH*SCALE-100, 95);
		g2.setColor((screenTime/1000000 > 16)?Color.RED:Color.BLACK);
		g2.drawString("DTS: " + screenTime/1000000, GamePanel.WIDTH*SCALE-100, 110);*/
			
		g2.dispose();
	}

	public void mouseClicked(MouseEvent m) {
		
		
	}

	public void mouseEntered(MouseEvent m) {
		
		
	}

	public void mouseExited(MouseEvent m) {
		
		
	}

	public void mousePressed(MouseEvent m) {
		game.mousePressed(m.getX() / GamePanel.SCALE, m.getY() / GamePanel.SCALE);
	}

	public void mouseReleased(MouseEvent m) {
		game.mouseReleased(m.getX() / GamePanel.SCALE, m.getY() / GamePanel.SCALE);
	}

	public void keyPressed(KeyEvent k) {
		game.keyPressed(k.getKeyCode());
	}

	public void keyReleased(KeyEvent k) {
		
		game.keyReleased(k.getKeyCode());
	}

	public void keyTyped(KeyEvent k) {
		
		
	}
}
