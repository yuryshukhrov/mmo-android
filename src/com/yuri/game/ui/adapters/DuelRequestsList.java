package com.yuri.game.ui.adapters;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuri.game.GameApplication;
import com.yuri.game.R;
import com.yuri.game.model.DialogType;
import com.yuri.game.model.duel.DuelRequest;
import com.yuri.game.network.ServerRequestFormer;

public class DuelRequestsList extends ArrayAdapter<DuelRequest> {
	
	private List<DuelRequest> duelRequests;
	private ViewHolder holder;
	private LayoutInflater inflater;
	
	private GameApplication app;
	
	private OnClickListener clickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			ViewHolder vh = getViewHolder(v);
			int position = vh.position;
			
			DuelRequest duelRequest = duelRequests.get(position);
			
			switch (v.getId()) {
			case R.id.iv_accept_duel_request:
				acceptDuelRequest(duelRequest);
				break;
			case R.id.iv_remove_duel_request:
				removeDuelRequest();
				break;
			case R.id.iv_duel_icon:
				requestOwnerInfo(duelRequest);
				break;
			}
		}

		private void requestOwnerInfo(DuelRequest duelRequest) {
			app.world.modelContainer.location.dialogTypeStack.push(DialogType.DUEL);
			app.controllers.networkController
					.sendRequest(new ServerRequestFormer()
							.getAnyChar(duelRequest.getOwner()));
		}
	};

	public DuelRequestsList(Context context, int resource,
			List<DuelRequest> objects) {
		super(context, resource, objects);
		
		app = GameApplication.getApplicationFromActivityContext(context);
		duelRequests = objects;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}
	
	static class ViewHolder {
		TextView tvPostTime;
		TextView tvTimeout;
		TextView tvRequestSpan;
		TextView tvNameLevel;
		ImageButton ivRequestIcon;
		ImageButton ivCheckStats;
		ImageView ivAcceptRequest;
		ImageButton ivRemoveRequest;
		View separator;
		int position;
		
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if (convertView == null) {

			// inflate the layout
			convertView = inflater.inflate(R.layout.list_duel_requests,
					parent, false);

			// well set up the ViewHolder
			holder = new ViewHolder();
			holder.tvPostTime = (TextView) convertView.findViewById(R.id.tv_duel_request_post_time);
//			holder.tvTimeout = (TextView) convertView.findViewById(R.id.tv_timeout);
			holder.tvNameLevel = (TextView) convertView.findViewById(R.id.tv_name_and_level);
			//holder.tvRequestSpan = (TextView) convertView.findViewById(R.id.tv_duel_life_span);
			holder.ivRequestIcon = (ImageButton) convertView.findViewById(R.id.iv_duel_icon);
			//holder.ivCheckStats = (ImageButton) convertView.findViewById(R.id.iv_check_player_stats);
			holder.ivAcceptRequest = (ImageView) convertView.findViewById(R.id.iv_accept_duel_request);
			holder.ivRemoveRequest = (ImageButton) convertView.findViewById(R.id.iv_remove_duel_request);
			holder.separator = (View) convertView.findViewById(R.id.separator_6);

			// store the holder with the view.
			convertView.setTag(holder);

		} else {

			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.position = position;
		
		if (holder.ivRequestIcon != null) {
			holder.ivRequestIcon.setOnClickListener(clickListener);
		}

		if (holder.ivAcceptRequest != null) {
			holder.ivAcceptRequest.setOnClickListener(clickListener);
		}

		if (holder.ivRemoveRequest != null) {
			holder.ivRemoveRequest.setOnClickListener(clickListener);
		}
		
		DuelRequest duelRequest = duelRequests.get(position);
		holder.tvPostTime.setText(duelRequest.getPostTime());
		//holder.tvTimeout.setText(duelRequest.getTimeout());
		holder.tvNameLevel.setText(duelRequest.getOwner());
		//holder.tvRequestSpan.setText(duelRequest.getLifeSpan());
		
		String s = holder.tvNameLevel.getText().toString();
		holder.ivRemoveRequest.setVisibility(View.GONE);
		holder.separator.setVisibility(View.GONE);
		if (duelRequest.getOwner().equals(app.world.modelContainer.player.getName())) {
			Log.e("COMPARISON", "Position: " + position + ", Name: " + s);
			holder.separator.setVisibility(View.VISIBLE);
			holder.ivRemoveRequest.setVisibility(View.VISIBLE);
		}
		

		return convertView;
	}
	
	private void acceptDuelRequest(DuelRequest duelRequest) {
		app.controllers.networkController
		.sendRequest(new ServerRequestFormer()
				.addPlayerToDuelRequest(duelRequest.getOwner()));
	}
	
	private void removeDuelRequest() {
		app.controllers.networkController
		.sendRequest(new ServerRequestFormer().removeDuelRequest());
	}
	
	public void hideAcceptButton() {
		app.world.modelContainer.player.setAcceptedDuelRequest(true);
		notifyDataSetChanged();
	}
	
	public ViewHolder getViewHolder(View v) {
		if (v.getTag() == null) {
			return getViewHolder((View) v.getParent());
		}
		return (ViewHolder) v.getTag();
	}
}
