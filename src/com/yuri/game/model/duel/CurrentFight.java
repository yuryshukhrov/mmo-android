package com.yuri.game.model.duel;

import java.util.List;

public class CurrentFight {
	
	private int id;
	private int timeout;
	private List<String> bluePlayers;
	private List<String> redPlayers;
	
	public CurrentFight() {}
	
	public CurrentFight(int id, int timeout, List<String> bluePlayers,
			List<String> redPlayers) {
		this.id = id;
		this.timeout = timeout;
		this.bluePlayers = bluePlayers;
		this.redPlayers = redPlayers;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public List<String> getBluePlayers() {
		return bluePlayers;
	}

	public void setBluePlayers(List<String> bluePlayers) {
		this.bluePlayers = bluePlayers;
	}

	public List<String> getRedPlayers() {
		return redPlayers;
	}

	public void setRedPlayers(List<String> redPlayers) {
		this.redPlayers = redPlayers;
	}

	@Override
	public String toString() {
		return "CurrentFight [id=" + id + ", timeout=" + timeout
				+ ", bluePlayers=" + bluePlayers + ", redPlayers=" + redPlayers
				+ "]";
	}
}
