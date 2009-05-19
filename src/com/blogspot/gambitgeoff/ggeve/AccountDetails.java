package com.blogspot.gambitgeoff.ggeve;

public class AccountDetails {
	
	private String myAPIKey;
	private int myUserID;
	
	public AccountDetails(String inAPIKey, int inUserID)
	{
		myAPIKey = inAPIKey;
		myUserID = inUserID;
	}
	
	public String getAPIKey()
	{
		return myAPIKey;
	}
	
	public int getUserID()
	{
		return myUserID;
	}

}
