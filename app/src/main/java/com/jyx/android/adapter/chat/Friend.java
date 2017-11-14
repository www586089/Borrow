package com.jyx.android.adapter.chat;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yiyi on 2015/11/9
 */
public class Friend implements Parcelable{

	private String sortLetters;
	private String userName;
	private boolean userSelected = false;
	private String userId = null;
	private String portraituri = null;
	
	public String getSortLetters() {
		return sortLetters;
	}
	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public boolean getSelected() {
		return userSelected;
	}
	public void setSelected(boolean selected) {
		this.userSelected = selected;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPortraituri() {
		return portraituri;
	}

	public void setPortraituri(String portraituri) {
		this.portraituri = portraituri;
	}

	public Friend() {}
	public Friend(Parcel in) {
		this.sortLetters = in.readString();
		this.userName = in.readString();
		this.userId = in.readString();
	}
	public static final Parcelable.Creator<Friend> CREATOR = new Creator<Friend>() {
		@Override
		public Friend[] newArray(int size) {
			return new Friend[size];
		}

		@Override
		public Friend createFromParcel(Parcel in) {
			return new Friend(in);
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.sortLetters);
		dest.writeString(this.userName);
		dest.writeString(this.userId);
	}
}
