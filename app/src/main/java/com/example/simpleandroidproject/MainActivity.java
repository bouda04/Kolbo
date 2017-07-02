package com.example.simpleandroidproject;






import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;





import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;


import com.example.kolbodb.Item;
import com.example.kolbodb.XMLParser;
import com.google.firebase.auth.FirebaseAuth;


import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements ItemClickListener, Sender{
	static final int PICK_CONTACT_REQUEST = 1;  
Context context = this; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_logout) {
			FirebaseAuth.getInstance().signOut();
			startActivity(new Intent(this, SignupActivity.class));
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


	@Override
	public void addNewItem(ItemsAdapter adapter, int position, Double total,
			int numberOfItems) {
		// TODO Auto-generated method stub
		List MyListfragment = (List) getFragmentManager().findFragmentById(R.id.listItems); 
		MyListfragment.addNewItem(adapter.getNameofItem(position), total, numberOfItems, adapter.getUnits(position)); 
		
	}

/**
 * This method enables the user to search for someone to send him an sms with list of items he wants....
 * 
 * 
  */
 
	@Override
	public void send() {
		// TODO Auto-generated method stub
		 Uri uri2 = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
			Intent pickContactIntent = new Intent(Intent.ACTION_PICK, uri2);
           	startActivityForResult(pickContactIntent,PICK_CONTACT_REQUEST);
	}
	  

	
	private class ContactInfo{
		String cellNumber;
        String FirstName;
        String LastName;
	}
	
	private ContactInfo getContactInfo(Uri uriContact) {
//        long contactID = ContentUris.parseId(uriContact);    // a quicker way to retreive the contact id
		String contactID;
        ContactInfo ci = new ContactInfo();
        
        Cursor cursorID = getContentResolver().query(uriContact,
                new String[]{ContactsContract.Contacts._ID, ContactsContract.Data.CONTACT_ID},
                null, null, null);
 
        if (cursorID.moveToFirst()) {
 
//            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Data.CONTACT_ID)); 
            cursorID.close(); 
            
            Cursor cursorDetails = getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                    new String[]{ContactsContract.Data.MIMETYPE,
            		ContactsContract.CommonDataKinds.Phone.NUMBER,
            		ContactsContract.CommonDataKinds.Phone.TYPE,
            		ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,
            		ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME},
                    ContactsContract.Data.CONTACT_ID + " = ?",
                    new String[]{contactID},
                    null);
	        int cnt = cursorDetails.getCount();
	        Log.d("Records count", Integer.toString(cnt));
	        while (cursorDetails.moveToNext()){
	            String rowType = cursorDetails.getString(cursorDetails.getColumnIndex(ContactsContract.Data.MIMETYPE));
	            Log.d("Records type", rowType);
	            if (rowType.equals(Phone.CONTENT_ITEM_TYPE)){
	        		int phoneType = cursorDetails.getInt((cursorDetails.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)));
		            ci.cellNumber = cursorDetails.getString(cursorDetails.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
	        		continue;
	        	}

	        	
	        	if (rowType.equals(StructuredName.CONTENT_ITEM_TYPE)) {
	        		if (ci.FirstName == null || ci.FirstName.isEmpty())
		            	ci.FirstName = cursorDetails.getString(cursorDetails.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME));
	        		if (ci.LastName == null || ci.FirstName.isEmpty())
	        			ci.LastName = cursorDetails.getString(cursorDetails.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME));
	        		continue;
	        	}
	        }
	        cursorDetails.close();
        }
        
        Uri x = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
       return ci;
    }


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
    		Uri uriSelectedItem = data.getData();
        	switch (requestCode){
        	case PICK_CONTACT_REQUEST:
        		ContactInfo ci = getContactInfo(uriSelectedItem);
        		sendShopingList(ci); 
         		break;
        	}
        }
	}
	
        private void sendShopingList(ContactInfo ci){
        	String list; 
        	List MyListfragment = (List) getFragmentManager().findFragmentById(R.id.listItems); 
    		Toast.makeText(this,  list = MyListfragment.getShoppingList(), Toast.LENGTH_LONG).show();
    		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + ci.cellNumber));     
    		intent.putExtra("sms_body", list); 
    		startActivity(intent);
        }
		
}
