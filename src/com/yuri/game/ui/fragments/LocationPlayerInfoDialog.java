package com.yuri.game.ui.fragments;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;
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

public class LocationPlayerInfoDialog extends DialogFragment {
	
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
	
	private Player player;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		app = GameApplication.getApplicationFromActivity(activity);
		actor = new WeakReference<Actor>(app.world.modelContainer.actor);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_location_player_dialog, container,
				false);

		HpBar hpBar = (HpBar) view.findViewById(R.id.player_hp_bar);
		
		TextView tvStrength = (TextView) view.findViewById(R.id.tv_strength);
		TextView tvAgility = (TextView) view.findViewById(R.id.tv_agility);
		TextView tvLuck = (TextView) view.findViewById(R.id.tv_luck);
		TextView tvToughness = (TextView) view.findViewById(R.id.tv_toughness);
		TextView tvDamage = (TextView) view.findViewById(R.id.tv_damage);
		
		TextView tvCritical = (TextView) view.findViewById(R.id.tv_critical);
		TextView tvAntiCritical = (TextView) view.findViewById(R.id.tv_anticritical);
		TextView tvDodge = (TextView) view.findViewById(R.id.tv_dodge);
		TextView tvAntiDodge = (TextView) view.findViewById(R.id.tv_antidodge);
		
		TextView tvHeadArmor = (TextView) view.findViewById(R.id.tv_head_armor);
		TextView tvChestArmor = (TextView) view.findViewById(R.id.tv_chest_armor);
		TextView tvAbsArmor = (TextView) view.findViewById(R.id.tv_abdomen_armor);
		TextView tvLegArmor = (TextView) view.findViewById(R.id.tv_leg_armor);
		
		ImageView onlineState = (ImageView) view
				.findViewById(R.id.iv_player_state);
		ImageView charImage = (ImageView) view
				.findViewById(R.id.player_char_image);
		
		Actor a = actor.get();

		if (a != null) {
			try {
				player = (Player) a;
				
				hpBar.setText("HP " + player.getCurrentHP() + "/" + player.getMaxHP());
				
				tvStrength.setText("Strength: " + player.getStrength());
				tvAgility.setText("Agility: " + player.getAgility());
				tvLuck.setText("Luck: " + player.getLuck());
				tvToughness.setText("Toughness: " + player.getToughness());
				
				tvCritical.setText("Critical: " + player.getCritical());
				tvAntiCritical.setText("AntiCritical: " + player.getAntiCritical());
				tvDodge.setText("Dodge: " + player.getDodge());
				tvAntiDodge.setText("AntiDodge: " + player.getAntiDodge());
				
				tvHeadArmor.setText("Head Armor: " + player.getHeadArmor());
				tvChestArmor.setText("Chest Armor: " + player.getChestArmor());
				tvAbsArmor.setText("Abdomen Armor: " + player.getAbdomenArmor());
				tvLegArmor.setText("Leg Armor: " + player.getLegArmor());
				
				switch (player.getState()) {
				case ONLINE:
					onlineState.setImageResource(ONLINE);
					break;
				case OFFLINE:
					onlineState.setImageResource(OFFLINE);
					break;
				}
				
				setPlayerIcon(charImage , player);
				setDamage(tvDamage, player);
				
			} catch (ClassCastException e) {
				Log.e("Error", e.toString());
			} catch (Exception ex) {
				Log.e("Error", ex.toString());
			}
		}
		
		return view;
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

	/** The system calls this only when creating the layout in a dialog. */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog dialog = super.onCreateDialog(savedInstanceState);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		return dialog;
	}
	
	private void setDamage(TextView tvDamage, Player player) {
		String first = "Damage: ";
		String minDamage = "<font color='#006600'>" + player.getMinDamage() + "</font>";
		String maxDamage = "<font color='#EE0000'>" + player.getMaxDamage() + "</font>";
		tvDamage.setText(Html.fromHtml(first + minDamage + "-" + maxDamage));
	}
}
