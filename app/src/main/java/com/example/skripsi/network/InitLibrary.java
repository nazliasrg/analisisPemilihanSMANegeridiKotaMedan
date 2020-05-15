package com.example.skripsi.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InitLibrary {
    // https://maps.googleapis.com/maps/api/directions/json?origin=Medan,ID&destination=Jakarta,ID&key=AIzaSyBCITS4OOelRvvFGj8yQAZ0Knz6m8Mvsz8

    public static String BASE_URL = "https://maps.googleapis.com/maps/api/directions/";
    public static Retrofit setInit(){
        return new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    public static ApiServices getInstance(){
        return setInit().create(ApiServices.class);
    }
}
