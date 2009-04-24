package com.blogspot.gambitgeoff.ggeve;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class GGEveOverviewActivity extends Activity {

	static final private int MENU_ITEM = Menu.FIRST;
	private static final int MENU_APIKEY = 234;
	private static final int MENU_USERNAME = 83485;

	private Dialog myAPIKeyDialog;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		SubMenu sub = menu.addSubMenu(0, 0, Menu.NONE, R.string.menu_item);
		sub.setHeaderIcon(R.drawable.menu_item_icon);
		sub.setIcon(R.drawable.menu_item_icon);
		MenuItem submenuItem = sub.add(0, MENU_APIKEY, Menu.NONE,
				R.string.enter_api_key);
		MenuItem submenuItem2 = sub.add(0, MENU_USERNAME, Menu.NONE,
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

			final EditText apiKeyText = (EditText) myAPIKeyDialog
					.findViewById(R.id.api_edittext);
			Button okButton = (Button) myAPIKeyDialog
					.findViewById(R.id.ok_button);
			okButton.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
					EveSettings.setEveAPIKey(apiKeyText.toString());
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