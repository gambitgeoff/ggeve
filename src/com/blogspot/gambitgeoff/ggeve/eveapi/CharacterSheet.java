package com.blogspot.gambitgeoff.ggeve.eveapi;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class CharacterSheet {
	//http://api.eve-online.com
	///char/CharacterSheet.xml.aspx
	
	private DocumentBuilder myDocumentBuilder;
	private Document myDocument;
	
	private int myUserID;
	private int myCharacterID;
	private String myAPIKey;
	
	public CharacterSheet(int inUserID, String inAPIKey, int inCharacterID)
	{
		myUserID = inUserID;
		myCharacterID = inCharacterID;
		myAPIKey = inAPIKey;
		try {
			myDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			myDocument = myDocumentBuilder.parse(getCharacterSheetData());
			System.out.println(myDocument.toString());
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String getCharacterSheetData()
	{
		try {
			URL eveOnlineURL = new URL("http://api.eve-online.com/char/CharacterSheet.xml.aspx");
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
			return response.toString();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
