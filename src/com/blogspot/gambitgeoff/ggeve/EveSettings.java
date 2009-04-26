package com.blogspot.gambitgeoff.ggeve;

import com.blogspot.gambitgeoff.ggeve.eveapi.EveAPI;

/**
 * A class used for storing common settings 
 * information for use within the GGEve application.
 * 
 * @author geoffrey
 *
 */
public class EveSettings {
	
	/**
	 * This is the public API key which can be 
	 * retrieved from the Internet
	 */
	private String myEveAPIKey;
	private int myUserID, myCharacterID;
	
	public EveSettings()
	{
	}
	
	public void setEveAPIKey(String inEveAPIKey)
	{
		myEveAPIKey = inEveAPIKey; 
	}
	
	public String getEveAPIKey()
	{
		return myEveAPIKey;
	}
	
	public int getCharacterID()
	{
		return myCharacterID;
	}
	
	public int getUserID()
	{
		return myUserID;
	}

}
