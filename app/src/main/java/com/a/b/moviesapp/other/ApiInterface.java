package com.a.b.moviesapp.other;

import android.content.ClipData;

import com.a.b.moviesapp.pojo.ResultPOJO;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Andrew on 1/14/2016.
 */
public interface ApiInterface {
    @GET("/3/movie/{id}?api_key=22e815e44d11366f2446d1fa6aa31e75&append_to_response=reviews,trailers")
    Call<ResultPOJO> getMovieExtras(@Path("id") String id);

}
