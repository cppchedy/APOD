package com.example.apod;

import java.util.Date;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AstronomyPODApi {

    @GET("planetary/apod")
    Call<PictureOfTheDay> getPictureOFtheDay(@Query("api_key") String apiKey, @Query("date") String dt, @Query("hd") String hd);
    @GET("planetary/apod")
    Call<PictureOfTheDay> getPictureOFtheDay(@Query("api_key") String apiKey);
}
