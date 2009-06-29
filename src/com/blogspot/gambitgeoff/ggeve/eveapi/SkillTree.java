package com.blogspot.gambitgeoff.ggeve.eveapi;

import java.util.Vector;

public class SkillTree {
	
	private Vector<SkillGroup> mySkillGroups;
	
	public SkillTree()
	{
		mySkillGroups = new Vector<SkillGroup>();
	}
	
	public void addSkillGroup(SkillGroup inSkillGroup)
	{
		if (!mySkillGroups.contains(inSkillGroup))
			mySkillGroups.add(inSkillGroup);
	}
	
	public Vector<SkillGroup> getSkillGroups()
	{
		return mySkillGroups;
	}
	
	public SkillGroup getSkillGroup(int inGroupID)
	{
		for (SkillGroup sg: mySkillGroups)
		{
			if (sg.getGroupID()==inGroupID)
				return sg;
		}
		return null;
	}
	
	public void addSkill(Skill inSkill)
	{
		int groupID = inSkill.getGroupID();
		SkillGroup group = getSkillGroup(groupID);
		if (group!=null)
		{
			group.addSkill(inSkill);
		}
	}

}
