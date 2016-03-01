package com.a.b.moviesapp.other;

import android.util.Log;

import com.google.gson.Gson;
import retrofit.Call;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.io.IOException;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * This class sets up the OKHttp and Retrofit client for retrieving movie trailers and ratings
 * Created by Andrew on 1/14/2016.
 */
public class RestClient {

    private static ApiInterface mApiInterface ;

    public static ApiInterface getClient() {
//        String TAG="RestClient";

        /* Logging for http request, very handy and detailed logging output*/
        HttpLoggingInterceptor logging= new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient=new OkHttpClient();
        httpClient.interceptors().add(logging);

        if (mApiInterface == null) {

            OkHttpClient okClient = new OkHttpClient();
            okClient.interceptors().add(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                Response response = chain.proceed(chain.request());
                return null;
                }
            });

            Retrofit client = new Retrofit.Builder()
                .baseUrl(Constants.TRAILER_BASE_URL)
                .addConverter(String.class, new ToStringConverter())
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
            mApiInterface = client.create(ApiInterface.class);
//            Log.e(TAG,"RestClient testing log statement: "+client);
        }

        return mApiInterface ;
    }
}
