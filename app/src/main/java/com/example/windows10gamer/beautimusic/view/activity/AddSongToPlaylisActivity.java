package com.example.windows10gamer.beautimusic.view.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.windows10gamer.beautimusic.R;
import com.example.windows10gamer.beautimusic.database.SongDatabase;
import com.example.windows10gamer.beautimusic.model.Song;
import com.example.windows10gamer.beautimusic.utilities.Utils;
import com.example.windows10gamer.beautimusic.view.adapter.PlaylistAddSongAdapter;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AddSongToPlaylisActivity extends AppCompatActivity implements PlaylistAddSongAdapter.GetList {
    private Toolbar toolbar;
    private RecyclerView recyclSongList;
    private PlaylistAddSongAdapter adapter;
    private List<Song> mSongList;
    private List<Song> songList;
    private LinearLayoutManager linearLayoutManager;
    private String namePlaylist;
    private Gson gson;
    private Type type;
    private SongDatabase songDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_song_to_playlis);

        namePlaylist = getIntent().getStringExtra(Utils.NAME_PLAYLIST);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.choose_list_song);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();
        loadData();
    }

    private void loadData() {
        new AsyncTask<String, Void, Void>() {

            @Override
            protected Void doInBackground(String... params) {
                mSongList.clear();
                mSongList.addAll(SongDatabase.getSongFromDevice(getApplicationContext()));

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                adapter.notifyDataSetChanged();

            }
        }.execute("");
    }

    private void initView() {
        songList = new ArrayList<>();
        mSongList = new ArrayList<>();
        recyclSongList = (RecyclerView) findViewById(R.id.recycleCheckList);
        recyclSongList.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclSongList.setLayoutManager(linearLayoutManager);
        adapter = new PlaylistAddSongAdapter(this, mSongList, this);
        recyclSongList.setAdapter(adapter);
        songDatabase = new SongDatabase(this);

    }

    @Override
    public void sendList(List<Song> songs) {
        songList.clear();
        songList.addAll(songs);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_done, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemDone:

                gson = new Gson();
                String listSong = gson.toJson(songList);
                songDatabase.addPlayList(namePlaylist, listSong);

                setResult(RESULT_OK,new Intent());
                finish();

                break;

        }
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        //onBackPressed();
        Toast.makeText(this,"ABC",Toast.LENGTH_SHORT).show();
        return true;
    }

}
