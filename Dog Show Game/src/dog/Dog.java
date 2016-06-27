package dog;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.Random;

import javax.imageio.ImageIO;

import xmlManager.XmlManager;

public class Dog implements Serializable{
	private String name;
	private String type;
	private int age;
	
	private String gender;
	
	private boolean firstTime;
	
	transient private BufferedImage portrait;
	transient private BufferedImage icon;
	transient private BufferedImage sprite;
	
	/*protected int stamina;
	protected int speed;
	protected int obedience;
	protected int beauty;
	protected int intelligence;*/
	private String[] statNames = {
			"Speed",
			"Beauty",
			"Intelligence",
			"Obedience",
			"Stamina",
			"Happiness",
			"Hunger"
	};
	private double[] stats = new double[statNames.length];
	
	private String[] tricks;
	
	transient private XmlManager dogDB;
	
	public Dog(String type){
		Random rand = new Random();
		gender = (Math.random()>=.5)? "Male" : "Female";
		age = rand.nextInt(20)+3;
		this.type = type;
		init();
	}
	
	public void init(){
		dogDB = new XmlManager("Resources/XML_Files/DogDatabase.xml");
		
		try{
			//Grab images
			String s = (String)dogDB.getElement(type, "portrait");
			//System.out.println(s);
			portrait = ImageIO.read(
					getClass().getResourceAsStream(s)
					);
			
			sprite = ImageIO.read(
					getClass().getResourceAsStream((String)dogDB.getElement(type, "Sprite"))
					);
			//Grab stats
			if(!firstTime)
				for(int i = 0; i < statNames.length; i++){
					stats[i] = Double.parseDouble(dogDB.getElement(type, statNames[i]));
				}
			firstTime  = true;
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void setName(String s){name = s;}
	public String getName(){return name;}
	public String getType(){return type;}
	
	public String getBio(String e){
		String s = "";
		
		s = dogDB.getElement(type, e);
		
		return s;
	}
	
	public String getGender(){return gender;}
	
	public void setStat(int s, double x){
		stats[s] += x;
		if(stats[s] < 0) stats[s] = 0;
	}
	public String[] getStatNames() {return statNames;}
	public double[] getStats(){return stats;}
	
	public void learnTrick(int x){}
	
	public int getAge(){return age;}
	
	public Image getPortrait(){return portrait;}
	public Image getIcon(){return icon;}
	public Image getSprite(){return sprite;}
}
