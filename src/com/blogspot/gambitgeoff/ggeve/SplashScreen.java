package com.blogspot.gambitgeoff.ggeve;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends Activity {
	
	private static final int SPLASH_TIMEOUT = 3000;
	
	public void onCreate(Bundle inBundle){
		super.onCreate(inBundle);
		setContentView(R.layout.splashscreen);
//		try {
//			Thread.sleep(4000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		this.finish();
		new Handler().postDelayed(new Runnable (){

			public void run() {
				SplashScreen.this.finish();   
			}}, SPLASH_TIMEOUT);
        
	}
}