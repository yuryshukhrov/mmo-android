package com.yuri.game.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuri.game.GameApplication;
import com.yuri.game.HpBar;
import com.yuri.game.R;
import com.yuri.game.model.actor.GenderType;
import com.yuri.game.model.actor.Player;
import com.yuri.game.ui.adapters.LocationPlayersList;

public class CharDetailsFragment extends Fragment {
	
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
	
	public LocationPlayersList adapter;
	
	private GameApplication app;
	private Player player;
	
	public CharDetailsFragment() {
		
	}	

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		app = GameApplication.getApplicationFromActivity(activity);
		player = app.world.modelContainer.player;
		
		adapter = new LocationPlayersList(activity,
				0,
				app.world.modelContainer.location.getPlayers());
		adapter.notifyDataSetChanged();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_char_details, container, false);
		
		TextView tvName = (TextView) view.findViewById(R.id.tv_name_and_level);
		HpBar hpBar = (HpBar) view.findViewById(R.id.player_hp_bar);
		
		ImageView ivPlayerIcon = (ImageView) view.findViewById(R.id.player_char_image);
		setPlayerIcon(ivPlayerIcon);
		
		TextView tvLocation = (TextView) view.findViewById(R.id.tv_location_name);
		tvLocation.setText("Location: " + player.getLocationType().name());
		
		TextView tvStrength = (TextView) view.findViewById(R.id.tv_strength);
		TextView tvAgility = (TextView) view.findViewById(R.id.tv_agility);
		TextView tvLuck = (TextView) view.findViewById(R.id.tv_luck);
		TextView tvToughness = (TextView) view.findViewById(R.id.tv_toughness);
		TextView tvDamage = (TextView) view.findViewById(R.id.tv_damage);
		setDamage(tvDamage);
		
		TextView tvCritical = (TextView) view.findViewById(R.id.tv_critical);
		TextView tvAntiCritical = (TextView) view.findViewById(R.id.tv_anticritical);
		TextView tvDodge = (TextView) view.findViewById(R.id.tv_dodge);
		TextView tvAntiDodge = (TextView) view.findViewById(R.id.tv_antidodge);
		
		TextView tvHeadArmor = (TextView) view.findViewById(R.id.tv_head_armor);
		TextView tvChestArmor = (TextView) view.findViewById(R.id.tv_chest_armor);
		TextView tvAbsArmor = (TextView) view.findViewById(R.id.tv_abdomen_armor);
		TextView tvLegArmor = (TextView) view.findViewById(R.id.tv_leg_armor);
		
		ImageView tvOnlineState = (ImageView) view.findViewById(R.id.iv_player_state);
		switch (player.getState()) {
		case ONLINE:
			tvOnlineState.setImageResource(ONLINE);
			break;
		case OFFLINE:
			tvOnlineState.setImageResource(OFFLINE);
			break;
		}
	
		tvName.setText("" + player.getName() + " [" + player.getLevel() + "]");
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

		return view;
	}

	private void setDamage(TextView tvDamage) {
		String first = "Damage: ";
		String minDamage = "<font color='#006600'>" + player.getMinDamage() + "</font>";
		String maxDamage = "<font color='#EE0000'>" + player.getMaxDamage() + "</font>";
		tvDamage.setText(Html.fromHtml(first + minDamage + "-" + maxDamage));
	}

	private void setPlayerIcon(ImageView ivPlayerIcon) {
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
