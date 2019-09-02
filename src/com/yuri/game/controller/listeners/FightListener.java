package com.yuri.game.controller.listeners;

import com.yuri.game.model.actor.FightEnemy;
import com.yuri.game.model.duel.AttackState;
import com.yuri.game.model.duel.DuelMessage;

public interface FightListener {
	
	void onFightRegistered();

	void onFightMessage(DuelMessage message);

	void onFightUnregistered();
	
	void onEnemyAttackState(AttackState attackState, String target);
	
	void onEnemyInitStats(FightEnemy enemy);
	
}
