package com.blogspot.gambitgeoff.ggeve.eveapi;

import java.util.Vector;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.blogspot.gambitgeoff.ggeve.EveCharacter;

public class AccountCharactersEventHandler extends DefaultHandler {

	private Vector<EveCharacter> myEveCharacters;

	AccountCharactersEventHandler() {
		myEveCharacters = new Vector<EveCharacter>();
	}

	public Vector<EveCharacter> getAccountCharacters() {
		return myEveCharacters;
	}

	public void startDocument() throws SAXException {
		myEveCharacters = new Vector<EveCharacter>();
	}

	public void startElement(String inNamespaceURI, String inLocalName,
			String inQName, Attributes inAttributes) throws SAXException {
		if (inLocalName.equals("row")) {
			EveCharacter ec = new EveCharacter();
			ec.setCharacterName(inAttributes.getValue("name"));
			ec.setCharacterID(Integer.parseInt(inAttributes.getValue("characterID")));
			ec.setCorporationID(Integer.parseInt(inAttributes.getValue("corporationID")));
			ec.setCorporationName(inAttributes.getValue("corporationName"));
			myEveCharacters.add(ec);
			return;
		}
		return;
	}

}
