package com.yuri.game.ui.activities;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.yuri.game.FightActivity;
import com.yuri.game.GameApplication;
import com.yuri.game.R;
import com.yuri.game.controller.listeners.DuelRequestListener;
import com.yuri.game.controller.listeners.FightListener;
import com.yuri.game.controller.listeners.LocationListener;
import com.yuri.game.model.DialogType;
import com.yuri.game.model.actor.FightEnemy;
import com.yuri.game.model.actor.LocationType;
import com.yuri.game.model.duel.AttackState;
import com.yuri.game.model.duel.DuelMessage;
import com.yuri.game.model.listeners.ActorStatsListener;
import com.yuri.game.network.ServerRequestFormer;
import com.yuri.game.ui.adapters.TabsPager;
import com.yuri.game.ui.fragments.ActiveFightsFragment;
import com.yuri.game.ui.fragments.ActivityCallback;
import com.yuri.game.ui.fragments.CharDetailsFragment;
import com.yuri.game.ui.fragments.DuelOwnerInfoDialog;
import com.yuri.game.ui.fragments.DuelRequestFragment;
import com.yuri.game.ui.fragments.LocationFragment;
import com.yuri.game.ui.fragments.LocationPlayerInfoDialog;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener, LocationListener, ActivityCallback,
		DuelRequestListener, FightListener, OnPageChangeListener,
		ActorStatsListener {

	private ViewPager viewPager;
	private TabsPager tabsPagerAdapter;
	private ActionBar actionBar;
	private GameApplication app;
	private Menu menu;

	private ProgressDialog barProgressDialog;
	private Handler updateBarHandler;
	private List<TabInfo> tabs;

	// This stuff aimed for FightActivity
	private DuelMessage duelMessage;
	private FightEnemy fightEnemy;

	public static final class TabInfo {
		public Class<?> fragment;
		public Bundle args;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();
		tabs = new ArrayList<TabInfo>();
		tabsPagerAdapter = new TabsPager(this, getSupportFragmentManager(),
				tabs);
		updateBarHandler = new Handler();

		viewPager.setAdapter(tabsPagerAdapter);
		viewPager.setOnPageChangeListener(this);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		app = GameApplication.getApplicationFromActivity(this);

		initTabs();
	}

	private void initTabs() {

		Tab tab = actionBar.newTab();
		tab.setText("Character");
		TabInfo tag = new TabInfo();
		Bundle tabBundle = new Bundle();
		tabBundle.putInt("POS", 0);
		tabBundle.putString("NAME", "Character");
		tag.fragment = CharDetailsFragment.class;
		tag.args = tabBundle;
		tab.setTag(tag);
		tab.setTabListener(this);
		actionBar.addTab(tab);
		tabs.add(tag);
		tabsPagerAdapter.notifyDataSetChanged();

		tab = actionBar.newTab();
		tab.setText("Training Room");
		tag = new TabInfo();
		tabBundle = new Bundle();
		tabBundle.putInt("POS", 1);
		tabBundle.putString("NAME", "Training Room");
		tag.fragment = LocationFragment.class;
		tag.args = tabBundle;
		tab.setTag(tag);
		tab.setTabListener(this);
		actionBar.addTab(tab);
		tabs.add(tag);
		tabsPagerAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onPause() {
		super.onPause();
		app.controllers.locationController.locationListeners.remove(this);
		app.controllers.duelController.duelRequestListeners.remove(this);
		app.controllers.fightController.fightListeners.remove(this);
		app.controllers.actorStatsController.actorStatsListeners.remove(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		app.controllers.locationController.locationListeners.add(this);
		app.controllers.duelController.duelRequestListeners.add(this);
		app.controllers.fightController.fightListeners.add(this);
		app.controllers.actorStatsController.actorStatsListeners.add(this);

		notifyAdapters();
	}

	private void notifyAdapters() {
		WeakHashMap<Class<?>, Object> locAdapters = app.world.modelContainer.location.locAdapters;

		for (WeakHashMap.Entry<Class<?>, Object> entry : locAdapters.entrySet()) {
			Object value = entry.getValue();

			if (value instanceof ArrayAdapter<?> && value != null) {
				ArrayAdapter<?> ar = (ArrayAdapter<?>) value;
				ar.notifyDataSetChanged();
			}
		}
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

		switch (item.getItemId()) {
		case R.id.action_jump_to_fight:
			movePlayerToFight();
			break;
		case R.id.action_duel_requests:
			showDuelRequests();
			break;
		case R.id.action_current_fights:
			showCurrentFights();
			break;
		}

		return true;
	}

	private void showCurrentFights() {

		Fragment fragment = tabsPagerAdapter.getRegisteredFragment(1);
		if (fragment != null) {
			if (fragment instanceof LocationFragment) {
				Fragment child = fragment.getChildFragmentManager()
						.findFragmentById(R.id.middle_fragment_container);
				if (child != null) {
					ActiveFightsFragment aff = new ActiveFightsFragment();
					fragment.getChildFragmentManager().beginTransaction()
							.replace(R.id.middle_fragment_container, aff)
							.commit();

					menu.removeItem(R.id.action_current_fights);
					getMenuInflater().inflate(R.menu.duel_requests, menu);
				}
			}
		}
	}

	private void showDuelRequests() {

		Fragment fragment = tabsPagerAdapter.getRegisteredFragment(1);
		if (fragment != null) {
			if (fragment instanceof LocationFragment) {
				Fragment child = fragment.getChildFragmentManager()
						.findFragmentById(R.id.middle_fragment_container);
				if (child != null) {
					DuelRequestFragment drf = new DuelRequestFragment();
					fragment.getChildFragmentManager().beginTransaction()
							.replace(R.id.middle_fragment_container, drf)
							.commit();

					menu.removeItem(R.id.action_duel_requests);
					getMenuInflater().inflate(R.menu.current_fights, menu);
				}
			}
		}

	}

	private void movePlayerToFight() {

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.menu = menu;
		getMenuInflater().inflate(R.menu.current_fights, menu);
		return true;
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		viewPager.setCurrentItem(tab.getPosition());
		Log.e("onTabSelected", "Position " + tab.getPosition());

	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onWrongLocationCommand() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLocationPlayersListReceived() {
		Fragment fragment = tabsPagerAdapter.getRegisteredFragment(1);
		if (fragment != null) {
			if (fragment instanceof LocationFragment) {
				((LocationFragment) fragment).playersAdapter
						.notifyDataSetChanged();
			}
		}
	}

	@Override
	public void onNewPlayerAddedToLocation() {
		Fragment fragment = tabsPagerAdapter.getRegisteredFragment(1);
		if (fragment != null) {
			if (fragment instanceof LocationFragment) {
				((LocationFragment) fragment).playersAdapter
						.notifyDataSetChanged();
			}
		}
	}

	@Override
	public void onPlayerRemovedFromLocation() {
		Fragment fragment = tabsPagerAdapter.getRegisteredFragment(1);
		if (fragment != null) {
			if (fragment instanceof LocationFragment) {
				((LocationFragment) fragment).playersAdapter
						.notifyDataSetChanged();
			}
		}
	}

	@Override
	public void onCallback(AdapterView<?> parent, View view, int position,
			long id) {
		switch (position) {
		case 0:
			setupLoadingDialog("Moving to Training Room...");
			launchLoadingDialog(position);
			break;
		case 1:
			setupLoadingDialog("Moving to Castle...");
			launchLoadingDialog(position);
			break;
		case 2:
			setupLoadingDialog("Moving to Barracks...");
			launchLoadingDialog(position);
			break;
		case 3:
			swapFragment();
			break;
		}

	}

	private void swapFragment() {
		Fragment fragment = tabsPagerAdapter.getRegisteredFragment(1);
		if (fragment != null) {
			if (fragment instanceof LocationFragment) {
				Fragment child = fragment.getChildFragmentManager()
						.findFragmentById(R.id.middle_fragment_container);
				if (child != null) {
					if (child instanceof DuelRequestFragment) {
						ActiveFightsFragment aff = new ActiveFightsFragment();
						fragment.getChildFragmentManager().beginTransaction()
								.replace(R.id.middle_fragment_container, aff)
								.commit();
					} else if (child instanceof ActiveFightsFragment) {
						DuelRequestFragment df = new DuelRequestFragment();
						fragment.getChildFragmentManager().beginTransaction()
								.replace(R.id.middle_fragment_container, df)
								.commit();
					}
				}
			}
		}
	}

	private void setupLoadingDialog(String where) {

		int currentOrientation = getResources().getConfiguration().orientation;
		if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
		}

		barProgressDialog = new ProgressDialog(this);

		barProgressDialog.setTitle(where);
		barProgressDialog.setMessage("Please wait ...");
		barProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		barProgressDialog.setCancelable(false);
		barProgressDialog.setProgressNumberFormat(null);
		barProgressDialog.setProgressPercentFormat(null);
		barProgressDialog.setProgress(0);
		barProgressDialog.setMax(100);
		barProgressDialog.show();
	}

	private void move(int position) {
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
		
		LocationType lt = null;

		switch (position) {
		case 0:
			app.controllers.networkController
					.sendRequest(new ServerRequestFormer()
							.movePlayerTo(LocationType.TRAININGROOM));
			lt = LocationType.TRAININGROOM;
			break;
		case 1:
			app.controllers.networkController
					.sendRequest(new ServerRequestFormer()
							.movePlayerTo(LocationType.CASTLE));
			lt = LocationType.CASTLE;
			break;
		case 2:
			app.controllers.networkController
					.sendRequest(new ServerRequestFormer()
							.movePlayerTo(LocationType.BARRACKS));
			lt = LocationType.BARRACKS;
			break;
		}

		app.controllers.networkController.sendRequest(new ServerRequestFormer()
				.getLocationPlayers());
		
		adjustTabToLocation(lt);
	}

	private void adjustTabToLocation(LocationType lt) {
		
		Tab tab = actionBar.getTabAt(1);
		
		switch (lt) {
		case BARRACKS:
			tab.setText("Barracks");
			break;
		case CASTLE:
			tab.setText("Castle");
			break;
		case TRAININGROOM:
			tab.setText("Training Room");
			break;
		}
		
		app.world.modelContainer.player.setLocationType(lt);

		Fragment fragment = tabsPagerAdapter.getRegisteredFragment(0);
		if (fragment != null) {
			if (fragment instanceof CharDetailsFragment) {
				CharDetailsFragment cdf = (CharDetailsFragment) fragment;
				TextView tvLocation = (TextView) cdf.getView().findViewById(
						R.id.tv_location_name);
				tvLocation.setText("Location: "
						+ app.world.modelContainer.player.getLocationType()
								.name());
			}
		}
	}

	private void launchLoadingDialog(final int position) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {

					// Here you should write your time consuming task...
					while (barProgressDialog.getProgress() <= barProgressDialog
							.getMax()) {

						Thread.sleep(100);

						updateBarHandler.post(new Runnable() {

							public void run() {

								barProgressDialog.incrementProgressBy(1);

							}

						});

						if (barProgressDialog.getProgress() == barProgressDialog
								.getMax()) {

							barProgressDialog.dismiss();
							barProgressDialog = null;

							updateBarHandler.post(new Runnable() {

								public void run() {
									move(position);
								}

							});

						}
					}
				} catch (Exception e) {
				}
			}
		}).start();
	}

	@Override
	public void onDuelRequestListReceived() {
		Log.e("onDuelRequestListReceived", "called");

		Fragment fragment = tabsPagerAdapter.getRegisteredFragment(1);
		if (fragment != null) {
			if (fragment instanceof LocationFragment) {
				Fragment child = fragment.getChildFragmentManager()
						.findFragmentById(R.id.middle_fragment_container);
				if (child != null) {
					if (child instanceof DuelRequestFragment) {
						DuelRequestFragment df = (DuelRequestFragment) child;
						df.adapter.notifyDataSetChanged();
					}
				}
			}
		}
	}

	@Override
	public void onDuelRequestPublished() {
		Log.e("onDuelRequestPublished", "called");

		Fragment fragment = tabsPagerAdapter.getRegisteredFragment(1);
		if (fragment != null) {
			if (fragment instanceof LocationFragment) {
				Fragment child = fragment.getChildFragmentManager()
						.findFragmentById(R.id.middle_fragment_container);
				if (child != null) {
					if (child instanceof DuelRequestFragment) {
						DuelRequestFragment df = (DuelRequestFragment) child;
						df.adapter.notifyDataSetChanged();
					}
				}
			}
		}
	}

	@Override
	public void onDuelRequestRemoved() {
		Log.e("onDuelRequestPublished", "called");

		Fragment fragment = tabsPagerAdapter.getRegisteredFragment(1);
		if (fragment != null) {
			if (fragment instanceof LocationFragment) {
				Fragment child = fragment.getChildFragmentManager()
						.findFragmentById(R.id.middle_fragment_container);
				if (child != null) {
					if (child instanceof DuelRequestFragment) {
						DuelRequestFragment df = (DuelRequestFragment) child;
						df.adapter.notifyDataSetChanged();
					}
				}
			}
		}
	}

	@Override
	public void onDuelIsFull(String owner, String name) {
		String message = "Duel is full";
		if (app.world.modelContainer.player.getName().equals(name)) {
			Toast.makeText(this, message, Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onNewPlayerAddedToDuel(String owner, String challenger) {

		if (challenger.equals(app.world.modelContainer.player.getName())) {
			Fragment fragment = tabsPagerAdapter.getRegisteredFragment(1);
			if (fragment != null) {
				if (fragment instanceof LocationFragment) {
					Fragment child = fragment.getChildFragmentManager()
							.findFragmentById(R.id.middle_fragment_container);
					if (child != null) {
						if (child instanceof DuelRequestFragment) {
							DuelRequestFragment df = (DuelRequestFragment) child;
							df.adapter.hideAcceptButton();
						}
					}
				}
			}
			return;
		}

		final Dialog dialog = new Dialog(MainActivity.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_duel_request_accepted);
		TextView enemyName = (TextView) dialog
				.findViewById(R.id.tv_challenger_info);
		enemyName.setText("Player " + challenger + " accepted your request!");
		Button accept = (Button) dialog.findViewById(R.id.btn_accept);
		accept.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				app.controllers.networkController
						.sendRequest(new ServerRequestFormer().startDuel());
				dialog.dismiss();
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
			}
		});

		Button decline = (Button) dialog.findViewById(R.id.btn_decline);
		decline.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
			}
		});

		int currentOrientation = getResources().getConfiguration().orientation;
		if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
		}

		dialog.setCancelable(false);
		dialog.show();

	}

	@Override
	public void onDuelToStartNotFound() {
		String message = "Duel to start is not found";
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onDuelStartFail() {
		// Здесь нада показать что игрок отозвал заявку
		String message = "Cannot start duel, because callenger withdrew the request";
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onDuelToRemoveNotFound() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDuelToAddNewPlayerNotFound() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDuelToRemovePlayerFromNotFound() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPlayerRemovedFromDuel(String owner, String name) {
		String message = "Player " + name + " rejected your duel request!";
		if (app.world.modelContainer.player.getName().equals(owner)) {
			Toast.makeText(this, message, Toast.LENGTH_LONG).show();
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// Handle the back button
		if (keyCode == KeyEvent.KEYCODE_BACK && isTaskRoot()) {
			// Ask the user if they want to quit
			new AlertDialog.Builder(this)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle("Quit")
					.setMessage("Sure?")
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									clearResources();
									finish();
								}
							}).setNegativeButton("No", null).show();

			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	private void clearResources() {
		app.controllers.networkController.closeConnection();
		app.world.modelContainer.location.clearLists();
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int position) {
		actionBar.setSelectedNavigationItem(position);
	}

	@Override
	public void onPlayerStatsChanged() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onActorStatsRequested() {
		DialogType dt = app.world.modelContainer.location.dialogTypeStack.pop();
		
		switch (dt) {
		case DUEL:
			DuelOwnerInfoDialog dod = new DuelOwnerInfoDialog();
			dod.show(getSupportFragmentManager(), "dialog1");
			break;
		case PLAYER:
			LocationPlayerInfoDialog lpd = new LocationPlayerInfoDialog();
			lpd.show(getSupportFragmentManager(), "dialog2");
			break;
		}
	}

	@Override
	public void onCharNotFound(String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFightRegistered() {

		Fragment fragment = tabsPagerAdapter.getRegisteredFragment(1);
		if (fragment != null) {
			if (fragment instanceof LocationFragment) {
				Fragment child = fragment.getChildFragmentManager()
						.findFragmentById(R.id.middle_fragment_container);
				if (child != null) {
					if (child instanceof ActiveFightsFragment) {
						ActiveFightsFragment aff = (ActiveFightsFragment) child;
						aff.currentFightsAdapter.notifyDataSetChanged();
					}
				}
			}
		}
	}

	@Override
	public void onFightMessage(DuelMessage message) {
		duelMessage = message;
	}

	@Override
	public void onFightUnregistered() {
		Fragment fragment = tabsPagerAdapter.getRegisteredFragment(1);
		if (fragment != null) {
			if (fragment instanceof LocationFragment) {
				Fragment child = fragment.getChildFragmentManager()
						.findFragmentById(R.id.middle_fragment_container);
				if (child != null) {
					if (child instanceof ActiveFightsFragment) {
						ActiveFightsFragment aff = (ActiveFightsFragment) child;
						aff.currentFightsAdapter.notifyDataSetChanged();
					}
				}
			}
		}
	}

	@Override
	public void onEnemyAttackState(AttackState attackState, String target) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEnemyInitStats(FightEnemy enemy) {
		fightEnemy = enemy;

		Intent intent = new Intent(MainActivity.this, FightActivity.class);
		intent.putExtra("START_MESSAGE", duelMessage);
		intent.putExtra("FIGHT_ENEMY", fightEnemy);
		startActivity(intent);

		getMenuInflater().inflate(R.menu.jump_to_fight, menu);
	}
}
