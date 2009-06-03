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

import com.blogspot.gambitgeoff.ggeve.AccountDetails;
import com.blogspot.gambitgeoff.ggeve.EveCharacter;
import com.blogspot.gambitgeoff.ggeve.GGEveApplicationRunner;
import com.blogspot.gambitgeoff.ggeve.TrainingInformation;

public class SkillInTraining {
	
	private TrainingInformation myTrainingInformation;
	
	public SkillInTraining(int inCharacterID)
	{
		EveCharacter ec = GGEveApplicationRunner.getDatabaseAdapter().getEveCharacter(inCharacterID);
		AccountDetails ad = GGEveApplicationRunner.getDatabaseAdapter().getAccount(ec.getUserID());
		setupSkillInTraining(ad, inCharacterID);
	}
	
	public TrainingInformation getTrainingInformation()
	{
		return myTrainingInformation;
	}

	private void setupSkillInTraining(AccountDetails inAccountDetails, int inCharacterID)
	{
		try {
			URL eveOnlineURL = null;
			HttpURLConnection myConnection = null;
			eveOnlineURL = new URL("http://api.eve-online.com/char/SkillInTraining.xml.aspx");
			myConnection = (HttpURLConnection) eveOnlineURL.openConnection();
			myConnection.setRequestMethod("POST");
			String data = "userid=" + inAccountDetails.getUserID() + "&apikey=" + inAccountDetails.getPublicAPIKey() + "&characterid=" + inCharacterID;
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
					SAXParserFactory spf = SAXParserFactory.newInstance();
					SAXParser sp = spf.newSAXParser();
					XMLReader reader = sp.getXMLReader();
					SkillInTrainingEventHandler handler = new SkillInTrainingEventHandler();
					reader.setContentHandler(handler);
					InputSource is = null;
					if (!GGEveApplicationRunner.getIsRunningOffline())
					{is = new InputSource(myConnection.getInputStream());}
					else
					{is = new InputSource(GGEveApplicationRunner.getGGEveOfflineCharacters());}
					reader.parse(is);
					myTrainingInformation = handler.getTrainingInformation();
					
				} catch (SAXException e) {
					e.printStackTrace();
				}
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
