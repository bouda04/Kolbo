package com.example.kolbodb;







import com.example.kolbodb.DBContract.items;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;



public class DBhelper extends SQLiteOpenHelper{

	
	private static DBhelper mInstance = null;
	private SQLiteDatabase db = null;
	
	

	private DBhelper(Context context){
		super(context, DBContract.DB_NAME, null, DBContract.DB_VERSION);    
	}
	
	
	public static DBhelper getInstance(Context context){ // singleTone
		if (mInstance == null){
			mInstance = new DBhelper(context);
		}
		return mInstance;
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		this.db = db;
		this.db.execSQL(DBContract.items.CREATE_ITEMS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + DBContract.items.TABLE_ITEMS);
		onCreate(db); 
	}
	
	public void deleteAllFood()
	{
		 String deleteQry = "delete From " + DBContract.items.TABLE_ITEMS + " WHERE " + DBContract.items.NAME+ " = '" + "food" + "'";
		 this.db.rawQuery(deleteQry, null);
	}
	public boolean addNewItem(Item item){
		
		boolean hasToBeAdded = true; 
		if (db == null)
			this.db = this.getWritableDatabase();
		 String selectQry = "Select * From " + DBContract.items.TABLE_ITEMS + " WHERE " + DBContract.items.NAME+ " = '" + item.name + "'";
			Cursor cursor = this.db.rawQuery(selectQry, null);
			if(cursor!=null && cursor.getCount()>0) {
				cursor.moveToFirst();
				if(cursor.getInt(cursor.getColumnIndex(items.VERSION))<item.version) 
				{
					hasToBeAdded = true; 
					 String deleteQry = "delete From " + DBContract.items.TABLE_ITEMS + " WHERE " + DBContract.items.NAME+ " = '" + item.name + "'";
						 this.db.rawQuery(deleteQry, null);
					
					
				}
				else
					hasToBeAdded = false; 
			}
			
		 
//		this.db.delete(DBContract.items.TABLE_ITEMS, whereClause, whereArgs)
		 
		
		if(hasToBeAdded)  
		{                                   
		ContentValues values = new ContentValues(); //bundle                 
		values.put(DBContract.items.CATEGORY, item.category);
		values.put(DBContract.items.NAME, item.name);
		values.put(DBContract.items.PRICE, item.price);
		values.put(DBContract.items.IMAGE, item.image);               
		values.put(DBContract.items.DISCOUNT_LIMIT, item.discount_limit);  
		values.put(DBContract.items.DISCOUNT_PRICE, item.discount_price); 
		values.put(DBContract.items.VERSION, item.version); 
		String text="";  
		if(item.discount_limit!=0)
			text += "buy" + item.discount_limit + "in" + item.discount_price + "shekels"; 
		values.put(DBContract.items.DISCOUNT_TEXT,  text); 
		values.put(DBContract.items.DISCOUNT_PRICE, item.discount_price);        
		values.put(DBContract.items.UNITS, item.units);  
		long rowId = this.db.insert(DBContract.items.TABLE_ITEMS, null, values); 
		
		}
		return hasToBeAdded;
		
	}
	
	public Cursor getItemsForCategory(String Category){
		 String selectQry = "Select * From " + DBContract.items.TABLE_ITEMS + " WHERE " + DBContract.items.CATEGORY + " = '" +  Category +"'";
		if (db == null)
			this.db = this.getReadableDatabase();
		Cursor cursor = this.db.rawQuery(selectQry, null); // ����� �� ������ �� ������
		return cursor;
	}
	
	

}
