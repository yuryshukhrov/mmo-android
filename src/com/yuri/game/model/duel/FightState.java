package com.yuri.game.model.duel;

import android.os.Parcel;
import android.os.Parcelable;

public class FightState implements Parcelable{
	
	private String type;
	private String red;
	private String blue;
	private String winner;
	
	public FightState() {
		
	}

	public FightState(String type, String red, String blue, String winner) {
		this.type = type;
		this.red = red;
		this.blue = blue;
		this.winner = winner;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRed() {
		return red;
	}

	public void setRed(String red) {
		this.red = red;
	}

	public String getBlue() {
		return blue;
	}

	public void setBlue(String blue) {
		this.blue = blue;
	}

	public String getWinner() {
		return winner;
	}

	public void setWinner(String winner) {
		this.winner = winner;
	}

	@Override
	public String toString() {
		return "FightState [type=" + type + ", red=" + red + ", blue=" + blue
				+ ", winner=" + winner + "]";
	}

	
	/* *****************************  Parcelable Area  ***************************** */
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(type);
		out.writeString(red);
		out.writeString(blue);
		out.writeString(winner);
	}

	public static final Parcelable.Creator<FightState> CREATOR = new Parcelable.Creator<FightState>() {
		public FightState createFromParcel(Parcel in) {
			return new FightState(in);
		}

		@Override
		public FightState[] newArray(int size) {
			return new FightState[size];
		}
	};

	private FightState(Parcel in) {
		type = in.readString();
		red = in.readString();
		blue = in.readString();
		winner = in.readString();
	}
}
