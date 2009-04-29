package com.blogspot.gambitgeoff.ggeve;

public class EveCharacter {
	
	private String myName;
	private int myCharacterID;
	
	public EveCharacter(String inName, int inCharacterID)
	{
		myName = inName;
		myCharacterID = inCharacterID;
	}
	
	public String getName()
	{
		return myName;
	}
	
	public int getCharacterID()
	{
		return myCharacterID;
	}

}
