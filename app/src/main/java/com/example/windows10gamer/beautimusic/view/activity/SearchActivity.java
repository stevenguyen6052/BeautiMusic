package com.example.windows10gamer.beautimusic.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.windows10gamer.beautimusic.R;
import com.example.windows10gamer.beautimusic.database.SongDatabase;
import com.example.windows10gamer.beautimusic.model.Song;
import com.example.windows10gamer.beautimusic.view.adapter.SongAdapter;
import com.example.windows10gamer.beautimusic.view.utilities.Utils;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private List<Song> mSongList, filteredModelList;
    private ListView lvSongs;
    private SongAdapter songAdapter;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setUpToolbar();
        lvSongs = (ListView) findViewById(R.id.lvSongSearch);
        mSongList = new ArrayList<>();
        mSongList.addAll(SongDatabase.getSongFromDevice(this));
        songAdapter = new SongAdapter(this, mSongList, R.layout.item_song);
        lvSongs.setAdapter(songAdapter);
        lvSongOnItemClick();

    }

    private void lvSongOnItemClick() {
        lvSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchActivity.this, PlayMusicActivity.class);
                Bundle b = new Bundle();
                b.putString(Utils.TAG, Utils.SEARCH);
                b.putInt(Utils.POSITION, position);
                if (filteredModelList.size() != 0) {
                    b.putParcelableArrayList(Utils.LIST_SONG, (ArrayList<Song>) filteredModelList);
                } else {
                    b.putParcelableArrayList(Utils.LIST_SONG, (ArrayList<Song>) mSongList);
                }
                intent.putExtras(b);
                startActivity(intent);
            }
        });
    }


    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null)
            setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchActivity.super.onBackPressed();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem itemSearch = menu.findItem(R.id.itemSearch);
        searchView = (SearchView) itemSearch.getActionView();
        searchView.hasFocusable();
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        filteredModelList = filter(mSongList, newText.trim());

        songAdapter.setFilter(filteredModelList);

        return true;
    }

    // get list after search
    private List<Song> filter(List<Song> mlistSong, String query) {
        String s = Utils.unAccent(query.toLowerCase());
        List<Song> filteredModelList = new ArrayList<>();

        for (Song song : mlistSong) {
            String text = Utils.unAccent(song.getNameSong().toLowerCase());
            if (text.contains(s)) {
                filteredModelList.add(song);
            }
        }
        return filteredModelList;
    }
}
