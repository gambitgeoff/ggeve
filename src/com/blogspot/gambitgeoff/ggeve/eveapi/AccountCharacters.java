package com.blogspot.gambitgeoff.ggeve.eveapi;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.blogspot.gambitgeoff.ggeve.*;

public class AccountCharacters {

	// http://api.eve-online.com/account/Characters.xml.aspx?userid=XXXXXXXX&apikey=XXXXXXXXXXXXXXXXXXXXX
	private Vector<EveCharacter> myCharacters;
	private int myUserID;
	private String myAPIKey;
	private boolean myIsRunningOffline;

	public AccountCharacters(int inUserID, String inAPIKey)
			throws EveAuthenticationException {
		myUserID = inUserID;
		myAPIKey = inAPIKey;
		myCharacters = new Vector<EveCharacter>();
		myIsRunningOffline = GGEveApplicationRunner.getIsRunningOffline();
		setupAccountCharacters();

	}

	public Vector<EveCharacter> getCharacters() {
		for (EveCharacter ec: myCharacters)
		{
			ec.setUserID(myUserID);
		}
		return myCharacters;
	}

	public void setupAccountCharacters()
	{
	try {
		URL eveOnlineURL = null;
		HttpURLConnection myConnection = null;
		if (!myIsRunningOffline)
		{
		eveOnlineURL = new URL("http://api.eve-online.com/account/Characters.xml.aspx");
		myConnection = (HttpURLConnection) eveOnlineURL.openConnection();
		myConnection.setRequestMethod("POST");
		String data = "userid=" + myUserID + "&apikey=" + myAPIKey;
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
				AccountCharactersEventHandler handler = new AccountCharactersEventHandler();
				reader.setContentHandler(handler);
				InputSource is = null;
				if (!myIsRunningOffline)
				{is = new InputSource(myConnection.getInputStream());}
				else
				{is = new InputSource(GGEveApplicationRunner.getGGEveOfflineCharacters());}
				reader.parse(is);
				myCharacters = handler.getAccountCharacters();
				
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
