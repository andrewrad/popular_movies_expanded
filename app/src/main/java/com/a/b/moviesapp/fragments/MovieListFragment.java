package com.a.b.moviesapp.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.a.b.moviesapp.Constants;
import com.a.b.moviesapp.GridViewAdapter;
import com.a.b.moviesapp.MainInterface;
import com.a.b.moviesapp.Movie;
import com.a.b.moviesapp.R;
import com.a.b.moviesapp.RecyclerClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MovieListFragment extends Fragment implements RecyclerClickListener {
    String TAG="MovieListFragment";
    MainInterface.MovieInterface mListener;
    RecyclerView mRecyclerView;
    private GridViewAdapter mGridViewAdapter;
    private GridLayoutManager mLayout;
    private ArrayList<Movie> mMovieArray;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.grid_fragment, container, false);

        mLayout = new GridLayoutManager(getActivity(), 2);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayout);
        mGridViewAdapter = new GridViewAdapter(getActivity(), this);
        mRecyclerView.setAdapter(mGridViewAdapter);
//        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        if(mMovieArray!=null) {
            mGridViewAdapter.setList(mMovieArray);
        }
        return view;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRetainInstance(true);
        if(savedInstanceState==null){
            getMovies(Constants.MOST_POPULAR);
//            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.action_popular_sort);
        }else{
//            Log.e(TAG, "OnCreate-- savedinstancestate--not null");
//            Log.e(TAG, "movies saved: " + savedInstanceState.getParcelableArray("movies"));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
//        Log.e(TAG, "Paused");
//        Bundle bun=new Bundle();
//        bun.putParcelableArrayList("movies",mMovieArray);
//        onSaveInstanceState(bun);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
//        outState.putParcelableArrayList("movies", mMovieArray);
//        Log.e(TAG, "savedInstanceState method called");
        super.onSaveInstanceState(outState);
    }

    public void getMovies(String order) {
        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask();
        fetchMoviesTask.execute(order);
    }

    @Override
    public void recyclerClicked(View v, int position) {
//        Log.e(TAG, "interface callback: " + position + ", Movie: " + mMovieArray.get(position).getMovieTitle());

        Bundle bundle=new Bundle();
        bundle.putString(Constants.POSTER_PATH,mMovieArray.get(position).getPosterUrl());
        bundle.putString(Constants.BACKDROP_PATH,mMovieArray.get(position).getBackDropUrl());
        bundle.putString(Constants.TITLE,mMovieArray.get(position).getMovieTitle());
        bundle.putString(Constants.DATE, mMovieArray.get(position).getDate());
        bundle.putString(Constants.OVERVIEW, mMovieArray.get(position).getSummary());
        bundle.putDouble(Constants.RATING, mMovieArray.get(position).getRating());

        mListener.openDetailFragment(bundle);
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, ArrayList<Movie>> {
        private final String LOG_TAG = MovieListFragment.class.getSimpleName();

        @Override
        protected ArrayList<Movie> doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String forecastJsonStr = null;
            try {
                Log.e(TAG, "sort order: " + params.toString());
                Uri builtUri = Uri.parse(Constants.BASE_URL).buildUpon()
                        .appendQueryParameter(Constants.SORT_BY, params[0])
                        .appendQueryParameter(Constants.API_KEY, getResources().getString(R.string.api_key))
                        .build();

                URL url = new URL(builtUri.toString());
//                Log.e("URL","Movies URL JSON Object: "+url);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                forecastJsonStr = buffer.toString();
            } catch (IOException e) {
//                Log.e(TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
//                        Log.e(TAG, "Error closing stream", e);
                    }
                }
            }
            try {
                return parseJson(forecastJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        private ArrayList<Movie> parseJson(String JsonStr) throws JSONException {

            mMovieArray=new ArrayList<Movie>();

            JSONObject MoviesJson = new JSONObject(JsonStr);
            JSONArray moviesArray = MoviesJson.has("results")? MoviesJson.getJSONArray("results"):null;
            if(moviesArray!=null){
                for(int i=0; i<moviesArray.length();i++){

                    JSONObject movieObj=moviesArray.getJSONObject(i);
                    Movie movie=new Movie();

                    movie.mTitle = movieObj.has(Constants.TITLE)?movieObj.getString(Constants.TITLE):"";
                    movie.mOverview = movieObj.has(Constants.OVERVIEW)?movieObj.getString(Constants.OVERVIEW):"";
                    movie.mPosterUrl=movieObj.has(Constants.POSTER_PATH)?movieObj.getString(Constants.POSTER_PATH):"";
                    movie.mBackDropUrl=movieObj.has(Constants.BACKDROP_PATH)?movieObj.getString(Constants.BACKDROP_PATH):"";
                    movie.mDate=movieObj.has(Constants.DATE)?movieObj.getString(Constants.DATE):"";
                    movie.mVoteAverage = movieObj.has(Constants.RATING)?movieObj.getDouble(Constants.RATING):null;

                    mMovieArray.add(movie);
                }
            }
            return mMovieArray;
        }

        @Override
        protected void onPostExecute(final ArrayList<Movie> result) {

            mGridViewAdapter.setList(result);
            mRecyclerView.scrollToPosition(0);

            if(mMovieArray!=null)
                for(Movie m:mMovieArray){
//                    Log.e("onPostExecute","movie: "+m.getMovieTitle()+", url: "+m.getPosterUrl());
                }
            }
        }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (MainInterface.MovieInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }
}