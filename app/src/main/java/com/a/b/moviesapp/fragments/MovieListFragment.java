package com.a.b.moviesapp.fragments;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.a.b.moviesapp.other.Constants;
import com.a.b.moviesapp.GridViewAdapter;
import com.a.b.moviesapp.other.MainInterface;
import com.a.b.moviesapp.other.RefreshGridView;
import com.a.b.moviesapp.other.UpdateGridView;
import com.a.b.moviesapp.pojo.Movie;
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
import java.util.List;

import de.greenrobot.event.EventBus;

public class MovieListFragment extends Fragment implements RecyclerClickListener{
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
        mGridViewAdapter.setList(mMovieArray);

        return view;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if(savedInstanceState==null){
            getMovies(Constants.MOST_POPULAR);
        }else{
            mMovieArray=savedInstanceState.getParcelableArrayList("movies");
//            Log.e(TAG, "movies retrieved from savedInstanceState: " + savedInstanceState.getParcelableArrayList("movies"));
        }
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_item_popular_sort) {
            getMovies(Constants.MOST_POPULAR);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.action_popular_sort);

        }else if(id==R.id.menu_item_highest_rated) {
            getMovies(Constants.HIGHEST_RATED);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.action_highest_rated);
        }else if(id==R.id.menu_item_favorited){
            getFavorites();
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.action_favorited);
        }
        return super.onOptionsItemSelected(item);
    }
    public void getFavorites(){
        Log.e(TAG,"getFavorites");
        String[] selection=new String[]{Constants.TITLE};
        Cursor cursor = getContext().getContentResolver().query(Uri.parse(Constants.CONTENT_AUTHORITY+"/get_stored_movies"),null,null,null,null);
        if(cursor!=null&& cursor.getCount()>0) {
                cursor.moveToFirst();
                ArrayList<Movie> movies = new ArrayList<>();
                do {
                    Log.e(TAG, "getFavorites: " + cursor.getString(1));

                    Movie m = new Movie();

                    m.setId(cursor.getInt(cursor.getColumnIndex(Constants.MOVIE_ID)));
                    m.setMovieTitle(cursor.getString(cursor.getColumnIndex(Constants.TITLE)));
                    m.setPosterUrl(cursor.getString(cursor.getColumnIndex(Constants.POSTER_PATH)));
                    m.setBackDropUrl(cursor.getString(cursor.getColumnIndex(Constants.BACKDROP_PATH)));
                    m.setDate(cursor.getString(cursor.getColumnIndex(Constants.DATE)));
                    m.setRating(cursor.getDouble(cursor.getColumnIndex(Constants.RATING)));
                    m.setSummary(cursor.getString(cursor.getColumnIndex(Constants.OVERVIEW)));
                    m.setFavotite(cursor.getInt(cursor.getColumnIndex(Constants.FAVORITED)) == 1 ? Boolean.TRUE : Boolean.FALSE);

                    //                Constants.TRAILERS
                    //                Constants.REVIEWS

                    movies.add(m);
                } while (cursor.moveToNext());
                cursor.close();
                mMovieArray = movies;

                mGridViewAdapter.setList(movies);
                mRecyclerView.scrollToPosition(0);

        }else{
            mGridViewAdapter.setList(null);
            Toast.makeText(getActivity(),"No Movies Favorited",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
//        Log.e(TAG, "Paused");
        Bundle bun=new Bundle();
        bun.putParcelableArrayList("movies", mMovieArray);
        onSaveInstanceState(bun);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("movies", mMovieArray);
//        Log.e(TAG, "savedInstanceState method called");
        super.onSaveInstanceState(outState);
    }

    public void getMovies(String order) {
        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask();
        fetchMoviesTask.execute(order);
    }

    @Override
    public void recyclerClicked(View v, int position) {
        mListener.openDetailFragment(mMovieArray.get(position));
    }

//    @Override
//    public void updateGridView() {
//        getFavorites();
//        Cursor c=getContext().getContentResolver().query(Uri.parse(Constants.CONTENT_AUTHORITY),null,null,null,null);
//        List<Movie>movies = new ArrayList<>();
//        if(c!=null&&c.getCount()>0) {
//            do {
//                Movie mv=new Movie();
//                mv.setFavotite(c.getString());
//
//            } while (c.moveToNext())
//
//            cv.put(Constants.MOVIE_ID, mMovieDetails.getId());
//            cv.put(Constants.TITLE, mMovieDetails.getMovieTitle());
//            cv.put(Constants.POSTER_PATH, mMovieDetails.getPosterUrl());
//            cv.put(Constants.BACKDROP_PATH, mMovieDetails.getBackDropUrl());
//            cv.put(Constants.DATE, mMovieDetails.getDate());
//            cv.put(Constants.RATING, mMovieDetails.getRating());
//            cv.put(Constants.OVERVIEW, mMovieDetails.getSummary());
//            cv.put(Constants.FAVORITED, Boolean.TRUE);
//            cv.put(Constants.TRAILERS, "trailers");
//            cv.put(Constants.REVIEWS, "reviews and stuff");
//        }
//
//        mGridViewAdapter.setList(movies);
//    }

    public class FetchMoviesTask extends AsyncTask<String, Void, ArrayList<Movie>> {
        private final String LOG_TAG = MovieListFragment.class.getSimpleName();

        @Override
        protected ArrayList<Movie> doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String forecastJsonStr = null;
            try {
//                Log.e(TAG, "sort order: " + params.toString());
                Uri builtUri = Uri.parse(Constants.BASE_URL).buildUpon()
                        .appendQueryParameter(Constants.SORT_BY, params[0])
                        .appendQueryParameter(Constants.API_KEY, Constants.API_KEY_STRING)
                        .build();

                URL url = new URL(builtUri.toString());
                Log.e("URL", "Movies URL JSON Object: " + url);

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
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), R.string.dead_network, Toast.LENGTH_LONG).show();
                    }
                });

                Log.e(TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(TAG, "Error closing stream", e);
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

            mMovieArray = new ArrayList<Movie>();

            JSONObject MoviesJson = new JSONObject(JsonStr);
            JSONArray moviesArray = MoviesJson.has("results") ? MoviesJson.getJSONArray("results") : null;
            if (moviesArray != null) {
                for (int i = 0; i < moviesArray.length(); i++) {

                    JSONObject movieObj = moviesArray.getJSONObject(i);
                    Movie movie = new Movie();

                    movie.mTitle = movieObj.has(Constants.TITLE) ? movieObj.getString(Constants.TITLE) : "";
                    movie.mOverview = movieObj.has(Constants.OVERVIEW) ? movieObj.getString(Constants.OVERVIEW) : "";
                    movie.mPosterUrl = movieObj.has(Constants.POSTER_PATH) ? movieObj.getString(Constants.POSTER_PATH) : "";
                    movie.mBackDropUrl = movieObj.has(Constants.BACKDROP_PATH) ? movieObj.getString(Constants.BACKDROP_PATH) : "";
                    movie.mDate = movieObj.has(Constants.DATE) ? movieObj.getString(Constants.DATE) : "";
                    movie.mVoteAverage = movieObj.has(Constants.RATING) ? movieObj.getDouble(Constants.RATING) : null;
                    movie.mId=movieObj.has(Constants.ID)?movieObj.getInt(Constants.ID):null;

                    mMovieArray.add(movie);
                }
            }
            return mMovieArray;
        }

        @Override
        protected void onPostExecute(final ArrayList<Movie> result) {
            if (mMovieArray != null) {
                mGridViewAdapter.setList(result);
                mRecyclerView.scrollToPosition(0);

//                for (Movie m : mMovieArray) {
//                    Log.e("onPostExecute", "movie: " + m.getMovieTitle() + ", url: " + m.getPosterUrl());
//                }
            }
        }
    }
    public void onEvent(RefreshGridView event){
        getFavorites();
    }
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().registerSticky(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
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