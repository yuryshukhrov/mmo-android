package com.yuri.game.ui.fragments;

import java.lang.ref.WeakReference;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuri.game.GameApplication;
import com.yuri.game.HpBar;
import com.yuri.game.R;
import com.yuri.game.model.actor.Actor;
import com.yuri.game.model.actor.GenderType;
import com.yuri.game.model.actor.Player;
import com.yuri.game.model.duel.DuelRequest;

public class DuelOwnerInfoDialog extends DialogFragment {

	private static final int HUMAN_MALE_ONE = R.drawable.human_male_one;
	private static final int HUMAN_MALE_TWO = R.drawable.human_male_two;
	private static final int HUMAN_FEMALE_ONE = R.drawable.human_female_one;
	private static final int HUMAN_FEMALE_TWO = R.drawable.human_female_two;
	private static final int ORC_MALE = R.drawable.orc_male;
	private static final int ORC_FEMALE = R.drawable.orc_female;
	private static final int ELF_MALE = R.drawable.elf_male;
	private static final int ELF_FEMALE = R.drawable.elf_female;
	private static final int UNDEAD_MALE = R.drawable.undead_male;
	private static final int UNDEAD_FEMALE = R.drawable.undead_female;

	private static final int ONLINE = R.drawable.online;
	private static final int OFFLINE = R.drawable.offline;

	private GameApplication app;
	private WeakReference<Actor> actor;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		app = GameApplication.getApplicationFromActivity(activity);
		actor = new WeakReference<Actor>(app.world.modelContainer.actor);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_duel_owner_dialog, container,
				false);

		TextView timeout = (TextView) view.findViewById(R.id.tv_duel_timeout);
		TextView duration = (TextView) view.findViewById(R.id.tv_duel_duration);
		TextView owner = (TextView) view.findViewById(R.id.tv_duel_owner);

		ImageView onlineState = (ImageView) view
				.findViewById(R.id.iv_player_state);
		ImageView charImage = (ImageView) view
				.findViewById(R.id.player_char_image);

		HpBar hpBar = (HpBar) view.findViewById(R.id.player_hp_bar);

		fillDuelInfo(timeout, duration, owner);
		fillActorStats(onlineState, charImage, hpBar);

		return view;
	}

	private void fillActorStats(ImageView onlineState,
			ImageView charImage, HpBar hpBar) {
		Actor a = actor.get();

		if (a != null) {
			try {
				Player p = (Player) a;
				hpBar.setText(p.getCurrentHP() + "/" + p.getMaxHP());
				setPlayerIcon(charImage, p);

				switch (p.getState()) {
				case ONLINE:
					onlineState.setImageResource(ONLINE);
					break;
				case OFFLINE:
					onlineState.setImageResource(OFFLINE);
					break;
				}
			} catch (ClassCastException e) {
				Log.e("fillActorStats", e.toString());
			}
		}
	}

	private void setPlayerIcon(ImageView ivPlayerIcon, Player player) {
		switch (player.getRace()) {
		case HUMAN:
			if (player.getGender() == GenderType.MALE) {
				ivPlayerIcon.setImageResource(HUMAN_MALE_ONE);
			} else {
				ivPlayerIcon.setImageResource(HUMAN_FEMALE_ONE);
			}
			break;
		case ORC:
			if (player.getGender() == GenderType.MALE) {
				ivPlayerIcon.setImageResource(ORC_MALE);
			} else {
				ivPlayerIcon.setImageResource(ORC_FEMALE);
			}
			break;
		case ELF:
			if (player.getGender() == GenderType.MALE) {
				ivPlayerIcon.setImageResource(ELF_MALE);
			} else {
				ivPlayerIcon.setImageResource(ELF_FEMALE);
			}
			break;
		case UNDEAD:
			if (player.getGender() == GenderType.MALE) {
				ivPlayerIcon.setImageResource(UNDEAD_MALE);
			} else {
				ivPlayerIcon.setImageResource(UNDEAD_FEMALE);
			}
			break;
		}
	}

	private void fillDuelInfo(TextView timeout, TextView duration,
			TextView owner) {
		List<DuelRequest> duelRequests = app.world.modelContainer.location
				.getDuelRequestsList();
		DuelRequest targetDuelRequest = null;
		Actor a = actor.get();
		Player p = null;

		if (a != null) {
			try {
				p = (Player) a;
			} catch (ClassCastException e) {
				e.printStackTrace();
			}
			for (DuelRequest d : duelRequests) {
				if (d.getOwner().equals(a.getName())) {
					targetDuelRequest = d;
					break;
				}
			}
		}

		if (targetDuelRequest != null) {
			timeout.setText("Timeout: " + targetDuelRequest.getTimeout());
			duration.setText("Duration: " + targetDuelRequest.getLifeSpan());
			owner.setText("Owner: " + targetDuelRequest.getOwner() + " [" + p.getLevel() + "]");
		}
	}

	/** The system calls this only when creating the layout in a dialog. */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog dialog = super.onCreateDialog(savedInstanceState);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		return dialog;
	}
}
