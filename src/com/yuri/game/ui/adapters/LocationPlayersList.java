package com.yuri.game.ui.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuri.game.GameApplication;
import com.yuri.game.R;
import com.yuri.game.model.DialogType;
import com.yuri.game.model.location.LocationPlayer;
import com.yuri.game.network.ServerRequestFormer;

public class LocationPlayersList extends ArrayAdapter<LocationPlayer> {
	
	private List<LocationPlayer> players;
	private ViewHolder holder;
	private LayoutInflater inflater;
	
	private GameApplication app;
	
	private OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			ViewHolder vh = getViewHolder(v);
			int position = vh.position;
			
			LocationPlayer lp = players.get(position);
			
			app.world.modelContainer.location.dialogTypeStack.push(DialogType.PLAYER);
			app.controllers.networkController
			.sendRequest(new ServerRequestFormer()
					.getAnyChar(lp.getName()));
		}
	};
	
	public LocationPlayersList(Context context, int resource,
			List<LocationPlayer> players) {
		super(context, resource, players);
		
		app = GameApplication.getApplicationFromActivityContext(context);
		this.players = players;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
    
	static class ViewHolder {
    	TextView tvName;
    	ImageView ivStats;
    	ImageView ivGender;
    	int position;
    }

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if (convertView == null) {

			// inflate the layout
			convertView = inflater.inflate(R.layout.list_location_players,
					parent, false);

			// well set up the ViewHolder
			holder = new ViewHolder();
			holder.tvName = (TextView) convertView.findViewById(R.id.tv_name_and_level);
			holder.ivStats = (ImageView) convertView.findViewById(R.id.iv_check_stats);
			holder.ivGender = (ImageView) convertView.findViewById(R.id.iv_gender);

			// store the holder with the view.
			convertView.setTag(holder);

		} else {

			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.position = position;
		LocationPlayer locationPlayer = players.get(position);
		switch (locationPlayer.getGender()) {
		case MALE:
			holder.ivGender.setImageResource(R.drawable.male);
			break;
		case FEMALE:
			holder.ivGender.setImageResource(R.drawable.female);
			break;
		}
		holder.tvName.setText(locationPlayer.getName() + " ["
				+ locationPlayer.getLevel() + "]");
		holder.ivStats.setOnClickListener(listener);

		return convertView;
	}
	
	public ViewHolder getViewHolder(View v) {
		if (v.getTag() == null) {
			return getViewHolder((View) v.getParent());
		}
		return (ViewHolder) v.getTag();
	}
}
