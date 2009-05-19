package com.blogspot.gambitgeoff.ggeve.eveapi;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.blogspot.gambitgeoff.ggeve.EveCharacter;

public class CharacterSheetEventHandler extends DefaultHandler {

	public static final int CHAR_SHEET_CURRENT_TIME = 0;
	public static final int CHAR_SHEET_RESULT = 1;
	public static final int CHAR_SHEET_CHARACTER_ID = 2;
	public static final int CHAR_SHEET_NAME = 3;
	public static final int CHAR_SHEET_RACE = 4;
	public static final int CHAR_SHEET_BLOODLINE = 5;
	public static final int CHAR_SHEET_GENDER = 6;
	public static final int CHAR_SHEET_CORP_NAME = 7;
	public static final int CHAR_SHEET_CORP_ID = 8;
	public static final int CHAR_SHEET_BALANCE = 9;

	public static final int CHAR_SHEET_ATTRIBUTE_ENHANCERS = 10;
	public static final int CHAR_SHEET_INT_BONUS = 11;
	public static final int CHAR_SHEET_MEMORY_BONUS = 12;

	public static final int CHAR_SHEET_ATTRIBUTES = 13;
	public static final int CHAR_SHEET_INTELLIGENCE = 14;
	public static final int CHAR_SHEET_MEMORY = 15;
	public static final int CHAR_SHEET_CHARISMA = 16;
	public static final int CHAR_SHEET_PERCEPTION = 17;
	public static final int CHAR_SHEET_WILLPOWER = 18;

	private EveCharacter myEveCharacter;
	private int myCurrentState = -1;

	public CharacterSheetEventHandler() {

	}

	public EveCharacter getEveCharacter() {
		return myEveCharacter;
	}

	public void startDocument() throws SAXException {
		myEveCharacter = new EveCharacter();
	}

	public void endDocument() throws SAXException {

	}

	public void startElement(String inNamespaceURI, String inLocalName,
			String inQName, Attributes inAttributes) throws SAXException {
		if (inLocalName.equals("characterID")) {
			myCurrentState = CHAR_SHEET_CHARACTER_ID;
			return;
		} else if (inLocalName.equals("name")) {
			myCurrentState = CHAR_SHEET_NAME;
			return;
		} else if (inLocalName.equals("race")) {
			myCurrentState = CHAR_SHEET_RACE;
			return;
		} else if (inLocalName.equals("bloodLine")) {
			myCurrentState = CHAR_SHEET_BLOODLINE;
			return;
		} else if (inLocalName.equals("gender")) {
			myCurrentState = CHAR_SHEET_GENDER;
			return;
		} else if (inLocalName.equals("corporationName")) {
			myCurrentState = CHAR_SHEET_CORP_NAME;
			return;
		} else if (inLocalName.equals("corporationID")) {
			myCurrentState = CHAR_SHEET_CORP_ID;
			return;
		} else if (inLocalName.equals("balance")) {
			myCurrentState = CHAR_SHEET_BALANCE;
			return;
		}
	}

	public void endElement(String namespaceURI, String localName, String qName)
			throws SAXException {
	}

	public void characters(char ch[], int start, int length) {
		String string = new String(ch, start, length);
		switch (myCurrentState) {
		case CHAR_SHEET_NAME: {
			myEveCharacter.setCharacterName(string);
			myCurrentState = -1;
			break;
		}
		case CHAR_SHEET_CHARACTER_ID: {
			myEveCharacter.setCharacterID(Integer.parseInt(string));
			myCurrentState = -1;
			break;
		}
		case CHAR_SHEET_BLOODLINE: {
			myEveCharacter.setBloodline(string);
			myCurrentState = -1;
			break;
		}
		case CHAR_SHEET_BALANCE: {
			myEveCharacter.setBalance(Double.parseDouble(string));
			myCurrentState = -1;
			break;
		}
		case CHAR_SHEET_GENDER: {
			myEveCharacter.setGender(string);
			myCurrentState = -1;
			break;
		}
		case CHAR_SHEET_RACE: {
			myEveCharacter.setRace(string);
			myCurrentState = -1;
			break;
		}
		case CHAR_SHEET_CORP_NAME: {
			myEveCharacter.setCorporationName(string);
			myCurrentState = -1;
			break;
		}

		}
	}

}
