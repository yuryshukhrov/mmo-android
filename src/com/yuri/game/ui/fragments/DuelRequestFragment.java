package com.yuri.game.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.yuri.game.GameApplication;
import com.yuri.game.R;
import com.yuri.game.network.ServerRequestFormer;
import com.yuri.game.ui.adapters.DuelRequestsList;

public class DuelRequestFragment extends Fragment {
	
	public DuelRequestsList adapter; // Should it be public?
	
	private GameApplication app;
	
	private int duration;
	private int timeout;
	
	public DuelRequestFragment() {
		
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		app = GameApplication.getApplicationFromActivity(activity);
		adapter = new DuelRequestsList(activity, 0,
				app.world.modelContainer.location.getDuelRequestsList());
		app.world.modelContainer.location.locAdapters.put(DuelRequestsList.class, adapter);
		app.controllers.networkController.sendRequest(new ServerRequestFormer()
				.getDuelRequestList());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_duel_requests, container,
				false);
		ListView duelRequests = (ListView) view.findViewById(R.id.lv_duel_requests);
		
		Spinner spDuration = (Spinner) view.findViewById(
				R.id.sp_duel_request_span);
	
		ArrayAdapter<CharSequence> adapterDuration = ArrayAdapter.createFromResource(
	            getActivity(), R.array.duel_request_life_span, R.layout.spinner_request_duel_list);
		adapterDuration.setDropDownViewResource(R.layout.spinner_request_duel_list);
		spDuration.setAdapter(adapterDuration);

		Spinner spTimeout = (Spinner) view.findViewById(
				R.id.sp_duel_request_timeout);
		ArrayAdapter<CharSequence> adapterTimeout = ArrayAdapter.createFromResource(
	            getActivity(), R.array.duel_request_timeout, R.layout.spinner_request_duel_list);
		adapterTimeout.setDropDownViewResource(R.layout.spinner_request_duel_list);
		spTimeout.setAdapter(adapterTimeout);
		
		view.findViewById(R.id.btn_post_duel_request).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Spinner spDuration = (Spinner) getView().findViewById(R.id.sp_duel_request_span);
	            
				Spinner spTimeout = (Spinner) getView().findViewById(R.id.sp_duel_request_timeout);

				if (spDuration != null && spTimeout != null) {
					String _duration = spDuration.getSelectedItem().toString();
					String _timeout = spTimeout.getSelectedItem().toString();
					
					duration = Integer.parseInt(_duration.replaceAll("[\\D]", ""));
							timeout = Integer.parseInt(_timeout.replaceAll(
									"[\\D]", ""));

							app.controllers.networkController
									.sendRequest(new ServerRequestFormer()
											.registerDuelRequest(5,
													timeout));
						}
			}
		});
		
		duelRequests.setAdapter(adapter);
		
		duelRequests.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Log.e("LIST ROW CLICKED", "Position: " + arg2 + ", ID: " + arg3);
				
			}
			
		});

		return view;
	}	
}
