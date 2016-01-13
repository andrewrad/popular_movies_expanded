package com.a.b.moviesapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Andrew on 7/13/2015.
 */
public class Movie implements Parcelable {
    public String mTitle;
    public String mOverview;
    public String mPosterUrl;
    public String mBackDropUrl;
    public String mDate;
    public Double mVoteAverage;

//    public Movie(String title, String overview, String posterUrl, String backdropUrl,String date, Double vote){
//        this.mTitle=title;
//        this.mOverview=overview;
//        this.mPosterUrl =posterUrl;
//        this.mBackDropUrl=backdropUrl;
//        this.mDate=date;
//        this.mVoteAverage=vote;
//    }
    public Movie(){
    }

//    private Movie(Parcel in){
//        mTitle=in.readString();
//        mOverview=in.readString();
//        mPosterUrl =in.readString();
//        mBackDropUrl=in.readString();
//        mDate=in.readString();
//        mVoteAverage=in.readDouble();
//    }

    public Movie(Parcel source){
        readFromParcel(source);
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mOverview);
        dest.writeString(mPosterUrl);
        dest.writeString(mBackDropUrl);
        dest.writeString(mDate);
        dest.writeDouble(mVoteAverage);

    }
    public void readFromParcel(Parcel source){
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
}
