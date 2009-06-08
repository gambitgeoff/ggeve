package com.blogspot.gambitgeoff.ggeve;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Vector;

import com.blogspot.gambitgeoff.ggeve.eveapi.AccountCharacters;
import com.blogspot.gambitgeoff.ggeve.eveapi.CharacterSheet;
import com.blogspot.gambitgeoff.ggeve.eveapi.EveAPI;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class GGEveOverviewActivity extends Activity {

	private static final int MENU_ADD_ACCOUNT = 100;
	private static final int MENU_LIST_ACCOUNTS = 101;
	private static final int MENU_RESETDB = 102;
	private GGEveDBAdapter myGGEveDBAdapter;

	private Dialog myAddAccountDialog, myListAccountsDialog, myInvalidKeyUserIDDialog;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		myGGEveDBAdapter = GGEveApplicationRunner.getDatabaseAdapter();
		//AccountDetails account = GGEveApplicationRunner.getAccountDetails();
//		Vector<AccountDetails> tempAccounts = myGGEveDBAdapter.getAccounts();
//		for (AccountDetails account: tempAccounts){
////		if (account != null) {
//			try {
//				Vector <EveCharacter> accountCharacters = myGGEveDBAdapter.getEveCharacters(account.getUserID());
//				
//				
//				
////				AccountCharacters tempChars = new AccountCharacters(account.getUserID(), account.getPublicAPIKey());
////
////				for (EveCharacter ec : tempChars.getCharacters()) {
////					CharacterSheet cs = new CharacterSheet(account.getUserID(), account.getPublicAPIKey(), ec.getCharacterID());
////					EveCharacter ec2 = cs.getCharacter();
////					myGGEveDBAdapter.updateEveCharacter(ec2);
////				}
//			} catch (EveAuthenticationException e) {
//				myInvalidKeyUserIDDialog = new Dialog(GGEveOverviewActivity.this);
//				Window window = myInvalidKeyUserIDDialog.getWindow();
//				window.setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND, WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
//				myInvalidKeyUserIDDialog.setTitle("Invalid Credentials!");
//				myInvalidKeyUserIDDialog.setContentView(R.layout.invalidapikeyuserid);
//				myInvalidKeyUserIDDialog.show();
//
//				Button ackButton = (Button) myInvalidKeyUserIDDialog.findViewById(R.id.button_acknowledge);
//				ackButton.setOnClickListener(new OnClickListener() {
//					public void onClick(View v) {
//						if (myInvalidKeyUserIDDialog != null) {
//							if (myInvalidKeyUserIDDialog.isShowing()) {
//								myInvalidKeyUserIDDialog.cancel();
//							}
//						}
//					}
//				});
//			}
//		} //else {
//			displayGGEveInitializationInstructions();
//		}
		setContentView(R.layout.mainoverview);
		setupButtonNames();
		updateOverallISK();
	}

	private void updateOverallISK() {
		long totalISK = 0;
		Vector<EveCharacter> chars = myGGEveDBAdapter.getEveCharacters();
		for (EveCharacter ec : chars) {
			totalISK += (long) ec.getBalance();
		}
		TextView tv = (TextView) GGEveOverviewActivity.this.findViewById(R.id.combined_isk);
		tv.setText("Combined Wealth: " + NumberFormat.getInstance().format(totalISK) + " ISK");
	}

	private void displayGGEveInitializationInstructions() {
		Dialog myInitialisationErrorDialog = new Dialog(GGEveOverviewActivity.this);
		Window window = myInitialisationErrorDialog.getWindow();
		window.setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND, WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		myInitialisationErrorDialog.setTitle("Initialisation Error");
		myInitialisationErrorDialog.setContentView(R.layout.initialisationerror);
		myInitialisationErrorDialog.show();
	}

	private void setupButtonNames() {
//		ImageButton[] buttons = new ImageButton[3];
		Gallery gallery = (Gallery)GGEveOverviewActivity.this.findViewById(R.id.character_gallery);
		gallery.setAdapter(new ImageAdapter(GGEveOverviewActivity.this));
		final Vector<EveCharacter> chars = myGGEveDBAdapter.getEveCharacters();
		gallery.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView parent, View v, int position, long id) {
//	            Toast.makeText(GGEveOverviewActivity.this, "" + position, Toast.LENGTH_SHORT).show();
	            loadCharacter(chars.get(position).getCharacterName());
	        }
	    });

	}

	public void loadCharacter(String inCharacter) {
		SharedPreferences prefs = getSharedPreferences(GGEveApplicationRunner.EVE_PREFERENCES, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(GGEveApplicationRunner.EVE_CURRENT_CHARACTER, inCharacter);
		editor.commit();
		Intent characterInfoActivity = new Intent(this, CharacterInfoActivity.class);
		startActivity(characterInfoActivity);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		SubMenu sub = menu.addSubMenu(0, 0, Menu.NONE, R.string.menu_item);
		sub.setHeaderIcon(R.drawable.menu_item_icon);
		sub.setIcon(R.drawable.menu_item_icon);
		sub.add(0, MENU_ADD_ACCOUNT, Menu.NONE, R.string.AddAccount);
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
		case (MENU_LIST_ACCOUNTS): {
//			if (myListAccountsDialog == null)
//				myListAccountsDialog = new Dialog(GGEveOverviewActivity.this);
//			Window window = myListAccountsDialog.getWindow();
//			window.setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND, WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
//			myListAccountsDialog.setContentView(R.layout.list_accounts_dialog);
//			myListAccountsDialog.show();
			return true;
		}
		case (MENU_ADD_ACCOUNT): {
//			if (myAddAccountDialog == null)
//				myAddAccountDialog = new Dialog(GGEveOverviewActivity.this);
//			Window window = myAddAccountDialog.getWindow();
//			window.setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND, WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
//			// myAddAccountDialog.setTitle(R.string.AddAccount);
//			myAddAccountDialog.setContentView(R.layout.add_account_dialog);
//			myAddAccountDialog.show();
//
//			Button okButton = (Button) myAddAccountDialog.findViewById(R.id.ok_button);
//			okButton.setOnClickListener(new OnClickListener() {
//				// get the account details
//				// add the account to the database.
//				// close the window
//				public void onClick(View v) {
//					if (myAddAccountDialog.isShowing()) {
//						try {
//							final String publickey = ((EditText) GGEveOverviewActivity.this.findViewById(R.id.publickey)).toString();
//							final String privatekey = ((EditText) GGEveOverviewActivity.this.findViewById(R.id.privatekey)).toString();
//							final int userid = Integer.parseInt(((EditText) GGEveOverviewActivity.this.findViewById(R.id.userid))
//									.toString());
//							AccountDetails ad = new AccountDetails(userid, publickey, privatekey);
//							myGGEveDBAdapter.updateAccountDetails(ad);
//							myAddAccountDialog.dismiss();
//						} catch (Exception e) {
//							myAddAccountDialog.dismiss();
//							e.printStackTrace();
//						}
//					}
//				}
//			});
			return true;
		}
		}
		// Return false if you have not handled the menu item.
		return false;
	}
}