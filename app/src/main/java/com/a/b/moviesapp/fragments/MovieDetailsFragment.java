package com.a.b.moviesapp.fragments;

import android.app.ActionBar;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.a.b.moviesapp.other.ApiInterface;
import com.a.b.moviesapp.other.Constants;
import com.a.b.moviesapp.pojo.Movie;
import com.a.b.moviesapp.R;
import com.a.b.moviesapp.other.RestClient;
import com.a.b.moviesapp.pojo.ResultPOJO;
import com.a.b.moviesapp.pojo.ReviewResult;
import com.a.b.moviesapp.pojo.Reviews;
import com.a.b.moviesapp.pojo.Youtube;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

/**
 * Created by Andrew on 1/2/2016.
 */
public class MovieDetailsFragment extends Fragment{
    ImageView mBackGroundImage;
    ImageView mPosterPic;
    TextView mTitle;
    TextView mDate;
    TextView mRating;
    TextView mSummary;
    TextView mReviews;
    TextView mTrailersHeader;
    ListView mTrailerListView;

    ResultPOJO mMovieExtras;
    Movie mMovieDetails;

    Boolean mCurrentlyFavorited=false;


    ToggleButton mFavorite;
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
        mReviews = (TextView) view.findViewById(R.id.reviews_textview);
        mTrailersHeader=(TextView) view.findViewById(R.id.trailers_subtitle);
        mTrailerListView=(ListView) view.findViewById(R.id.trailer_listview);
        mFavorite=(ToggleButton) view.findViewById(R.id.toggleButton);

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
        mMovieDetails=(Movie) args.getParcelable(Constants.DETAILS_BUNDLE);

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

//            mFavorite.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Log.e(TAG,"star clicked");
//                    if (mFavorite.getDrawable().getConstantState().equals(getResources().getDrawable(android.R.drawable.star_big_on).getConstantState())) {
//                        mFavorite.setImageResource(android.R.drawable.star_big_off);
//                    } else {
//                        mFavorite.setImageResource(android.R.drawable.star_big_on);
//                    }
//                }
//            });

            getTrailersAndReviews(mMovieDetails.getId());
        }
    }
    public void getTrailersAndReviews(Integer id){
//        Retrofit client = new Retrofit.Builder()
//                .baseUrl(Constants.TRAILER_BASE_URL)
//                .build();

//        ApiInterface service = client.create(ApiInterface.class);

        ApiInterface service=RestClient.getClient();
        Call<ResultPOJO> call = service.getMovieExtras(String.valueOf(id));
//        Call<ResultPOJO> call=service.getUsersNamedTom("tom");
        call.enqueue(new Callback<ResultPOJO>() {
            @Override
            public void onResponse(Response<ResultPOJO> response) {
                Log.e(TAG, "Response code: " + response.code());
                if (response.isSuccess()) {
                    // request successful (status code 200, 201)
                    Log.e(TAG, "response body: " + response.body().getTitle() + ", message: " + response.message());

                    mMovieExtras = response.body();
                    Log.e(TAG, "result id: " + mMovieExtras.getHomepage());

//                    Log.e(TAG,"response JSON: "+new Gson().toJson(response.body())+", movie: "+result.getItems().get(0));
                    Log.e(TAG, "response = " + new Gson().toJson(mMovieExtras));
//                    mMoreDetails = result.getId();
//                    Log.e(TAG, "Items = " + mMoreDetails.size() + ", results inner JSON: " + mMoreDetails.get(0).getKey());


//                    final Youtube trailer = result.getTrailers().getYoutube().get(0);
//                    Log.e(TAG, "youtube address: https://www.youtube.com/watch?v=" + trailer.getSource());

                    final List<Youtube> trail=mMovieExtras.getTrailers().getYoutube();
                    mTrailersHeader.setText(trail.size() > 1 ? "Movie Trailers" : "Movie Trailer");

                    List<String> trailers=new ArrayList<String>();
                    for(int i=0;i<trail.size();i++){
                        trailers.add(trail.get(i).getName());
//                        Log.e(TAG,"trailer "+i+": "+trail.get(i).getName());
                    }

                    mTrailerListView.setItemsCanFocus(false);

                    ArrayAdapter sa = new ArrayAdapter(getActivity(),R.layout.trailer_button, trailers);
                    mTrailerListView.setAdapter(sa);
                    mTrailerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            Log.e(TAG, "clicked " + trail.get(position).getName() + ", https://www.youtube.com/watch?v=" + trail.get(position).getSource());
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + trail.get(position).getSource())));
                        }
                    });
//                    ViewGroup.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 150);
//                    mTrailerListView.setLayoutParams(params);
                    setListViewHeightBasedOnChildren(mTrailerListView);

                    List<ReviewResult> rev = mMovieExtras.getReviews().getResults();
                    String reviews = "";
                    for (ReviewResult r : rev) {
                        reviews += "Movie Review from " + r.getAuthor() + ": \n\n" +
                                r.getContent() + "\n\n\n";
                    }
                    mReviews.setText(reviews);
                } else {
                    Log.e(TAG, "request not successful??");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, "throwable failure!!!");
            }
        });
    }
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG, "onPause, toggleview is set to: " + mFavorite.isChecked());

        if(mFavorite.isChecked()) {

            ContentValues cv = new ContentValues();
//        cv.put(Constants.MOVIE_ID,"efgwegerg415gh5e4r54ghe5rh");
            if (mMovieDetails != null && mMovieExtras != null) {
                cv.put(Constants.MOVIE_ID, mMovieDetails.getId());
                cv.put(Constants.TITLE, mMovieDetails.getMovieTitle());
                cv.put(Constants.POSTER_PATH, mMovieDetails.getPosterUrl());
                cv.put(Constants.BACKDROP_PATH, mMovieDetails.getBackDropUrl());
                cv.put(Constants.DATE, mMovieDetails.getDate());
                cv.put(Constants.RATING, mMovieDetails.getRating());
                cv.put(Constants.OVERVIEW, mMovieDetails.getSummary());
                cv.put(Constants.TRAILERS, "trailers");
                cv.put(Constants.REVIEWS, "reviews and stuff");

//            cv.put(Constants.TRAILERS, mMovieExtras.getTrailers());
//            cv.put(Constants.REVIEWS, mMovieExtras.getReviews());
            }
            getContext().getContentResolver().insert(Uri.parse(Constants.CONTENT_AUTHORITY + "/insert"), cv);
        }else{
            String[]deleteId=new String[]{mMovieDetails.getMovieTitle()};
            getContext().getContentResolver().delete(Uri.parse(Constants.CONTENT_AUTHORITY + "/delete"),Constants.MOVIE_ID,deleteId);
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