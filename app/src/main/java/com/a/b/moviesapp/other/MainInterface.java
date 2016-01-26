package com.a.b.moviesapp.other;

import com.a.b.moviesapp.pojo.Movie;

/**
 * Created by Andrew on 1/11/2016.
 */
public class MainInterface {
    public interface MovieInterface{
        void openDetailFragment(Movie movie);
//        void updateGridViewAdapter(Movie movie);
        void holdOldTitle();
        void backToMovieList();
        void deleteMovie();
    }
}
