package com.yuri.game.resource.parsers;

import java.io.StringReader;
import java.util.Locale;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

import com.yuri.game.GameApplication;
import com.yuri.game.model.duel.AttackTarget;
import com.yuri.game.model.duel.AttackType;
import com.yuri.game.model.duel.DuelAttack;
import com.yuri.game.model.duel.DuelEvent;
import com.yuri.game.model.duel.DuelMessage;
import com.yuri.game.model.duel.EventType;
import com.yuri.game.model.duel.FightState;

public class FightMessageParser extends DefaultHandler {

	private final String TAG = "FightParser";
	private final int HOW_MUCH = 5;

	private GameApplication app;
	private String date;
	private FightState fightState;
	private DuelAttack duelAttack;
	private DuelEvent duelEvent;
	private DuelAttack duelAttacks[];
	private DuelEvent duelEvents[];
	private StringBuffer buffer;
	private int code;
	private ParentTag parentTag;
	
	private int duelAttacksCounter = 0;
	private int duelEventsCounter = 0;

	public FightMessageParser(GameApplication app) {
		this.app = app;
		buffer = new StringBuffer();
		initLists();
	}

	private void initLists() {
		duelAttacks = new DuelAttack[HOW_MUCH];
		duelEvents = new DuelEvent[HOW_MUCH];
		
		for (int i = 0; i < HOW_MUCH; i++) {
			duelAttacks[i] = null;
			duelEvents[i] = null;
		}
	}

	private enum ParentTag {
		FIGHT_STATE, ATTACK, EVENT
	}

	public static void startParsing(GameApplication app, String message) {
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			FightMessageParser handler = new FightMessageParser(app);
			saxParser
					.parse(new InputSource(new StringReader(message)), handler);
		} catch (Exception ex) {
			Log.e("FightParser", "SAXParser: parse() failed");
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

		if (localName.equalsIgnoreCase("date")) {
			date = buffer.toString();
			Log.e(TAG, "Date: " + date);
		}

		if (localName.equalsIgnoreCase("type")) {
			switch (parentTag) {
			case ATTACK:
				Log.e("TYPE ATTACK: ",
						""
								+ AttackType.valueOf(buffer.toString()
										.toUpperCase(Locale.getDefault())));
				duelAttack.setAttackType(AttackType.valueOf(buffer.toString()
						.toUpperCase(Locale.getDefault())));
				break;
			case FIGHT_STATE:
				Log.e("TYPE FIGHT_STATE: ", "" + buffer.toString());
				fightState.setType(buffer.toString());
				break;
			case EVENT:
				Log.e("DUEL EVENT: ", "" + buffer.toString());
				
				if (buffer.toString().equals("finished")) {
					duelEvent.setEventType(EventType.FINISHED);
				}

				if (buffer.toString().equals("sleep")) {
					duelEvent.setEventType(EventType.SLEEP);
				}

				if (buffer.toString().equals("defeat")) {
					duelEvent.setEventType(EventType.DEFEATED);
				}
				
				if (buffer.toString().equals("non_finish")) {
					duelEvent.setEventType(EventType.MERCY);
				}

				break;
			}
		}

		if (localName.equalsIgnoreCase("attacker")) {
			Log.e("ATTACKER", "" + buffer.toString());
			duelAttack.setAttacker(buffer.toString());
		}

		if (localName.equalsIgnoreCase("event")) {
			Log.e("EVENT", "" + buffer.toString());
			duelEvents[duelEventsCounter++] = duelEvent;
		}

		if (localName.equalsIgnoreCase("receiver")) {
			Log.e("RECEIVER", "" + buffer.toString());
			duelAttack.setReceiver(buffer.toString());
		}

		if (localName.equalsIgnoreCase("damage")) {
			Log.e("DAMAGE", "" + buffer.toString());
			duelAttack.setDamage(Integer.parseInt(buffer.toString()));
		}

		if (localName.equalsIgnoreCase("hpLeft")) {
			Log.e("HP LEFT", "" + buffer.toString());
			duelAttack.setCurrentHp(Integer.parseInt(buffer.toString()));
		}

		if (localName.equalsIgnoreCase("hp")) {
			Log.e("HP", "" + buffer.toString());
			duelAttack.setMaxHp(Integer.parseInt(buffer.toString()));
		}

		if (localName.equalsIgnoreCase("red")) {
			Log.e("RED", "" + buffer.toString());
			fightState.setRed(buffer.toString());
		}

		if (localName.equalsIgnoreCase("blue")) {
			Log.e("BLUE", "" + buffer.toString());
			fightState.setBlue(buffer.toString());
		}
		
		if (localName.equalsIgnoreCase("target")) {
			Log.e("TARGET", "" + buffer.toString());
			duelEvent.setTarget(buffer.toString());
		}

		if (localName.equalsIgnoreCase("winner")) {
			Log.e("WINNER", "" + buffer.toString());
			fightState.setWinner(buffer.toString());
		}

		if (localName.equalsIgnoreCase("fightState")) {
			Log.e(TAG, "FightState: " + fightState.toString());
		}

		if (localName.equalsIgnoreCase("attackPlace")) {
			Log.e("ATTACK",
					""
							+ AttackTarget.valueOf(buffer.toString()
									.toUpperCase(Locale.ENGLISH)));
			duelAttack.setAttackTarget(AttackTarget.valueOf(buffer.toString()
					.toUpperCase(Locale.ENGLISH)));
		}

		if (localName.equalsIgnoreCase("attack")) {
			Log.e(TAG, "DuelAttack: " + duelAttack.toString());
			duelAttacks[duelAttacksCounter++] = duelAttack;
		}

		if (localName.equalsIgnoreCase("gameMessage")) {
			DuelMessage duelMessage = new DuelMessage(duelAttacks[0],
					duelAttacks[1], fightState, duelEvents[0],
					duelEvents[1], date);

			app.controllers.fightController.onFightMessage(duelMessage);
		}
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);

		buffer.setLength(0);

		if (localName.equalsIgnoreCase("fightState")) {
			fightState = new FightState();
			parentTag = ParentTag.FIGHT_STATE;
		}

		if (localName.equalsIgnoreCase("attack")) {
			duelAttack = new DuelAttack();
			parentTag = ParentTag.ATTACK;
		}

		if (localName.equalsIgnoreCase("event")) {
			duelEvent = new DuelEvent();
			parentTag = ParentTag.EVENT;
		}
	}
}
