package com.yuri.game.resource.parsers;

import java.io.StringReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

import com.yuri.game.GameApplication;
import com.yuri.game.model.actor.FightEnemy;
import com.yuri.game.model.actor.GenderType;

public class FightStartInitStatsParser extends DefaultHandler {
	
	private final String TAG = "FightStartInitStatsParser";

	private GameApplication app;
	private FightEnemy enemy;
	private StringBuffer buffer;
	private int code;

	public FightStartInitStatsParser(GameApplication app) {
		this.app = app;
		buffer = new StringBuffer();
		enemy = new FightEnemy();
	}

	public static void startParsing(GameApplication app, String message) {
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			FightStartInitStatsParser handler = new FightStartInitStatsParser(app);
			saxParser
					.parse(new InputSource(new StringReader(message)), handler);
		} catch (Exception ex) {
			Log.e("FightStartInitStatsParser", "SAXParser: parse() failed");
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

		if (localName.equalsIgnoreCase("name")) {	
			enemy.setName(buffer.toString());
			Log.e(TAG, "Name: " + buffer.toString());
		}
		
		if (localName.equalsIgnoreCase("sex")) {	
			enemy.setGender(GenderType.valueOf(buffer.toString().toUpperCase()));
			Log.e(TAG, "Gender: " + buffer.toString());
		}
		
		if (localName.equalsIgnoreCase("level")) {	
			enemy.setLevel(Integer.parseInt(buffer.toString()));
			Log.e(TAG, "Level: " + buffer.toString());
		}
		
		if (localName.equalsIgnoreCase("hp")) {	
			enemy.setMaxHp(Integer.parseInt(buffer.toString()));
			Log.e(TAG, "Max Hp: " + buffer.toString());
		}
		
		if (localName.equalsIgnoreCase("currentHp")) {	
			enemy.setCurrentHp(Integer.parseInt(buffer.toString()));
			Log.e(TAG, "Current Hp: " + buffer.toString());
		}

		if (localName.equalsIgnoreCase("gameMessage")) {
			app.controllers.fightController.onEnemyInitStats(enemy);
		}
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);

		buffer.setLength(0);
	}
}
