package com.blogspot.gambitgeoff.ggeve;

import com.blogspot.gambitgeoff.ggeve.eveapi.CharacterSheet;
import com.blogspot.gambitgeoff.ggeve.eveapi.EveAPI;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class CharacterInfoActivity extends Activity {

	private GGEveDBAdapter myGGEveDBAdapter;
	private EveCharacter myEveCharacter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		myGGEveDBAdapter = GGEveApplicationRunner.getDatabaseAdapter();
		setContentView(R.layout.charactersheet);

		SharedPreferences prefs = getSharedPreferences(GGEveApplicationRunner.EVE_PREFERENCES, Activity.MODE_PRIVATE);
		String char1 = prefs.getString(GGEveApplicationRunner.EVE_CURRENT_CHARACTER, "No Characters!");
		myEveCharacter = myGGEveDBAdapter.getEveCharacter(char1);
		updateDetails();
	}

	private void updateDetails() {
		AccountDetails account = GGEveApplicationRunner.getAccountDetails();
		CharacterSheet cs = new CharacterSheet(account.getUserID(), account.getAPIKey(), myEveCharacter.getCharacterID());
		myEveCharacter = cs.getCharacter();
		TextView viewt = (TextView) CharacterInfoActivity.this
				.findViewById(R.id.character_name);
		viewt.setText(myEveCharacter.getCharacterName());
		viewt = (TextView) CharacterInfoActivity.this
				.findViewById(R.id.character_corp);
		viewt.setText("Corporation Name: "
				+ myEveCharacter.getCorporationName());
		viewt = (TextView) CharacterInfoActivity.this
				.findViewById(R.id.character_race);
		viewt.setText("Race: " + myEveCharacter.getRace());
		viewt = (TextView) CharacterInfoActivity.this
				.findViewById(R.id.character_bloodline);
		viewt.setText("Bloodline: " + myEveCharacter.getBloodline());
		viewt = (TextView) CharacterInfoActivity.this
				.findViewById(R.id.character_balance);
		viewt.setText("Balance: " + myEveCharacter.getBalance());
		viewt = (TextView) CharacterInfoActivity.this
				.findViewById(R.id.character_gender);
		viewt.setText("Gender: " + myEveCharacter.getGender());
			ImageView b = (ImageView) CharacterInfoActivity.this.findViewById(R.id.character_image);
			b.setImageDrawable(EveAPI.getCharacterImage(myEveCharacter.getCharacterID()));
	}
}