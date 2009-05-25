package com.blogspot.gambitgeoff.ggeve;

public class AccountDetails {
	
	private String myAPIKey;
	private int myUserID;
	
	public static final String KEY_ACCOUNT_USERID = "UserID";
	public static final String KEY_ACCOUNT_APIKEY = "APIKey";
	
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
