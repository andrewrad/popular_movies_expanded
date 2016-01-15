package com.a.b.moviesapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.a.b.moviesapp.other.Constants;
import com.a.b.moviesapp.other.Movie;
import com.a.b.moviesapp.R;
import com.bumptech.glide.Glide;

/**
 * Created by Andrew on 1/2/2016.
 */
public class MovieDetailsFragment extends Fragment {
    ImageView mBackGroundImage;
    ImageView mPosterPic;
    TextView mTitle;
    TextView mDate;
    TextView mRating;
    TextView mSummary;
    String TAG="MovieDetailsFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.movie_details);
            View view = inflater.inflate(R.layout.movie_details_fragment, container, false);

            mBackGroundImage = (ImageView) view.findViewById(R.id.background_image);
            mPosterPic = (ImageView) view.findViewById(R.id.poster_detail);
            mTitle = (TextView) view.findViewById(R.id.title_header);
            mDate = (TextView) view.findViewById(R.id.date_view);
            mRating = (TextView) view.findViewById(R.id.rating_view);
            mSummary = (TextView) view.findViewById(R.id.summary_view);

            populateViews();
            return view;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void populateViews(){
        Bundle args=getArguments();
        Movie mMovieDetails=(Movie) args.getParcelable(Constants.DETAILS_BUNDLE);

        if(mMovieDetails!=null) {
            String fullUrl=Constants.TMDB_IMAGE_BASE_URL_LARGE+mMovieDetails.getBackDropUrl();
//            Log.e(TAG, "Backdrop path: " + fullUrl);
            Glide.with(getActivity())
                    .load(fullUrl)
                    .into(mBackGroundImage);

            fullUrl=Constants.TMDB_IMAGE_BASE_URL_SMALL+mMovieDetails.getPosterUrl();
            Glide.with(getActivity())
                .load(fullUrl)
                .into(mPosterPic);

            mTitle.setText(mMovieDetails.getMovieTitle());
            mDate.setText(mMovieDetails.getDate());
            mRating.setText(String.valueOf(mMovieDetails.getRating()));
            mSummary.setText(mMovieDetails.getSummary());
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.movie_details_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_item_back) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
        return super.onOptionsItemSelected(item);
    }
}