package com.example.kolbodb;

import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.util.Log;


public class XMLParser {
	final static String KEY_ITEM="item";
	final static String KEY_NAME="name";
	final static String KEY_IMAGE="image";
	final static String KEY_CATEGORY="category";
	final static String KEY_COST="cost";
	final static String KEY_VERSION = "version";
	final static String KEY_DISCOUNT_LIMIT = "discount_limit"; 
	final static String KEY_DISCOUNT_PRICE = "discount_price"; 
	final static String  KEY_UNIT = "units"; 
	
	public static ArrayList<Item> parseFromStream(InputStream in){
		ArrayList<Item> data = null;
		XmlPullParserFactory xmlFactoryObject;//
		try{
			
			xmlFactoryObject = XmlPullParserFactory.newInstance();
			XmlPullParser parser = xmlFactoryObject.newPullParser();
	
			parser.setInput(in, null);
	        int eventType = parser.getEventType();
	       Item currentItem = null;
	        String inTag = "";
	        String strTagText = null;
	        while (eventType != XmlPullParser.END_DOCUMENT)
	        {
	        	inTag = parser.getName();
	            switch (eventType){
	                case XmlPullParser.START_DOCUMENT:
	                	data = new ArrayList<Item>();
	                    break;
	                case XmlPullParser.START_TAG:
	                	if (inTag.equalsIgnoreCase(KEY_ITEM))
	                    	currentItem = new Item();
	                    break;
	                case XmlPullParser.TEXT:
	                	strTagText = parser.getText();
	                	break;
	                case XmlPullParser.END_TAG:
	                	if (inTag.equalsIgnoreCase(KEY_ITEM))		
	                    	data.add(currentItem);	
	                	else if (inTag.equalsIgnoreCase(KEY_NAME))
	                		currentItem.setName(strTagText);  
	                	else if (inTag.equalsIgnoreCase(KEY_IMAGE))
	                		currentItem.setImage(strTagText) ; 
	                	else if (inTag.equalsIgnoreCase(KEY_CATEGORY))
	                		currentItem.setCategory(strTagText); 
	                	else if (inTag.equalsIgnoreCase(KEY_COST))
	                    	currentItem.setPrice(Double.parseDouble(strTagText)); 
	                	else if (inTag.equalsIgnoreCase(KEY_DISCOUNT_PRICE))
	                    	currentItem.setDiscountPrice(Double.parseDouble(strTagText)); 
	                	else if (inTag.equalsIgnoreCase(KEY_UNIT))
	                    	currentItem.setUnits(strTagText); 
	                	else if (inTag.equalsIgnoreCase(KEY_VERSION))
	                    	currentItem.setVersion(Integer.parseInt(strTagText));
	                	else if (inTag.equalsIgnoreCase(KEY_DISCOUNT_LIMIT))
	                		currentItem.setDiscountLimit(Integer.parseInt(strTagText)); 
	            		inTag ="";
	                	break;	       
	            }
	            eventType = parser.next();
	        }
		}
		 catch (Exception e) {
			 Log.d("XMLParser", e.getMessage());
			 e.printStackTrace();
			 }
		
		
		
		
		return data;
		
	}
	
	
	

}
