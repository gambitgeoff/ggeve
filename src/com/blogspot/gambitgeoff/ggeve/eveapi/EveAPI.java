package com.blogspot.gambitgeoff.ggeve.eveapi;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.drawable.Drawable;

public class EveAPI {

	public static final int myCharacterID = -1;
	public static final String myAPIKey = "";
	public static final int myUserID = -1;

	private CharacterSheet myCharacterSheet;

	public EveAPI() {
	}

	/**
	 * http://img.eve.is/serv.asp?s=64&c=1040214115
	 * 
	 * @return
	 */
//	public static Drawable getCharacterDrawable64(String inCharacterID) {
//		try {
//			URL eveOnlineURL = new URL("http://img.eve.is/serv.asp");
//			HttpURLConnection myConnection = (HttpURLConnection) eveOnlineURL
//					.openConnection();
//			myConnection.setRequestMethod("POST");
//			String data = "s=64&c=" + inCharacterID;
//			myConnection.setUseCaches(false);
//			myConnection.setDoInput(true);
//			myConnection.setDoOutput(true);
//			DataOutputStream os = new DataOutputStream(myConnection.getOutputStream());
//			os.writeBytes(data);
//			os.flush();
//			os.close();
//			
//			return Drawable.createFromStream(myConnection.getInputStream(), "CharacterImage");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
//	}
	
	/**
	 * http://img.eve.is/serv.asp?s=64&c=1040214115
	 * 
	 * @return
	 */
	public static Drawable getCharacterImage(int inCharacterID) {
		
		File f = new File("sdcard/" + inCharacterID + ".jpg");
		if (f.exists())
		{
				return Drawable.createFromPath("/sdcard/"+inCharacterID + ".jpg");
		}
		else
		{
			try {
				URL eveOnlineURL = new URL("http://img.eve.is/serv.asp");
				HttpURLConnection myConnection = (HttpURLConnection) eveOnlineURL.openConnection();
				myConnection.setRequestMethod("POST");
				String data = "s=64&c=" + inCharacterID;
				myConnection.setUseCaches(false);
				myConnection.setDoInput(true);
				myConnection.setDoOutput(true);
				DataOutputStream os = new DataOutputStream(myConnection.getOutputStream());
				os.writeBytes(data);
				os.flush();
				os.close();
				
				InputStream is = myConnection.getInputStream();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				int result=0;
				while((result=is.read())!=-1)
				{
					baos.write(result);
				}
				is.close();
				baos.flush();
				baos.close();
				FileOutputStream fos = new FileOutputStream(f);
				fos.write(baos.toByteArray());
				fos.flush();
				fos.close();
				return Drawable.createFromPath("/sdcard/"+inCharacterID + ".jpg");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		return null;
	}

	public static void getLogin(String inUserName, String inAPIKey) {
		try {
			URL eveOnlineURL = new URL(
					"http://api.eve-online.com/eve/SkillTree.xml.aspx");
			URLConnection tempConnection = eveOnlineURL.openConnection();
			HttpURLConnection myConnection = (HttpURLConnection) tempConnection;
			int responseCode = myConnection.getResponseCode();
			System.out.println("code: " + responseCode);
			myConnection.setRequestMethod("POST");
			String data = "characterID=" + myCharacterID + "&userid="
					+ myUserID + "&apikey=" + myAPIKey;
			myConnection.setUseCaches(false);
			myConnection.setDoInput(true);
			myConnection.setDoOutput(true);
			DataOutputStream os = new DataOutputStream(myConnection
					.getOutputStream());
			os.writeBytes(data);
			os.flush();
			os.close();

			DataInputStream is = new DataInputStream(myConnection
					.getInputStream());
			String line;
			StringBuffer response = new StringBuffer();
			while ((line = is.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			System.out.println("response: " + response);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public AccountJournal getAccountJournal() {
		return null;
	};

	public CharacterSheet getCharacterSheet() {
		if (myCharacterSheet == null)
			myCharacterSheet = new CharacterSheet(myUserID, myAPIKey,
					myCharacterID);
		return myCharacterSheet;
	};

	public GenericData getGenericData() {
		return null;
	};

	public MapInformation getMapInformation() {
		return null;
	};

	public MarketTransactions getMarketTransactions() {
		return null;
	};

	public MemberTrackingData getMemberTrackingData() {
		return null;
	};

	public WalletBalances getWalletBalances() {
		return null;
	};

	public static void main(String[] args) {
		// EveAPI api = new EveAPI();
		// CharacterSheet sheet = api.getCharacterSheet();
	}
}
