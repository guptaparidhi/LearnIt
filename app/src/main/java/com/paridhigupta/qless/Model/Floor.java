package com.paridhigupta.qless.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Floor implements Parcelable {
    private String floor, floor_id;

    public Floor() {
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getFloor_id() {
        return floor_id;
    }

    public void setFloor_id(String floor_id) {
        this.floor_id = floor_id;
    }

    public static Creator<Floor> getCREATOR() {
        return CREATOR;
    }

    protected Floor(Parcel in) {
        floor = in.readString();
        floor_id = in.readString();
    }

    public static final Creator<Floor> CREATOR = new Creator<Floor>() {
        @Override
        public Floor createFromParcel(Parcel in) {
            return new Floor(in);
        }

        @Override
        public Floor[] newArray(int size) {
            return new Floor[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(floor);
        dest.writeString(floor_id);
    }
}
