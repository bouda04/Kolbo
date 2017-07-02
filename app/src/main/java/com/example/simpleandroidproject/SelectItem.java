package com.example.simpleandroidproject;





import java.io.File;
import java.util.Date;

import com.example.kolbodb.DBContract.items;
import com.example.kolbodb.Message;
import com.example.kolbodb.MyApplication;
import com.example.simpleandroidproject.MyDialog.ResultsListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.app.DialogFragment;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SelectItem extends Fragment implements ResultsListener{
	GridView gv; 
	public SelectItem() {
		super();
		// TODO Auto-generated constructor stub
	}


	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);      
	}
	
	 public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	 {
		View view = inflater.inflate(R.layout.items, container, false); 
		gv	= (GridView) view.findViewById(R.id.Second);          
	
		final ItemsAdapter adapter = new ItemsAdapter(getActivity(), R.layout.item); 
		gv.setAdapter(adapter); 
		// Toast.makeText(getActivity(), "a click on"  + "was done", 300).show();
		 ImageButton b = (ImageButton)view.findViewById(R.id.back); 
		 b.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				adapter.goBack(); 
			} 
		 
		 }); 
		gv.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String name; 
				Toast toast = Toast.makeText(getActivity(), "a click on" + (name = adapter.getNameofItem(position)) + "was done", Toast.LENGTH_LONG);
				//toast.show();
				if(!adapter.onItemClick(name)) 
				{
					MyDialog.newInstance(MyDialog.SCORE_DIALOG).
						show(getChildFragmentManager(), "Score Dialog");
					
				}
				
			}
		});
		
		gv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				
				buyDLG dlg = new buyDLG(adapter, position);
				dlg.setStyle(DialogFragment.STYLE_NORMAL, 0);
				dlg.setCancelable(false);
				dlg.show(getFragmentManager(), null);
				
				return true;
			}
		});
		return view; 
	 }
	 /**
	  * This class implements a dialog that enables the user to choose how many to buy according to cost and sale prices
	  * 
	  * 
	   */
	 public class buyDLG extends DialogFragment      
		{
		 ItemsAdapter adapter; 
		 int positionOfItem; 
		 Integer amount; 
		 Double total; 
		 TextView tvTotal;


		public  buyDLG(ItemsAdapter adapter, int positionOfItem)
		{
			this.adapter   = adapter; 
			this.positionOfItem = positionOfItem; 
			
		}
			@Override
			public View onCreateView(LayoutInflater l, ViewGroup v, Bundle b) 
			{
				
				final View view = l.inflate(R.layout.dialog, v  ,false); 
				getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE); 
				
			ImageView img = (ImageView)view.findViewById(R.id.imageView1); 
			File file = new File(adapter.getContext().getFilesDir().getPath()  + "/" + adapter.getImage(positionOfItem));
			String str = file.getAbsolutePath();
			Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath());
			img.setImageBitmap(bm);
			TextView tv = (TextView)view.findViewById(R.id.Myoperation); 
			tv.setText(adapter.getDiscountText(positionOfItem)); 
			 tv = (TextView)view.findViewById(R.id.myprice); 
			tv.setText(adapter.getPrice(positionOfItem).toString());
			tvTotal = (TextView)view.findViewById(R.id.total); 
			 TextView perWhat = (TextView)view.findViewById(R.id.perWhat);
			 TextView tvUnits = (TextView)view.findViewById(R.id.tvUnits);
			 perWhat.setText("per " + adapter.getUnits(positionOfItem)); 
			 tvUnits.setText(adapter.getUnits(positionOfItem)); 
			final EditText edit = (EditText)view.findViewById(R.id.howmany);
			edit.addTextChangedListener(new TextWatcher() {        

		       

		          public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

		          public void onTextChanged(CharSequence s, int start, int before, int count) {}

				@Override
				public void afterTextChanged(Editable s) {
					
					 //TODO Auto-generated method stub
					 amount = Integer.parseInt(edit.getText().toString()); 
					double price = adapter.getPrice(positionOfItem);  
					double d_price = adapter.getDiscountPrice(positionOfItem);  
					int d_limit  = adapter.getDiscountLimit(positionOfItem);  
					if(d_limit!=0)
					 total = new Double ((amount/d_limit)*d_price + (amount%d_limit)*price); 
					else
						 total =  new Double(amount*price); 
					 tvTotal.setText(total.toString());   
					 
					
				}
		       });
			Button buy = 	(Button)view.findViewById(R.id.buy); 
			buy.setOnClickListener(new OnClickListener(){
				 /**
				  * This method is implemented by the listener to the event when user clicks on buy button
				  * @param v the view was clicked      
				  * this method calls addNewItem method of main activity with appropriate parameters with details about the shopping, then dismisses the dialog 
				   */
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				((ItemClickListener)getActivity()).addNewItem(adapter,positionOfItem,  total, amount); 
				dismiss();    
			} 
		 
		 });
			Button cancel = 	(Button)view.findViewById(R.id.cancel); 
			cancel.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dismiss();
				} 
			 
			 });

				Button negogiate = 	(Button)view.findViewById(R.id.btnNegogiate);
				negogiate.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						String itemName = adapter.getNameofItem(positionOfItem);
						final EditText edNewPrice = (EditText)view.findViewById(R.id.edtNewPrice);
						sendMessageToManager("Client is requesting discount for item '"
								+ itemName + "', amount =" + amount + ", request price = "
								+ edNewPrice.getText());
						Toast.makeText(MyApplication.getAppContext(), "A request was sent to the store manager, please wait!", Toast.LENGTH_LONG).show();
					}

				});

				return view;
			}

			private  void sendMessageToManager(String msg){
				Message message = new Message();
				message.setRequest(msg);
				message.setCreationTime(new Date().getTime());
				message.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());
				final DatabaseReference mDatabase;
				mDatabase = FirebaseDatabase.getInstance().getReference();
				final String key = mDatabase.child("messages").push().getKey();
				mDatabase.child("messages").child(key).setValue(message);
				mDatabase.child("messages").child(key).addValueEventListener(new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot dataSnapshot) {

						Message msg = dataSnapshot.getValue(Message.class);
						if (msg!= null && msg.getResponse()!= null) {
							Toast.makeText(MyApplication.getAppContext(), "Response received:" + msg.getResponse(), Toast.LENGTH_LONG).show();
							total =  Double.parseDouble(msg.getResponse());
							tvTotal.setText(msg.getResponse());
							mDatabase.child("messages").child(key).removeValue();
						}
					}

					@Override
					public void onCancelled(DatabaseError databaseError) {

					}
				});

			}

		}


	@Override
	public void onFinishedDialog(int requestCode, Object results) {
		Toast.makeText(getActivity(), 
				"The score received is: " + (Integer)results, Toast.LENGTH_LONG).show();
	}
	
	 

}
