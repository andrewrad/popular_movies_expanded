package com.a.b.moviesapp.pojo;

/**
 * Plain old Java Object for storing reviews and trailers acquired through Retrofit
 * Since the JSON object retrieved from the API has objects within objects, you will see an orderly
 * progression of objects within objects, starting with this one.
 * Example: getTrailers() returns a Trailer object. Inside Trailer, a function returns YouTube objects.
 * Created by Andrew on 1/15/2016.
 */
public class ResultPOJO {

    private Reviews reviews;
    private Trailers trailers;

    public Reviews getReviews() {
        return reviews;
    }

    public void setReviews(Reviews reviews) {
        this.reviews = reviews;
    }

    public Trailers getTrailers() {
        return trailers;
    }

    public void setTrailers(Trailers trailers) {
        this.trailers = trailers;
    }
}