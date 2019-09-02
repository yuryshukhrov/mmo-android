package com.yuri.game.ui.fragments;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yuri.game.GameApplication;
import com.yuri.game.HpBar;
import com.yuri.game.R;
import com.yuri.game.model.actor.Actor;
import com.yuri.game.model.actor.FightEnemy;
import com.yuri.game.model.actor.GenderType;
import com.yuri.game.model.actor.Player;
import com.yuri.game.model.duel.DuelMessage;

public class DuelBluePlayerFragment extends Fragment {
	
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
	
	private GameApplication app;
	
	

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		app = GameApplication.getApplicationFromActivity(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_duel_blue_player,
				container, false);
		HpBar hpBar = (HpBar) view.findViewById(R.id.player2HpBar);
		ImageView blueIcon = (ImageView) view.findViewById(R.id.iv_blue_player);
		setIcon(blueIcon);
		Bundle args = getArguments();

		// Starting stage
		if (args.containsKey("FIGHT_ENEMY")) {
			FightEnemy fightEnemy = args.getParcelable("FIGHT_ENEMY");
			processFightStart(hpBar, fightEnemy);
		} else if (args.containsKey("DUEL_MESSAGE")) { // Progress stage
			int attackNumber = args.getInt("ATTACK_NUMBER");
			DuelMessage duelMessage = args.getParcelable("DUEL_MESSAGE");
			switch (attackNumber) {
			case 1:
				processFirstAttack(hpBar, duelMessage);
				break;
			case 2:
				processSecondAttack(hpBar, duelMessage);
				break;
			}
		}

		return view;
	}

	private void processSecondAttack(HpBar hpBar, DuelMessage duelMessage) {
		hpBar.setMax(duelMessage.getSecondAttack().getMaxHp());
		hpBar.setProgress(duelMessage.getSecondAttack().getCurrentHp());
		hpBar.setText("[" + duelMessage.getSecondAttack().getCurrentHp() + "/"
				+ duelMessage.getSecondAttack().getMaxHp() + "]");
	}

	private void processFirstAttack(HpBar hpBar, DuelMessage duelMessage) {
		hpBar.setMax(duelMessage.getFirstAttack().getMaxHp());
		hpBar.setProgress(duelMessage.getFirstAttack().getCurrentHp());
		hpBar.setText("[" + duelMessage.getFirstAttack().getCurrentHp() + "/"
				+ duelMessage.getFirstAttack().getMaxHp() + "]");
	}

	private void processFightStart(HpBar hpBar, FightEnemy fightEnemy) {
		hpBar.setMax(fightEnemy.getMaxHp());
		hpBar.setProgress(fightEnemy.getCurrentHp());
		hpBar.setText("" + fightEnemy.getCurrentHp() + "/"
				+ fightEnemy.getMaxHp());
	}
	
	public void setIcon(ImageView blueIcon) {
		WeakReference<Actor> actor = new WeakReference<Actor>(app.world.modelContainer.actor);
		Actor a = actor.get();
		Player p = null;
		
		if (a != null) {
			try {
				p = (Player) a;
			} catch (ClassCastException e) {
				Log.e("onActorStatsRequested", "Player-Actor Cast Exception: " + e.toString());
			}
		}
		
		if (p != null) {
			setPlayerIcon(blueIcon, p);
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
}
