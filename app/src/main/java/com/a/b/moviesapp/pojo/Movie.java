package com.a.b.moviesapp.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Andrew on 1/1/2015.
 * Java class object for storing all movie data
 */
public class Movie implements Parcelable {
    public Integer mId;
    public String mTitle;
    public String mOverview;
    public String mPosterUrl;
    public String mBackDropUrl;
    public String mDate;
    public Double mVoteAverage;
    public Boolean mFavorited;
    public String mIso6391;
    public String mKey;
    public String mName;
    public String mSite;
//    public Integer mSize;
    public String mType;

    public List<Youtube> mTrailers;
    public List<ReviewResult> mReviews;

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
        dest.writeString(mType);
        dest.writeTypedList(mTrailers);
        dest.writeTypedList(mReviews);
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
        mType=source.readString();
        source.readTypedList(mTrailers, (Creator<Youtube>) Youtube.class.getClassLoader());
        source.readTypedList(mReviews, (Creator<ReviewResult>) ReviewResult.class.getClassLoader());

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
//    public Boolean getFavorite(){return mFavorited;}
    public void setFavorite(Boolean favorite){mFavorited=favorite;}

    public void setTrailer(List<Youtube> trailers){
        mTrailers=trailers;
    }
    public List<Youtube> getTrailers(){
        return mTrailers;
    }

    public void setReviews(List<ReviewResult> reviews){
        mReviews=reviews;
    }
    public List<ReviewResult> getReviews(){
        return mReviews;
    }


//    public String getIso6391() {
//        return mIso6391;
//    }
//
//    public void setIso6391(String mIso6391) {
//        this.mIso6391 = mIso6391;
//    }
//
//    public String getKey() {
//        return mKey;
//    }
//
//    public void setKey(String mKey) {
//        this.mKey = mKey;
//    }
//
//    public String getSite() {
//        return mSite;
//    }
//
//    public void setSite(String site) {
//        this.mSite = site;
//    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        this.mType = type;
    }
}
