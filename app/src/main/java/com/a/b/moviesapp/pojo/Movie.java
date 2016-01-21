package com.a.b.moviesapp.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Andrew on 1/1/2015.
 */
public class Movie implements Parcelable {
    public Integer mId;
    public String mTitle;
    public String mOverview;
    public String mPosterUrl;
    public String mBackDropUrl;
    public String mDate;
    public Double mVoteAverage;

    public Movie(){
    }

    public Movie(Parcel source){
        readFromParcel(source);
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mTitle);
        dest.writeString(mOverview);
        dest.writeString(mPosterUrl);
        dest.writeString(mBackDropUrl);
        dest.writeString(mDate);
        dest.writeDouble(mVoteAverage);

    }
    public void readFromParcel(Parcel source){
        mId=source.readInt();
        mTitle=source.readString();
        mOverview=source.readString();
        mPosterUrl =source.readString();
        mBackDropUrl=source.readString();
        mDate=source.readString();
        mVoteAverage=source.readDouble();
    }
    private final Parcelable.Creator<Movie>CREATOR=new Parcelable.Creator<Movie>(){

        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public void setId(Integer id){mId=id;}
    public Integer getId(){return mId;}
    public String getMovieTitle(){
        return mTitle;
    }
    public String getPosterUrl(){
        return mPosterUrl;
    }
    public String getBackDropUrl(){
        return mBackDropUrl;
    }
    public String getDate(){
        return mDate;
    }
    public String getSummary(){
        return mOverview;
    }
    public Double getRating(){
        return mVoteAverage;
    }



    private String id;
    private String iso6391;
    private String key;
    private String name;
    private String site;
    private Integer size;
    private String type;

//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }

    public String getIso6391() {
        return iso6391;
    }

    public void setIso6391(String iso6391) {
        this.iso6391 = iso6391;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
