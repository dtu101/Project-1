package mapData;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import main.GamePanel;

public class Background {

	private BufferedImage image;
	
	private double x;
	private double y;
	private double dx;
	private double dy;
	
	private int w;
	private int h;
	
	private double moveScale;
	private int maxNumberAcross;
	private int maxNumberVertical;
	
	
	public Background(String s, double ms){
		try{
			image = ImageIO.read(
					getClass().getResourceAsStream(s)
					);
			w = image.getWidth();
			h = image.getHeight();
			maxNumberAcross = (GamePanel.WIDTH*GamePanel.SCALE)/w + 1;
			maxNumberVertical = (GamePanel.HEIGHT*GamePanel.SCALE)/h + 1;
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void update(){
		x += dx;
		y += dy;
	}
	
	public void setPosition(double x, double y){
		this.x = (x*moveScale)%GamePanel.WIDTH;
		this.y = (y*moveScale)%GamePanel.HEIGHT;
	}
	
	public void setVector(double dx, double dy){
		this.dx = dx;
		this.dy = dy;
	}
	
	public void setSize(int h){
		this.h = h;
	}
	
	public void draw(Graphics2D g){
		
		for(int i = 0; i <= maxNumberAcross; i++){
			for(int j = 0; j <= maxNumberVertical; j++){
				double fy = y;
				double fx = x;
				
				
				//Generate all y values
				if((fy - (j*h)) < 0) fy += j * h;
				else if((fy + (j*h)) > 0) fy = j * h;
				if(y >= h || y <= -h) y = 0;
				
				//Generate all x values
				if((fx - (i*w)) < 0) fx += i * w;
				else if((fx + (i*w)) > 0) fx -= i * w;
				if(x >= image.getWidth() || x <= -image.getWidth()) x = 0;
				
				g.drawImage(image, (int)fx, (int)fy, w, h, null);
			}
		}
		
		/*//Draw all pictures
		for(int i = 0; i < maxNumberAcross*maxNumberVertical; i++){
			g.drawImage(image, (int)(x+((i/maxNumberVertical)*w)), (int)(y+((i/maxNumberVertical)*h)), w, h, null);
			System.out.println("X: " + (int)x+((x/maxNumberVertical)*w) + " Y: " + (int)((y/maxNumberAcross)*h));
		}*/
	}
	
}
