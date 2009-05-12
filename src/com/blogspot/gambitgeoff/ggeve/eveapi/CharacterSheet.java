package com.blogspot.gambitgeoff.ggeve.eveapi;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.blogspot.gambitgeoff.ggeve.EveAuthenticationException;
import com.blogspot.gambitgeoff.ggeve.EveCharacter;
import com.blogspot.gambitgeoff.ggeve.GGEveApplicationRunner;

public class CharacterSheet {
	//http://api.eve-online.com
	///char/CharacterSheet.xml.aspx
	
	private int myUserID;
	private int myCharacterID;
	private String myAPIKey;
	private EveCharacter myCharacterSheet;
	private boolean myOfflineModeFlag = true;
	
	public CharacterSheet(int inUserID, String inAPIKey, int inCharacterID)
	{
		myUserID = inUserID;
		myCharacterID = inCharacterID;
		myAPIKey = inAPIKey;
		myOfflineModeFlag = Boolean.parseBoolean(GGEveApplicationRunner.getGGEveProperties().getProperty(GGEveApplicationRunner.PROPERTY_GGEVE_OFFLINE_MODE));
		try {
			setupCharacterSheet();
		} catch (EveAuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public EveCharacter getCharacter()
	{
		return myCharacterSheet;
	}
	
	private void setupCharacterSheet() throws EveAuthenticationException
	{
		try {
			HttpURLConnection myConnection = null;
			if (!myOfflineModeFlag)
			{
			URL eveOnlineURL = new URL("http://api.eve-online.com/char/CharacterSheet.xml.aspx");
			myConnection = (HttpURLConnection) eveOnlineURL.openConnection();
			myConnection.setRequestMethod("POST");
			String data = "characterID=" + myCharacterID + "&userid=" + myUserID + "&apikey=" + myAPIKey;
			myConnection.setUseCaches(false);
			myConnection.setDoInput(true);
			myConnection.setDoOutput(true);
			DataOutputStream os = new DataOutputStream(myConnection.getOutputStream());
			os.writeBytes(data);
			os.flush();
			os.close();
			}
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			try {
				DocumentBuilder db = dbf.newDocumentBuilder();
				db.reset();
				try {
					SAXParserFactory spf = SAXParserFactory.newInstance();
					SAXParser sp = spf.newSAXParser();
					XMLReader reader = sp.getXMLReader();
					CharacterSheetEventHandler handler = new CharacterSheetEventHandler();
					reader.setContentHandler(handler);
					InputSource is = null;
					if (!myOfflineModeFlag)
					{is = new InputSource(myConnection.getInputStream());}
					else
					{is = new InputSource(GGEveApplicationRunner.getGGEveOfflineCharacterSheet());}
					reader.parse(is);
					myCharacterSheet = handler.getEveCharacter();
					
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
