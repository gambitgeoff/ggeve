package com.blogspot.gambitgeoff.ggeve;

import android.app.Activity;
import android.app.Dialog;
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

public class GGEveOverviewActivity extends Activity {

	private static final int MENU_APIKEY = 234;
	private static final int MENU_USERNAME = 83485;

	private Dialog myAPIKeyDialog;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainoverview);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		SubMenu sub = menu.addSubMenu(0, 0, Menu.NONE, R.string.menu_item);
		sub.setHeaderIcon(R.drawable.menu_item_icon);
		sub.setIcon(R.drawable.menu_item_icon);
		sub.add(0, MENU_APIKEY, Menu.NONE,
				R.string.enter_api_key);
		sub.add(0, MENU_USERNAME, Menu.NONE,
				R.string.enter_username);

		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		// Find which menu item has been selected
		switch (item.getItemId()) {
		// Check for each known menu item
		case (MENU_APIKEY): {
			if (myAPIKeyDialog == null)
				myAPIKeyDialog = new Dialog(GGEveOverviewActivity.this);
			Window window = myAPIKeyDialog.getWindow();
			window.setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
					WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
			myAPIKeyDialog.setTitle(R.string.enter_api_key);
			myAPIKeyDialog.setContentView(R.layout.apikeydialog);
			myAPIKeyDialog.show();
			
			final EditText apiKeyText = (EditText) myAPIKeyDialog.findViewById(R.id.api_edittext);
			SharedPreferences prefs = getSharedPreferences(GGEveApplicationRunner.EVE_PREFERENCES, Activity.MODE_PRIVATE);
			String apikey = prefs.getString(GGEveApplicationRunner.EVE_PUBLIC_API_KEY, "Not yet set!");
			apiKeyText.setText(apikey);

			Button cancelButton = (Button) myAPIKeyDialog
					.findViewById(R.id.cancel_button);
			cancelButton.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
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
					// TODO Auto-generated method stub
					SharedPreferences prefs = getSharedPreferences(GGEveApplicationRunner.EVE_PREFERENCES, Activity.MODE_PRIVATE);
					SharedPreferences.Editor editor = prefs.edit();
					editor.putString(GGEveApplicationRunner.EVE_PUBLIC_API_KEY, apiKeyText.getText().toString());
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