package com.blogspot.gambitgeoff.ggeve;

public class EveCharacter {
	
	public static final String KEY_CHARACTER_NAME = "KEY_CHARACTER_NAME";
	public static final String KEY_CHARACTER_RACE = "KEY_CHARACTER_RACE";
	public static final String KEY_CHARACTER_ID = "KEY_CHARACTER_ID";
	public static final String KEY_CHARACTER_BLOODLINE = "KEY_CHARACTER_BLOODLINE";
	public static final String KEY_CHARACTER_GENDER = "KEY_CHARACTER_GENDER";
	public static final String KEY_CHARACTER_CORP_NAME = "KEY_CHARACTER_CORP_NAME";
	public static final String KEY_CHARACTER_CORP_ID = "KEY_CHARACTER_CORP_ID";
	public static final String KEY_CHARACTER_BALANCE = "KEY_CHARACTER_BALANCE";
	private String myName, myRace, myBloodline, myGender, myCorporationName;
	private int myCharacterID, myCorporationID, myBalance;
	
	public EveCharacter(String inName, int inCharacterID, String inRace, String inBloodline, String inGender, String inCorporationName, int inCorporationID, int inBalance)
	{
		myName = inName;
		myCharacterID = inCharacterID;
		myRace = inRace;
		myBloodline = inBloodline;
		myGender = inGender;
		myCorporationName = inCorporationName;
		myBalance = inBalance;
		myCorporationID = inCorporationID;
	}
	
	/**
	 * This method allows the construction from getting of information from the URL:
	 * http://api.eve-online.com/account/Characters.xml.aspx
	 * @param inName
	 * @param inCharacterID
	 * @param inCorporationName
	 * @param inCorporationID
	 */
	public EveCharacter(String inName, int inCharacterID, String inCorporationName, int inCorporationID)
	{
		myName = inName;
		myCharacterID = inCharacterID;
		myCorporationName = inCorporationName;
		myCorporationID = inCorporationID;
	}
	
	public int getCorporationID()
	{
		return myCorporationID;
	}
	
	public void setCorporationID(int inCorporationID)
	{
		myCorporationID = inCorporationID;
	}
	
	public int getBalance()
	{
		return myBalance;
	}
	
	public void setBalance(int inBalance)
	{
		myBalance = inBalance;
	}
	
	public String getCorporationName()
	{
		return myCorporationName;
	}
	
	public void setCorporationName(String inCorporationName)
	{
		myCorporationName = inCorporationName;
	}
	
	public String getGender()
	{
		return myGender;
	}
	
	public void setGender(String inGender)
	{
		myGender = inGender;
	}
	
	public String getBloodline()
	{
		return myBloodline;
	}
	
	public void setBloodline(String inBloodline)
	{
		myBloodline = inBloodline;
	}
	
	public String getRace()
	{
		return myRace;
	}
	
	public void setRace(String inRace)
	{
		myRace = inRace;
	}
	
	public String getCharacterName()
	{
		return myName;
	}
	
	public void setCharacterName(String inCharacterName)
	{
		myName = inCharacterName;
	}
	
	public int getCharacterID()
	{
		return myCharacterID;
	}
	
	public void setCharacterID(int inCharacterID)
	{
		myCharacterID = inCharacterID;
	}
}
