package playerData;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SaveData {

	private Player p;
	
	public SaveData(){}
	
	public synchronized void saveGame(Player p){
		FileOutputStream fs = null;
		ObjectOutputStream out = null;
		
		try{
			fs = new FileOutputStream("Resources/SaveData/player" + p.getFileNumber() + ".sav", false);
			out = new ObjectOutputStream(fs);
			out.writeObject(p);
			out.close();
			System.out.println("Save Success");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public synchronized Player loadGame(int i){
		Player f = null;
		try{
			if(!new File("Resources/SaveData/player" + i + ".sav").exists())
				return null;
			FileInputStream file = new FileInputStream("Resources/SaveData/player" + i + ".sav");
			ObjectInputStream in = new ObjectInputStream(file);
			Object obj = in.readObject();
			if(obj instanceof Player){
				f = (Player)obj;
			}
			in.close();
			file.close();
			return f;
		}catch(Exception e){
			e.printStackTrace();
		}
		return f;
	}
	
}
