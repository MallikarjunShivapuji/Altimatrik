package com.mallikarjun.altimatrik.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.mallikarjun.altimatrik.R;
import com.mallikarjun.altimatrik.list.AlbumListAdapter;
import com.mallikarjun.altimatrik.list.AlbumListAdapter.SearchMode;
import com.mallikarjun.altimatrik.mvvm.AlbumViewModel;
import com.mallikarjun.altimatrik.repository.Album;

import java.util.ArrayList;
import java.util.List;

public class DashBoardActivity extends AppCompatActivity {

    private AlbumViewModel viewModel;

    AlbumListAdapter adapter = new AlbumListAdapter(this, new ArrayList<Album>());

    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        viewModel = ViewModelProviders.of(this).get(AlbumViewModel.class);

        projetList(new ArrayList<Album>());

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait...");
        progressDialog.setMessage("Loading Albums");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        viewModel.getAlbumList().observe(this, new Observer<List<Album>>() {
            @Override
            public void onChanged(List<Album> results) {
                progressDialog.dismiss();
                adapter.setNewList(results);
            }
        });

    }

    private void projetList(List<Album> moviesList) {
        RecyclerView recyclerView = findViewById(R.id.rvAlbumlist);
        adapter = new AlbumListAdapter(this, moviesList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(DashBoardActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        MenuItem searchViewItem = menu.findItem(R.id.app_bar_search);

        searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                viewModel.searchAlbum(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        searchView.clearFocus();
        searchView.onActionViewCollapsed();
        searchView.setQuery("", false);
        switch (item.getItemId()) {
            case R.id.action_searchByTrackName :
                viewModel.setSearchMode(SearchMode.TRACK_NAME);
                break;
            case R.id.action_searchByArtistName :
                viewModel.setSearchMode(SearchMode.ARTIST_NAME);
                break;
            default :
                viewModel.setSearchMode(SearchMode.COLLECTION_NAME);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}