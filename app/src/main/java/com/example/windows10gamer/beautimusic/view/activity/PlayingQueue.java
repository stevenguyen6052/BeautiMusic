package com.example.windows10gamer.beautimusic.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.example.windows10gamer.beautimusic.R;
import com.example.windows10gamer.beautimusic.database.SongDatabase;
import com.example.windows10gamer.beautimusic.model.Song;
import com.example.windows10gamer.beautimusic.view.helper.dragandswipe.QueueAdapter;
import com.example.windows10gamer.beautimusic.view.helper.dragandswipe.SimpleItemTouchHelperCallback;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PlayingQueue extends AppCompatActivity implements QueueAdapter.OnStartDragListener {

    private static final String NAME_ALBUM = "Name Album";
    private static final String NAME_ARTIST = "Name Artist";
    private final static String TAG = "TAG";
    private final static String TAG_SONG = "SONG";
    private final static String TAG_ARTIST = "ARTIST";
    private final static String TAG_ALBUM = "ALBUM";
    private final static String LIST = "List";

    private ItemTouchHelper mItemTouchHelper;
    private List<Song> mSongList;
    private List<Song> sendListSong;
    private SongDatabase mSongDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing_queue);

        getData();
        setUpAdapter();
        setUpToolbar();

    }

    private void setUpToolbar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        dataResult();
        finish();
        return true;
    }

    private void dataResult() {
        Intent intent = new Intent();
        Bundle sendData = new Bundle();
        sendData.putSerializable(LIST, (Serializable) sendListSong);
        intent.putExtras(sendData);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private void setUpAdapter() {
        QueueAdapter adapter = new QueueAdapter(this, this, mSongList);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycleQueue);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);


        sendListSong = adapter.getSongList();

    }

    private void getData() {
        mSongDatabase = new SongDatabase(this);
        if (mSongList == null) {
            mSongList = new ArrayList<>();
        }

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String tag, nameAlbum, nameArtist;
        tag = bundle.getString(TAG);

        if (tag.equals(TAG_SONG)) {
            mSongList = mSongDatabase.getAllListSong();

        } else if (tag.equals(TAG_ALBUM)) {
            nameAlbum = bundle.getString(NAME_ALBUM);
            mSongList = mSongDatabase.getAllSongFromAlbum(nameAlbum);

        } else if (tag.equals(TAG_ARTIST)) {
            nameArtist = bundle.getString(NAME_ARTIST);
            mSongList = mSongDatabase.getAlLSongFromArtist(nameArtist);
        }
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

}
