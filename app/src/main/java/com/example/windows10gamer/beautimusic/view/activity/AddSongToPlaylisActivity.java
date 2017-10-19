package com.example.windows10gamer.beautimusic.view.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.windows10gamer.beautimusic.R;
import com.example.windows10gamer.beautimusic.utilities.LoadData;
import com.example.windows10gamer.beautimusic.utilities.singleton.SongDatabase;
import com.example.windows10gamer.beautimusic.model.Song;
import com.example.windows10gamer.beautimusic.utilities.Utils;
import com.example.windows10gamer.beautimusic.view.adapter.PlaylistAddSongAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class AddSongToPlaylisActivity extends AppCompatActivity implements PlaylistAddSongAdapter.GetList {

    private RecyclerView mLvSong;
    private PlaylistAddSongAdapter mAdapter;
    private List<Song> mSongList;
    private List<Song> mGetList;
    private LinearLayoutManager mLinearLayout;
    private SongDatabase mDbHanler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_song_to_playlis);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
                mSongList.addAll(LoadData.getSongFromDevice(getApplicationContext()));
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                mAdapter.notifyDataSetChanged();

            }
        }.execute("");
    }

    private void initView() {
        mGetList = new ArrayList<>();
        mSongList = new ArrayList<>();
        mDbHanler = SongDatabase.getInstance(this);

        mLvSong = (RecyclerView) findViewById(R.id.recycleCheckList);
        mLvSong.setHasFixedSize(true);
        mLinearLayout = new LinearLayoutManager(this);
        mLvSong.setLayoutManager(mLinearLayout);
        mAdapter = new PlaylistAddSongAdapter(this, mSongList, this);
        mLvSong.setAdapter(mAdapter);

    }

    @Override
    public void sendList(List<Song> songs) {
        mGetList.clear();
        mGetList.addAll(songs);
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

                Gson gson = new Gson();
                String namePlaylist = getIntent().getStringExtra(Utils.NAME_PLAYLIST);
                String listSong = gson.toJson(mGetList);

                mDbHanler.addPlayList(namePlaylist, listSong);

                setResult(RESULT_OK, new Intent());
                finish();

                break;
        }
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        Toast.makeText(this, "ABC", Toast.LENGTH_SHORT).show();
        return true;
    }

}
