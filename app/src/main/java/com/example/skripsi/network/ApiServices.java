package com.example.skripsi.network;

import com.example.skripsi.response.ResponseRoute;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiServices {
    // https://maps.googleapis.com/maps/api/directions/json?origin=Medan,ID&destination=Jakarta,ID&key=AIzaSyBCITS4OOelRvvFGj8yQAZ0Knz6m8Mvsz8
    @GET("json")
    Call<ResponseRoute> request_route(
            @Query("origin") String origin,
            @Query("destination") String destination,
            @Query("key") String api_key
    );
}
