package com.blogspot.gambitgeoff.ggeve;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import android.app.Activity;
import android.content.Intent;
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

	/** Called when the activity is first created. */
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
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
}
