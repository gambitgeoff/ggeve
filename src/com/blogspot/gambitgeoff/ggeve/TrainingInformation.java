package com.blogspot.gambitgeoff.ggeve;

import java.util.Date;

public class TrainingInformation {
	
	private int myCharacterID;
	private Date myCurrentTime;
	private Date myTrainingStartTime, myTrainingEndTime;
	private int myTrainingTypeID;
	private int myTrainingStartSP;
	private int myTrainingDestinationSP;
	private int myTrainingToLevel;
	private int mySkillInTrainingID;
	private String mySkillInTraining;
	private Date myCachedUntil;
	private int myTQTimeOffset;
	private Date myTQTime;
	
	public TrainingInformation()
	{
		
	}
	
	public TrainingInformation(int inCharacterID, Date inCurrentTime, Date inTrainingStartTime, Date inTrainingEndTime, int inTrainingTypeID, int inTrainingStartSP, int inTrainingDestinationSP, int inTrainingToLevel, int inSkillInTrainingID, String inSkillInTraining, Date inCachedUntil, Date inTQTime, int inTQTimeOffset)
	{
		myCharacterID = inCharacterID;
		myCurrentTime = inCurrentTime;
		myTrainingStartTime = inTrainingStartTime;
		myTrainingEndTime = inTrainingEndTime;
		myTrainingTypeID = inTrainingTypeID;
		myTrainingStartSP = inTrainingStartSP;
		myTrainingDestinationSP = inTrainingDestinationSP;
		myTrainingToLevel = inTrainingToLevel;
		mySkillInTrainingID = inSkillInTrainingID;
		mySkillInTraining = inSkillInTraining;
		myCachedUntil = inCachedUntil;
		myTQTime = inTQTime;
		myTQTimeOffset = inTQTimeOffset;
	}
	
	public int getCharacterID()
	{
		return myCharacterID;
	}
	
	public void setCharacterID(int inCharacterID)
	{
		myCharacterID = inCharacterID;
	}
	
	public Date getTrainingStartTime()
	{
		return myTrainingStartTime;
	}
	
	public void setTrainingStartTime(Date inTrainingStartTime)
	{
		myTrainingStartTime = inTrainingStartTime;
	}
	
	public Date getCurrentTime()
	{
		return myCurrentTime;
	}
	
	public void setCurrentTime(Date inCurrentTime)
	{
		myCurrentTime = inCurrentTime;
	}
	
	public Date getTrainingEndTime()
	{
		return myTrainingEndTime;
	}
	
	public void setTrainingEndTime(Date inTrainingEndTime)
	{
		myTrainingEndTime = inTrainingEndTime;
	}
	
	public int getTrainingTypeID()
	{
		return myTrainingTypeID;
	}
	
	public void setTrainingTypeID(int inTrainingTypeID)
	{
		myTrainingTypeID = inTrainingTypeID;
	}
	
	public int getTrainingStartSP()
	{
		return myTrainingStartSP;
	}
	
	public void setTrainingStartSP(int inTrainingStartSP)
	{
		myTrainingStartSP = inTrainingStartSP;
	}
	
	public int getTrainingDestinationSP()
	{
		return myTrainingDestinationSP;
	}
	
	public void setTrainingDestinationSP(int inDestinationSP)
	{
		myTrainingDestinationSP = inDestinationSP;
	}
	
	public int getTrainingToLevel()
	{
		return myTrainingToLevel;
	}
	
	public void setTrainingToLevel(int inTrainingToLevel)
	{
		myTrainingToLevel = inTrainingToLevel;
	}
	
	public int getSkillInTrainingID()
	{
		return mySkillInTrainingID;
	}
	
	public void setSkillInTrainingID(int inSkillInTrainingID)
	{
		mySkillInTrainingID = inSkillInTrainingID;
	}
	
	public String getSkillInTraining()
	{
		return mySkillInTraining;
	}
	
	public void setSkillInTraining(String inSkillInTraining)
	{
		mySkillInTraining = inSkillInTraining;
	}
	
	public Date getCachedUntil()
	{
		return myCachedUntil;
	}
	
	public void setCachedUntil(Date inCachedUntil)
	{
		myCachedUntil = inCachedUntil;
	}
	
	public Date getTQTime()
	{
		return myTQTime;
	}
	
	public void setTQTime(Date inTQTime)
	{
		myTQTime = inTQTime;
	}
	
	public int getTQTimeOffset()
	{
		return myTQTimeOffset;
	}
	
	public void setTQTimeOffset(int inTQTimeOffset)
	{
		myTQTimeOffset = inTQTimeOffset;
	}
}