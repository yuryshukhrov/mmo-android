package com.yuri.game.model.location;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.WeakHashMap;

import android.util.Log;

import com.yuri.game.model.DialogType;
import com.yuri.game.model.actor.LocationType;
import com.yuri.game.model.duel.CurrentFight;
import com.yuri.game.model.duel.DuelRequest;

public class Location {
	
	private LocationType type;
	private List<LocationPlayer> players = new ArrayList<LocationPlayer>();
	private List<DuelRequest> duelRequests = new ArrayList<DuelRequest>();
	private List<CurrentFight> currentFights = new ArrayList<CurrentFight>();
	
	public WeakHashMap<Class<?>, Object> locAdapters = new WeakHashMap<Class<?>, Object>();
	
	public Stack<DialogType> dialogTypeStack = new Stack<DialogType>();
	
	public Location() {}
	
	public List<DuelRequest> getDuelRequests() {
		return duelRequests;
	}

	public void setDuelRequests(List<DuelRequest> duelRequests) {
		this.duelRequests = duelRequests;
	}
	
	public List<CurrentFight> getCurrentFights() {
		return currentFights;
	}

	public void setCurrentFights(List<CurrentFight> currentFights) {
		this.currentFights = currentFights;
	}

	public void setPlayers(List<LocationPlayer> players) {
		this.players = players;
	}
	
	public LocationType getType() {
		return type;
	}

	public void setType(LocationType type) {
		this.type = type;
	}

	public List<LocationPlayer> getPlayers() {
		return players;
	}

	public void addPlayers(List<LocationPlayer> players) {
		this.players = players;
	}
	
	public void addNewPlayer(LocationPlayer player) {
		Log.e("addNewPlayer", "Adding: " + player.toString());

		boolean hasPlayerName = false;
		for (LocationPlayer p : players) {
			if (p.getName().equals(player.getName())) {
				hasPlayerName = true;
				break;
			}
		}
		
		if (!hasPlayerName) {
			players.add(player);
			Log.e("addNewPlayer", "Added!");
		}
	}
	
	public void registerNewFight(CurrentFight currentFight) {
		Log.e("registerNewFight", "Adding: " + currentFight.toString());

		boolean isAlreadyRegistered = false;
		for (CurrentFight c : currentFights) {
			if (c.getId() == currentFight.getId()) {
				isAlreadyRegistered = true;
				break;
			}
		}
		
		if (!isAlreadyRegistered) {
			currentFights.add(currentFight);
			Log.e("registerNewFight", "Added!");
		}
	}
	
	public void unRegisterFight(CurrentFight currentFight) {
		Log.e("unRegisterFight", "Removing: " + currentFight.toString());
		Iterator<CurrentFight> it = currentFights.iterator();
		while (it.hasNext()) {
			CurrentFight c = it.next();
			if (c.getId() == currentFight.getId()) {
				it.remove();
				Log.e("unRegisterFight", "Removed");
			}
		}
	}
	
	public void removePlayer(LocationPlayer player) {
		Log.e("removePlayer", "Removing: " + player.toString());
		Iterator<LocationPlayer> it = players.iterator();
		while (it.hasNext()) {
			LocationPlayer p = it.next();
			if (p.getName().equals(player.getName())) {
				it.remove();
				Log.e("removePlayer", "Removed");
			}
		}
	}
	
	public void clearLists() {
		players.clear();
		duelRequests.clear();
	}

	public List<DuelRequest> getDuelRequestsList() {
		return duelRequests;
	}

	public void setDuelRequestsList(List<DuelRequest> duelList) {
		this.duelRequests = duelList;
	}

	
	public void addDuelRequest(DuelRequest duelRequest) {
		Log.e("addDuelRequest", "Adding: " + duelRequest.toString());

		boolean hasPlayerName = false;
		for (DuelRequest d : duelRequests) {
			if (d.getOwner().equals(duelRequest.getOwner())) {
				hasPlayerName = true;
				break;
			}
		}
		
		if (!hasPlayerName) {
			duelRequests.add(duelRequest);
			Log.e("addDuelRequest", "Added!");
		}
	}

	public void removeDuelRequest(DuelRequest duelRequest) {
		Log.e("removeDuelRequest", "Removing: " + duelRequest.toString());
		Iterator<DuelRequest> it = duelRequests.iterator();
		while (it.hasNext()) {
			DuelRequest d = it.next();
			if (d.getOwner().equals(duelRequest.getOwner())) {
				it.remove();
				Log.e("removeDuelRequest", "Removed");
			}
		}
	}
	
	public void showPlayersInLocation() {
		
		Log.e("showPlayersInLocation()", "");
		for (LocationPlayer p : players) {
		    String name = p.getName();
		    int level = p.getLevel();
		    Log.e("Player: " + name + ", ", "Level: " + level + ".");
		}
	}
	
	public CurrentFight findFightByFighterName(String name) {
		
		for (CurrentFight c : currentFights) {
			if (fightByFighterNameFound(c, name)) {
				return c;
			}
		}
		
		return null;
	}
	
	private boolean fightByFighterNameFound(CurrentFight c, String name) {
		for (String red: c.getRedPlayers()) {
			if (red.equals(name)) {
				return true;
			}
		}
		
		for (String blue: c.getBluePlayers()) {
			if (blue.equals(name)) {
				return true;
			}
		}
		
		return false;
	}
}
