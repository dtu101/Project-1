package playerData;

import java.awt.Image;
import java.io.Serializable;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import dog.Dog;
import xmlManager.XmlManager;

public class Player implements Serializable{
	private String name;
	private int fileNumber;
	
	private int gold;
	
	private ArrayList<Dog> dogsOwned;
	private int currentDog;
	
	private String[] achievements;
	private String[] trophies;
	
	private ArrayList<Integer> inventory;
	
	public static final int SPEED = 0;
	public static final int BEAUTY = 1;
	public static final int INTELLIGENCE = 2;
	public static final int OBEDIENCE = 3;
	public static final int STAMINA = 4;
	public static final int HAPPINESS = 5;
	public static final int HUNGER = 6;
	
	transient private XmlManager inventoryDB;
	
	public Player(String name, int i){
		inventoryDB = new XmlManager("Resources/XML_Files/InventoryDatabase.xml");
		dogsOwned = new ArrayList<Dog>();
		currentDog = 0;
		this.name = name;
		fileNumber = i;
		inventory = new ArrayList<Integer>();
		
		//TEMPORARY**************88
		inventory.add(1);
		inventory.add(2);
		inventory.add(3);
		
		System.out.println("INVENTORY SIZE: " + inventory.size());
	}
	
	public void updatePlayer(){
		inventoryDB = new XmlManager("Resources/XML_Files/InventoryDatabase.xml");
		initAllDogs();
	}
	
	public String getName(){
		return name;
	}
	
	public int getFileNumber(){ return fileNumber;}
	
	public void setCurrentDog(int i){currentDog = i;}
	public int getCurrentDog(){return currentDog;}
	
	public void addDog(Dog dog){
		dogsOwned.add(dog);
	}
	
	public String[] getDogStatNames(int i){
		return dogsOwned.get(i).getStatNames();
	}
	
	public double[] getDogStats(int i){
		return dogsOwned.get(i).getStats();
	}
	
	public void setDogStat(int i, int s, double x){
		dogsOwned.get(i).setStat(s, x);
	}
	
	public int getInventorySize(){
		return inventory.size();
	}
	
	public String getInventoryItemName(int i){
		String element = "";
		if(i < inventory.size() && i >= 0){
			element = inventoryDB.getElementByAttribute("name", inventory.get(i) + "");
			//System.out.println(element);
		}else System.out.println("ERROR: INVENTORY NUMBER: " + i + " INVENTORY SIZE: " + inventory.size());
		return element;
	}
	
	public String getInventoryItemInfo(String info, int i){
		String element = "";
		if(i < inventory.size() && i >= 0){
			element = inventoryDB.getElementByAttribute(info, "" + inventory.get(i));
			//System.out.println(info + ": " + element);
		}
		return element;
	}
	
	public Image getItemIcon(int id){
		Image image = null;
		try{
			image = ImageIO.read(getClass().getResourceAsStream(inventoryDB.getElementByAttribute("icon", "" + inventory.get(id))));
		}catch(Exception e){
			e.printStackTrace();
		}
		return image; 
	}
	
	public void addItemToInventory(int id){
		inventory.add(id);
	}
	
	public int[] getAllOfElement(String element){
		return inventoryDB.grabAllOf(element);
	}
	
	public int getNumberOfElements(String element){
		int x = 0;
		x = inventoryDB.getNumberOfElements(element);
		return x;
	}
	
	public void feedDog(int selection){
		dogsOwned.get(currentDog).setStat(Player.HUNGER, Double.parseDouble(getInventoryItemInfo("hunger_value", selection)));
		if(dogsOwned.get(currentDog).getStats()[Player.HUNGER]>200) dogsOwned.get(currentDog).setStat(HUNGER, -dogsOwned.get(currentDog).getStats()[Player.HUNGER]%200);
		inventory.remove(selection);
	}
	
	public Image getDogPortrait(int i){
		if(i < dogsOwned.size())
			return dogsOwned.get(i).getPortrait();
		System.out.println("NO DOGS");
		return null;
	}
	
	public Image getDogSprite(int i){
		return dogsOwned.get(i).getSprite();
	}
	
	public String getDogName(int i){
		if(i < dogsOwned.size())
			//return dogsOwned.size() + "";
			return dogsOwned.get(i).getName();
		return "NOT FOUND";
	}
	
	public String getDogBio(int i, String e){
		if(i < dogsOwned.size())
			return dogsOwned.get(i).getBio(e);
		return "NOT FOUND";
	}
	
	public String getDogGender(int i){
		if(i < dogsOwned.size())
			return dogsOwned.get(i).getGender();
		return "NOT FOUND";
	}
	
	public int getDogAge(int i){
		if(i < dogsOwned.size())
			return dogsOwned.get(i).getAge();
		return 0;
	}
	
	public void initAllDogs(){
		for(int i = 0; i < dogsOwned.size(); i++)
			dogsOwned.get(i).init();
	}
}
