package com.yuri.game.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.yuri.game.R;

public class CharSelectFragment extends Fragment {
	
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
	
	private OnCharSelectedListener charSelectListener;
	private String race;
	private Spinner spGender;
	
	private OnClickListener btnListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			View view = getView();

			if (view != null) {
				String gender = spGender.getSelectedItem().toString();
				if (charSelectListener != null) {
					charSelectListener.onCharSelected(race, gender);
				}
			}
		}
	};
	
	private OnItemSelectedListener spListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parentView,
				View selectedItemView, int position, long id) {
			
			String selected = parentView.getItemAtPosition(position).toString();
			ImageView charImage = (ImageView) getView().findViewById(R.id.iv_char_select_image);
			
			if (selected.equalsIgnoreCase("male")) {
				if (charImage != null) {
					if (race.equalsIgnoreCase("Human")) {
						charImage.setBackgroundResource(HUMAN_MALE_ONE);
					} else if (race.equalsIgnoreCase("Orc")) {
						charImage.setBackgroundResource(ORC_MALE);
					} else if (race.equalsIgnoreCase("Elf")) {
						charImage.setBackgroundResource(ELF_MALE);
					} else if (race.equalsIgnoreCase("Undead")) {
						charImage.setBackgroundResource(UNDEAD_MALE);
					}
				}
			} else if (selected.equalsIgnoreCase("female")) {
				if (charImage != null) {
					if (race.equalsIgnoreCase("Human")) {
						charImage.setBackgroundResource(HUMAN_FEMALE_ONE);
					} else if (race.equalsIgnoreCase("Orc")) {
						charImage.setBackgroundResource(ORC_FEMALE);
					} else if (race.equalsIgnoreCase("Elf")) {
						charImage.setBackgroundResource(ELF_FEMALE);
					} else if (race.equalsIgnoreCase("Undead")) {
						charImage.setBackgroundResource(UNDEAD_FEMALE);
					}
				}
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parentView) {
			
		}
		
	};

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		if (activity instanceof OnCharSelectedListener) {
			charSelectListener = (OnCharSelectedListener) activity;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		race = getArguments().getString("RACE");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_char_select, container,
				false);
		ImageView charImage = (ImageView) view.findViewById(R.id.iv_char_select_image);
		TextView raceDescription = (TextView) view.findViewById(R.id.tv_char_description);
		
		spGender = (Spinner) view.findViewById(R.id.gender_spinner);
		spGender.setOnItemSelectedListener(spListener);
		
		if (race.equalsIgnoreCase("Human")) {
			charImage.setBackgroundResource(HUMAN_MALE_TWO);
			raceDescription.setText(R.string.human_race_desc);
		} else if (race.equalsIgnoreCase("Orc")) {
			charImage.setBackgroundResource(ORC_MALE);
			raceDescription.setText(R.string.orc_race_desc);
		} else if (race.equalsIgnoreCase("Elf")) {
			charImage.setBackgroundResource(ELF_MALE);
			raceDescription.setText(R.string.elf_race_desc);
		} else if (race.equalsIgnoreCase("Undead")) {
			charImage.setBackgroundResource(UNDEAD_MALE);
			raceDescription.setText(R.string.undead_race_desc);
		}
		
		view.findViewById(R.id.btn_select_char).setOnClickListener(btnListener);
		return view;
	}

	@Override
	public void onDetach() {
		super.onDetach();

		charSelectListener = null;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	public interface OnCharSelectedListener {
		void onCharSelected(String race, String gender);
	}
}
