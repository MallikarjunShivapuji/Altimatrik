package com.mallikarjun.altimatrik.repository;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AlbumRepository {

    private static AlbumRepository instance;
    private static JsonPlaceHolderApi jsonPlaceHolderApi;

    private AlbumRepository() {

    }

    public static AlbumRepository getAppsRepositoryInstance() {
        if (instance == null) {
            instance = new AlbumRepository();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://itunes.apple.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        }
        return instance;
    }

    public AlbumResponse getApps(Callback callback) {
        Log.i("Mallikarjun.S calling ", "Apps list");
        Call<AlbumResponse> call = jsonPlaceHolderApi.getApps();

        call.enqueue(callback);
        return null;
    }
}
