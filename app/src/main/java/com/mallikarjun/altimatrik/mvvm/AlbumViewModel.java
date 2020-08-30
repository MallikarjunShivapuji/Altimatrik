package com.mallikarjun.altimatrik.mvvm;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mallikarjun.altimatrik.repository.AlbumRepository;
import com.mallikarjun.altimatrik.repository.Album;
import com.mallikarjun.altimatrik.repository.AlbumResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlbumViewModel extends AndroidViewModel {
    private AlbumRepository mRepo;

    MutableLiveData<List<Album>> mutableAlbumList = new MutableLiveData<List<Album>>();

    public AlbumViewModel(@NonNull Application application) {
        super(application);

        mRepo = AlbumRepository.getAppsRepositoryInstance();

        mRepo.getApps(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if(response.isSuccessful()) {
                    List albumList = ((AlbumResponse) response.body()).getResults();
                    Collections.sort(albumList);
                    mutableAlbumList.postValue(albumList);
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e("Mallik", t.toString());
            }
        });
    }

    public LiveData<List<Album>> getAlbumList() {
        return mutableAlbumList;

    }
}
