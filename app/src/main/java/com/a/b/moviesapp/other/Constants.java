package com.a.b.moviesapp.other;

import android.net.Uri;

/**
 * Created by Andrew on 1/12/2016.
 */
public class Constants {
    public static final String API_KEY_STRING="22e815e44d11366f2446d1fa6aa31e75";

    public static final String MOVIE_ID="movie_id";
    public static final String POSTER_PATH ="poster_path";
    public static final String BACKDROP_PATH ="backdrop_path";
    public static final String TITLE="title";
    public static final String OVERVIEW="overview";
    public static final String DATE="release_date";
    public static final String RATING="vote_average";
    public static final String ID="id";
    public static final String FAVORITED="favorited";

    public static final String SORT_BY= "sort_by";
    public static final String MOST_POPULAR ="popularity.desc";
    public static final String HIGHEST_RATED="vote_average.desc";
    public static final String API_KEY="api_key";

    public static final String BASE_URL ="http://api.themoviedb.org/3/discover/movie?";
    public static final String TMDB_IMAGE_BASE_URL_LARGE="http://image.tmdb.org/t/p/w500/";
    public static final String TMDB_IMAGE_BASE_URL="http://image.tmdb.org/t/p/w342/";
    public static final String TMDB_IMAGE_BASE_URL_SMALL="http://image.tmdb.org/t/p/w185/";

//    public static final String TRAILER_BASE_URL="http://api.themoviedb.org/3/movie/";
    public static final String TRAILER_BASE_URL="http://api.themoviedb.org/";

    public static final String DETAILS_BUNDLE="movie_details";

    public static final String CONTENT_AUTHORITY="content://com.a.b";
    public static final String AUTHORITY="com.a.b";
    public static final Uri CONTENT_URI=Uri.parse("content://com.a.b");
    public static final String TRAILERS="trailers";
    public static final String SQL_ID="_id";
    public static final String REVIEWS="reviews";
    public static final String TABLE_NAME = "saved";
}
