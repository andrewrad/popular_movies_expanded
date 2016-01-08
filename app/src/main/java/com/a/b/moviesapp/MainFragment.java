package com.a.b.moviesapp;

import android.app.Fragment;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

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

public class MainFragment extends Fragment {
    ImageView mImage;
    GridView mGridView;
    ImageAdapter mImageAdapter;
    RecyclerView mRecyclerView;
    private GridViewAdapter mGridViewAdapter;
    private ArrayList<Movie> mMovieArray;

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        View view =inflater.inflate(R.layout.fragment_main,container,false);
//        mImage=(ImageView) view.findViewById(R.id.image_view);

        View view =inflater.inflate(R.layout.grid_fragment, container, false);
        mRecyclerView=(RecyclerView) view.findViewById(R.id.recycler_view);


//        mGridView=(GridView) view.findViewById(R.id.grid_view);
//        mImageAdapter=new ImageAdapter(getActivity().getApplicationContext(),R.layout.individual_movie_pics_for_gridview, mGridArray);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getMovies();
    }
    private void getMovies() {
        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask();
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
//        String location = prefs.getString(getString(R.string.pref_location_key),
//                getString(R.string.pref_location_default));
        fetchMoviesTask.execute();

    }

    public class FetchMoviesTask extends AsyncTask<ArrayList<Movie>, Void, ArrayList<Movie>> {

        private final String LOG_TAG = MainFragment.class.getSimpleName();

        private ArrayList<Movie> parseJson(String JsonStr) throws JSONException {
            final String POSTER_URL="poster_path";
            final String PHOTO_URL="backdrop_path";
            final String TITLE="title";
            final String OVERVIEW="overview";
            final String DATE="release_date";
            final String RATING="vote_count";

            mMovieArray=new ArrayList<Movie>();

            JSONObject MoviesJson = new JSONObject(JsonStr);
            JSONArray moviesArray = MoviesJson.has("results")? MoviesJson.getJSONArray("results"):null;
            if(moviesArray!=null){
                for(int i=0; i<moviesArray.length();i++){

                    JSONObject movieObj=moviesArray.getJSONObject(i);
                    Log.e("test","test object: "+i+": "+movieObj.toString());
                    Log.e("testing","URL: "+(movieObj.has(POSTER_URL)?movieObj.getString(POSTER_URL):null));
                    String title = movieObj.has(TITLE)?movieObj.getString(TITLE):"";
                    String overview = movieObj.has(OVERVIEW)?movieObj.getString(OVERVIEW):"";
                    String url=movieObj.has(POSTER_URL)?movieObj.getString(POSTER_URL):"";
                    String date=movieObj.has(DATE)?movieObj.getString(DATE):"";
                    Double review = movieObj.has(RATING)?movieObj.getDouble(RATING):null;

//                    Movie movie = new Movie(title,overview,url,date,review);
//                    movies.add(new Movie(title,overview,url,date,review));
                    mMovieArray.add(new Movie(title,overview,url,date,review));
//                    Log.e("test grab","grabbing url: "+movie.)
                }
            }
            return mMovieArray;
        }
        @Override
        protected ArrayList<Movie> doInBackground(ArrayList<Movie>... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String forecastJsonStr = null;
            try {
                final String BASE_URL ="http://api.themoviedb.org/3/discover/movie?";
                final String SORT_BY= "sort_by";
                final String POPULARITY="popularity.desc";
                final String API_KEY="api_key";

                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_BY, "popularity.desc")
                        .appendQueryParameter(API_KEY, getResources().getString(R.string.api_key))
//                        .appendQueryParameter(DAYS_PARAM, Integer.toString(numDays))
                        .build();

                URL url = new URL(builtUri.toString());
                Log.e("URL","Movies URL: "+url);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                forecastJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
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

        @Override
        protected void onPostExecute(ArrayList<Movie> result) {
//            if (result != null) {
//                Log.e("json", result.toString());
//                for(int i=0; i<result.size();i++){
//                    Log.e("result","result: "+i+": "+result.get(i).getMovieUrl());
//                }
//                }
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mGridViewAdapter = new GridViewAdapter(getActivity(),mMovieArray);
            mRecyclerView.setAdapter(mGridViewAdapter);
//            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            if(mMovieArray!=null)
                for(Movie m:mMovieArray){
                    Log.e("onPostExecute","movie: "+m.getMovieTitle()+", url: "+m.getMovieUrl());
                }
//            mGridView.setAdapter(new ImageAdapter(getActivity().getApplicationContext(),result));

//            mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                public void onItemClick(AdapterView<?> parent, View v,
//                                        int position, long id) {
//                    Toast.makeText(getActivity().getApplicationContext(), "" + position,
//                            Toast.LENGTH_SHORT).show();
//                }
//            });

            }
        }
    }


