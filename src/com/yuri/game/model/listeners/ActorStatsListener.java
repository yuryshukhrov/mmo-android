package com.yuri.game.model.listeners;


public interface ActorStatsListener {
	void onPlayerStatsChanged();
	void onActorStatsRequested();
	void onCharNotFound(String message);
}
