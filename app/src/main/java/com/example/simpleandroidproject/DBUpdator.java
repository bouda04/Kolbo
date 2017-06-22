package com.example.simpleandroidproject;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.DropboxFileInfo;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AppKeyPair;
import com.example.kolbodb.DBhelper;
import com.example.kolbodb.Item;
import com.example.kolbodb.MyApplication;
import com.example.kolbodb.XMLParser;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class DBUpdator extends IntentService {

	private Context context;

	final static private String MY_PREFS_NAME = MyApplication.getMyPref(); 
	private DropboxAPI<AndroidAuthSession> mDBApi;
	
	private static final String  DATA_FILE_NAME = "items.xml";
	
	
	public DBUpdator() {
		super("DBUpdator");
	}


	
	@Override
	protected void onHandleIntent(Intent intent) {
		this.context = MyApplication.getAppContext();
		if (MyApplication.isNetworkAvailable()){
			try {
				this.mDBApi = MyApplication.getDropBoxAPI();
				downloadFile(DATA_FILE_NAME);
				InputStream in = realDataStream(); 
				List<Item> data = XMLParser.parseFromStream(in); 
				in.close();
				List<String> imgFilesNames = updateDataBase(data); 
				if (!imgFilesNames.isEmpty()){
					for(int i = 0; i <  imgFilesNames.size(); i++ ){
						downloadFile(imgFilesNames.get(i)); 
					}
				}
				Intent i = new Intent(MyApplication.ACTION_NEW_DATA);
				this.context.sendBroadcast(i);
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	private void downloadFile(String fileName){
		
    	File file = new File(context.getFilesDir().getPath() + "/" + fileName);
    	FileOutputStream outputStream;
		try {
			outputStream = new FileOutputStream(file);
	    	DropboxFileInfo info = mDBApi.getFile("/kolbo/"+ fileName, null, outputStream, null);
	        Log.i("download file completed",fileName);
	        outputStream.close();
		} catch (DropboxException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
	
	private List<String> updateDataBase(List<Item> data)
	{
		//DBhelper.getInstance(context).deleteAllFood(); //
		Date newDate  = new Date(System.currentTimeMillis());
		String UpdateDate = newDate.toString();  //This will print the Human Readable String
		List<String> imgFilesNames = new ArrayList<String>();
		for(int i = 0; i <  data.size(); i++ )
		{
			if (DBhelper.getInstance(context).addNewItem(data.get(i))){
				imgFilesNames.add(data.get(i).image);
			}
		}
		return imgFilesNames;
	}
	
	private InputStream realDataStream() {
		// TODO Auto-generated method stub
		File file = new File(context.getFilesDir().getPath()  + "/" +  DATA_FILE_NAME); 
		try {
			return new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null; 
		} 
		
	}
}
