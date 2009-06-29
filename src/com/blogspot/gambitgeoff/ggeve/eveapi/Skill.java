package com.blogspot.gambitgeoff.ggeve.eveapi;

public class Skill {
	
	public static final String TYPE_NAME = "typeName";
	public static final String TYPE_ID = "typeID";
	public static final String GROUP_ID = "groupID";
	
	private String myName, myDescription;
	private int myTypeID, myGroupID;
	
	public Skill(String inName, int inGroupID, int inTypeID)
	{
		myName = inName;
		myTypeID = inTypeID;
		myGroupID = inGroupID;
	}
	
	public String getName()
	{
		return myName;
	}
	
	public String getDescription()
	{
		return myDescription;
	}
	
	public int getTypeID()
	{
		return myTypeID;
	}
	
	public int getGroupID()
	{
		return myGroupID;
	}

}
