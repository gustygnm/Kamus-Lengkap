package com.gnm.kamus.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Kamus implements Parcelable {

    private int id;
    private String kata;
    private String terjemahan;

    public Kamus() {
    }

    public Kamus(String kata, String terjemahan) {
        this.kata = kata;
        this.terjemahan = terjemahan;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKata() {
        return kata;
    }

    public void setKata(String kata) {
        this.kata = kata;
    }

    public String getTerjemahan() {
        return terjemahan;
    }

    public void setTerjemahan(String terjemahan) {
        this.terjemahan = terjemahan;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.kata);
        dest.writeString(this.terjemahan);
    }

    protected Kamus(Parcel in) {
        this.id = in.readInt();
        this.kata = in.readString();
        this.terjemahan = in.readString();
    }

    public static final Parcelable.Creator<Kamus> CREATOR = new Parcelable.Creator<Kamus>() {
        @Override
        public Kamus createFromParcel(Parcel source) {
            return new Kamus(source);
        }

        @Override
        public Kamus[] newArray(int size) {
            return new Kamus[size];
        }
    };
    
}
