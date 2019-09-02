package com.yuri.game.controller;

import android.util.Log;

import com.yuri.game.context.WorldContext;
import com.yuri.game.model.actor.Actor;
import com.yuri.game.model.listeners.ActorStatsListeners;

public final class ActorStatsController {

	private final WorldContext world;
	
	public final ActorStatsListeners actorStatsListeners = new ActorStatsListeners();

	public ActorStatsController(WorldContext world) {
    	this.world = world;
    }
	
	public void updatePlayerStats() {	
		actorStatsListeners.onPlayerStatsChanged();	
	}
	
	public void updateActorStats(Actor actor) {
		Log.e("updateActorStats", "START");
		world.modelContainer.actor = actor;
		actorStatsListeners.onActorStatsRequested();
		Log.e("updateActorStats", "FINISH");
	}
	
	public void handleCharNotFound() {
		actorStatsListeners.onCharNotFound("Char not found!");
	}
}
