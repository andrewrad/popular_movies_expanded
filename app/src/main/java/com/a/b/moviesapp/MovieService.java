//package com.a.b.moviesapp;
//
//import android.app.IntentService;
//import android.content.Intent;
//import android.net.Uri;
//import android.util.Log;
//
//import org.json.JSONException;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//
///**
// * Created by Andrew on 7/15/2015.
// */
//public class MovieService extends IntentService {
//
//    private final String LOG_TAG="intent service";
//
//    public MovieService(String name) {
//        super(name);
//    }
//
//    @Override
//    protected void onHandleIntent(Intent intent) {
//        HttpURLConnection urlConnection = null;
//        BufferedReader reader = null;
//        String forecastJsonStr = null;
//        try {
//            final String BASE_URL =
//                    "http://api.themoviedb.org/3/discover/movie?";
//
//            final String SORT_BY= "sort_by";
//            final String MOST_POPULAR="popularity.desc";
//            final String API_KEY="api_key";
//
//            Uri builtUri = Uri.parse(BASE_URL).buildUpon()
//                    .appendQueryParameter(SORT_BY, "popularity.desc")
//                    .appendQueryParameter(API_KEY, "22e815e44d11366f2446d1fa6aa31e75")
////                        .appendQueryParameter(DAYS_PARAM, Integer.toString(numDays))
//                    .build();
//
//            URL url = new URL(builtUri.toString());
//            Log.e("URL", "Movies URL: " + url);
//
//            urlConnection = (HttpURLConnection) url.openConnection();
//            urlConnection.setRequestMethod("GET");
//            urlConnection.connect();
//
//            // Read the input stream into a String
//            InputStream inputStream = urlConnection.getInputStream();
//            StringBuffer buffer = new StringBuffer();
//            if (inputStream == null) {
//                // Nothing to do.
//                return;
//            }
//            reader = new BufferedReader(new InputStreamReader(inputStream));
//
//            String line;
//            while ((line = reader.readLine()) != null) {
//                buffer.append(line + "\n");
//            }
//
//            if (buffer.length() == 0) {
//                // Stream was empty.  No point in parsing.
//                return;
//            }
//            forecastJsonStr = buffer.toString();
//        } catch (IOException e) {
//            Log.e(LOG_TAG, "Error ", e);
//            return;
//        } finally {
//            if (urlConnection != null) {
//                urlConnection.disconnect();
//            }
//            if (reader != null) {
//                try {
//                    reader.close();
//                } catch (final IOException e) {
//                    Log.e(LOG_TAG, "Error closing stream", e);
//                }
//            }
//        }
//        try {
////            return parseJson(forecastJsonStr);
//        } catch (JSONException e) {
//            Log.e(LOG_TAG, e.getMessage(), e);
//            e.printStackTrace();
//        }
//        return;
//    }
//
//}
