package animateHandlers;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import main.GamePanel;

public class AnimateFadingText extends Animation implements Runnable{
	private Graphics2D g;
	private float opacity = 1f;
	
	private Font font = new Font("Arial", Font.PLAIN, GamePanel.FONTSIZE/2);

	private Color color;
	
	private String s;
	
	private Thread t;
	private BufferedImage image;
	
	private final long target = 1000/60;
	
	public AnimateFadingText(AnimationHandler a, String s, double x, double y, Color c){
		animationHandler = a;
		this.x = x;
		this.y = y;
		this.s = s;
		
		color = c;
		t = new Thread(this, "Animate Fading Text");
		t.start();
	}
	
	public void run(){
		while(opacity > 0){
			long start = System.nanoTime();
			long elapsedTime;
			long wait;
			
			BufferedImage bi = new BufferedImage(GamePanel.WIDTH, GamePanel.HEIGHT, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = bi.createGraphics();
			
			
			g2.setFont(font);
			g2.setColor(color);
			
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
			g2.drawString(s, (int)x, (int)y);
			g2.dispose();
			if(bi != null)
				image = bi;
			
			opacity -= .02;
			y -= 3;
			
			elapsedTime = System.nanoTime()-start;
			wait = target - elapsedTime/1000000;
			if(wait < 0) wait = 5;
			try{
				Thread.sleep(wait);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		finished();
	}
	
	public void finished(){
		animationHandler.removeAnimation(this);
	}

	public Image getImage(){ return image;}
	
}
