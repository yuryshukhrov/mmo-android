package com.yuri.game.resource.parsers;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

import com.yuri.game.GameApplication;
import com.yuri.game.model.duel.CurrentFight;

public class FightRegistrationParser extends DefaultHandler {

	private final String TAG = "FightRegistrationParser";

	private GameApplication app;
	private CurrentFight currentFight;
	private List<String> reds;
	private List<String> blues;
	private StringBuffer buffer;
	private ParentTag parentTag;
	private int code;

	private enum ParentTag {
		RED, BLUE
	}

	public FightRegistrationParser(GameApplication app) {
		this.app = app;
		buffer = new StringBuffer();
		currentFight = new CurrentFight();
	}

	public static void startParsing(GameApplication app, String message) {
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			FightRegistrationParser handler = new FightRegistrationParser(app);
			saxParser
					.parse(new InputSource(new StringReader(message)), handler);
		} catch (Exception ex) {
			Log.e("FightRegistrationParser", "SAXParser: parse() failed");
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		super.characters(ch, start, length);

		buffer.append(ch, start, length);
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		super.endElement(uri, localName, qName);

		if (localName.equalsIgnoreCase("code")) {
			code = Integer.parseInt(buffer.toString());
			Log.e(TAG, "Code: " + code);
		}

		if (localName.equalsIgnoreCase("character")) {
			switch (parentTag) {
			case RED:
				String red = buffer.toString();
				Log.e(TAG, "Red: " + red);
				reds.add(red);
				break;
			case BLUE:
				String blue = buffer.toString();
				Log.e(TAG, "Blue: " + blue);
				blues.add(blue);
				break;
			}
		}

		if (localName.equalsIgnoreCase("timeout")) {
			Log.e("TIMEOUT", "" + buffer.toString());
			currentFight.setTimeout(Integer.parseInt(buffer.toString()));
		}

		if (localName.equalsIgnoreCase("id")) {
			Log.e("ID", "" + buffer.toString());
			currentFight.setId(Integer.parseInt(buffer.toString()));
		}

		if (localName.equalsIgnoreCase("red")) {
			currentFight.setRedPlayers(reds);
		}

		if (localName.equalsIgnoreCase("blue")) {
			currentFight.setBluePlayers(blues);
		}

		if (localName.equalsIgnoreCase("gameMessage")) {

			switch (code) {
			case 1001:
				app.world.modelContainer.location
						.registerNewFight(currentFight);
				app.controllers.fightController.onFightRegistered();
				break;
			case 1002:
				app.world.modelContainer.location.unRegisterFight(currentFight);
				app.controllers.fightController.onFightUnregistered();
				break;
			}
		}
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);

		buffer.setLength(0);

		if (localName.equalsIgnoreCase("red")) {
			reds = new ArrayList<String>();
			parentTag = ParentTag.RED;
		}

		if (localName.equalsIgnoreCase("blue")) {
			blues = new ArrayList<String>();
			parentTag = ParentTag.BLUE;
		}
	}
}
