package playerData;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.Serializable;

import javax.imageio.ImageIO;

import xmlManager.XmlManager;

public class Item implements Serializable{
	
	private double x;
	private double y;
	
	transient private BufferedImage icon;
	transient private BufferedImage sprite;
	
	transient private XmlManager itemDB;
	
	private String name;
	private int goldValue;
	private String id;
	
	public Item(String id){
		
		this.id = id;
		
		try{
			itemDB = new XmlManager("Resources/XML_Files/InventoryDatabase.xml");
			
			//Grab images
			icon = (ImageIO.read(
					getClass().getResourceAsStream(itemDB.getElementByAttribute("icon", id))) != null)
					? ImageIO.read(getClass().getResourceAsStream(itemDB.getElementByAttribute("icon", id)))
					: null;
			
			sprite = (ImageIO.read(
					getClass().getResourceAsStream(itemDB.getElementByAttribute("sprite", id))) != null) 
					? ImageIO.read(getClass().getResourceAsStream(itemDB.getElementByAttribute("sprite", id))) 
					: null;
					
			name = itemDB.getElementByAttribute("name", id);
			//goldValue = Integer.parseInt(itemDB.getElementByAttribute("gold_value", id));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public Item(String id, double x, double y){
		this.id = id;
		setPos(x, y);
		try{
			
			itemDB = new XmlManager("Resources/XML_Files/InventoryDatabase.xml");
			
			//Grab images
			if((ImageIO.read(
					getClass().getResourceAsStream(itemDB.getElementByAttribute("icon", id))) != null))
				icon = ImageIO.read(getClass().getResourceAsStream(itemDB.getElementByAttribute("icon", id)));
			else System.out.print("ITEM ICON ERROR!!! ID: " + id);
			
			if(ImageIO.read(
					getClass().getResourceAsStream(itemDB.getElementByAttribute("sprite", id))) != null)
				sprite = ImageIO.read(getClass().getResourceAsStream(itemDB.getElementByAttribute("sprite", id)));
			else System.out.println("ITEM SPRITE ERROR!!! ID: " + id);
			
					
			name = itemDB.getElementByAttribute("name", id);
			//goldValue = Integer.parseInt(itemDB.getElementByAttribute("gold_value", id));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void setPos(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	public double getX(){ return x;}
	public double getY(){ return y;}
	
	public Image getIcon(){ return icon;}
	public Image getSprite(){ return sprite;}
	public int getGoldValue(){ return goldValue;}
	public String getItemName(){ return name;}
	public String getInfo(){ return (id!=null)?itemDB.getElementByAttribute("type", id):"NULL";}
	public int getID() {return Integer.parseInt(id);}
}
