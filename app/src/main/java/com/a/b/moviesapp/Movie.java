package com.a.b.moviesapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Andrew on 7/13/2015.
 */
public class Movie implements Parcelable {
    private String mTitle;
    private String mOverview;
    private String mUrl;
    private String mDate;
    private Double mVoteAverage;

    public Movie(String title, String overview, String url, String date, Double vote){
        this.mTitle=title;
        this.mOverview=overview;
        this.mUrl=url;
        this.mDate=date;
        this.mVoteAverage=vote;
    }

    private Movie(Parcel in){
        mTitle=in.readString();
        mOverview=in.readString();
        mUrl=in.readString();
        mDate=in.readString();
        mVoteAverage=in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mOverview);
        dest.writeString(mUrl);
        dest.writeString(mDate);
        dest.writeDouble(mVoteAverage);

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
    public String getMovieUrl(){
        return mUrl;
    }
    public String getMovieTitle(){
        return mTitle;
    }
    public String getUrl(){
        return mUrl;
    }
}
