package com.blogspot.gambitgeoff.ggeve;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

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
	private static GGEveApplicationRunner myAppRunner;
	private static GGEveDBAdapter myGGEveDBAdapter;

	/** Called when the activity is first created. */
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		if (myAppRunner == null)
			myAppRunner = this;
		try {
			InputStream is = getResources().openRawResource(R.raw.ggeve_properties);
			if (is != null) {
				myGGEveProperties.load(is);
				myGGEveProperties.list(System.out);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		myGGEveOfflineCharacterSheet = getResources().openRawResource(R.raw.charactersheet);
		myGGEveOfflineCharacters = getResources().openRawResource(R.raw.characters);
		setContentView(R.layout.main);
		bootstrapAccountsFromFile();
		startUpdateService();

		Intent splashIntent = new Intent(this, SplashScreen.class);
		startActivityForResult(splashIntent, SPLASH_FINISHED);
	}
	
	private void startUpdateService()
	{
		startService(new Intent(this, GGEveUpdateService.class));
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		System.out.println("Returned with: " + requestCode);
		if (requestCode == SPLASH_FINISHED) {
			Intent eveOverview = new Intent(this, GGEveOverviewActivity.class);
			this.startActivity(eveOverview);
			this.finish();
		}
	}
	
	public static SimpleDateFormat getEveDateFormatter()
	{
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}

	public static Properties getGGEveProperties() {
		return myGGEveProperties;
	}

	public static boolean getIsRunningOffline() {
		return Boolean.parseBoolean(myGGEveProperties.getProperty(GGEveApplicationRunner.PROPERTY_GGEVE_OFFLINE_MODE));
	}

	public static InputStream getGGEveOfflineCharacterSheet() {
		return myGGEveOfflineCharacterSheet;
	}

	public static InputStream getGGEveOfflineCharacters() {
		return myGGEveOfflineCharacters;
	}

	public static GGEveDBAdapter getDatabaseAdapter() {
		if (myGGEveDBAdapter == null) {
			myGGEveDBAdapter = new GGEveDBAdapter(myAppRunner);
			myGGEveDBAdapter.open();
		}
		return myGGEveDBAdapter;
	}

	private void bootstrapAccountsFromFile() {
		File f = new File("/sdcard/ggeve.txt");
		Vector<AccountDetails> tempAccounts = new Vector<AccountDetails>();
		try {
			BufferedReader r = new BufferedReader(new FileReader(f));
			String details = "";
			StringTokenizer st;
			int userid = -1;
			String publickey, privatekey;
			while ((details = r.readLine()) != null) {
				if (details.indexOf("userid=")!=-1)
				{//need to handle this better for the old ggeve.txt format.
					return;
				}
				st = new StringTokenizer(details, ":");
				userid = Integer.parseInt(st.nextToken());
				publickey = st.nextToken();
				privatekey = st.nextToken();
				tempAccounts.add(new AccountDetails(userid, publickey, privatekey));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (AccountDetails accounts: tempAccounts)
		{
			getDatabaseAdapter().updateAccountDetails(accounts);
		}
	}
}
