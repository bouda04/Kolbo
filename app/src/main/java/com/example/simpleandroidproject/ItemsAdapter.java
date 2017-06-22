package com.example.simpleandroidproject;

import java.io.File;
import java.util.Stack;

import com.example.kolbodb.DBContract.items;
import com.example.kolbodb.DBhelper;
import com.example.kolbodb.Item;
import com.example.kolbodb.MyApplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

public class ItemsAdapter extends ResourceCursorAdapter {

	Stack<String>categories = new Stack<String>(); 
	DBhelper myDB;
	Context context;
	NewDataReceiver ndr =null;
	public static final String NONE_CATEGORY = "None";   
	public ItemsAdapter(Context context, int layout, Cursor c, int flags) {
		super(context, layout, c, flags);
		this.context = context;
		 categories.push(NONE_CATEGORY); 
        ndr = new NewDataReceiver();
        IntentFilter ifl = new IntentFilter(MyApplication.ACTION_NEW_DATA);
        context.registerReceiver(ndr, ifl);
	        
		Log.i("ItemsAdapter", "number of records:" + c.getCount());
	}

	public ItemsAdapter(Context context, int layout){
		this(context, layout, DBhelper.getInstance(context).getItemsForCategory(Item.NONE_CATEGORY), 0);
	}
	
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		TextView tv1= (TextView)view.findViewById(R.id.textView); 
		tv1.setText(cursor.getString(cursor.getColumnIndex(items.NAME))); 

		ImageView im = (ImageView)view.findViewById(R.id.imageView1); 
		File file = new File(context.getFilesDir().getPath()  + "/" + cursor.getString(cursor.getColumnIndex(items.IMAGE)));
		String str = file.getAbsolutePath();
		Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath());
		im.setImageBitmap(bm);
		String discountText =  cursor.getString(cursor.getColumnIndex(items.DISCOUNT_TEXT));                     
		String discountPrice =  cursor.getString(cursor.getColumnIndex(items.DISCOUNT_PRICE));
		String discountLimit =  cursor.getString(cursor.getColumnIndex(items.DISCOUNT_LIMIT));
		 Cursor c = DBhelper.getInstance(context).getItemsForCategory(cursor.getString(cursor.getColumnIndex(items.NAME))); 
//		 imb.setOnClickListener((OnClickListener) this); 
		
	}
   
	public class NewDataReceiver extends BroadcastReceiver {

	@Override
	  public void onReceive(Context context, Intent intent) {
		
		  if(intent.getAction().equals(MyApplication.ACTION_NEW_DATA))
		  {
				swapCursor(DBhelper.getInstance(context).getItemsForCategory(Item.NONE_CATEGORY));
				notifyDataSetChanged();
			  
		  }
	  }
}
	
	 /**
	  * @param position the position of the item 
	  * @return the name of the item
	  * 
	   */
	public String getNameofItem(int position)
	{
		Cursor cursor = (Cursor) this.getItem(position); 
		return cursor.getString(cursor.getColumnIndex(items.NAME)); 
	}
	 /**
	  * @param position the position of the item 
	  * @return the text that describes the discount we have about the item
	  * 
	   */
	public String getDiscountText(int position)
	{
		Cursor cursor = (Cursor) this.getItem(position); 
		return cursor.getString(cursor.getColumnIndex(items.DISCOUNT_TEXT)); 
	}
	 /**
	  * @param position the position of the item 
	  * @return the number of items take place in the sale price, or 0 if there is no sale price
	  * 
	   */
	public Integer getDiscountLimit(int position)
	{
		Cursor cursor = (Cursor) this.getItem(position); 
		return cursor.getInt(cursor.getColumnIndex(items.DISCOUNT_LIMIT)); 
	}
	 /**
	  * @param position the position of the item 
	  * @return the price of the amount of items bought in sales price, or 0 if there is no sale price
	  * 
	   */
	public Double getDiscountPrice(int position)
	{
		Cursor cursor = (Cursor) this.getItem(position); 
		return cursor.getDouble(cursor.getColumnIndex(items.DISCOUNT_PRICE)); 
	}
	 /**
	  * @param position the position of the item 
	  * @return the price of single item
	  * 
	   */
	public Double getPrice(int position)
	{
		Cursor cursor = (Cursor) this.getItem(position); 
		return cursor.getDouble(cursor.getColumnIndex(items.PRICE)); 
	}
	 /**
	  * @param position the position of the item 
	  * @return a string describing units (for instance kilo, items, liters...)
	  * 
	   */
	public String getUnits(int position)
	{
		Cursor cursor = (Cursor) this.getItem(position); 
		return cursor.getString(cursor.getColumnIndex(items.UNITS)); 
	}
	 /**
	  * @param position the position of the item 
	  * @return a string describing the name of image file downloaded that is saved in memory
	  * 
	   */
	public String getImage(int position)
	{
		Cursor cursor = (Cursor) this.getItem(position); 
		return cursor.getString(cursor.getColumnIndex(items.IMAGE)); 
	}
	 /**
	  * this method is responsible for going back and showing the categories that participate with the father category for the items /category we see now in greedview
	  *
	  * 
	   */
	public void goBack()
	{
		if(categories.pop().equals(NONE_CATEGORY)) 
			categories.push(NONE_CATEGORY); 
		swapCursor(DBhelper.getInstance(context).getItemsForCategory(categories.peek()));     
	}
	public Context getContext()
	{
		return context;
		
	}
	 /**
	  * this method is responsible for treating a click on an item
	  * @param name the name of item that was clicked
	  * @result true if a category was clicked and then it swaps itself to show the items under this category, false if this is a finite item you can buy
	   */
	public boolean onItemClick(String name) {//adt-b.-sdk-adb shell data data 
		// TODO Auto-generated method stub
		
		 Cursor c = DBhelper.getInstance(context).getItemsForCategory(name);
		 categories.push(name); 
		 if(c!=null && c.getCount()>0)
		 {
				swapCursor(c); 
				return true; 
		 }
		 return false; 
	}     

}
