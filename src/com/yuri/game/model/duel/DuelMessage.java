package com.yuri.game.model.duel;

import android.os.Parcel;
import android.os.Parcelable;

public class DuelMessage implements Parcelable {
	
	private DuelAttack firstAttack;
	private DuelAttack secondAttack;
	private FightState fightState;
	private DuelEvent firstEvent;
	private DuelEvent secondEvent;
	private String date;
	
	public DuelMessage() {
		
	}
	
	public DuelMessage(DuelAttack firstAttack, DuelAttack secondAttack,
			FightState fightState, DuelEvent firstEvent, DuelEvent secondEvent,
			String date) {
		this.firstAttack = firstAttack;
		this.secondAttack = secondAttack;
		this.fightState = fightState;
		this.firstEvent = firstEvent;
		this.secondEvent = secondEvent;
		this.date = date;
	}

	public DuelAttack getFirstAttack() {
		return firstAttack;
	}

	public void setFirstAttack(DuelAttack firstAttack) {
		this.firstAttack = firstAttack;
	}

	public DuelAttack getSecondAttack() {
		return secondAttack;
	}

	public void setSecondAttack(DuelAttack secondAttack) {
		this.secondAttack = secondAttack;
	}

	public FightState getFightState() {
		return fightState;
	}

	public void setFightState(FightState fightState) {
		this.fightState = fightState;
	}

	public DuelEvent getFirstEvent() {
		return firstEvent;
	}

	public void setFirstEvent(DuelEvent firstEvent) {
		this.firstEvent = firstEvent;
	}

	public DuelEvent getSecondEvent() {
		return secondEvent;
	}

	public void setSecondEvent(DuelEvent secondEvent) {
		this.secondEvent = secondEvent;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "DuelMessage [firstAttack=" + firstAttack + ", secondAttack="
				+ secondAttack + ", fightState=" + fightState + ", firstEvent="
				+ firstEvent + ", secondEvent=" + secondEvent + ", date="
				+ date + "]";
	}

	/* *****************************  Parcelable Area  ***************************** */
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeParcelable(firstAttack, flags);
		out.writeParcelable(secondAttack, flags);
		out.writeParcelable(fightState, flags);
		out.writeParcelable(firstEvent, flags);
		out.writeParcelable(secondEvent, flags);
		out.writeString(date);
	}

	public static final Parcelable.Creator<DuelMessage> CREATOR = new Parcelable.Creator<DuelMessage>() {
		public DuelMessage createFromParcel(Parcel in) {
			return new DuelMessage(in);
		}

		@Override
		public DuelMessage[] newArray(int size) {
			return new DuelMessage[size];
		}
	};

	private DuelMessage(Parcel in) {
		firstAttack = (DuelAttack) in.readParcelable(DuelAttack.class.getClassLoader());
		secondAttack = (DuelAttack) in.readParcelable(DuelAttack.class.getClassLoader());
		fightState = (FightState) in.readParcelable(FightState.class.getClassLoader());
		firstEvent = (DuelEvent) in.readParcelable(DuelEvent.class.getClassLoader());
		secondEvent = (DuelEvent) in.readParcelable(DuelEvent.class.getClassLoader());
		date = in.readString();
	}	
}
