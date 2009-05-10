package com.blogspot.gambitgeoff.ggeve;

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
				SplashScreen.this.finish();
			}}, SPLASH_TIMEOUT);
        
	}
}