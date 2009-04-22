package com.blogspot.gambitgeoff.ggeve;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class GGEveOverviewActivity extends Activity {

	static final private int MENU_ITEM = Menu.FIRST;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// Group ID
		int groupId = 0;
		// Unique menu item identifier. Used for event handling.
		int menuItemId = MENU_ITEM;
		// The order position of the item
		int menuItemOrder = Menu.NONE;
		// Text to be displayed for this menu item.
		int menuItemText = R.string.menu_item;
		// Create the menu item and keep a reference to it.
		MenuItem menuItem = menu.add(groupId, menuItemId, menuItemOrder,
				menuItemText);
		menuItem.setIcon(R.drawable.menu_item_icon);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
    	  super.onOptionsItemSelected(item);
    	  // Find which menu item has been selected
    	  switch (item.getItemId()) {
    	    // Check for each known menu item
    	    case (MENU_ITEM):
    	      //TODO [ ... Perform menu handler actions ... ]
    	      return true;
    	  }
    	  // Return false if you have not handled the menu item.
    	  return false;
    	}

}