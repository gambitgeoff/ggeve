package com.blogspot.gambitgeoff.ggeve;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.blogspot.gambitgeoff.ggeve.eveapi.SkillTree;
import com.blogspot.gambitgeoff.ggeve.eveapi.SkillTreeEventHandler;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class GGEveOverviewActivity extends Activity {

	private static final int MENU_ADD_ACCOUNT = 100;
	private static final int MENU_LIST_ACCOUNTS = 101;
	private static final int MENU_RESETDB = 102;
	private static final int MENU_HELP = 103;
	private static final int MENU_ACTIONS = 104;
	private static final int MENU_ACTIONS_UPDATE_SKILL_DB = 105;
	private GGEveDBAdapter myGGEveDBAdapter;

	private Dialog myListAccountsDialog, myHelpMenuDialog, myAddAccountDialog;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		myGGEveDBAdapter = GGEveApplicationRunner.getDatabaseAdapter();
		setContentView(R.layout.mainoverview);
		setupButtonNames();
		updateOverallISK();
		updateServerStatus();
		NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		manager.cancel(1);
	}

	private void updateServerStatus() {
		StatusInformation si = myGGEveDBAdapter.getServerStatus();
		String serverStatus = "";
		String currentPlayers = "";
		if (si == null) {
			serverStatus = "Server Status: Unknown";
			currentPlayers = "Current Players: Unknown";
		} else {
			if (si.getIsOnline()) {
				serverStatus = "Server Status: Online";
				currentPlayers = "Current Players: " + si.getNumberOfPlayers();
			} else {
				serverStatus = "Server Status: Offline";
				currentPlayers = "Current Players: 0";
			}
		}
		TextView tv = (TextView) GGEveOverviewActivity.this
				.findViewById(R.id.server_status);
		tv.setText(serverStatus);
		tv = (TextView) GGEveOverviewActivity.this
				.findViewById(R.id.current_players);
		tv.setText(currentPlayers);
	}

	private void updateOverallISK() {
		long totalISK = 0;
		Vector<EveCharacter> chars = myGGEveDBAdapter.getEveCharacters();
		for (EveCharacter ec : chars) {
			totalISK += (long) ec.getBalance();
		}
		TextView tv = (TextView) GGEveOverviewActivity.this
				.findViewById(R.id.combined_isk);
		tv.setText("Combined Wealth: "
				+ NumberFormat.getInstance().format(totalISK) + " ISK");
	}

	private void setupButtonNames() {
		Gallery gallery = (Gallery) GGEveOverviewActivity.this
				.findViewById(R.id.character_gallery);
		gallery.setAdapter(new ImageAdapter(GGEveOverviewActivity.this));
		final Vector<EveCharacter> chars = myGGEveDBAdapter.getEveCharacters();
		gallery.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				// Toast.makeText(GGEveOverviewActivity.this, "" + position,
				// Toast.LENGTH_SHORT).show();
				loadCharacter(chars.get(position).getCharacterName());
			}
		});

	}

	public void loadCharacter(String inCharacter) {
		SharedPreferences prefs = getSharedPreferences(
				GGEveApplicationRunner.EVE_PREFERENCES, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(GGEveApplicationRunner.EVE_CURRENT_CHARACTER,
				inCharacter);
		editor.commit();
		Intent characterInfoActivity = new Intent(this,
				CharacterInfoActivity.class);
		startActivity(characterInfoActivity);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		SubMenu settings = menu.addSubMenu(0, 0, Menu.NONE, "Settings");
		settings.setHeaderIcon(R.drawable.menu_item_icon);
		settings.setIcon(R.drawable.menu_item_icon);
//		settings.add(0, MENU_ADD_ACCOUNT, Menu.NONE, R.string.AddAccount);
//		settings.add(0, 84724422, Menu.NONE, "Change Update Interval");
//		settings.add(0, MENU_RESETDB, Menu.NONE, "Reset Database");

		SubMenu help = menu.addSubMenu(0, 0, Menu.NONE, "Help");
		help.setHeaderIcon(R.drawable.menu_item_icon);
		help.setIcon(R.drawable.menu_item_icon);
		help.add(0, MENU_HELP, Menu.NONE, "Help");
		
		SubMenu actions = menu.addSubMenu(0, 0, Menu.NONE, "Actions");
		actions.setHeaderIcon(R.drawable.menu_item_icon);
		actions.setIcon(R.drawable.menu_item_icon);
//		actions.add(0, MENU_ACTIONS, Menu.NONE, "Update Character Info");
		actions.add(0, MENU_ACTIONS_UPDATE_SKILL_DB, Menu.NONE, "Update Skills DB");
//		actions.add(0, MENU_ACTIONS, Menu.NONE, "Update Server Status");
		actions.add(0, MENU_RESETDB, Menu.NONE, "Reset DB");

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
		case (MENU_HELP): {
			if (myHelpMenuDialog == null)
				myHelpMenuDialog = new Dialog(GGEveOverviewActivity.this);
			Window window = myHelpMenuDialog.getWindow();
			// SimpleCursorAdapter sca = new
			// SimpleCursorAdapter(GGEveOverviewActivity.this,
			// R.layout.list_accounts_dialog, myGGEveDBAdapter
			// .getAllAccounts(), new String[] {
			// AccountDetails.KEY_ACCOUNT_USERID }, new int[] { R.id.a1,
			// R.id.a2, R.id.a3, R.id.a4,
			// R.id.a5 });
			window.setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
					WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
			myHelpMenuDialog.setContentView(R.layout.ggeve_help);
			myHelpMenuDialog.show();

			return true;
		}
		case (MENU_LIST_ACCOUNTS): {
			if (myListAccountsDialog == null)
				myListAccountsDialog = new Dialog(GGEveOverviewActivity.this);
			Window window = myListAccountsDialog.getWindow();
			// SimpleCursorAdapter sca = new
			// SimpleCursorAdapter(GGEveOverviewActivity.this,
			// R.layout.list_accounts_dialog, myGGEveDBAdapter
			// .getAllAccounts(), new String[] {
			// AccountDetails.KEY_ACCOUNT_USERID }, new int[] { R.id.a1,
			// R.id.a2, R.id.a3, R.id.a4,
			// R.id.a5 });
			window.setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
					WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
			myListAccountsDialog.setContentView(R.layout.list_accounts_dialog);
			myListAccountsDialog.show();
			return true;
		}
		case (MENU_ADD_ACCOUNT): {
			 if (myAddAccountDialog == null)
			 myAddAccountDialog = new Dialog(GGEveOverviewActivity.this);
			 Window window = myAddAccountDialog.getWindow();
			 window.setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
			 WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
			 // myAddAccountDialog.setTitle(R.string.AddAccount);
			 myAddAccountDialog.setContentView(R.layout.add_account_dialog);
			 myAddAccountDialog.show();
			
			 Button okButton = (Button)
			 myAddAccountDialog.findViewById(R.id.ok_button);
			 okButton.setOnClickListener(new OnClickListener() {
			 // get the account details
			 // add the account to the database.
			 // close the window
			 public void onClick(View v) {
			 if (myAddAccountDialog.isShowing()) {
			 try {
			 final String publickey = ((EditText)
			 GGEveOverviewActivity.this.findViewById(R.id.publickey)).toString();
			 final String privatekey = ((EditText)
			 GGEveOverviewActivity.this.findViewById(R.id.privatekey)).toString();
			 final int userid = Integer.parseInt(((EditText)
			 GGEveOverviewActivity.this.findViewById(R.id.userid))
			 .toString());
			 AccountDetails ad = new AccountDetails(userid, publickey,
			 privatekey);
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
		case (MENU_ACTIONS_UPDATE_SKILL_DB):
		{
			updateSkillTree();
		}
		}
		// Return false if you have not handled the menu item.
		return false;
	}
	
	private void updateSkillTree()
	{
		if (!GGEveApplicationRunner.getIsRunningOffline()) {
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet("http://api.eve-online.com/eve/SkillTree.xml.aspx");
			HttpResponse response = null;
			try {
				response = client.execute(request);
			} catch (ClientProtocolException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			int status = response.getStatusLine().getStatusCode();
			System.out.println("httpresponse error status: " + status);
			if (status == HttpStatus.SC_OK) {
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				try {
					DocumentBuilder db = dbf.newDocumentBuilder();
					db.reset();
					try {
						SAXParserFactory spf = SAXParserFactory.newInstance();
						SAXParser sp = spf.newSAXParser();
						XMLReader reader = sp.getXMLReader();
						SkillTreeEventHandler handler = new SkillTreeEventHandler();
						reader.setContentHandler(handler);
						InputSource is = null;
						try {
							is = new InputSource(response.getEntity().getContent());
						} catch (IllegalStateException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						try {
							reader.parse(is);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						SkillTree tree = handler.getSkillTree();
						myGGEveDBAdapter.updateSkillTree(tree);

					} catch (SAXException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
}