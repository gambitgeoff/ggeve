package com.blogspot.gambitgeoff.ggeve;

import java.text.NumberFormat;
import java.util.Vector;

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
		// AccountDetails account = GGEveApplicationRunner.getAccountDetails();
		Vector<AccountDetails> tempAccounts = this.myGGEveDBAdapter.getAccounts();
		for (AccountDetails account : tempAccounts) {
			try {
//				CharacterSheet cs = new CharacterSheet(account.getUserID(), account.getPublicAPIKey(), myEveCharacter.getCharacterID());
//				myEveCharacter = cs.getCharacter();
//				mySkillInTraining = new SkillInTraining(myEveCharacter.getCharacterID());
//				myGGEveDBAdapter.updateEveCharacter(myEveCharacter);
				TextView viewt = (TextView) CharacterInfoActivity.this.findViewById(R.id.character_name);
				viewt.setText(myEveCharacter.getCharacterName());
				viewt = (TextView) CharacterInfoActivity.this.findViewById(R.id.character_corp);
				viewt.setText("Corporation Name: " + myEveCharacter.getCorporationName());
				viewt = (TextView) CharacterInfoActivity.this.findViewById(R.id.character_race);
				viewt.setText("Race: " + myEveCharacter.getRace());
				viewt = (TextView) CharacterInfoActivity.this.findViewById(R.id.character_bloodline);
				viewt.setText("Bloodline: " + myEveCharacter.getBloodline());
				viewt = (TextView) CharacterInfoActivity.this.findViewById(R.id.character_balance);
				long balance = (long) myEveCharacter.getBalance();
				viewt.setText("Balance: " + NumberFormat.getInstance().format(balance) + " ISK");
				viewt = (TextView) CharacterInfoActivity.this.findViewById(R.id.character_gender);
				viewt.setText("Gender: " + myEveCharacter.getGender());
				ImageView b = (ImageView) CharacterInfoActivity.this.findViewById(R.id.character_image);
				b.setImageDrawable(EveAPI.getCharacterImage(myEveCharacter.getCharacterID()));
				
				

//				TrainingInformation info = mySkillInTraining.getTrainingInformation();
//				Date startTime = info.getTrainingStartTime();
//				Date endTime = info.getTrainingEndTime();

//				if (startTime != null && endTime != null) {
				TrainingInformation info = myEveCharacter.getTrainingInformation();
					viewt = (TextView) CharacterInfoActivity.this.findViewById(R.id.character_training);
					viewt.setText("Currently Training: " + info.getSkillInTraining());
//					long start = mySkillInTraining.getTrainingInformation().getTrainingStartTime().getTime();
//					long end = mySkillInTraining.getTrainingInformation().getTrainingEndTime().getTime();
//					long difference = end - start;
//					long days = difference / 86400000;
//					difference = difference - (days * 86400000);
//					long hours = difference / 3600000;
//					difference = difference - (hours * 3600000);
//					long minutes = difference / 60000;
//					difference = difference - (minutes * 60000);
//					long seconds = difference / 1000;
//					viewt = (TextView) CharacterInfoActivity.this.findViewById(R.id.train_time_left);
//					if (days > 0) {
//						viewt.setText("Training time left: " + days + " days, " + hours + " hrs, " + minutes + " mins");
//					} else if (hours > 0) {
//						viewt.setText("Training time left: " + hours + " hours, " + minutes + " mins");
//					} else if (minutes > 0) {
//						viewt.setText("Training time left: " + minutes + " mins");
//					} else if (seconds > 0) {
//						viewt.setText("Training time left: " + seconds + " seconds");
//					}

//				} else {
//					viewt = (TextView) CharacterInfoActivity.this.findViewById(R.id.character_training);
//					viewt.setText("Currently not training any skills");
//					viewt = (TextView) CharacterInfoActivity.this.findViewById(R.id.train_time_left);
//					viewt.setText("");
//				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}