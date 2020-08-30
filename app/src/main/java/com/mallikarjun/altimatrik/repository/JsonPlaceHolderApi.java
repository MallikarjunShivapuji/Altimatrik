package com.mallikarjun.altimatrik.repository;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonPlaceHolderApi {
    @GET("search?term=all")
    Call<AlbumResponse> getApps();
}
