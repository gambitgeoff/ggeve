package com.blogspot.gambitgeoff.ggeve;

import java.util.Date;

public class EveCharacter {
	
	public static final String KEY_CHARACTER_NAME = "KEY_CHARACTER_NAME";
	public static final String KEY_CHARACTER_RACE = "KEY_CHARACTER_RACE";
	public static final String KEY_CHARACTER_ID = "KEY_CHARACTER_ID";
	public static final String KEY_CHARACTER_BLOODLINE = "KEY_CHARACTER_BLOODLINE";
	public static final String KEY_CHARACTER_GENDER = "KEY_CHARACTER_GENDER";
	public static final String KEY_CHARACTER_CORP_NAME = "KEY_CHARACTER_CORP_NAME";
	public static final String KEY_CHARACTER_CORP_ID = "KEY_CHARACTER_CORP_ID";
	public static final String KEY_CHARACTER_BALANCE = "KEY_CHARACTER_BALANCE";
	public static final String KEY_CHARACTER_USERID = "KEY_CHARACTER_USERID";
	private String myName, myRace, myBloodline, myGender, myCorporationName, myCharacterImageName;
	private int myCharacterID = -1, myCorporationID = -1, myUserID = -1;
	private double myBalance = -1;
	private TrainingInformation myTrainingInformation;
	
	public EveCharacter()
	{
		
	}
	
	public EveCharacter(String inName, int inCharacterID, String inRace,
						String inBloodline, String inGender, String inCorporationName, 
						int inCorporationID, int inBalance, int inUserID, TrainingInformation inTrainingInformation)
	{
		myName = inName;
		myCharacterID = inCharacterID;
		myRace = inRace;
		myBloodline = inBloodline;
		myGender = inGender;
		myCorporationName = inCorporationName;
		myBalance = inBalance;
		myCorporationID = inCorporationID;
		myUserID = inUserID;
		myTrainingInformation = inTrainingInformation;
	}
	
	/**
	 * This method allows the construction from getting of information from the URL:
	 * http://api.eve-online.com/account/Characters.xml.aspx
	 * @param inName
	 * @param inCharacterID
	 * @param inCorporationName
	 * @param inCorporationID
	 */
	public EveCharacter(String inName, int inCharacterID, String inCorporationName, int inCorporationID, int inUserID)
	{
		myName = inName;
		myCharacterID = inCharacterID;
		myCorporationName = inCorporationName;
		myCorporationID = inCorporationID;
		myUserID = inUserID;
	}
	
	public void setTrainingInformation(TrainingInformation inTrainingInformation)
	{
		myTrainingInformation = inTrainingInformation;
	}
	
	public TrainingInformation getTrainingInformation()
	{
		return myTrainingInformation;
	}
	
	public int getUserID()
	{
		return myUserID;
	}
	
	public void setUserID(int inUserID)
	{
		myUserID = inUserID;
	}
	
	public String getCharacterImageName()
	{
		return myCharacterImageName;
	}
	
	public void setCharacterImageName(String inCharacterImage)
	{
		myCharacterImageName = inCharacterImage;
	}
	
	public int getCorporationID()
	{
		return myCorporationID;
	}
	
	public void setCorporationID(int inCorporationID)
	{
		myCorporationID = inCorporationID;
	}
	
	public double getBalance()
	{
		return myBalance;
	}
	
	public void setBalance(double inBalance)
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
