package com.blogspot.gambitgeoff.ggeve;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class CharacterInfoActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.charactersheet);
		SharedPreferences prefs = getSharedPreferences(GGEveApplicationRunner.EVE_PREFERENCES, Activity.MODE_PRIVATE);
		String char1 = prefs.getString(GGEveApplicationRunner.EVE_CURRENT_CHARACTER,"No Characters!");
		TextView viewt = (TextView)CharacterInfoActivity.this.findViewById(R.id.character_name);
		viewt.setText(char1);
	}
}