package com.a.b.moviesapp.other;

import com.a.b.moviesapp.pojo.Movie;

/**
 * Interface for allowing fragments to callback to the main activity
 * Created by Andrew on 1/11/2016.
 */
public class MainInterface {
    public interface MovieInterface{
        void openDetailFragment(Movie movie);
        void holdOldTitle();
        void deleteMovie();
    }
}
