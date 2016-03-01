package com.a.b.moviesapp.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Java object filled by Retrofit, part of retrieving movies and trailers
 * Created by Andrew on 1/20/2016.
 */
public class ReviewResult implements Parcelable {

    public String id;
    public String author;
    public String content;
    public String url;

    public ReviewResult(){
    }

    public ReviewResult(Parcel source) {
        readfromParcel(source);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(author);
        dest.writeString(content);
        dest.writeString(url);
    }

    public void readfromParcel(Parcel source){
        id=source.readString();
        author=source.readString();
        content=source.readString();
        url=source.readString();
    }
    private final Parcelable.Creator<ReviewResult>CREATOR=new Parcelable.Creator<ReviewResult>(){

        @Override
        public ReviewResult createFromParcel(Parcel source) {
            return new ReviewResult(source);
        }

        @Override
        public ReviewResult[] newArray(int size) {
            return new ReviewResult[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


}
