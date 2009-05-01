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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.blogspot.gambitgeoff.ggeve.EveAuthenticationException;
import com.blogspot.gambitgeoff.ggeve.EveCharacter;

public class CharacterSheet {
	//http://api.eve-online.com
	///char/CharacterSheet.xml.aspx
	
	private Document myCharacterSheetDocument;
	
	private int myUserID;
	private int myCharacterID;
	private String myAPIKey;
	private EveCharacter myCharacterSheet;
	
	public CharacterSheet(int inUserID, String inAPIKey, int inCharacterID)
	{
		myUserID = inUserID;
		myCharacterID = inCharacterID;
		myAPIKey = inAPIKey;
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
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			try {
				DocumentBuilder db = dbf.newDocumentBuilder();
				db.reset();
				try {
					EveCharacter tc = new EveCharacter();
					myCharacterSheetDocument = db.parse(myConnection.getInputStream(), "http://api.eve-online.com");
					Element el = myCharacterSheetDocument.getElementById("result");
					NodeList list = el.getChildNodes();

					int size = list.getLength();
					if (size == 0)
						throw new EveAuthenticationException();
					for (int i = 0; i < size; i++) {
					Node n = list.item(i);
						NamedNodeMap map = n.getAttributes();
						int size2 = map.getLength();
						for (int i2=0;i2<size2;i2++)
						{
							Node tempNode = map.item(i2);
							if (tempNode.getNodeName().equals("name"))
							{
								tc.setCharacterName(tempNode.getNodeValue());
							}
							else if (tempNode.getNodeName().equals("characterID"))
							{
								tc.setCharacterID(Integer.parseInt(tempNode.getNodeValue()));
							}
							else if (tempNode.getNodeName().equals("corporationName"))
							{
								tc.setCorporationName(tempNode.getNodeValue());
							}
							else if (tempNode.getNodeName().equals("corporationID"))
							{
								tc.setCorporationID(Integer.parseInt(tempNode.getNodeValue()));
							}
							else if (tempNode.getNodeName().equals("race"))
							{
								tc.setRace(tempNode.getNodeValue());
							}
							else if (tempNode.getNodeName().equals("bloodLine"))
							{
								tc.setBloodline(tempNode.getNodeValue());
							}
							else if (tempNode.getNodeName().equals("gender"))
							{
								tc.setGender(tempNode.getNodeValue());
							}
							else if (tempNode.getNodeName().equals("balance"))
							{
								tc.setBalance(Integer.parseInt(tempNode.getNodeValue()));
							}
						}
						myCharacterSheet = tc;
					}
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
