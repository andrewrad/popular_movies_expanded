package com.a.b.moviesapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toolbar;

import com.a.b.moviesapp.Movie;
import com.a.b.moviesapp.R;
import com.bumptech.glide.Glide;

/**
 * Created by Andrew on 7/11/2015.
 */
public class MovieDetailsFragment extends Fragment {
    ImageView mBackGroundImage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movie_details_fragment, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Movie Details");
        mBackGroundImage=(ImageView) view.findViewById(R.id.background_image);
        loadBackgroundImage();
        return view;
    }
    public void loadBackgroundImage(){

        Glide.with(getActivity())
            .load("http://image.tmdb.org/t/p/w780//fYzpM9GmpBlIC893fNjoWCwE24H.jpg")
//            .placeholder(R.drawable.placeholder_vertical)
            .into(mBackGroundImage);
    }
}
