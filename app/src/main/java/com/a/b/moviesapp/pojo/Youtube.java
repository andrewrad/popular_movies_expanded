package com.a.b.moviesapp.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Java object filled by Retrofit, part of retrieving movies and trailers.
 * Created by Andrew on 1/20/2016.
 */
public class Youtube implements Parcelable{

    public String name;
    public String size;
    public String source;
    public String type;

    public Youtube(){
    }

    public Youtube(Parcel source){
        readFromParcel(source);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(size);
        dest.writeString(source);
        dest.writeString(type);
    }
    public void readFromParcel(Parcel source) {
        name = source.readString();
        size = source.readString();
        this.source = source.readString();
        type = source.readString();
    }

    private final Parcelable.Creator<Youtube>CREATOR=new Parcelable.Creator<Youtube>(){

        @Override
        public Youtube createFromParcel(Parcel source) {
            return new Youtube(source);
        }

        @Override
        public Youtube[] newArray(int size) {
            return new Youtube[size];
        }
    };


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
