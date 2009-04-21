package com.blogspot.gambitgeoff.ggeve;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;

public class SplashScreen extends Activity {
	
	private static final int SPLASH_TIMEOUT = 3000;
	
	public void onCreate(Bundle inBundle){
		super.onCreate(inBundle);
		setContentView(R.layout.splashscreen);
		new Handler().postDelayed(new Runnable (){

			@Override
			public void run() {
                SplashScreen.this.finish();
			}}, SPLASH_TIMEOUT);
		
	}
}