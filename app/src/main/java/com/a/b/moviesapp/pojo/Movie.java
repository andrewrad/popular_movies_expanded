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

    private String mIso6391;
    private String mKey;
    private String mName;
    private String mSite;
    private Integer mSize;
    private String mType;

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

        dest.writeString(mIso6391);
        dest.writeString(mKey);
        dest.writeString(mName);
        dest.writeString(mSite);
        dest.writeInt(mSize);
        dest.writeString(mType);

    }
    public void readFromParcel(Parcel source){
        mId=source.readInt();
        mTitle=source.readString();
        mOverview=source.readString();
        mPosterUrl =source.readString();
        mBackDropUrl=source.readString();
        mDate=source.readString();
        mVoteAverage=source.readDouble();

        mIso6391=source.readString();
        mKey=source.readString();
        mName=source.readString();
        mSite=source.readString();
        mSize=source.readInt();
        mType=source.readString();

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

    public void setId(Integer id){
        mId=id;}
    public Integer getId(){
        return mId;}
    public String getMovieTitle(){
        return mTitle;
    }
    public void setMovieTitle(String title){
        mTitle=title;
    }
    public String getPosterUrl(){
        return mPosterUrl;
    }
    public void setPosterUrl(String url){
        mPosterUrl=url;
    }
    public String getBackDropUrl(){
        return mBackDropUrl;
    }
    public void setBackDropUrl(String url){
        mBackDropUrl=url;
    }
    public String getDate(){
        return mDate;
    }
    public void setDate(String date){
        mDate=date;
    }
    public String getSummary(){
        return mOverview;
    }
    public void setSummary(String summary){
        mOverview=summary;
    }
    public Double getRating(){
        return mVoteAverage;
    }
    public void setRating(Double rating){
        mVoteAverage=rating;
    }



    public String getIso6391() {
        return mIso6391;
    }

    public void setIso6391(String mIso6391) {
        this.mIso6391 = mIso6391;
    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String mKey) {
        this.mKey = mKey;
    }

//    public String getName() {
//        return mName;
//    }

//    public void setName(String name) {
//        this.mName = name;
//    }

    public String getSite() {
        return mSite;
    }

    public void setSite(String site) {
        this.mSite = site;
    }

    public Integer getSize() {
        return mSize;
    }

    public void setSize(Integer size) {
        this.mSize = size;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        this.mType = type;
    }
}
