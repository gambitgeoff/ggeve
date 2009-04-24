package com.blogspot.gambitgeoff.ggeve;

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
	private static String myEveAPIKey;
	
	public EveSettings()
	{
		
	}
	
	public static void setEveAPIKey(String inEveAPIKey)
	{
		myEveAPIKey = inEveAPIKey; 
	}
	
	public static String getEveAPIKey()
	{
		return myEveAPIKey;
	}

}
