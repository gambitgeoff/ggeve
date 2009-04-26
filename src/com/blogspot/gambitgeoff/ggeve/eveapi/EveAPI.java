package com.blogspot.gambitgeoff.ggeve.eveapi;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class EveAPI {

	public static final int myCharacterID = -1;
	public static final String myAPIKey = "";
	public static final int myUserID = -1;
	
	private CharacterSheet myCharacterSheet;
	
	public EveAPI()
	{
	}

	public static void getLogin(String inUserName, String inAPIKey) {
		try {
			URL eveOnlineURL = new URL("http://api.eve-online.com/eve/SkillTree.xml.aspx");
			HttpURLConnection myConnection = (HttpURLConnection) eveOnlineURL.openConnection();
			myConnection.setRequestMethod("POST");
			String data = "characterID=" + myCharacterID + "&userid=" + myUserID + "&apikey=" + myAPIKey;
			myConnection.setUseCaches(false);
			myConnection.setDoInput(true);
			myConnection.setDoOutput(true);
			DataOutputStream os = new DataOutputStream(myConnection.getOutputStream());
			os.writeBytes(data);
			os.flush();
			os.close();
			
			DataInputStream is = new DataInputStream(myConnection.getInputStream());
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
		if (myCharacterSheet==null)
			myCharacterSheet = new CharacterSheet(myUserID, myAPIKey, myCharacterID);
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
	
	public static void main(String [] args)
	{
		EveAPI api = new EveAPI();
		CharacterSheet sheet = api.getCharacterSheet();
	}
}
