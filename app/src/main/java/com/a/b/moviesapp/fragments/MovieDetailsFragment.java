package com.a.b.moviesapp.fragments;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.a.b.moviesapp.other.ApiInterface;
import com.a.b.moviesapp.other.Constants;
import com.a.b.moviesapp.other.MainInterface;
import com.a.b.moviesapp.pojo.Movie;
import com.a.b.moviesapp.R;
import com.a.b.moviesapp.other.RestClient;
import com.a.b.moviesapp.pojo.ResultPOJO;
import com.a.b.moviesapp.pojo.ReviewResult;
import com.a.b.moviesapp.pojo.Youtube;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

/**
 * Created by Andrew on 1/2/2016.
 */
public class MovieDetailsFragment extends Fragment implements View.OnClickListener{
    ImageView mBackGroundImage;
    ImageView mPosterPic;
    TextView mTitle;
    TextView mDate;
    TextView mRating;
    TextView mSummary;
    TextView mReviews;
    TextView mTrailersHeader;
    ListView mTrailerListView;

    Call<ResultPOJO> mCall;

    ResultPOJO mMovieExtras;
    Movie mMovieDetails;

    MainInterface.MovieInterface mListener;

    ToggleButton mFavorite;
    String TAG="MovieDetailsFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        if(!getActivity().getResources().getBoolean(R.bool.isTablet)) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.movie_details);
        }
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
        mFavorite.setOnClickListener(this);

        populateViews();
        setFavorites();
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
            Log.e(TAG, "Backdrop path: " + fullUrl);
            if(getResources().getBoolean(R.bool.isTablet)) {

                WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
                DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
                Display display = wm.getDefaultDisplay();
                Point point=new Point();
                display.getSize(point);

                Integer height=Math.round(281*(point.x/2)/500);
                Log.e(TAG, "display metrics, x:" + point.x + ", y: " + point.y+", calculated new height: "+height);
                mBackGroundImage.getLayoutParams().height=height;

                Glide.with(getActivity())
                        .load(fullUrl)
                        .into(mBackGroundImage);
            }else{
                Glide.with(getActivity())
                        .load(fullUrl)
                        .into(mBackGroundImage);
            }


            Log.e(TAG, "background image height: " + mBackGroundImage.getMeasuredHeight());

            fullUrl=Constants.TMDB_IMAGE_BASE_URL_SMALL+mMovieDetails.getPosterUrl();
            Glide.with(getActivity())
                .load(fullUrl)
                .into(mPosterPic);

            mTitle.setText(mMovieDetails.getMovieTitle());
            mDate.setText(mMovieDetails.getDate());
            mRating.setText(String.valueOf(mMovieDetails.getRating()));
            mSummary.setText(mMovieDetails.getSummary());

            getTrailersAndReviews(mMovieDetails.getId());
        }
    }
    public void  setFavorites(){
        String[]column=new String[]{Constants.FAVORITED};
        String[]selection=new String[]{mMovieDetails.getMovieTitle()};
        Cursor c= getContext().getContentResolver().query(Uri.parse(Constants.CONTENT_AUTHORITY + "/get_favorite"), column, Constants.TITLE + " = ? ", selection, null);
        if(c.getCount()>0) {
            c.moveToFirst();
            Log.e(TAG, "favorite? " + c.getString(0));
            Boolean checked=c.getInt(0)==1?Boolean.TRUE:Boolean.FALSE;
            mFavorite.setChecked(checked);
        }
    }
    public void getTrailersAndReviews(Integer id){

        ApiInterface service=RestClient.getClient();
        mCall = service.getMovieExtras(String.valueOf(id));
        mCall.enqueue(new Callback<ResultPOJO>() {
            @Override
            public void onResponse(Response<ResultPOJO> response) {
                Log.e(TAG, "Response code: " + response.code());
                if (response.isSuccess()) {
//                    Log.e(TAG, "response trailers: " + response.body().getTrailers() + ", message: " + response.message());

                    mMovieExtras = response.body();

//                    mMoreDetails = result.getId();

//                    final Youtube trailer = result.getTrailers().getYoutube().get(0);
//                    Log.e(TAG, "youtube address: https://www.youtube.com/watch?v=" + trailer.getSource());

//                    final List<Youtube> trail = mMovieExtras.getTrailers().getYoutube();
                    setTrailersView(mMovieExtras.getTrailers().getYoutube());
//                    mTrailersHeader.setText(trail.size() > 1 ? "Movie Trailers" : "Movie Trailer");

//                    List<String> trailers = new ArrayList<String>();
//                    for (int i = 0; i < trail.size(); i++) {
//                        trailers.add(trail.get(i).getName());
////                        Log.e(TAG,"trailer "+i+": "+trail.get(i).getName());
//                    }
//
//                    mTrailerListView.setItemsCanFocus(false);
//                    setListViewHeight(mTrailerListView);
//
//                    ArrayAdapter sa = new ArrayAdapter(getActivity(), R.layout.trailer_button, trailers);
//                    mTrailerListView.setAdapter(sa);
//                    mTrailerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                            Log.e(TAG, "clicked " + trail.get(position).getName() + ", https://www.youtube.com/watch?v=" + trail.get(position).getSource());
//                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + trail.get(position).getSource())));
//                        }
//                    });

//                    List<ReviewResult> rev = mMovieExtras.getReviews().getResults();
//                    String reviews = "";
//                    for (ReviewResult r : rev) {
//                        reviews += "Movie Review from " + r.getAuthor() + ": \n\n" +
//                                r.getContent() + "\n\n\n";
//                    }
//
//                    mReviews.setText(reviews);

                    setReviewsView(mMovieExtras.getReviews().getResults());

                } else {
                    Log.e(TAG, "request not successful??");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, "movies.getTrailers: " + mMovieDetails.getTrailers());

                setTrailersView(mMovieDetails.getTrailers());
                setReviewsView(mMovieDetails.getReviews());
            }
        });
    }
    public void setTrailersView(final List<Youtube> trail){
        mTrailersHeader.setText(trail.size() > 1 ? "Movie Trailers" : "Movie Trailer");

        List<String> trailers = new ArrayList<String>();
        for (int i = 0; i < trail.size(); i++) {
            trailers.add(trail.get(i).getName());
        }

        mTrailerListView.setItemsCanFocus(false);
        setListViewHeight(mTrailerListView);

        ArrayAdapter sa = new ArrayAdapter(getActivity(), R.layout.trailer_button, trailers);
        mTrailerListView.setAdapter(sa);
        mTrailerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            Log.e(TAG, "clicked " + trail.get(position).getName() + ", https://www.youtube.com/watch?v=" + trail.get(position).getSource());
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + trail.get(position).getSource())));
            }
        });
    }

    public void setReviewsView(List<ReviewResult> review){
        Log.e(TAG,"setReviewsView: "+review);
//        if(review!=null) {
            String reviews = "";
            for (ReviewResult r : review) {
                reviews += "Movie Review from " + r.getAuthor() + ": \n\n" +
                        r.getContent() + "\n\n\n";
            }
            mReviews.setText(reviews);
//        }
    }

    public static void setListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
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

    /*
    onPause: to determine if the favorite star  was selected or not
    if favorited, check to see if is was previously favorited.
    if not previously favorited, add the movie to the DB
    if the movie is not favorited, attempt to remove it from the DB, if it returns 1, remove from DB and update the gridView
     */

    @Override
    public void onPause() {
        super.onPause();
        mCall.cancel();

        Log.e(TAG, "onPause, toggleview is set to: " + mFavorite.isChecked());

        if(mFavorite.isChecked()) {
            if(mMovieDetails.mFavorited!=Boolean.TRUE) {
                saveFavoritedMovie();
            }
        }else{
//            String[]deleteId=new String[]{mMovieDetails.getMovieTitle()};
            int deleted=getContext().getContentResolver().delete(Uri.parse(Constants.CONTENT_AUTHORITY + "/delete"),String.valueOf(mMovieDetails.getId()),null);
            Log.e(TAG, "Deleted "+deleted+" movie");
            if(deleted>0) {
                mListener.deleteMovie();
//                EventBus.getDefault().postSticky(new RefreshGridView());
            }
        }
    }

    public void saveFavoritedMovie(){
        ContentValues cv = new ContentValues();
        if (mMovieDetails != null && mMovieExtras != null) {
            Gson gson=new Gson();
            String trailers=gson.toJson(mMovieExtras.getTrailers().getYoutube());
            String reviews=gson.toJson(mMovieExtras.getReviews());

            Log.e(TAG,"inserting trailers: "+trailers+", reviews: "+reviews);

            cv.put(Constants.MOVIE_ID, mMovieDetails.getId());
            cv.put(Constants.TITLE, mMovieDetails.getMovieTitle());
            cv.put(Constants.POSTER_PATH, mMovieDetails.getPosterUrl());
            cv.put(Constants.BACKDROP_PATH, mMovieDetails.getBackDropUrl());
            cv.put(Constants.DATE, mMovieDetails.getDate());
            cv.put(Constants.RATING, mMovieDetails.getRating());
            cv.put(Constants.OVERVIEW, mMovieDetails.getSummary());
            cv.put(Constants.FAVORITED, Boolean.TRUE);
            cv.put(Constants.TRAILERS, trailers);
            cv.put(Constants.REVIEWS, reviews);

            getContext().getContentResolver().insert(Uri.parse(Constants.CONTENT_AUTHORITY + "/insert"), cv);
            Log.e(TAG,"Added a favorite movie");
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
        if (id == R.id.menu_item_share) {
//            if(mMovieExtras!=null) {
//                List<Youtube> trailers = mMovieExtras.getTrailers().getYoutube();
                List<Youtube> trailers = mMovieDetails.getTrailers();
                if (trailers.size() > 0) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "http://www.youtube.com/watch?v=" + trailers.get(0).getSource());
                    sendIntent.setType("text/plain");
                    startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.share_movie_trailer)));
                } else {
                    Toast.makeText(getActivity(), R.string.no_trailer_to_share, Toast.LENGTH_SHORT).show();
                }
            }
//        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (MainInterface.MovieInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public void onClick(View v) {
        if(!mFavorite.isChecked()&&getResources().getBoolean(R.bool.isTablet)) {
            int deleted=getContext().getContentResolver().delete(Uri.parse(Constants.CONTENT_AUTHORITY + "/delete"),String.valueOf(mMovieDetails.getId()),null);
            if(deleted>0) {
                mListener.deleteMovie();
            }
        }else{
            saveFavoritedMovie();
        }
    }
}