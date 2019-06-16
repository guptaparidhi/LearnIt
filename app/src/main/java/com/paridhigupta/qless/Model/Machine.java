package com.paridhigupta.qless.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Machine implements Parcelable {
    private String numb, identity, machine_id;

    public Machine() {
    }

    protected Machine(Parcel in) {
        numb = in.readString();
        identity = in.readString();
        machine_id = in.readString();
    }

    public static final Creator<Machine> CREATOR = new Creator<Machine>() {
        @Override
        public Machine createFromParcel(Parcel in) {
            return new Machine(in);
        }

        @Override
        public Machine[] newArray(int size) {
            return new Machine[size];
        }
    };

    public String getNumb() {
        return numb;
    }

    public void setNumb(String numb) {
        this.numb = numb;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getMachine_id() {
        return machine_id;
    }

    public void setMachine_id(String machine_id) {
        this.machine_id = machine_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(numb);
        dest.writeString(identity);
        dest.writeString(machine_id);
    }
}
