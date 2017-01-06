package com.a.b.moviesapp.fragments;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.ScrollView;
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
import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

/**
 * The fragment for displaying all the movie details once the user clicks on a movie poster image in the
 * MovieListFragment. Both phone and tablet (master-detail view) layout are considered in this fragment.
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
    ScrollView mScrollView;

    Call<ResultPOJO> mCall;

    ResultPOJO mMovieExtras;
    Movie mMovieDetails;

    MainInterface.MovieInterface mListener;

    ToggleButton mFavorite;
    String TAG="MovieDetailsFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        if (!getActivity().getResources().getBoolean(R.bool.isTablet)) {
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.movie_details);
        }

        View view = inflater.inflate(R.layout.movie_details_fragment, container, false);

        mBackGroundImage = (ImageView) view.findViewById(R.id.background_image);
        mPosterPic = (ImageView) view.findViewById(R.id.poster_detail);
        mTitle = (TextView) view.findViewById(R.id.title_header);
        mDate = (TextView) view.findViewById(R.id.date_view);
        mRating = (TextView) view.findViewById(R.id.rating_view);
        mSummary = (TextView) view.findViewById(R.id.summary_view);
        mReviews = (TextView) view.findViewById(R.id.reviews_textview);
        mTrailersHeader = (TextView) view.findViewById(R.id.trailers_subtitle);
        mTrailerListView = (ListView) view.findViewById(R.id.trailer_listview);
        mFavorite = (ToggleButton) view.findViewById(R.id.toggleButton);
        mFavorite.setOnClickListener(this);
        mScrollView =(ScrollView) view.findViewById(R.id.detail_scrollview);

        populateViews();
        setFavorites();
        return view;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    /**
     * Sets the views for the DetailFragment. Mostly used to pull data from the Movie object and fill corresponding
     * views with data. Glide is used to fill image data from a url.
     */
    public void populateViews() {
        Bundle args = getArguments();
        mMovieDetails = args.getParcelable(Constants.DETAILS_BUNDLE);

        if (mMovieDetails != null) {
            if (mMovieDetails.getBackDropUrl() != "null") {
                String fullUrlBackdrop = Constants.TMDB_IMAGE_BASE_URL_LARGE + mMovieDetails.getBackDropUrl();

                Glide.with(getActivity())
                        .load(fullUrlBackdrop)
                        .into(mBackGroundImage);

                if (getResources().getBoolean(R.bool.isTablet)) {
                    WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
                    Display display = wm.getDefaultDisplay();
                    Point point = new Point();
                    display.getSize(point);

                    /** On tablets, the image at the top of the movie details tends to have unpredictable characteristics,
                     * to fix this, the height is calculated based on the device's width. Since tablets have a master-detail
                     * view, the image height must be half of normal */
                    Integer height = Math.round(281 * (point.x / 2) / 500);
                    Log.e(TAG, "display metrics, x:" + point.x + ", y: " + point.y + ", calculated new height: " + height);
                    mBackGroundImage.getLayoutParams().height = height;
                }
            }

            String fullUrlPoster = Constants.TMDB_IMAGE_BASE_URL_SMALL+mMovieDetails.getPosterUrl();
            Glide.with(getActivity())
                .load(fullUrlPoster)
                .into(mPosterPic);

            mTitle.setText(mMovieDetails.getMovieTitle());
            mDate.setText(mMovieDetails.getDate());
            mRating.setText(String.valueOf(mMovieDetails.getRating()));
            mSummary.setText(mMovieDetails.getSummary());

            getTrailersAndReviews(mMovieDetails.getId());
        }
    }

    public void setFavorites() {
        String[] column = new String[]{Constants.FAVORITED};
        String[] selection = new String[]{mMovieDetails.getMovieTitle()};
        Cursor c = getContext().getContentResolver().query(Uri.parse(Constants.CONTENT_AUTHORITY
                + "/get_favorite"), column, Constants.TITLE + " = ? ", selection, null);

        if(c.getCount() > 0) {
            c.moveToFirst();
            Boolean checked = c.getInt(0) == 1 ? Boolean.TRUE : Boolean.FALSE;
            mFavorite.setChecked(checked);
        }
        c.close();
    }

    /**
     * Gets the trailers and reviews from a different api call. This uses Retrofit to pull and parse the
     * acquired JSON string.
     * @param id the unique movieId for the selected movie
     */
    public void getTrailersAndReviews(Integer id) {

        ApiInterface service = RestClient.getClient();
        mCall = service.getMovieExtras(String.valueOf(id));
        mCall.enqueue(new Callback<ResultPOJO>() {
            @Override
            public void onResponse(Response<ResultPOJO> response) {
                if (response.isSuccess()) {
                    mMovieExtras = response.body();
                    setTrailersView(mMovieExtras.getTrailers().getYoutube());
                    mMovieDetails.setTrailer(mMovieExtras.getTrailers().getYoutube());
                    setReviewsView(mMovieExtras.getReviews().getResults());
                    mMovieDetails.setReviews(mMovieExtras.getReviews().getResults());

                } else {
                    Log.e(TAG, "request not successful");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                setTrailersView(mMovieDetails.getTrailers());
                setReviewsView(mMovieDetails.getReviews());
            }
        });
    }

    /**
     * Sets up the adapter for displaying the list of clickable trailers. Prepends the youtube http url to an
     * intent if the user clicks on the trailer so it can open in a YouTube app on the device.
     * @param trail list of trailers
     */
    public void setTrailersView(final List<Youtube> trail) {
        if(trail != null && trail.size() > 0) {
            /* Creates title over the listView of trailers. If only one, makes it singular*/
            mTrailersHeader.setText(trail.size() > 1 ? getString(R.string.movie_trailers_name) : getString(R.string.movie_trailer_name));

            List<String> trailers = new ArrayList<>();
            for (int i = 0; i < trail.size(); i++) {
                trailers.add(trail.get(i).getName());
            }

            mTrailerListView.setItemsCanFocus(false);

            ArrayAdapter sa = new ArrayAdapter(getActivity(), R.layout.trailer_button, trailers);
            mTrailerListView.setAdapter(sa);
            setListViewHeight(mTrailerListView);
            mTrailerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.youtube_base_url) + trail.get(position).getSource())));
                }
            });
        }
    }

    /**
     * Need to set the listView of movie trailers to a specific height or else the listView doesn't look right
     * and causes the fragment to jump to the movie trailers section.
     * @param listView listView of movie trailers
     */
    public void setListViewHeight(ListView listView) {
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

    public void setReviewsView(List<ReviewResult> review) {
        if (review != null) {
            StringBuilder sb = new StringBuilder();
            for (ReviewResult r : review) {
                sb.append(R.string.movie_review_from);
                sb.append(r.getAuthor());
                sb.append(": \n\n");
                sb.append(r.getContent());
                sb.append("\n\n\n");
            }
            mReviews.setText(sb.toString());
        }
        mScrollView.smoothScrollTo(0, 0);
    }

    @Override
    public void onPause() {
        super.onPause();
        mCall.cancel();

        if (!getResources().getBoolean(R.bool.isTablet)) {
            if (mFavorite.isChecked()) {
                if (mMovieDetails.mFavorited != Boolean.TRUE) {
                    saveFavoriteMovie();
                }
            } else {
                int deleted = getContext().getContentResolver().delete(Uri.parse(Constants.CONTENT_AUTHORITY
                        + "/delete"), String.valueOf(mMovieDetails.getId()), null);
                if (deleted > 0) {
                    mListener.updateFavorites();
                }
            }
        }
    }

    /**
     * Adds currently selected movie to the database
     * All movies in the database are favorited (stared)
     * Adding movie through content provider "/insert" command
     */
    public void saveFavoriteMovie() {
        ContentValues cv = new ContentValues();
        if (mMovieDetails != null && mMovieExtras != null) {
            Gson gson = new Gson();
            String trailers = gson.toJson(mMovieExtras.getTrailers().getYoutube());
            String reviews = gson.toJson(mMovieExtras.getReviews());

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
        }
    }

    /**
     * Creates a new set of optionMenu for this fragment
     * @param menu provided by Android
     * @param inflater provided by Android
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.movie_details_menu, menu);
    }

    /**
     * Allows the user to share the first movie trailer via an intent. Opens any built in app that can process this
     * request, such as email or SMS service
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_item_share) {
            List<Youtube> trailers = mMovieDetails.getTrailers();
            if (trailers.size() > 0) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, R.string.youtube_base_url + trailers.get(0).getSource());
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.share_movie_trailer)));
            } else {
                Toast.makeText(getActivity(), R.string.no_trailer_to_share, Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainInterface.MovieInterface) {
            mListener = (MainInterface.MovieInterface) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OpenScoreCards");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Allows the user to save a movie to the database via the ContentProvider by toggling the star on the details
     * page. If the star is toggled on, the movie is added to the database. If the star is toggled off, the movie
     * is removed from the database.
     * @param v passed by Android
     */
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.toggleButton && getResources().getBoolean(R.bool.isTablet)) {
            if (!mFavorite.isChecked()){
                int deleted = getContext().getContentResolver().delete(Uri.parse(Constants.CONTENT_AUTHORITY + "/delete"), String.valueOf(mMovieDetails.getId()), null);

                /*if the delete function (above) returns anything above a 0, it shows that a row was deleted*/
                if (deleted > 0) {
                    mListener.updateFavorites();
                }
            } else {
                saveFavoriteMovie();
            }
        }
    }
}