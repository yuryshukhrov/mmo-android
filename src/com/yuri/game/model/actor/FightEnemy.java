package com.yuri.game.model.actor;

import android.os.Parcel;
import android.os.Parcelable;

public class FightEnemy implements Parcelable {

	private String name;
	private GenderType gender;
	private int level;
	private int currentHp;
	private int maxHp;

	public FightEnemy(String name, GenderType gender, int level, int currentHp,
			int maxHp) {
		this.name = name;
		this.gender = gender;
		this.level = level;
		this.currentHp = currentHp;
		this.maxHp = maxHp;
	}

	public FightEnemy() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public GenderType getGender() {
		return gender;
	}

	public void setGender(GenderType gender) {
		this.gender = gender;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
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
		return "FightEnemy [name=" + name + ", gender=" + gender + ", level="
				+ level + ", currentHp=" + currentHp + ", maxHp=" + maxHp + "]";
	}

	/*
	 *   Parcelable Area
	 * 
	 */
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(name);
		out.writeInt(gender.ordinal());
		out.writeInt(level);
		out.writeInt(currentHp);
		out.writeInt(maxHp);
	}

	public static final Parcelable.Creator<FightEnemy> CREATOR = new Parcelable.Creator<FightEnemy>() {
		public FightEnemy createFromParcel(Parcel in) {
			return new FightEnemy(in);
		}

		@Override
		public FightEnemy[] newArray(int size) {
			return new FightEnemy[size];
		}
	};

	private FightEnemy(Parcel in) {
		name = in.readString();
		gender = GenderType.values()[in.readInt()];
		level = in.readInt();
		currentHp = in.readInt();
		maxHp = in.readInt();
	}
}
