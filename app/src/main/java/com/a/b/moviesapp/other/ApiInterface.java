package com.a.b.moviesapp.other;

import android.content.ClipData;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Andrew on 1/14/2016.
 */
public interface ApiInterface {
    @GET("/3/movie/{id}/videos?&api_key=22e815e44d11366f2446d1fa6aa31e75")
    Call<Result> getMovieExtras(@Path("id") String id);

//    @GET("/search/users")
//    Call<Movie> getUsersNamedTom(@Query("q") String name);

    @POST("/user/create")
//    Call<Item> createUser(@Body String name, @Body String email);

    @PUT("/user/{id}/update")
    Call<ClipData.Item> updateUser(@Path("id") String id , @Body ClipData.Item user);

}
