package com.yuri.game.ui.adapters;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.yuri.game.GameApplication;
import com.yuri.game.R;
import com.yuri.game.model.duel.CurrentFight;

public class ActiveFightsList extends ArrayAdapter<CurrentFight>{
	
	private GameApplication app;
	private List<CurrentFight> currentFights;
	private Context context;
	private LayoutInflater inflater;
	private ViewHolder holder;
	
	public ActiveFightsList(Context context, int resource, List<CurrentFight> objects) {
		super(context, resource, objects);
		
		app = GameApplication.getApplicationFromActivityContext(context);
		currentFights = objects;
		this.context = context;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}
	
	static class ViewHolder {
    	TextView red;
    	TextView blue;
    	TextView time;
    	int position;
    }
	
private OnClickListener clickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			ViewHolder vh = getViewHolder(v);
			int position = vh.position;
			
			CurrentFight currentFight = currentFights.get(position);
			
			switch (v.getId()) {
			case R.id.tv_red_name_level:
				showRedPlayerStats(currentFight);
				break;
			case R.id.tv_blue_name_level:
				showBluePlayerStats(currentFight);
				break;
			}
		}
		
		private void showBluePlayerStats(CurrentFight currentFight) {
			// TODO Auto-generated method stub
			
		}

		private void showRedPlayerStats(CurrentFight currentFight) {
			// TODO Auto-generated method stub
			
		}
	};

	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if (convertView == null) {

			convertView = inflater.inflate(R.layout.list_current_fights,
					parent, false);

			holder = new ViewHolder();
			holder.red = (TextView) convertView.findViewById(R.id.tv_red_name_level);
			holder.blue = (TextView) convertView.findViewById(R.id.tv_blue_name_level);
			holder.time = (TextView) convertView.findViewById(R.id.tv_fight_start);

			convertView.setTag(holder);

		} else {

			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.position = position;
		
		if (holder.red != null) {
			holder.red.setOnClickListener(clickListener);
		}

		if (holder.blue != null) {
			holder.blue.setOnClickListener(clickListener);
		}

		CurrentFight currentFight = currentFights.get(position);
		
		setRedPlayer(currentFight);
		setBluePlayer(currentFight);
		setFightStartTime();
		
		
		
		return convertView;
	}
	
	private void setFightStartTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		holder.time.setText(dateFormat.format(date));
	}

	private void setBluePlayer(CurrentFight currentFight) {
		String name = "<font color='#0000FF'>" + currentFight.getBluePlayers().get(0) + "</font>";
		holder.blue.setText(Html.fromHtml(name));
	}

	private void setRedPlayer(CurrentFight currentFight) {
		String name = "<font color='#EE0000'>" + currentFight.getRedPlayers().get(0) + "</font>";
		holder.red.setText(Html.fromHtml(name));	
	}

	public ViewHolder getViewHolder(View v) {
		if (v.getTag() == null) {
			return getViewHolder((View) v.getParent());
		}
		return (ViewHolder) v.getTag();
	}
}
