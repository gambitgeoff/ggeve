package com.blogspot.gambitgeoff.ggeve;

public class AccountDetails {
	
	private String myPublicAPIKey, myPrivateAPIKey;
	private int myUserID;
	
	public static final String KEY_ACCOUNT_USERID = "UserID";
	public static final String KEY_ACCOUNT_APIKEY = "APIKey";
	public static final String KEY_ACCOUNT_PRIVATE_KEY = "PrivateKey";
	
	public AccountDetails(int inUserID, String inPublicAPIKey, String inPrivateAPIKey)
	{
		myPublicAPIKey = inPublicAPIKey;
		myPrivateAPIKey = inPrivateAPIKey;
		myUserID = inUserID;
	}
	
	public String getPublicAPIKey()
	{
		return myPublicAPIKey;
	}
	
	public String getPrivateAPIKey()
	{
		return myPrivateAPIKey;
	}
	
	public int getUserID()
	{
		return myUserID;
	}

}
