package com.yuri.game.model.duel;

import android.os.Parcel;
import android.os.Parcelable;

public class DuelAttack implements Parcelable {
	
	private AttackType attackType;
	private AttackTarget attackTarget;
	private String attacker;
	private String receiver;
	private int damage;
	private int currentHp;
	private int maxHp;
	
	public DuelAttack() {
		
	}

	public DuelAttack(AttackType attackType, AttackTarget attackTarget,
			String attacker, String receiver, int damage, int currentHp,
			int maxHp) {
		this.attackType = attackType;
		this.attackTarget = attackTarget;
		this.attacker = attacker;
		this.receiver = receiver;
		this.damage = damage;
		this.currentHp = currentHp;
		this.maxHp = maxHp;
	}

	public AttackType getAttackType() {
		return attackType;
	}

	public void setAttackType(AttackType attackType) {
		this.attackType = attackType;
	}

	public AttackTarget getAttackTarget() {
		return attackTarget;
	}

	public void setAttackTarget(AttackTarget attackTarget) {
		this.attackTarget = attackTarget;
	}

	public String getAttacker() {
		return attacker;
	}

	public void setAttacker(String attacker) {
		this.attacker = attacker;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public int getCurrentHp() {
		return currentHp;
	}

	public void setCurrentHp(int currentHp) {
		this.currentHp = currentHp;
	}

	public int getMaxHp() {
		return maxHp;
	}

	public void setMaxHp(int maxHp) {
		this.maxHp = maxHp;
	}

	@Override
	public String toString() {
		return "DuelAttack [attackType=" + attackType + ", attackTarget="
				+ attackTarget + ", attacker=" + attacker + ", receiver="
				+ receiver + ", damage=" + damage + ", currentHp=" + currentHp
				+ ", maxHp=" + maxHp + "]";
	}

	/* *****************************  Parcelable Area  ***************************** */
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(attackType.ordinal());
		out.writeInt(attackTarget.ordinal());
		out.writeString(attacker);
		out.writeString(receiver);
		out.writeInt(damage);
		out.writeInt(currentHp);
		out.writeInt(maxHp);
	}

	public static final Parcelable.Creator<DuelAttack> CREATOR = new Parcelable.Creator<DuelAttack>() {
		public DuelAttack createFromParcel(Parcel in) {
			return new DuelAttack(in);
		}

		@Override
		public DuelAttack[] newArray(int size) {
			return new DuelAttack[size];
		}
	};

	private DuelAttack(Parcel in) {
		attackType = AttackType.values()[in.readInt()];
		attackTarget = AttackTarget.values()[in.readInt()];
		attacker = in.readString();
		receiver = in.readString();
		damage = in.readInt();
		currentHp = in.readInt();
		maxHp = in.readInt();
	}
}
