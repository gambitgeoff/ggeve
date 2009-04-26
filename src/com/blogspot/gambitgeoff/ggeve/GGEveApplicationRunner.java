package com.blogspot.gambitgeoff.ggeve;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class GGEveApplicationRunner extends Activity {

	private static final int SPLASH_FINISHED = 1453234;

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
