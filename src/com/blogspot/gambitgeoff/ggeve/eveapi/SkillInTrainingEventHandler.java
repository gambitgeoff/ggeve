package com.blogspot.gambitgeoff.ggeve.eveapi;

import java.text.SimpleDateFormat;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.blogspot.gambitgeoff.ggeve.GGEveApplicationRunner;
import com.blogspot.gambitgeoff.ggeve.TrainingInformation;

/**
 * 
 * @author geoffrey
 * 
 * 
 *        − <eveapi version="2"> <currentTime>2009-05-21 12:48:16</currentTime>
 *         − <result> <currentTQTime offset="0">2009-05-21
 *         12:48:16</currentTQTime> <trainingEndTime>2009-05-27
 *         15:27:48</trainingEndTime> <trainingStartTime>2009-05-20
 *         03:34:12</trainingStartTime> <trainingTypeID>22807</trainingTypeID>
 *         <trainingStartSP>90510</trainingStartSP>
 *         <trainingDestinationSP>512000</trainingDestinationSP>
 *         <trainingToLevel>5</trainingToLevel>
 *         <skillInTraining>1</skillInTraining> </result>
 *         <cachedUntil>2009-05-21 13:48:16</cachedUntil> </eveapi>
 * 
 */
public class SkillInTrainingEventHandler extends DefaultHandler {

	private TrainingInformation myTrainingInformation;

	private static final int CURRENT_TAG_EVE_API = 0;
	private static final int CURRENT_TAG_CURRENT_TIME = 1;
	private static final int CURRENT_TAG_RESULT = 2;
	private static final int CURRENT_TAG_CURRENT_TQTIME = 3;
	private static final int CURRENT_TAG_TRAINING_END_TIME = 4;
	private static final int CURRENT_TAG_TRAINING_START_TIME = 5;
	private static final int CURRENT_TAG_TRAINING_TYPE_ID = 6;
	private static final int CURRENT_TAG_TRAINING_START_SP = 7;
	private static final int CURRENT_TAG_DESTINATION_SP = 8;
	private static final int CURRENT_TAG_TRAINING_TO_LEVEL = 9;
	private static final int CURRENT_TAG_SKILL_IN_TRAINING = 10;
	private static final int CURRENT_TAG_CACHED_UNTIL = 11;
	private SimpleDateFormat myEveDateFormat;
	private int myCurrentState = -1;

	SkillInTrainingEventHandler() {
		myTrainingInformation = new TrainingInformation();
		myEveDateFormat = GGEveApplicationRunner.getEveDateFormatter();
	}

	public TrainingInformation getTrainingInformation() {
		return myTrainingInformation;
	}

	public void startDocument() throws SAXException {
	}

	public void startElement(String inNamespaceURI, String inLocalName, String inQName, Attributes inAttributes) throws SAXException {
		System.out.println("Reading: " + inLocalName);
		if (inLocalName.equals("eveapi")) {
			myCurrentState = CURRENT_TAG_EVE_API;
		} else if (inLocalName.equals("currentTime")) {
			myCurrentState = CURRENT_TAG_CURRENT_TIME;
		} else if (inLocalName.equals("currentTQTime")) {
			myCurrentState = CURRENT_TAG_CURRENT_TQTIME;
		} else if (inLocalName.equals("trainingEndTime")) {
			myCurrentState = CURRENT_TAG_TRAINING_END_TIME;
		} else if (inLocalName.equals("trainingStartTime")) {
			myCurrentState = CURRENT_TAG_TRAINING_START_TIME;
		} else if (inLocalName.equals("trainingTypeID")) {
			myCurrentState = CURRENT_TAG_TRAINING_TYPE_ID;
		} else if (inLocalName.equals("trainingStartSP")) {
			myCurrentState = CURRENT_TAG_TRAINING_START_SP;
		} else if (inLocalName.equals("trainingDestinationSP")) {
			myCurrentState = CURRENT_TAG_DESTINATION_SP;
		} else if (inLocalName.equals("trainingToLevel")) {
			myCurrentState = CURRENT_TAG_TRAINING_TO_LEVEL;
		} else if (inLocalName.equals("skillInTraining")) {
			myCurrentState = CURRENT_TAG_SKILL_IN_TRAINING;
		} else if (inLocalName.equals("result")) {
			myCurrentState = CURRENT_TAG_RESULT;
		} else if (inLocalName.equals("cachedUntil")) {
			myCurrentState = CURRENT_TAG_CACHED_UNTIL;
		}
		return;
	}

	public void characters(char ch[], int start, int length) {
		String string = new String(ch, start, length);
		System.out.println("train info: " + string + " current state: " + myCurrentState);
		try {
			switch (myCurrentState) {
			case CURRENT_TAG_CACHED_UNTIL: {
				myTrainingInformation.setCachedUntil(myEveDateFormat.parse(string)); // parse
																						// string
				myCurrentState = -1;
				break;
			}
			case CURRENT_TAG_CURRENT_TIME: {
				this.myTrainingInformation.setCurrentTime(myEveDateFormat.parse(string));
				myCurrentState = -1;
				break;
			}
			case CURRENT_TAG_CURRENT_TQTIME: {
				myTrainingInformation.setTQTime(myEveDateFormat.parse(string));
				myCurrentState = -1;
				break;
			}
			case CURRENT_TAG_DESTINATION_SP: {
				myTrainingInformation.setTrainingDestinationSP(Integer.parseInt(string));
				myCurrentState = -1;
				break;
			}
			case CURRENT_TAG_EVE_API: {
				myCurrentState = -1;
				break;
			}
			case CURRENT_TAG_RESULT: {
				myCurrentState = -1;
				break;
			}
			case CURRENT_TAG_SKILL_IN_TRAINING: {
				myTrainingInformation.setSkillInTraining(Integer.parseInt(string));
				myCurrentState = -1;
				break;
			}
			case CURRENT_TAG_TRAINING_END_TIME: {
				myTrainingInformation.setTrainingEndTime(myEveDateFormat.parse(string));
				myCurrentState = -1;
				break;
			}
			case CURRENT_TAG_TRAINING_START_SP: {
				myTrainingInformation.setTrainingStartSP(Integer.parseInt(string));
				myCurrentState = -1;
				break;
			}
			case CURRENT_TAG_TRAINING_START_TIME: {
				myTrainingInformation.setTrainingStartTime(myEveDateFormat.parse(string));
				myCurrentState = -1;
				break;
			}
			case CURRENT_TAG_TRAINING_TO_LEVEL: {
				myTrainingInformation.setTrainingToLevel(Integer.parseInt(string));
				myCurrentState = -1;
				break;
			}
			case CURRENT_TAG_TRAINING_TYPE_ID: {
				myTrainingInformation.setTrainingTypeID(Integer.parseInt(string));
				myCurrentState = -1;
				break;
			}

			}
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
	}

}
