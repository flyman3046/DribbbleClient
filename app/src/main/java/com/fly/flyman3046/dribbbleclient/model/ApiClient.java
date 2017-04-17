package com.fly.flyman3046.dribbbleclient.model;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public class ApiClient {
    static Retrofit mRetrofit;
    private static final String BASEURL = "https://api.dribbble.com/v1/";
    public static final String APIKEY = "5073d90525ab55efe8e32b610fce30712c8ab5c5dcd7349d5e7d3cb156f9f152";

    public static Retrofit retrofit() {
        if (mRetrofit == null) {
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(BASEURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
        }
        return mRetrofit;
    }

    public interface ApiStores {

        @GET("shots?")
        Observable<List<Shot>> getShots(@Query("access_token") String apiKey);

        @GET("shots/{shootId}/comments?")
        Observable<List<Comment>> getCommentsByShotId(@Path("shootId") Integer shotId, @Query("access_token") String apiKey);
    }
}
