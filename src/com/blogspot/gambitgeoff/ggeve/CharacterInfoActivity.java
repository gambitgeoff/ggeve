package com.blogspot.gambitgeoff.ggeve;

import com.blogspot.gambitgeoff.ggeve.eveapi.CharacterSheet;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class CharacterInfoActivity extends Activity {
	
	private GGEveDBAdapter myGGEveDBAdapter;
	private EveCharacter myEveCharacter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		myGGEveDBAdapter = new GGEveDBAdapter(this);
		myGGEveDBAdapter.open();
		setContentView(R.layout.charactersheet);
		SharedPreferences prefs = getSharedPreferences(GGEveApplicationRunner.EVE_PREFERENCES, Activity.MODE_PRIVATE);
		String char1 = prefs.getString(GGEveApplicationRunner.EVE_CURRENT_CHARACTER,"No Characters!");
		myEveCharacter = myGGEveDBAdapter.getEveCharacter("'" + char1 + "'");
		TextView viewt = (TextView)CharacterInfoActivity.this.findViewById(R.id.character_name);
		viewt.setText(char1);
		viewt = (TextView)CharacterInfoActivity.this.findViewById(R.id.character_corp);
		viewt.setText(viewt.getText() + myEveCharacter.getCorporationName());
		viewt = (TextView)CharacterInfoActivity.this.findViewById(R.id.character_race);
		viewt.setText(viewt.getText() + myEveCharacter.getRace());
		viewt = (TextView)CharacterInfoActivity.this.findViewById(R.id.character_bloodline);
		viewt.setText(viewt.getText() + myEveCharacter.getBloodline());
		
		Button updateDetails = (Button)CharacterInfoActivity.this.findViewById(R.id.get_details);
		updateDetails.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				updateDetails();
			}});
	}
	
	private void updateDetails()
	{
		SharedPreferences prefs = getSharedPreferences(GGEveApplicationRunner.EVE_PREFERENCES, Activity.MODE_PRIVATE);
		String userID = prefs.getString(GGEveApplicationRunner.EVE_USER_ID,"-1");
		String apikey = prefs.getString(GGEveApplicationRunner.EVE_PUBLIC_API_KEY, "notset");
		int charID = myEveCharacter.getCharacterID();
		
		CharacterSheet cs = new CharacterSheet(Integer.parseInt(userID), apikey, charID);
//		myGGEveDBAdapter.updateEveCharacter(1, cs.getCharacter());
		myEveCharacter = cs.getCharacter();
		TextView viewt = (TextView)CharacterInfoActivity.this.findViewById(R.id.character_race);
		viewt.setText(viewt.getText() + myEveCharacter.getRace());
		viewt = (TextView)CharacterInfoActivity.this.findViewById(R.id.character_bloodline);
		viewt.setText(viewt.getText() + myEveCharacter.getBloodline());
	}
}