package com.yuri.game.model.location;

import android.os.Parcel;
import android.os.Parcelable;

import com.yuri.game.model.actor.GenderType;
import com.yuri.game.model.actor.LocationType;

public class LocationPlayer implements Parcelable {
	
	private String name;
	private int level;
	private GenderType gender;
	private LocationType locationType;
	
	public LocationPlayer() {}
	
	public LocationPlayer(String name, int level, GenderType gender,
			LocationType locationType) {
		this.name = name;
		this.level = level;
		this.gender = gender;
		this.locationType = locationType;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public GenderType getGender() {
		return gender;
	}

	public void setGender(GenderType gender) {
		this.gender = gender;
	}

	public LocationType getLocationType() {
		return locationType;
	}

	public void setLocationType(LocationType locationType) {
		this.locationType = locationType;
	}
	
	@Override
	public String toString() {
		return "LocationPlayer [name=" + name + ", level=" + level
				+ ", gender=" + gender + ", locationType=" + locationType + "]";
	}

/**
 * 	Parcelable Area
 */
	public static final Parcelable.Creator<LocationPlayer> CREATOR = new Parcelable.Creator<LocationPlayer>() {

		public LocationPlayer createFromParcel(Parcel in) {
			return new LocationPlayer(in);
		}

		public LocationPlayer[] newArray(int size) {
			return new LocationPlayer[size];
		}
	};

	private LocationPlayer(Parcel in) {
		name = in.readString();
		level = in.readInt();
		gender = GenderType.values()[in.readInt()];
		locationType = LocationType.values()[in.readInt()];
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(name);
		out.writeInt(level);
		out.writeInt(gender.ordinal());
		out.writeInt(locationType.ordinal());
	}
}
