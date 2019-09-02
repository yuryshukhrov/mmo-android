package com.yuri.game.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.yuri.game.R;

public class DuelReturnFragment extends Fragment {

	private OnDuelReturnListener duelReturnListener;

	public interface OnDuelReturnListener {
		void onDuelReturn();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		try {
			duelReturnListener = (OnDuelReturnListener) activity;
		} catch (ClassCastException e) {
			e.printStackTrace();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_duel_return, container,
				false);
		view.findViewById(R.id.ib_duel_return).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						duelReturnListener.onDuelReturn();
					}
				});

		return view;
	}
}
