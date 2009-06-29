package com.blogspot.gambitgeoff.ggeve.eveapi;

import java.util.Vector;

public class SkillGroup {
	
	public static final String SKILLGROUP_NAME = "groupName";
	public static final String SKILLGROUP_ID = "groupID";
	
	private String myGroupName;
	private int myGroupID;
	private Vector<Skill> mySkills;
	
	public SkillGroup(String inGroupName, int inGroupID)
	{
		mySkills = new Vector<Skill>();
		myGroupName = inGroupName;
		myGroupID = inGroupID;
	}
	
	public String getGroupName()
	{
		return myGroupName;
	}
	
	public int getGroupID()
	{
		return myGroupID;
	}
	
	public Vector<Skill> getSkills()
	{
		return mySkills;
	}
	
	public void addSkill(Skill inSkill)
	{
		if (!mySkills.contains(inSkill))
			mySkills.add(inSkill);
	}
	
	public boolean equals(Object inObject)
	{
		if (inObject instanceof SkillGroup)
		{
			SkillGroup sg = (SkillGroup)inObject;
			if (sg.myGroupID==myGroupID)
				return true;
		}
		return false;
	}

}
