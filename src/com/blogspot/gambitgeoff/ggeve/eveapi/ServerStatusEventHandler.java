package com.blogspot.gambitgeoff.ggeve.eveapi;

import java.text.SimpleDateFormat;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.blogspot.gambitgeoff.ggeve.GGEveApplicationRunner;
import com.blogspot.gambitgeoff.ggeve.StatusInformation;

/**
 * 
 * @author GambitGeoff
 * 
 * 
 *         <eveapi version="2"> <currentTime>2009-06-08 12:53:28</currentTime> −
 *         <result> <serverOpen>True</serverOpen>
 *         <onlinePlayers>24072</onlinePlayers> </result>
 *         <cachedUntil>2009-06-08 12:56:28</cachedUntil> </eveapi>
 * 
 */
public class ServerStatusEventHandler extends DefaultHandler {

	private StatusInformation myStatusInformation;

	private static final int CURRENT_TAG_SERVER_OPEN = 1;
	private static final int CURRENT_TAG_ONLINE_PLAYERS = 2;
	private static final int CURRENT_TAG_CACHED_UNTIL = 3;
	private static final int CURRENT_TAG_CURRENT_TIME = 4;

	private SimpleDateFormat myEveDateFormat;
	private int myCurrentState = -1;

	ServerStatusEventHandler() {
		myStatusInformation = new StatusInformation();
		myEveDateFormat = GGEveApplicationRunner.getEveDateFormatter();
	}

	public StatusInformation getServerStatus() {
		return myStatusInformation;
	}

	public void startDocument() throws SAXException {
	}

	public void startElement(String inNamespaceURI, String inLocalName, String inQName, Attributes inAttributes) throws SAXException {
		System.out.println("Reading: " + inLocalName);
		if (inLocalName.equals("currentTime")) {
			myCurrentState = CURRENT_TAG_CURRENT_TIME;
		} else if (inLocalName.equals("serverOpen")) {
			myCurrentState = CURRENT_TAG_SERVER_OPEN;
		} else if (inLocalName.equals("onlinePlayers")) {
			myCurrentState = CURRENT_TAG_ONLINE_PLAYERS;
		} else if (inLocalName.equals("cachedUntil")) {
			myCurrentState = CURRENT_TAG_CACHED_UNTIL;
		}
		return;
	}

	public void characters(char ch[], int start, int length) {
		String string = new String(ch, start, length);
		System.out.println("train info: " + string + " current state: " + myCurrentState);
		try {
			switch (myCurrentState) {
			case CURRENT_TAG_SERVER_OPEN: {
				myStatusInformation.setIsOnline(Boolean.parseBoolean(string));
				myCurrentState = -1;
				break;
			}
			case CURRENT_TAG_ONLINE_PLAYERS: {
				myStatusInformation.setNumberOfPlayers(Integer.parseInt(string));
				myCurrentState = -1;
				break;
			}
			case CURRENT_TAG_CACHED_UNTIL: {
				myStatusInformation.setCachedUntilTime(myEveDateFormat.parse(string));
				myCurrentState = -1;
				break;
			}
			case CURRENT_TAG_CURRENT_TIME: {
				myStatusInformation.setCurrentTime(myEveDateFormat.parse(string));
				myCurrentState = -1;
				break;
			}

			}
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
	}

}
