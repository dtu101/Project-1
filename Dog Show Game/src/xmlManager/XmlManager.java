package xmlManager;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XmlManager{
	
	public static final int TOTALITEMS = 3;
	
	private String filePath;
	
	private String s;
	private int count;
	
	public XmlManager(String s){
		filePath = s;
	}
	
	public String getElement(String name, String element){
		
		s = "";
		
		try{
			SAXParserFactory f = SAXParserFactory.newInstance();
			SAXParser saxParser = f.newSAXParser();
			
			DefaultHandler handler = new DefaultHandler(){
				
				boolean foundDog = false;
				boolean foundElement = false;
				
				public void startElement(String uri, String localName,String qName, 
		                Attributes attributes) throws SAXException{
					
					if(qName.equalsIgnoreCase(element) && foundDog){
						foundElement = true;
					}
					if(qName.equalsIgnoreCase(name)){
						foundDog = true;
					}
				}
				
				public void characters(char ch[], int start, int length) throws SAXException{
					if(foundDog && foundElement){
						s = new String(ch, start, length);
						foundDog = false;
					}
				}
			};
			
			saxParser.parse(filePath, handler);
			
		}catch (Exception e){
			e.printStackTrace();
		}
		if(s.equalsIgnoreCase("")) System.out.println("XML OUTPUT ERROR");
		
		return s;
	}
	
	public synchronized String getElementByAttribute(String element, String id){
		
		s = "";
		
		try{
			SAXParserFactory f = SAXParserFactory.newInstance();
			SAXParser saxParser = f.newSAXParser();
			
			DefaultHandler handler = new DefaultHandler(){
				
				boolean foundItem = false;
				boolean foundElement = false;
				
				public void startElement(String uri, String localName,String qName, 
		                Attributes attributes) throws SAXException{
					
					if(qName.equalsIgnoreCase(element) && foundItem){
						foundElement = true;
					}
					if(attributes.getValue("id") == null) return;
					else if (attributes.getValue("id").equalsIgnoreCase(id)){
						foundItem = true;
					}
				}
				
				public void characters(char ch[], int start, int length) throws SAXException{
					if(foundItem && foundElement){
						s = new String(ch, start, length);
						foundItem = false;
					}
				}
			};
			
			saxParser.parse(filePath, handler);
			
		}catch (Exception e){
			e.printStackTrace();
		}
		
		if(s.equalsIgnoreCase("")) System.out.println("XML CANNOT FIND!!! ID: " + id + " Element: " + element);
		
		return s;
	}
	

	public int[] grabAllOf(String element){
		int[] IDs = new int[getNumberOfElements(element)];
		s = "";
		count = 0;
		
		try{
			SAXParserFactory f = SAXParserFactory.newInstance();
			SAXParser saxParser = f.newSAXParser();
			
			DefaultHandler handler = new DefaultHandler(){
				
				boolean foundElement = false;
				
				public void startElement(String uri, String localName,String qName, 
		                Attributes attributes) throws SAXException{
					
					if(attributes.getValue("id") != null) s = attributes.getValue("id");
					
					if(qName.equalsIgnoreCase(element)){
						foundElement = true;
					}
				}
				
				public void characters(char ch[], int start, int length) throws SAXException{
					if(new String(ch,start,length).equalsIgnoreCase("true") && foundElement){
						IDs[count] = Integer.parseInt(s);
						count ++;
					}
					foundElement = false;
				}
			};
			saxParser.parse(filePath, handler);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return IDs;
	}
	
	public int getNumberOfElements(String element){
		count = 0;
		
		try{
			SAXParserFactory f = SAXParserFactory.newInstance();
			SAXParser saxParser = f.newSAXParser();
			
			DefaultHandler handler = new DefaultHandler(){
				
				boolean foundItem = false;
				
				public void startElement(String uri, String localName,String qName, 
		                Attributes attributes) throws SAXException{
					
					if(qName.equalsIgnoreCase(element)){
						foundItem = true;
					}
				}
				
				public void characters(char ch[], int start, int length) throws SAXException{
					String s = new String(ch, start, length);
					if(s.equalsIgnoreCase("true") && foundItem){
						count ++;
					}
					foundItem = false;
				}
			};
			saxParser.parse(filePath, handler);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return count;
	}

}
