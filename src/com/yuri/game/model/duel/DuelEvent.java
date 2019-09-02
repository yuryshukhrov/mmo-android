package com.yuri.game.model.duel;

import android.os.Parcel;
import android.os.Parcelable;

public class DuelEvent implements Parcelable {
	
	private EventType eventType;
	private String target;
	
	public DuelEvent(EventType eventType, String target) {
		this.eventType = eventType;
		this.target = target;
	}
	
	public DuelEvent() {}

	public EventType getEventType() {
		return eventType;
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	@Override
	public String toString() {
		return "DuelEvent [eventType=" + eventType + ", target=" + target + "]";
	}
	
	/* *****************************  Parcelable Area  ***************************** */
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(eventType.ordinal());
		out.writeString(target);
	}

	public static final Parcelable.Creator<DuelEvent> CREATOR = new Parcelable.Creator<DuelEvent>() {
		public DuelEvent createFromParcel(Parcel in) {
			return new DuelEvent(in);
		}

		@Override
		public DuelEvent[] newArray(int size) {
			return new DuelEvent[size];
		}
	};

	private DuelEvent(Parcel in) {
		eventType = EventType.values()[in.readInt()];
		target = in.readString();
	}
}
