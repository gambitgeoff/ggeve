package com.blogspot.gambitgeoff.ggeve;

import java.util.Vector;

import com.blogspot.gambitgeoff.ggeve.eveapi.AccountCharacters;
import com.blogspot.gambitgeoff.ggeve.eveapi.EveAPI;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class GGEveOverviewActivity extends Activity {

	private static final int MENU_APIKEY = 100;
	private static final int MENU_USERNAME = 101;
	private static final int MENU_RESETDB = 102;
	private GGEveDBAdapter myGGEveDBAdapter;

	private Dialog myAPIKeyDialog, myUserNameDialog, myInvalidKeyUserIDDialog;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		myGGEveDBAdapter = GGEveApplicationRunner.getDatabaseAdapter();
		AccountDetails account = GGEveApplicationRunner.getAccountDetails();
		if (account!=null)
		{
			try {
				AccountCharacters tempChars = new AccountCharacters(account.getUserID(), account.getAPIKey());
				for (EveCharacter ec: tempChars.getCharacters())
					myGGEveDBAdapter.addEveCharacter(ec);
			} catch (EveAuthenticationException e) {
				myInvalidKeyUserIDDialog = new Dialog(GGEveOverviewActivity.this);
				Window window = myInvalidKeyUserIDDialog.getWindow();
				window.setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
				myInvalidKeyUserIDDialog.setTitle("Invalid Credentials!");
				myInvalidKeyUserIDDialog.setContentView(R.layout.invalidapikeyuserid);
				myInvalidKeyUserIDDialog.show();

				Button ackButton = (Button) myInvalidKeyUserIDDialog.findViewById(R.id.button_acknowledge);
				ackButton.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						if (myInvalidKeyUserIDDialog != null) {
							if (myInvalidKeyUserIDDialog.isShowing()) {
								myInvalidKeyUserIDDialog.cancel();
							}
						}
					}
				});
			}
		}
		else
		{
			displayGGEveInitializationInstructions();
		}
		setContentView(R.layout.mainoverview);
		setupButtonNames();
	}
	
	private void displayGGEveInitializationInstructions()
	{
		Dialog myInitialisationErrorDialog = new Dialog(GGEveOverviewActivity.this);
		Window window = myInitialisationErrorDialog.getWindow();
		window.setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
				WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		myInitialisationErrorDialog.setTitle("Initialisation Error");
		myInitialisationErrorDialog.setContentView(R.layout.initialisationerror);
		myInitialisationErrorDialog.show();
	}

	private void setupButtonNames() {
		ImageButton[] buttons = new ImageButton[3];
		buttons[0] = (ImageButton) this.findViewById(R.id.char1);
		buttons[1] = (ImageButton) this.findViewById(R.id.char2);
		buttons[2] = (ImageButton) this.findViewById(R.id.char3);

		Vector<EveCharacter> tempChars = myGGEveDBAdapter.getEveCharacters();
		if (tempChars.size() > 0 && tempChars.size() < 4) {
			for (int i = 0; i < tempChars.size(); i++) {
				EveCharacter ec = tempChars.get(i);
				buttons[i].setImageDrawable(EveAPI.getCharacterImage(tempChars.get(i).getCharacterID()));
				buttons[i].setTag(ec.getCharacterName());
				buttons[i].setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						GGEveOverviewActivity.this.loadCharacter((String)v.getTag());
					}
				});
			}
		}
	}

	private void loadCharacter(String inCharacter) {
		SharedPreferences prefs = getSharedPreferences(GGEveApplicationRunner.EVE_PREFERENCES, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(GGEveApplicationRunner.EVE_CURRENT_CHARACTER,inCharacter);
		editor.commit();
		Intent characterInfoActivity = new Intent(this,CharacterInfoActivity.class);
		startActivity(characterInfoActivity);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		SubMenu sub = menu.addSubMenu(0, 0, Menu.NONE, R.string.menu_item);
		sub.setHeaderIcon(R.drawable.menu_item_icon);
		sub.setIcon(R.drawable.menu_item_icon);
		sub.add(0, MENU_APIKEY, Menu.NONE, R.string.enter_api_key);
		sub.add(0, MENU_USERNAME, Menu.NONE, R.string.enter_userid);
		sub.add(0, MENU_RESETDB, Menu.NONE, "Reset Database");

		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		// Find which menu item has been selected
		switch (item.getItemId()) {
		// Check for each known menu item
		case (MENU_RESETDB): {
			myGGEveDBAdapter.reset();
			return true;
		}
		case (MENU_USERNAME): {
			if (myUserNameDialog == null)
				myUserNameDialog = new Dialog(GGEveOverviewActivity.this);
			Window window = myUserNameDialog.getWindow();
			window.setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
					WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
			myUserNameDialog.setTitle("Enter User ID");
			myUserNameDialog.setContentView(R.layout.useriddialog);
			myUserNameDialog.show();

			final EditText useridText = (EditText) myUserNameDialog
					.findViewById(R.id.userId_edittext);
			SharedPreferences prefs = getSharedPreferences(
					GGEveApplicationRunner.EVE_PREFERENCES,
					Activity.MODE_PRIVATE);
			String userid = prefs.getString(GGEveApplicationRunner.EVE_USER_ID,
					"Not yet set!");
			useridText.setText(userid);

			Button cancelButton = (Button) myUserNameDialog
					.findViewById(R.id.cancel_button);
			cancelButton.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					if (myUserNameDialog != null) {
						if (myUserNameDialog.isShowing())
							myUserNameDialog.cancel();
					}
				}

			});

			Button okButton = (Button) myUserNameDialog
					.findViewById(R.id.ok_button);
			okButton.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					SharedPreferences prefs = getSharedPreferences(
							GGEveApplicationRunner.EVE_PREFERENCES,
							Activity.MODE_PRIVATE);
					SharedPreferences.Editor editor = prefs.edit();
					editor.putString(GGEveApplicationRunner.EVE_USER_ID,
							useridText.getText().toString());
					editor.commit();
					myUserNameDialog.cancel();
				}
			});
			return true;
		}
		case (MENU_APIKEY): {
			if (myAPIKeyDialog == null)
				myAPIKeyDialog = new Dialog(GGEveOverviewActivity.this);
			Window window = myAPIKeyDialog.getWindow();
			window.setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
					WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
			myAPIKeyDialog.setTitle(R.string.enter_api_key);
			myAPIKeyDialog.setContentView(R.layout.apikeydialog);
			myAPIKeyDialog.show();

			final EditText apiKeyText = (EditText) myAPIKeyDialog
					.findViewById(R.id.api_edittext);
			SharedPreferences prefs = getSharedPreferences(
					GGEveApplicationRunner.EVE_PREFERENCES,
					Activity.MODE_PRIVATE);
			String apikey = prefs.getString(
					GGEveApplicationRunner.EVE_PUBLIC_API_KEY, "Not yet set!");
			apiKeyText.setText(apikey);

			Button cancelButton = (Button) myAPIKeyDialog
					.findViewById(R.id.cancel_button);
			cancelButton.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					if (myAPIKeyDialog != null) {
						if (myAPIKeyDialog.isShowing())
							myAPIKeyDialog.cancel();
					}
				}

			});

			Button okButton = (Button) myAPIKeyDialog
					.findViewById(R.id.ok_button);
			okButton.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					SharedPreferences prefs = getSharedPreferences(
							GGEveApplicationRunner.EVE_PREFERENCES,
							Activity.MODE_PRIVATE);
					SharedPreferences.Editor editor = prefs.edit();
					editor.putString(GGEveApplicationRunner.EVE_PUBLIC_API_KEY,
							apiKeyText.getText().toString());
					editor.commit();
					myAPIKeyDialog.cancel();
				}
			});
			return true;
		}
		}
		// Return false if you have not handled the menu item.
		return false;
	}
}