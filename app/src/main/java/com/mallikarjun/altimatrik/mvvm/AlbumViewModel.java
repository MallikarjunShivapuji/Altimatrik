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
import com.mallikarjun.altimatrik.list.AlbumListAdapter.SearchMode;

public class AlbumViewModel extends AndroidViewModel {
    private AlbumRepository mRepo;

    private static final String TAG = "AlbumViewModel";
    private SearchMode mSearchMode = SearchMode.TRACK_NAME;

    List albumOriginalList = new ArrayList<Album>();
    List dublicateRemovedlbumOriginalList = new ArrayList<Album>();
    MutableLiveData mutableAlbumList = new MutableLiveData<List<Album>>();

    public AlbumViewModel(@NonNull Application application) {
        super(application);

        mRepo = AlbumRepository.getAppsRepositoryInstance();

        mRepo.getApps(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if(response.isSuccessful()) {
                    albumOriginalList = ((AlbumResponse) response.body()).getResults();
                    System.out.println("Size of original list: " + albumOriginalList.size());
                    dublicateRemovedlbumOriginalList = new ArrayList<Album>(new HashSet<Album>(albumOriginalList));
                    System.out.println("Dublicate removed Size: " + dublicateRemovedlbumOriginalList.size());

                    mutableAlbumList.postValue(dublicateRemovedlbumOriginalList);
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

    public void setSearchMode(SearchMode searchMode) {
        if(getSearchMode() != searchMode) {
            mSearchMode = searchMode;
            switch (mSearchMode) {
                case TRACK_NAME://TrackName
                    sortByTrackName();
                    break;
                case ARTIST_NAME://ArtistName
                    sortArtistName();
                    break;
                default://CollectionName
                    sortCollectionName();
                    break;
            }
        }
    }

    public SearchMode getSearchMode() {
        return mSearchMode;
    }

    public void sortByTrackName() {
        Collections.sort(dublicateRemovedlbumOriginalList, new SortByTrackName());
        mutableAlbumList.setValue(dublicateRemovedlbumOriginalList);
    }

    public void sortArtistName() {
        Collections.sort(dublicateRemovedlbumOriginalList, new SortByArtistName());
        mutableAlbumList.setValue(dublicateRemovedlbumOriginalList);
    }

    public void sortCollectionName() {
        Collections.sort(dublicateRemovedlbumOriginalList, new SortByCollectionName());
        mutableAlbumList.setValue(dublicateRemovedlbumOriginalList);
    }

    public void searchAlbum(String trackName) {
        List searchResult = new ArrayList<Album>();
        for(Object album : dublicateRemovedlbumOriginalList) {
            Log.d(TAG, "Search Key: " + trackName + " and Name : " + ((Album)album).getTrackName());
            switch (mSearchMode) {
                case TRACK_NAME:
                    System.out.println("Search by TrackName");
                    if(((Album)album).getTrackName() != null
                            && !((Album)album).getTrackName().isEmpty()
                            && ((Album)album).getTrackName().toLowerCase().contains(trackName.toLowerCase())) {

                        searchResult.add(album);
                    }
                    break;
                case ARTIST_NAME:
                    System.out.println("Search by ArtistName");
                    if(((Album)album).getArtistName() != null
                            && !((Album)album).getArtistName().isEmpty()
                            && ((Album)album).getArtistName().toLowerCase().contains(trackName.toLowerCase())) {
                        searchResult.add(album);
                    }
                    break;
                default:
                    System.out.println("Search by CollectionName");
                    if(((Album)album).getCollectionName() != null
                            && !((Album)album).getCollectionName().isEmpty()
                            && ((Album)album).getCollectionName().toLowerCase().contains(trackName.toLowerCase())) {
                        searchResult.add(album);
                    }
                    break;
            }
        }
        mutableAlbumList.setValue(searchResult);
    }

    class SortByArtistName implements Comparator<Album> {

        @Override
        public int compare(Album albums1, Album albums2) {
            if(albums1.getCollectionName() != null && albums2.getCollectionName() != null) {
                return albums1.getArtistName().compareTo(albums2.getArtistName());
            }
            return -1;
        }
    }

    class SortByTrackName implements Comparator<Album> {

        @Override
        public int compare(Album albums1, Album albums2) {

            if(albums1.getTrackName() != null && albums2.getTrackName() != null) {
                return  albums1.getTrackName().compareTo(albums2.getTrackName());
            }
            return -1;
        }
    }
    class SortByCollectionName implements Comparator<Album> {

        @Override
        public int compare(Album albums1, Album albums2) {
            if(albums1.getCollectionName() != null && albums2.getCollectionName() != null) {
                return albums1.getCollectionName().compareTo(albums2.getCollectionName());
            }
            return -1;
        }
    }
}
