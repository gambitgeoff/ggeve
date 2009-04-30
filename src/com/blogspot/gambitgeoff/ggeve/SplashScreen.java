package com.blogspot.gambitgeoff.ggeve;

import com.blogspot.gambitgeoff.ggeve.eveapi.EveAPI;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends Activity {
	
	private static final int SPLASH_TIMEOUT = 3000;
	
	public void onCreate(Bundle inBundle){
		super.onCreate(inBundle);
		setContentView(R.layout.splashscreen);
		new Handler().postDelayed(new Runnable (){

			public void run() {
				EveAPI.getLogin("", "");
				SplashScreen.this.finish();
			}}, SPLASH_TIMEOUT);
        
	}
}