package com.blogspot.gambitgeoff.ggeve.eveapi;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.blogspot.gambitgeoff.ggeve.GGEveApplicationRunner;



/**
 * −
<eveapi version="2">
<currentTime>2009-06-08 12:53:28</currentTime>
−
<result>
<serverOpen>True</serverOpen>
<onlinePlayers>24072</onlinePlayers>
</result>
<cachedUntil>2009-06-08 12:56:28</cachedUntil>
</eveapi>
 * @author geoffrey
 *
 */

public class ServerStatus {
	
	private boolean myIsOnline = false;
	private int myNumberOfPlayers = -1;
	private Date myCurrentTime, myCachedUntilTime;
	private ServerStatus myServerStatus;
	
	public static final String KEY_IS_ONLINE = "serverOpen";
	public static final String KEY_NUM_PLAYERS = "onlinePlayers";
	public static final String KEY_CURRTIME = "currentTime";
	public static final String KEY_CACHETIME = "cachedUntil";
	
	public ServerStatus()
	{
		
	}
	
	private void updateStatus()
	{
		try {
			URL eveOnlineURL = null;
			HttpURLConnection myConnection = null;
			if (!GGEveApplicationRunner.getIsRunningOffline())
			{
			eveOnlineURL = new URL("http://api.eve-online.com/server/ServerStatus.xml.aspx");
			myConnection = (HttpURLConnection) eveOnlineURL.openConnection();
			myConnection.setRequestMethod("POST");
//			String data = "userid=" + myUserID + "&apikey=" + myAPIKey;
			myConnection.setUseCaches(false);
			myConnection.setDoInput(true);
			myConnection.setDoOutput(true);
//			DataOutputStream os = new DataOutputStream(myConnection.getOutputStream());
//			os.writeBytes(data);
//			os.flush();
//			os.close();
			}
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			try {
				DocumentBuilder db = dbf.newDocumentBuilder();
				db.reset();
				try {
					SAXParserFactory spf = SAXParserFactory.newInstance();
					SAXParser sp = spf.newSAXParser();
					XMLReader reader = sp.getXMLReader();
					ServerStatusEventHandler handler = new ServerStatusEventHandler();
					reader.setContentHandler(handler);
					InputSource is = null;
					if (!GGEveApplicationRunner.getIsRunningOffline())
					{is = new InputSource(myConnection.getInputStream());}
					else
					{is = new InputSource(GGEveApplicationRunner.getGGEveOfflineCharacters());}
					reader.parse(is);
					myServerStatus = handler.getServerStatus();
					
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
	
	public boolean getIsOnline()
	{
		return myIsOnline;
	}
	
	public void setIsOnline(boolean inIsOnline)
	{
		myIsOnline = inIsOnline;
	}
	
	public int getNumberOfPlayers()
	{
		return myNumberOfPlayers;
	}
	
	public void setNumberOfPlayers(int inNumberOfPlayers)
	{
		myNumberOfPlayers = inNumberOfPlayers;
	}
	
	public Date getCurrentTime()
	{
		return myCurrentTime;
	}
	
	public void setCurrentTime(Date inCurrentTime)
	{
		myCurrentTime = inCurrentTime;
	}
	
	public Date getCachedUntilTime()
	{
		return myCachedUntilTime;
	}
	
	public void setCachedUntilTime(Date inCachedUntilTime)
	{
		myCachedUntilTime = inCachedUntilTime;
	}
}
