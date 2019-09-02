package com.yuri.game.ui.fragments;

import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;

import com.yuri.game.R;

public class AttackBlockFragment extends Fragment {

	private OnCheckBoxSelectedListener boxSelectedListener;
	private boolean firstBlockChecked;
	private boolean secondBlockChecked;
	private int[] cbBlockArray;
	private int[] cbAttackArray;

	public AttackBlockFragment() {

	}

	public interface OnCheckBoxSelectedListener {
		void playerActions(HashMap<String, Integer> result);
	}

	public static AttackBlockFragment newInstance() {

		AttackBlockFragment duelTop = new AttackBlockFragment();
		Bundle bundle = new Bundle();
		bundle.putString("test", "It works");
		duelTop.setArguments(bundle);

		return duelTop;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		boxSelectedListener = (OnCheckBoxSelectedListener) activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_attack_block, container, false);
		
		ImageView attack = (ImageView) v.findViewById(R.id.attackIcon);
		attack.setOnClickListener(attackClick);
		
		CheckBox cbAttackHead = (CheckBox) v.findViewById(R.id.cbHeadAttack);
		CheckBox cbAttackBody = (CheckBox) v.findViewById(R.id.cbBodyAttack);
		CheckBox cbAttackAbdomen = (CheckBox) v
				.findViewById(R.id.cbAbdomenAttack);
		CheckBox cbAttackLeg = (CheckBox) v.findViewById(R.id.cbLegAttack);
		
		CheckBox cbBlockHead = (CheckBox) v.findViewById(R.id.cbHeadBlock);
		CheckBox cbBlockBody = (CheckBox) v.findViewById(R.id.cbBodyBlock);
		CheckBox cbBlockAbdomen = (CheckBox) v
				.findViewById(R.id.cbAbdomenBlock);
		CheckBox cbBlockLeg = (CheckBox) v.findViewById(R.id.cbLegBlock);

		cbAttackHead.setOnCheckedChangeListener(attackGroupListener);
		cbAttackBody.setOnCheckedChangeListener(attackGroupListener);
		cbAttackAbdomen.setOnCheckedChangeListener(attackGroupListener);
		cbAttackLeg.setOnCheckedChangeListener(attackGroupListener);
		
		cbBlockHead.setOnCheckedChangeListener(blockGroupListener);
		cbBlockBody.setOnCheckedChangeListener(blockGroupListener);
		cbBlockAbdomen.setOnCheckedChangeListener(blockGroupListener);
		cbBlockLeg.setOnCheckedChangeListener(blockGroupListener);

		cbBlockArray = new int[4];
		cbBlockArray[0] = cbBlockHead.getId();
		cbBlockArray[1] = cbBlockBody.getId();
		cbBlockArray[2] = cbBlockAbdomen.getId();
		cbBlockArray[3] = cbBlockLeg.getId();
		
		cbAttackArray = new int[4];
		cbAttackArray[0] = cbAttackHead.getId();
		cbAttackArray[1] = cbAttackBody.getId();
		cbAttackArray[2] = cbAttackAbdomen.getId();
		cbAttackArray[3] = cbAttackLeg.getId();

		return v;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		
		boxSelectedListener = null;
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	private OnCheckedChangeListener attackGroupListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {

			int checkedId = buttonView.getId();

			if (isChecked) {

				switch (checkedId) {
				case R.id.cbHeadAttack:
					getView().findViewById(R.id.cbBodyAttack).setClickable(
							false);
					getView().findViewById(R.id.cbAbdomenAttack).setClickable(
							false);
					getView().findViewById(R.id.cbLegAttack)
							.setClickable(false);
					break;
				case R.id.cbBodyAttack:
					getView().findViewById(R.id.cbHeadAttack).setClickable(
							false);
					getView().findViewById(R.id.cbAbdomenAttack).setClickable(
							false);
					getView().findViewById(R.id.cbLegAttack)
							.setClickable(false);
					break;
				case R.id.cbAbdomenAttack:
					getView().findViewById(R.id.cbBodyAttack).setClickable(
							false);
					getView().findViewById(R.id.cbHeadAttack).setClickable(
							false);
					getView().findViewById(R.id.cbLegAttack)
							.setClickable(false);
					break;
				case R.id.cbLegAttack:
					getView().findViewById(R.id.cbBodyAttack).setClickable(
							false);
					getView().findViewById(R.id.cbAbdomenAttack).setClickable(
							false);
					getView().findViewById(R.id.cbHeadAttack).setClickable(
							false);
					break;

				}

			} else {
				getView().findViewById(R.id.cbBodyAttack).setClickable(true);
				getView().findViewById(R.id.cbAbdomenAttack).setClickable(true);
				getView().findViewById(R.id.cbLegAttack).setClickable(true);
				getView().findViewById(R.id.cbHeadAttack).setClickable(true);
			}

		}
	};

	private OnCheckedChangeListener blockGroupListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {

			if (isChecked) {

				if (!firstBlockChecked && !secondBlockChecked) {
					firstBlockChecked = true;
				} else if (firstBlockChecked && !secondBlockChecked) {
					secondBlockChecked = true;
					// process blocking

					for (int i = 0; i < 4; i++) {
						CheckBox cb = (CheckBox) getView().findViewById(
								cbBlockArray[i]);
						if (!cb.isChecked()) {
							cb.setClickable(false);
						}
					}
				}

			} else {

				if (firstBlockChecked && !secondBlockChecked) {
					firstBlockChecked = false;
				} else if (firstBlockChecked && secondBlockChecked) {
					secondBlockChecked = false;

					for (int i = 0; i < 4; i++) {
						CheckBox cb = (CheckBox) getView().findViewById(
								cbBlockArray[i]);

						cb.setClickable(true);

					}

				}

			}

		}
	};
	
	private OnClickListener attackClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			CheckBox cbAttack;
			CheckBox cbBlock;
			HashMap<String, Integer> result = new HashMap<String, Integer>();
			int blockType = 1;
			
			for (int i = 0; i < 4; i++) {
				cbAttack = (CheckBox) getView().findViewById(cbAttackArray[i]);
				cbBlock = (CheckBox) getView().findViewById(cbBlockArray[i]);
				
				if (cbAttack.isChecked()) {
					result.put("ATTACK", i);
				}
				
				if (cbBlock.isChecked()) {
					if (blockType == 1) {
						result.put("BLOCK1", i);
						blockType++;
					}
					
					result.put("BLOCK2", i);	
				}	
			}
			
			if (boxSelectedListener != null) {
				boxSelectedListener.playerActions(result);
			}
		}
	};

}
