package com.example.kolbodb;



//import com.example.dataBase.UpdaterService;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.exception.DropboxUnlinkedException;
import com.dropbox.client2.session.AppKeyPair;
import com.example.simpleandroidproject.DBUpdator;

import android.app.ActivityManager;
import android.app.Application;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.JsonWriter;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

public class MyApplication extends Application {

	private static DropboxAPI<AndroidAuthSession> mDBApi = null;
	private static final String  CLIENT_FILE_NAME = "clients.json";

	
	    private static Context context;
	 //   private NewDataReceiver ndr;
	    public static final String MY_PREFS_NAME = "MyPrefsFile"; 
	    public static final String ACTION_NEW_DATA="com.Kolbo.newData";
	    
	    public void onCreate(){
	    	
	        super.onCreate();
	        MyApplication.context = getApplicationContext();
			if (isNetworkAvailable())
				initDropBox(context);
  		Intent in = new Intent(MyApplication.context , DBUpdator.class);
//			if(!isMyServiceRunning(DBModifier.class))
				startService(in);
	    }

	private static void initDropBox(Context context){
		String APP_KEY = getMetadata(context, "dropbox_key");
		String APP_SECRET = getMetadata(context, "dropbox_secret");
		String ACCESS_TOKEN = getMetadata(context, "dropbox_token");
		AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
		AndroidAuthSession session = new AndroidAuthSession(appKeys, ACCESS_TOKEN);
		mDBApi = new DropboxAPI<AndroidAuthSession>(session);

	}

	public static DropboxAPI<AndroidAuthSession> getDropBoxAPI(){
		return mDBApi;
	}

	    public static Context getAppContext() {
	        return MyApplication.context;
	    }
	    
	    
	    private boolean isMyServiceRunning(Class<?> serviceClass) {
		    ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
		        if (serviceClass.getName().equals(service.service.getClassName())) {
		            return true;
		        }
		    } 
		    return false;
		}
	    public static String getMyPref()
	    {
	    	return MY_PREFS_NAME; 
	    }

	  	public  static String getMetadata(Context context, String name) {
	    	try {
	    	ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(
	    	context.getPackageName(), PackageManager.GET_META_DATA);
	    	if (appInfo.metaData != null) {
	    	return appInfo.metaData.getString(name);
	    	}
	    	} catch (PackageManager.NameNotFoundException e) {

	    	}

	    	return null;
	    }
	    
	    public static boolean isNetworkAvailable() {
			ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE); 
			NetworkInfo net = cm.getActiveNetworkInfo(); 
			return net!=null && net.isConnected();    
		}


	public static class NetworkChangeReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(final Context context, final Intent intent) {

		//	int status = NetworkUtil.getConnectivityStatusString(context);

			if (!"android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
				if(isNetworkAvailable()){
					Toast.makeText(context, "Network is available",Toast.LENGTH_LONG).show();
					initDropBox(MyApplication.getAppContext());
				}else{

				}

			}
		}
	}

	public static void sendMessageToManager(String msg){

		File file = new File(context.getFilesDir().getPath() + "/" + CLIENT_FILE_NAME);

		try {
			FileOutputStream out = new FileOutputStream(file);
			JsonWriter writer = null;
			writer = new JsonWriter(new OutputStreamWriter(out, "UTF-8"));
			writer.setIndent("  ");
			Message message = new Message();
			message.setRequest(msg);
			writeMessage(writer, message);
			writer.close();
			out.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}


		try {
			FileInputStream input = new FileInputStream(file);
			DropboxAPI.Entry newEntry = mDBApi.putFileOverwrite("/kolbo/"+ CLIENT_FILE_NAME, input, file.length(), null);
		} catch (DropboxUnlinkedException e) {
			Log.e("DbExampleLog", "User has unlinked.");
		} catch (DropboxException e) {
			Log.e("DbExampleLog", "Something went wrong while uploading.");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	private static void writeMessage(JsonWriter writer, Message message) throws IOException {
		writer.beginObject();
		writer.name("id").value(message.getUserId());
		writer.name("request").value(message.getRequest());
		writer.name("response").value("");
		writer.endObject();
	}
}
	
