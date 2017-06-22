package com.example.simpleandroidproject;

import java.util.ArrayList;

import com.example.simpleandroidproject.SelectItem.buyDLG;

import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class List extends Fragment{
	TableLayout tab ; 
	LayoutInflater inflater; 
	View myview; 
	 public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	 {
		 this.inflater =  inflater; 
		View view = inflater.inflate(R.layout.list, container, false); 
		 tab = (TableLayout)view.findViewById(R.id.MyTable); 
	myview = view; 
	TextView tv = (TextView )myview.findViewById(R.id.totalSum); 
	tv.setText(0.0 + ""); 
	Button send =(Button) myview.findViewById(R.id.imageButton1); 
	send.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					((Sender)getActivity()).send(); 
				} 
			 
			 }); 
		return view; 
	 }
	 public void addNewItem(String name, Double total, int numberOfItems, String units)
	 {
		 LinearLayout row = (LinearLayout) inflater.inflate(R.layout.listrow,null);
		 TableRow.LayoutParams rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
			tab.addView(row);
		 //TableRow tableRow = new TableRow(getActivity());
		// tableRow.setLayoutParams(rowParams);
		// TextView textView = new TextView(getActivity());
		// textView.setLayoutParams(rowParams);
		 TextView tv = (TextView) row.findViewById(R.id.name); 
		tv.setText(name); 
		// textView.setWidth(convertDpToPixel(50));
		        
		 tv = (TextView) row.findViewById(R.id.amount); 
			tv.setText(numberOfItems + " " + units); 
	
	//	 textView.setLayoutParams(rowParams);          
		 
		// textView.setWidth(convertDpToPixel(50));
		// tableRow.addView(textView);
		 //textView = new TextView(getActivity());
	//	 textView.setLayoutParams(rowParams);
			tv = (TextView) row.findViewById(R.id.cost); 
			tv.setText(total + " shekels"); 
			 tv = (TextView )myview.findViewById(R.id.totalSum); 
			 tv.setText((total+ Double.parseDouble(tv.getText().toString()))+ ""); 

		
		ImageButton img =(ImageButton)row.findViewById(R.id.imageButton1); 
	//	img.setLayoutParams(rowParams); 
		
	
		
		img.setOnClickListener(new OnClickListener()
	    {
	        @Override public void onClick(View v)
	        {
	            // row is your row, the parent of the clicked button
	            View row = (View) v.getParent();
	           TextView tv = (TextView) row.findViewById(R.id.cost); 
	          ( (TextView)myview.findViewById(R.id.totalSum)).setText(Double.parseDouble(((TextView)myview.findViewById(R.id.totalSum)).toString())-Double.parseDouble(tv.getText().toString()) + ""); 
	            // container contains all the rows, you could keep a variable somewhere else to the container which you can refer to here
	            ViewGroup container = ((ViewGroup)row.getParent());
	            // delete the row and invalidate your view so it gets redrawn
	            container.removeView(row);
	            container.invalidate();
	        }
	    });
	 }
	 public String getShoppingList()
	 {
		 String list = ""; 
		 /*
		 ArrayList<View> allViews =  getAllChildren(tab); 
		 for(View child : allViews)
		 {
			 ArrayList<View> allViewsInRow=  getAllChildren((LinearLayout)child); 
			 for (View textchild :allViewsInRow ) {
			        if (textchild instanceof TextView) {
			        	list+= ((TextView)textchild).getText().toString() + " "; 
			        	
			        }
			        } 
			 list += "\n"; 
		
			 		
		 }*/
		 
		 for (int i = 0; i < tab.getChildCount(); i++) {
			    View child = tab.getChildAt(i);

			    if (child instanceof LinearLayout) {
			    	LinearLayout row = (LinearLayout) child;

			        for (int x = 0; x < row.getChildCount(); x++) {
			            View view = row.getChildAt(x);
			            
			            if (view instanceof TextView) {
				        	list+= ((TextView)view).getText().toString() + " "; 
				        	
				        }
			        }
			        list += "\n"; 
			    }
			}
		 
		 list += "Total = " + ((TextView )myview.findViewById(R.id.totalSum)).getText().toString() + " shekels"; 
		  return list; 
	 }
	 private ArrayList<View> getAllChildren(View v) {

		    if (!(v instanceof ViewGroup)) {
		        ArrayList<View> viewArrayList = new ArrayList<View>();
		        viewArrayList.add(v);
		        return viewArrayList;
		    }

		    ArrayList<View> result = new ArrayList<View>();

		    ViewGroup vg = (ViewGroup) v;
		    for (int i = 0; i < vg.getChildCount(); i++) {

		        View child = vg.getChildAt(i);

		        ArrayList<View> viewArrayList = new ArrayList<View>();
		        viewArrayList.add(v);
		        viewArrayList.addAll(getAllChildren(child));

		        result.addAll(viewArrayList);
		    }
		    return result;
		}
	// public  int convertDpToPixel(float dp){
		 
		//   Resources resources = getActivity().getResources();
		  //  DisplayMetrics metrics = resources.getDisplayMetrics();
		   // float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
		  //  return (int)px;
		//}
}
