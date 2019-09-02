package com.yuri.game.controller;

import com.yuri.game.controller.listeners.FightListeners;
import com.yuri.game.model.actor.FightEnemy;
import com.yuri.game.model.duel.AttackState;
import com.yuri.game.model.duel.DuelMessage;

public class FightController {

	public final FightListeners fightListeners = new FightListeners();

	public FightController() {

	}

	public void onFightRegistered() {
		fightListeners.onFightRegistered();
	}

	public void onFightMessage(DuelMessage message) {
		fightListeners.onFightMessage(message);
	}

	public void onFightUnregistered() {
		fightListeners.onFightUnregistered();
	}

	public void onEnemyAttackState(AttackState attackState, String target) {
		fightListeners.onEnemyAttackState(attackState, target);
	}
	
	public void onEnemyInitStats(FightEnemy enemy) {
		fightListeners.onEnemyInitStats(enemy);
	}
}
