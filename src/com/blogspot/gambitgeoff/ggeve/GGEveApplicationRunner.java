package com.blogspot.gambitgeoff.ggeve;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.StringTokenizer;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class GGEveApplicationRunner extends Activity {

	private static final int SPLASH_FINISHED = 1453234;
	public static final String EVE_PREFERENCES = "myEvePreferences";
	public static final String EVE_PUBLIC_API_KEY = "myEvePublicAPIKey";
	public static final String EVE_USER_ID = "myEveUserID";
	public static final String EVE_CURRENT_CHARACTER = "myCurrentCharacter";
	public static final String GGEVE_PROPERTIES_FILENAME = "ggeve.properties";
	
	public static Properties myGGEveProperties = new Properties();
	public static InputStream myGGEveOfflineCharacterSheet = null;
	public static InputStream myGGEveOfflineCharacters = null;
	
	public static final String PROPERTY_GGEVE_OFFLINE_MODE = "ggeve_offline_mode";
	private static GGEveApplicationRunner myAppRunner;
	private static GGEveDBAdapter myGGEveDBAdapter;

	/** Called when the activity is first created. */
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		if (myAppRunner==null)
			myAppRunner = this;
		try {
			InputStream is = getResources().openRawResource(R.raw.ggeve_properties);
			if (is != null){
				myGGEveProperties.load(is);
			myGGEveProperties.list(System.out);}
			} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			myGGEveOfflineCharacterSheet = getResources().openRawResource(R.raw.charactersheet);
			myGGEveOfflineCharacters = getResources().openRawResource(R.raw.characters);
		Intent splashIntent = new Intent(this, SplashScreen.class);
		this.startActivityForResult(splashIntent, SPLASH_FINISHED);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		System.out.println("Returned with: " + requestCode);
		if (requestCode == SPLASH_FINISHED) {
			Intent eveOverview = new Intent(this, GGEveOverviewActivity.class);
			this.startActivity(eveOverview);
			this.finish();
		}
	}
	
	public static Properties getGGEveProperties()
	{
		return myGGEveProperties;
	}
	
	public static boolean getIsRunningOffline()
	{
		return Boolean.parseBoolean(myGGEveProperties.getProperty(GGEveApplicationRunner.PROPERTY_GGEVE_OFFLINE_MODE));
	}
	
	public static InputStream getGGEveOfflineCharacterSheet()
	{
		return myGGEveOfflineCharacterSheet;
	}
	
	public static InputStream getGGEveOfflineCharacters()
	{
		return myGGEveOfflineCharacters;
	}
	
	public static GGEveDBAdapter getDatabaseAdapter()
	{
		if (myGGEveDBAdapter==null)
		{
			myGGEveDBAdapter = new GGEveDBAdapter(myAppRunner);
			myGGEveDBAdapter.open();
		}
		return myGGEveDBAdapter;
	}
	
	public static AccountDetails getAccountDetails()
	{
		SharedPreferences prefs = myAppRunner.getSharedPreferences(GGEveApplicationRunner.EVE_PREFERENCES, Activity.MODE_PRIVATE);
		String userID = prefs.getString(GGEveApplicationRunner.EVE_USER_ID,"-1");
		String apikey = prefs.getString(GGEveApplicationRunner.EVE_PUBLIC_API_KEY, "notset");
		if (!userID.equals("-1"))
				if (!apikey.equals("notset"))
					return new AccountDetails(apikey, Integer.parseInt(userID));
		File f = new File("/sdcard/ggeve.txt");
		try {
//			BufferedReader r = new BufferedReader(new InputStreamReader(myAppRunner.openFileInput("ggeve.txt")));
			BufferedReader r = new BufferedReader(new FileReader(f));
			String uid = r.readLine();
			uid = uid.substring(uid.indexOf("=")+1);
				String key = r.readLine();
				key = key.substring(key.indexOf("=")+1);
				return new AccountDetails(key, Integer.parseInt(uid));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 return null;
		}
		
}
