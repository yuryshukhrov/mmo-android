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
import com.yuri.game.model.duel.AttackState;

public class FightAttackStateParser extends DefaultHandler {
	
	private final String TAG = "FightAttackStateParser";

	private GameApplication app;
	private AttackState attackState;
	private String target;
	private StringBuffer buffer;
	private int code;

	public FightAttackStateParser(GameApplication app) {
		this.app = app;
		buffer = new StringBuffer();
	}

	public static void startParsing(GameApplication app, String message) {
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			FightAttackStateParser handler = new FightAttackStateParser(app);
			saxParser.parse(new InputSource(new StringReader(message)), handler);
		} catch (Exception ex) {
			Log.e("FightAttackStateParser", "SAXParser: parse() failed");
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
		
		if (localName.equalsIgnoreCase("command")) {
			Log.e(TAG, "Attack State: " + buffer.toString());	
			if (buffer.toString().equals("not_attackable")) {
				attackState = AttackState.NOT_ATTACKABLE;
			}
			
			if (buffer.toString().equals("attackable")) {
				attackState = AttackState.ATTACKABLE;
			}
			
			if (buffer.toString().equals("finish")) {
				attackState = AttackState.FINISH_HIM;
			}
		}
		
		if (localName.equalsIgnoreCase("detail")) {
			target = buffer.toString();
			Log.e(TAG, "Target: " + target);	
		}

		if (localName.equalsIgnoreCase("gameMessage")) {
			app.controllers.fightController.onEnemyAttackState(
					attackState, target);
		}
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		
		buffer.setLength(0);
	}
}
