package com.yuri.game.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.yuri.game.GameApplication;
import com.yuri.game.R;
import com.yuri.game.ui.adapters.ActiveFightsList;
import com.yuri.game.ui.adapters.LocationPlayersList;
import com.yuri.game.ui.adapters.LocationsList;

public class LocationFragment extends Fragment {

	public LocationsList locationsAdapter;
	public LocationPlayersList playersAdapter;
	//public DuelRequestsList duelRequestsAdapter;
	
	private ActivityCallback callback;
	
//	private int duration;
//	private int timeout;
	private Context context;

	private GameApplication app;
	
	private OnClickListener btnListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (callback != null) {
				callback.onCallback(null, getView(), 3, getId());
			}
		}
	};
	
	private OnItemClickListener listClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view,
			    int position, long id) {
			if (callback != null) {
				callback.onCallback(parent, view, position, id);
			}
		}
	};

	public LocationFragment() {

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		context = activity;
		app = GameApplication.getApplicationFromActivity(activity);
		locationsAdapter = new LocationsList(activity);
		playersAdapter = new LocationPlayersList(activity, 0,
				app.world.modelContainer.location.getPlayers());
		
		app.world.modelContainer.location.locAdapters.put(LocationsList.class, locationsAdapter);
		app.world.modelContainer.location.locAdapters.put(LocationPlayersList.class, playersAdapter);
//		duelRequestsAdapter = new DuelRequestsList(activity, 0,
//				app.world.modelContainer.location.getDuelRequestsList());
//		app.controllers.networkController.sendRequest(new ServerRequestFormer()
//				.getDuelRequestList());
		
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

		View view = inflater.inflate(R.layout.fragment_location, container,
				false);
		ListView lvLocations = (ListView) view.findViewById(R.id.lv_locations);
		ListView lvPlayers = (ListView) view.findViewById(R.id.lv_players);
		lvLocations.setAdapter(locationsAdapter);
		lvPlayers.setAdapter(playersAdapter);
		lvLocations.setOnItemClickListener(listClick);
		
		
//		ListView duelRequests = (ListView) view.findViewById(R.id.lv_duel_requests);
//		view.findViewById(R.id.btn_post_duel_request).setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Spinner spDuration = (Spinner) getView().findViewById(R.id.sp_duel_request_span);
//				Spinner spTimeout = (Spinner) getView().findViewById(R.id.sp_duel_request_timeout);
//				
//				if (spDuration != null && spTimeout != null) {
//					String _duration = spDuration.getSelectedItem().toString();
//					String _timeout = spTimeout.getSelectedItem().toString();
//					
//					duration = Integer.parseInt(_duration.replaceAll("[\\D]", ""));
//							timeout = Integer.parseInt(_timeout.replaceAll(
//									"[\\D]", ""));
//
//							app.controllers.networkController
//									.sendRequest(new ServerRequestFormer()
//											.registerDuelRequest(5,
//													timeout));
//							Toast.makeText(context, "POST DUEL", Toast.LENGTH_SHORT).show();
//						}
//			}
//		});
//		
//		duelRequests.setAdapter(duelRequestsAdapter);
		
		Fragment f = getChildFragmentManager().findFragmentById(R.id.middle_fragment_container);
		if (f == null) {
			DuelRequestFragment df = new DuelRequestFragment();
			getChildFragmentManager().beginTransaction().add(R.id.middle_fragment_container, df).commit();
		}
		
		return view;
	}
}
