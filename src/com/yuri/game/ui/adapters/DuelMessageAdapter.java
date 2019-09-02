package com.yuri.game.ui.adapters;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.yuri.game.GameApplication;
import com.yuri.game.R;
import com.yuri.game.model.duel.CurrentFight;
import com.yuri.game.model.duel.DuelAttack;
import com.yuri.game.model.duel.DuelEvent;
import com.yuri.game.model.duel.DuelMessage;

public class DuelMessageAdapter extends ArrayAdapter<DuelMessage> {

	private final List<DuelMessage> duelMessages;
	private final LayoutInflater inflater;
	private ViewHolder holder;
	private CurrentFight currentFight;
	private GameApplication app;

	public DuelMessageAdapter(Context context, int resource,
			List<DuelMessage> duelMessages) {
		super(context, resource, duelMessages);

		app = GameApplication.getApplicationFromActivityContext(context);
		this.duelMessages = duelMessages;

		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	public static class ViewHolder {
		TextView firstAttack;
		TextView secondAttack;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {

			// inflate the layout
			convertView = inflater.inflate(R.layout.list_duel_message, parent,
					false);

			// well set up the ViewHolder
			holder = new ViewHolder();
			holder.firstAttack = (TextView) convertView
					.findViewById(R.id.tv_first_attack);
			holder.secondAttack = (TextView) convertView
					.findViewById(R.id.tv_second_attack);

			// store the holder with the view.
			convertView.setTag(holder);

		} else {

			holder = (ViewHolder) convertView.getTag();
		}

		DuelMessage duelMessage = duelMessages.get(position);
		generateMessage(duelMessage);

		return convertView;
	}

	private void generateMessage(DuelMessage duelMessage) {

		String s1 = "";
		String s2 = "";

		if (duelMessage.getFirstAttack() != null) {
			s1 = createFirstAttackMessage(duelMessage);
			if (duelMessage.getSecondAttack() != null) {
				s2 = createSecondAttackMessage(duelMessage);
			} else if (duelMessage.getFirstEvent() != null) {
				s2 = createFirstEventMessage(duelMessage);
			} else if (duelMessage.getSecondEvent() != null) {
				s2 = createSecondEventMessage(duelMessage);
			}
		} else if (duelMessage.getFirstEvent() != null) {
			s1 = createFirstEventMessage(duelMessage);
			if (duelMessage.getSecondEvent() != null) {
				s2 = createSecondEventMessage(duelMessage);
			}
		}

		holder.firstAttack.setText(Html.fromHtml(s1), BufferType.SPANNABLE);
		holder.secondAttack.setText(Html.fromHtml(s2), BufferType.SPANNABLE);
	}

	private String createSecondEventMessage(DuelMessage message) {
		DuelEvent duelEvent = message.getSecondEvent();
		StringBuilder strBuilder = new StringBuilder();

		switch (duelEvent.getEventType()) {
		case DEFEATED:
			strBuilder.append("" + makeColoredString(duelEvent.getTarget())
					+ " was defeated!");
			break;
		case SLEEP:
			strBuilder.append("" + makeColoredString(duelEvent.getTarget())
					+ " was sleeping!");
			break;
		case FINISHED:
			strBuilder.append("" + makeColoredString(duelEvent.getTarget())
					+ " was finished!");
			break;
		case MERCY:
			strBuilder.append("" + makeColoredString(duelEvent.getTarget())
					+ " had mercy!");
			break;
		}

		return strBuilder.toString();
	}

	private String createFirstEventMessage(DuelMessage message) {
		DuelEvent duelEvent = message.getFirstEvent();
		StringBuilder strBuilder = new StringBuilder();

		switch (duelEvent.getEventType()) {
		case DEFEATED:
			strBuilder.append("" + makeColoredString(duelEvent.getTarget())
					+ " was defeated!");
			break;
		case SLEEP:
			strBuilder.append("" + makeColoredString(duelEvent.getTarget())
					+ " was sleeping!");
			break;
		case FINISHED:
			strBuilder.append("" + makeColoredString(duelEvent.getTarget())
					+ " was finished!");
			break;
		case MERCY:
			strBuilder.append("" + makeColoredString(duelEvent.getTarget())
					+ " had mercy!");
			break;
		}

		return strBuilder.toString();
	}

	private String createSecondAttackMessage(DuelMessage message) {
		DuelAttack duelAttack = message.getSecondAttack();
		StringBuilder strBuilder = new StringBuilder();

		switch (duelAttack.getAttackType()) {
		case BLOCK:
			strBuilder.append("" + makeColoredString(duelAttack.getAttacker())
					+ " attacked but "
					+ makeColoredString(duelAttack.getReceiver())
					+ " blocked the attack");
			break;
		case BREAK_BLOCK:
			strBuilder.append("" + makeColoredString(duelAttack.getAttacker())
					+ " attacked and broke "
					+ makeColoredString(duelAttack.getReceiver())
					+ " block dealing " + duelAttack.getDamage() + " damage");
			break;
		case CRITICAL:
			strBuilder.append("" + makeColoredString(duelAttack.getAttacker())
					+ " critically hit "
					+ makeColoredString(duelAttack.getReceiver()) + " dealing "
					+ duelAttack.getDamage() + " damage");
			break;
		case DODGE:
			strBuilder.append("" + makeColoredString(duelAttack.getAttacker())
					+ " attacked but "
					+ makeColoredString(duelAttack.getReceiver())
					+ " dodged the attack");
			break;
		case NORMAL:
			strBuilder.append("" + makeColoredString(duelAttack.getAttacker())
					+ " performed a normal attack on "
					+ makeColoredString(duelAttack.getReceiver()) + " dealing "
					+ duelAttack.getDamage() + " damage");
			break;
		}

		return strBuilder.toString();
	}

	private String createFirstAttackMessage(DuelMessage message) {
		DuelAttack duelAttack = message.getFirstAttack();
		StringBuilder strBuilder = new StringBuilder();

		switch (duelAttack.getAttackType()) {
		case BLOCK:
			strBuilder.append("" + makeColoredString(duelAttack.getAttacker())
					+ " attacked but "
					+ makeColoredString(duelAttack.getReceiver())
					+ " blocked the attack");
			break;
		case BREAK_BLOCK:
			strBuilder.append("" + makeColoredString(duelAttack.getAttacker())
					+ " attacked and broke "
					+ makeColoredString(duelAttack.getReceiver())
					+ " block dealing " + duelAttack.getDamage() + " damage");
			break;
		case CRITICAL:
			strBuilder.append("" + makeColoredString(duelAttack.getAttacker())
					+ " critically hit "
					+ makeColoredString(duelAttack.getReceiver()) + " dealing "
					+ duelAttack.getDamage() + " damage");
			break;
		case DODGE:
			strBuilder.append("" + makeColoredString(duelAttack.getAttacker())
					+ " attacked but "
					+ makeColoredString(duelAttack.getReceiver())
					+ " dodged the attack");
			break;
		case NORMAL:
			strBuilder.append("" + makeColoredString(duelAttack.getAttacker())
					+ " performed a normal attack on "
					+ makeColoredString(duelAttack.getReceiver()) + " dealing "
					+ duelAttack.getDamage() + " damage");
			break;
		}

		return strBuilder.toString();
	}

	@Override
	public void add(DuelMessage object) {
		duelMessages.add(object);
		notifyDataSetChanged();
	}

	@Override
	public void clear() {
		duelMessages.clear();
		notifyDataSetChanged();
	}

	private String makeColoredString(String target) {
		if (currentFight != null) {
			if (isTargetRed(target)) {
				return "<font color='red'>" + target + "</font>";
			} else if (isTargetBlue(target)) {
				return "<font color='blue'>" + target + "</font>";
			}
		} else {
			currentFight = app.world.modelContainer.location
					.findFightByFighterName(target);
			if (currentFight != null) {
				if (isTargetRed(target)) {
					return "<font color='red'>" + target + "</font>";
				} else if (isTargetBlue(target)) {
					return "<font color='blue'>" + target + "</font>";
				}
			}
		}

		return null;
	}

	private boolean isTargetBlue(String target) {
		for (String s : currentFight.getBluePlayers()) {
			if (s.equals(target)) {
				return true;
			}
		}
		return false;
	}

	private boolean isTargetRed(String target) {
		for (String s : currentFight.getRedPlayers()) {
			if (s.equals(target)) {
				return true;
			}
		}
		return false;
	}
}
