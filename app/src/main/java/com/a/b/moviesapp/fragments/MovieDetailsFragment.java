package com.a.b.moviesapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.a.b.moviesapp.Constants;
import com.a.b.moviesapp.R;
import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

/**
 * Created by Andrew on 7/11/2015.
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
        if(savedInstanceState==null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Movie Details");
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
        return null;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void populateViews(){
        Bundle args=getArguments();
        if(args!=null) {
            Log.e(TAG, "value passed through bundle: " + args.getString(Constants.BACKDROP_PATH));
            String fullUrl="http://image.tmdb.org/t/p/w780/"+args.getString(Constants.BACKDROP_PATH);
            Log.e(TAG, "Backdrop path: " + fullUrl);
            Glide.with(getActivity())
                    .load(fullUrl)
//                    .placeholder(R.drawable.placeholder_vertical)
                    .into(mBackGroundImage);

            fullUrl="http://image.tmdb.org/t/p/w780/"+args.getString(Constants.POSTER_PATH);
            Glide.with(getActivity())
                    .load(fullUrl)
//                    .placeholder(R.drawable.placeholder_vertical)
                    .into(mPosterPic);

            mTitle.setText(args.get(Constants.TITLE).toString());
            mDate.setText(args.get(Constants.DATE).toString());
            mRating.setText(args.get(Constants.RATING).toString());
            mSummary.setText(args.getString(Constants.OVERVIEW));

            Log.e(TAG, "Rating value: "+args.getDouble(Constants.RATING));
        }
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//    }
//
//    @Override
//    public void onPrepareOptionsMenu(Menu menu) {
//        menu.findItem(R.menu.menu_main).setVisible(false);
//        if (isFinalized)
//            menu.getItem(1).setEnabled(false);
//        super.onPrepareOptionsMenu(menu);
//    }
}