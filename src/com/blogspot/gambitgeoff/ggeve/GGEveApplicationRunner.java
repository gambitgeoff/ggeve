package com.blogspot.gambitgeoff.ggeve;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class GGEveApplicationRunner extends Activity {

	private static final int SPLASH_FINISHED = 1453234;
	public static final String EVE_PREFERENCES = "myEvePreferences";
	public static final String EVE_PUBLIC_API_KEY = "myEvePublicAPIKey";
	public static final String EVE_USER_ID = "myEveUserID";
	public static final String EVE_CURRENT_CHARACTER = "myCurrentCharacter";

	/** Called when the activity is first created. */
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
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
}
