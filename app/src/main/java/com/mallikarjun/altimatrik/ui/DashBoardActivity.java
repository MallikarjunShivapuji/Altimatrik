package com.mallikarjun.altimatrik.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.mallikarjun.altimatrik.R;
import com.mallikarjun.altimatrik.list.AlbumListAdapter;
import com.mallikarjun.altimatrik.mvvm.AlbumViewModel;
import com.mallikarjun.altimatrik.repository.Album;

import java.util.ArrayList;
import java.util.List;

public class DashBoardActivity extends AppCompatActivity {

    private AlbumViewModel viewModel;

    AlbumListAdapter adapter = new AlbumListAdapter(this, new ArrayList<Album>());

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
}