package animateHandlers;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import gameStates.GameStateManager;

public class AnimationHandler implements Runnable{
	
	public static final int FADINGTEXT = 0;
	
	private GameStateManager gm;
	
	private ArrayList<Image> currentFrame = new ArrayList<Image>();
	private String text;
	
	private int animationType;
	
	private Color color;
	
	private ArrayList<Animation> animations = new ArrayList<Animation>();
	private Thread t;
	
	private boolean animate;
	private boolean running;
	
	private double x;
	private double y;
	private int w;
	private int h;
	
	public AnimationHandler(GameStateManager gm){
		this.gm = gm;
		color = Color.WHITE;
		animate = false;
		running = true;
		t = new Thread(this, "Animation Handler");
		t.start();
	}
	
	public void setPos(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	public void setSize(int w, int h){
		this.w = w;
		this.h = h;
	}
	
	public void setColor(Color c){ color = c;}
	
	public void setText(String text){ this.text = text;}
	
	public synchronized void newFrame(BufferedImage bi){ currentFrame.add(bi);}
	
	public void finished(){
		running = false;
		try {
			t.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void removeAnimation(Animation a){ animations.remove(a);}
	
	public void drawFrame(Graphics2D g){
		for(int i = 0; i < animations.size(); i++){
			g.drawImage(animations.get(i).getImage(), 0, 0, null);
		}
		
	}
	
	public synchronized void addAnimation(int animationType){
		this.animationType = animationType;
		animate = true;
		System.out.println("NUMBER OF ANIMATIONS: " + animations.size()+1);
	}
	
	public void run(){
		while(running){
			long start = System.nanoTime();
			try{
				if(animate){
					switch(animationType){
					case FADINGTEXT:
						animations.add(new AnimateFadingText(this, text, x, y, color));
						System.out.println("NEW ANIMATION: FADING TEXT AT X: " + x + " Y: " + y);
						animate = false;
						break;
					}
				}
				long wait = (System.nanoTime()-start);
				if(wait/1000000 < 0) wait = 0;
					Thread.sleep(wait/1000000);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
}
