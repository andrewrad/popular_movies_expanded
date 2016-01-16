package com.a.b.moviesapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.a.b.moviesapp.other.ApiInterface;
import com.a.b.moviesapp.other.Constants;
import com.a.b.moviesapp.other.Movie;
import com.a.b.moviesapp.R;
import com.a.b.moviesapp.other.RestClient;
import com.a.b.moviesapp.other.Result;
import com.a.b.moviesapp.other.TrailerResults;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

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
    TextView mTrailer;
    String TAG="MovieDetailsFragment";
    List<TrailerResults> Users ;

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
        mTrailer = (TextView) view.findViewById(R.id.trailer);

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
            mTrailer.setText(mTrailer.getId()>0?String.valueOf(mTrailer.getId()):null);
            getTrailersAndReviews();
        }
    }
    public void getTrailersAndReviews(){
//        Retrofit client = new Retrofit.Builder()
//                .baseUrl(Constants.TRAILER_BASE_URL)
//                .build();

//        ApiInterface service = client.create(ApiInterface.class);

        ApiInterface service=RestClient.getClient();
        Call<Result> call = service.getMovieExtras("550");
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Response<Result> response) {
                Log.e(TAG, "Response code: " + response.code());
                if (response.isSuccess()) {
                    // request successful (status code 200, 201)
                    Log.e(TAG,"response body: "+response.body()+", message: "+response.message());

                    Result result = response.body();
                    Log.e(TAG,"result result: "+result.getItems());

//                    Log.e(TAG,"response JSON: "+new Gson().toJson(response.body())+", movie: "+result.getItems().get(0));
                    Log.e(TAG, "response = " + new Gson().toJson(result));
                    Users = result.getItems();
                    Log.e(TAG, "Items = " + Users.size());
                } else {
                    //request not successful (like 400,401,403 etc)
                    //Handle errors
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

//        Movie m = new Movie();
//        Log.e(TAG, "new stuff added to movie: "+res)

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