package com.blogspot.gambitgeoff.ggeve;

import java.util.Date;

public class StatusInformation {
	
	
	private boolean myIsOnline = false;
	private int myNumberOfPlayers = -1;
	private Date myCurrentTime, myCachedUntilTime;
	
	public static final String KEY_IS_ONLINE = "serverOpen";
	public static final String KEY_NUM_PLAYERS = "onlinePlayers";
	public static final String KEY_CURRTIME = "currentTime";
	public static final String KEY_CACHETIME = "cachedUntil";

	public StatusInformation()
	{
		
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
