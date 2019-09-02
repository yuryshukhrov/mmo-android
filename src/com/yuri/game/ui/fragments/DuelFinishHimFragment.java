package com.yuri.game.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.yuri.game.R;

public class DuelFinishHimFragment extends Fragment{
	
	private OnFinishHimListener finishHimListener;
	
	public interface OnFinishHimListener {
		void onFinishHim();
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		try {
			finishHimListener = (OnFinishHimListener) activity;
		} catch (ClassCastException e) {
			e.printStackTrace();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_duel_finish_him, container, false);
		view.findViewById(R.id.ib_finish_him).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finishHimListener.onFinishHim();
			}
		});
		
		return view;
	}
}
