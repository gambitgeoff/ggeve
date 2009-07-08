package com.blogspot.gambitgeoff.ggeve.eveapi;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class SkillTreeEventHandler extends DefaultHandler {
	
	private SkillTree mySkillTree;
//	private String myCurrentElement;
//	private int myCurrentSkillGroupID;
//	private Skill myCurrentSkill;
	
	public SkillTreeEventHandler()
	{
		mySkillTree = new SkillTree();
	}
	
	public void startElement(String inNamespaceURI, String inLocalName, String inQName, Attributes inAttributes)
	{
//		myCurrentElement = inLocalName;
		int index = -1;
		if ((index = inAttributes.getIndex("groupName"))!=-1)//parse a new skill group
		{
			String groupName = inAttributes.getValue(index);
			System.out.println("Adding skill group: " + groupName);
			index = inAttributes.getIndex("groupID");
			int groupID = Integer.parseInt(inAttributes.getValue(index));
//			myCurrentSkillGroupID = groupID;
			mySkillTree.addSkillGroup(new SkillGroup(groupName, groupID));
		}
		else if ((index = inAttributes.getIndex("typeName"))!=-1)//parse a new set of skills
		{
			String skillName = inAttributes.getValue(index);
			
			int groupID = Integer.parseInt(inAttributes.getValue(inAttributes.getIndex("groupID")));
			int typeID = Integer.parseInt(inAttributes.getValue(inAttributes.getIndex("typeID")));
			Skill tempSkill = new Skill(skillName, groupID, typeID);
			SkillGroup sg = mySkillTree.getSkillGroup(groupID);
			System.out.println("Adding skill: " + skillName + " to group: " + sg.getGroupName());
			sg.addSkill(tempSkill);
		}
	}
	
	public void characters(char ch[], int start, int length)
	{
	}
	
	public SkillTree getSkillTree()
	{
		return mySkillTree;
	}

}
