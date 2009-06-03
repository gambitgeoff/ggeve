package com.blogspot.gambitgeoff.ggeve;

import java.text.NumberFormat;
import java.util.Vector;

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
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class GGEveOverviewActivity extends Activity {

	private static final int MENU_ADD_ACCOUNT = 100;
	private static final int MENU_LIST_ACCOUNTS = 101;
	private static final int MENU_RESETDB = 102;
	private static final int MENU_HELP = 103;
	private GGEveDBAdapter myGGEveDBAdapter;

	private Dialog myAddAccountDialog, myListAccountsDialog, myHelpMenuDialog;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		myGGEveDBAdapter = GGEveApplicationRunner.getDatabaseAdapter();
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
						GGEveOverviewActivity.this.loadCharacter((String) v.getTag());
					}
				});
			}
		}
	}

	private void loadCharacter(String inCharacter) {
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
		sub.add(0, MENU_HELP, Menu.NONE, "Help");

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
		case(MENU_HELP):{
			if (myHelpMenuDialog == null)
				myHelpMenuDialog = new Dialog(GGEveOverviewActivity.this);
			Window window = myHelpMenuDialog.getWindow();
//			SimpleCursorAdapter sca = new SimpleCursorAdapter(GGEveOverviewActivity.this, R.layout.list_accounts_dialog, myGGEveDBAdapter
//					.getAllAccounts(), new String[] { AccountDetails.KEY_ACCOUNT_USERID }, new int[] { R.id.a1, R.id.a2, R.id.a3, R.id.a4,
//					R.id.a5 });
			window.setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND, WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
			myHelpMenuDialog.setContentView(R.layout.ggeve_help);
			myHelpMenuDialog.show();
			
			return true;
		}
		case (MENU_LIST_ACCOUNTS): {
			if (myListAccountsDialog == null)
				myListAccountsDialog = new Dialog(GGEveOverviewActivity.this);
			Window window = myListAccountsDialog.getWindow();
//			SimpleCursorAdapter sca = new SimpleCursorAdapter(GGEveOverviewActivity.this, R.layout.list_accounts_dialog, myGGEveDBAdapter
//					.getAllAccounts(), new String[] { AccountDetails.KEY_ACCOUNT_USERID }, new int[] { R.id.a1, R.id.a2, R.id.a3, R.id.a4,
//					R.id.a5 });
			window.setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND, WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
			myListAccountsDialog.setContentView(R.layout.list_accounts_dialog);
			myListAccountsDialog.show();
			return true;
		}
		case (MENU_ADD_ACCOUNT): {
			if (myAddAccountDialog == null)
				myAddAccountDialog = new Dialog(GGEveOverviewActivity.this);
			Window window = myAddAccountDialog.getWindow();
			window.setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND, WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
			// myAddAccountDialog.setTitle(R.string.AddAccount);
			myAddAccountDialog.setContentView(R.layout.add_account_dialog);
			myAddAccountDialog.show();

			Button okButton = (Button) myAddAccountDialog.findViewById(R.id.ok_button);
			okButton.setOnClickListener(new OnClickListener() {
				// get the account details
				// add the account to the database.
				// close the window
				public void onClick(View v) {
					if (myAddAccountDialog.isShowing()) {
						try {
							final String publickey = ((EditText) GGEveOverviewActivity.this.findViewById(R.id.publickey)).toString();
							final String privatekey = ((EditText) GGEveOverviewActivity.this.findViewById(R.id.privatekey)).toString();
							final int userid = Integer.parseInt(((EditText) GGEveOverviewActivity.this.findViewById(R.id.userid))
									.toString());
							AccountDetails ad = new AccountDetails(userid, publickey, privatekey);
							myGGEveDBAdapter.updateAccountDetails(ad);
							myAddAccountDialog.dismiss();
						} catch (Exception e) {
							myAddAccountDialog.dismiss();
							e.printStackTrace();
						}
					}
				}
			});
			return true;
		}
		}
		// Return false if you have not handled the menu item.
		return false;
	}
}