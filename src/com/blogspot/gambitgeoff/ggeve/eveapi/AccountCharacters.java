package com.blogspot.gambitgeoff.ggeve.eveapi;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.blogspot.gambitgeoff.ggeve.*;

public class AccountCharacters {

	//http://api.eve-online.com/account/Characters.xml.aspx?userid=XXXXXXXX&apikey=XXXXXXXXXXXXXXXXXXXXX
	private Vector<EveCharacter> myCharacters;
	private int myUserID;
	private String myAPIKey;
	private Document myAccountCharactersDocument;
	public AccountCharacters(int inUserID, String inAPIKey) throws EveAuthenticationException {
		myUserID = inUserID;
		myAPIKey = inAPIKey;
		myCharacters = new Vector<EveCharacter>();
		setupAccountCharacters();
	}

	public Vector<EveCharacter> getCharacters() {
		return myCharacters;
	}

	private void setupAccountCharacters() throws EveAuthenticationException {
		try {
			URL eveOnlineURL = new URL(
					"http://api.eve-online.com/account/Characters.xml.aspx");
			URLConnection tempConnection = eveOnlineURL.openConnection();
			HttpURLConnection myConnection = (HttpURLConnection) tempConnection;
			myConnection.setRequestMethod("POST");
			String data = "userid=" + myUserID + "&apikey=" + myAPIKey;
			myConnection.setUseCaches(false);
			myConnection.setDoInput(true);
			myConnection.setDoOutput(true);

			DataOutputStream os = new DataOutputStream(myConnection.getOutputStream());
			System.out.println("created the outputstream!");
			os.writeBytes(data);
			os.flush();
			os.close();
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			try {
				DocumentBuilder db = dbf.newDocumentBuilder();
				try {
					myAccountCharactersDocument = db.parse(myConnection
							.getInputStream(), "http://api.eve-online.com");
					NodeList list = myAccountCharactersDocument
							.getElementsByTagName("row");

					int size = list.getLength();
					if (size == 0)
						throw new EveAuthenticationException();
					for (int i = 0; i < size; i++) {
					Node n = list.item(i);
						NamedNodeMap map = n.getAttributes();
						int size2 = map.getLength();
						String tempName = "", tempCorpName = "";
						int tempCharID = -1, tempCorpID = -1;
						for (int i2=0;i2<size2;i2++)
						{
							Node tempNode = map.item(i2);
							if (tempNode.getNodeName().equals("name"))
							{
								tempName = tempNode.getNodeValue();
							}
							else if (tempNode.getNodeName().equals("characterID"))
							{
								tempCharID = Integer.parseInt(tempNode.getNodeValue());
							}
							else if (tempNode.getNodeName().equals("corporationName"))
							{
								tempCorpName = tempNode.getNodeValue();
							}
							else if (tempNode.getNodeName().equals("corporationID"))
							{
								tempCorpID = Integer.parseInt(tempNode.getNodeValue());
							}
						}
						myCharacters.add(new EveCharacter(tempName, tempCharID, tempCorpName, tempCorpID));
					}
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(myAccountCharactersDocument.toString());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
