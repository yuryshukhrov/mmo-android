package com.yuri.game.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.yuri.game.GameApplication;
import com.yuri.game.R;
import com.yuri.game.ui.adapters.ActiveFightsList;

public class ActiveFightsFragment extends Fragment {
	
	private ActivityCallback callback;
	public ActiveFightsList currentFightsAdapter;
	private GameApplication app;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		app = GameApplication.getApplicationFromActivity(activity);
		
		try {
			callback = (ActivityCallback) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement ActivityCallback");
		}	
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_active_fights, container,
				false);
		
		
		ListView currentFights = (ListView) view.findViewById(R.id.lv_current_fights);
		currentFightsAdapter = new ActiveFightsList(getActivity(), 0, app.world.modelContainer.location.getCurrentFights());
		currentFights.setAdapter(currentFightsAdapter);
		app.world.modelContainer.location.locAdapters.put(ActiveFightsList.class, currentFightsAdapter);
		
		return view;
	}

	
}
