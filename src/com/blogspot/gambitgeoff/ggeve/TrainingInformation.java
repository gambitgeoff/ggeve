package com.blogspot.gambitgeoff.ggeve;

import java.util.Date;

public class TrainingInformation {
	
	private int myCharacterID = -1;
	private Date myCurrentTime = null;
	private Date myTrainingStartTime = null, myTrainingEndTime = null;
	private int myTrainingTypeID = -1;
	private int myTrainingStartSP = -1;
	private int myTrainingDestinationSP = -1;
	private int myTrainingToLevel = -1;
	private int mySkillInTrainingID = -1;
	private int mySkillInTraining = -1;
	private Date myCachedUntil = null;
	private int myTQTimeOffset = -1;
	private Date myTQTime = null;
	
	public static final String KEY_CHARACTER_ID = "characterID", KEY_CURRENT_TIME = "currentTime", KEY_TRAIN_START_TIME = "trainingStartTime", KEY_TRAIN_END_TIME = "trainingEndTime";
	public static final String KEY_TRAIN_TYPE_ID = "trainingTypeID", KEY_TRAIN_START_SP = "trainingStartSP", KEY_TRAIN_END_SP = "trainingEndSP", KEY_TRAIN_TO_LEVEL = "trainingToLevel";
	public static final String KEY_SKILL_ID = "skillInTrainingID", KEY_SKILL_IN_TRAINING = "skillInTraining", KEY_CACHED_UNTIL = "cachedUntil";
	public static final String KEY_TQTIME = "tqTime", KEY_TQTIME_OFFSET = "tqTimeOffset";
	
	
	
	public TrainingInformation()
	{
		
	}
	
	public TrainingInformation(int inCharacterID, Date inCurrentTime, Date inTrainingStartTime, Date inTrainingEndTime, int inTrainingTypeID, int inTrainingStartSP, int inTrainingDestinationSP, int inTrainingToLevel, int inSkillInTrainingID, int inSkillInTraining, Date inCachedUntil, Date inTQTime, int inTQTimeOffset)
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
	
	public int getSkillInTraining()
	{
		return mySkillInTraining;
	}
	
	public void setSkillInTraining(int inSkillInTraining)
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