package com.example.kolbodb;


import android.provider.BaseColumns;

public class DBContract {
	static final String DB_NAME = "KolboData";
	static final int DB_VERSION = 6;
	public static abstract class items implements BaseColumns {
		
		
		// Contacts table name
		    static final String TABLE_ITEMS = "items";
		 
		    // Contacts Table Columns names
		    
			
		    public static final String VERSION = "version";
		    public static final String  UNITS= "units";
		    static final String KEY_ID = _ID;
		    public static final String IMAGE = "image"; 
		    public static final String PRICE = "price";
		    public static final String NAME = "name";
		    public static final String DISCOUNT_LIMIT = "discountLimit";                      
		    public static final String DISCOUNT_TEXT = "discountText";                      
		    public static final String DISCOUNT_PRICE = "discountPrice"; 
		    public static final String CATEGORY = "category";   
		    
		    static final String CREATE_ITEMS_TABLE = "CREATE TABLE " + TABLE_ITEMS + 
		    		"(" + KEY_ID + " INTEGER PRIMARY KEY, " + CATEGORY + " TEXT, " +VERSION + " INTEGER, " +
		    		NAME + " TEXT, "  + PRICE + " REAL, "  +  DISCOUNT_PRICE + " REAL, " +  IMAGE + " TEXT, " + UNITS + " TEXT, " + 
		    		 DISCOUNT_LIMIT + " INTEGER, " + DISCOUNT_TEXT + " TEXT" + ")"; 
		  
		    
	}
}
