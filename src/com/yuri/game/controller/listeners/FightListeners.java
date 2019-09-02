package com.yuri.game.controller.listeners;

import com.yuri.game.model.actor.FightEnemy;
import com.yuri.game.model.duel.AttackState;
import com.yuri.game.model.duel.DuelMessage;
import com.yuri.game.utils.ListenersAdapter;

public class FightListeners extends ListenersAdapter<FightListener> implements
		FightListener {

	private final Function<FightListener> onFightRegistered = new Function<FightListener>() {

		@Override
		public void call(FightListener listener) {
			listener.onFightRegistered();
		}
	};

	private final Function1<FightListener, DuelMessage> onFightMessage = new Function1<FightListener, DuelMessage>() {

		@Override
		public void call(FightListener listener, DuelMessage message) {
			listener.onFightMessage(message);
		}
	};

	private final Function<FightListener> onFightUnregistered = new Function<FightListener>() {

		@Override
		public void call(FightListener listener) {
			listener.onFightUnregistered();
		}
	};

	private final Function2<FightListener, AttackState, String> onEnemyAttackState = new Function2<FightListener, AttackState, String>() {

		@Override
		public void call(FightListener listener, AttackState attackState,
				String target) {
			listener.onEnemyAttackState(attackState, target);
		}
	};

	private final Function1<FightListener, FightEnemy> onEnemyInitStats = new Function1<FightListener, FightEnemy>() {

		@Override
		public void call(FightListener listener, FightEnemy enemy) {
			listener.onEnemyInitStats(enemy);
		}
	};

	@Override
	public void onFightRegistered() {
		callAllListeners(this.onFightRegistered);
	}

	@Override
	public void onFightMessage(DuelMessage message) {
		callAllListeners(this.onFightMessage, message);
	}

	@Override
	public void onFightUnregistered() {
		callAllListeners(this.onFightUnregistered);
	}

	@Override
	public void onEnemyAttackState(AttackState attackState, String target) {
		callAllListeners(this.onEnemyAttackState, attackState, target);
	}

	@Override
	public void onEnemyInitStats(FightEnemy enemy) {
		callAllListeners(this.onEnemyInitStats, enemy);
	}
}
