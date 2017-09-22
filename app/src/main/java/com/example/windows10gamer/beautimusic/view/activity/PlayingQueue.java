package com.example.windows10gamer.beautimusic.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.windows10gamer.beautimusic.R;
import com.example.windows10gamer.beautimusic.database.SongDatabase;
import com.example.windows10gamer.beautimusic.model.Song;
import com.example.windows10gamer.beautimusic.view.utilities.dragandswipe.ListChangedListener;
import com.example.windows10gamer.beautimusic.view.utilities.dragandswipe.QueueAdapter;
import com.example.windows10gamer.beautimusic.view.utilities.dragandswipe.SimpleItemTouchHelperCallback;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PlayingQueue extends AppCompatActivity implements QueueAdapter.OnStartDragListener, View.OnClickListener, ListChangedListener {
    //tag check debug
    private static final String TAG_CHECK_ERROR = "PlayingQueue";
    // tag to get data
    private final static String TAG = "TAG";
    private final static String TAG_SONG = "SONG";
    private final static String TAG_ARTIST = "ARTIST";
    private final static String TAG_ALBUM = "ALBUM";
    private final static String LIST = "LIST";
    private static final String TAG_DETAIL = "DETAIL";

    private ItemTouchHelper mItemTouchHelper;
    private List<Song> mSongList;
    private List<Song> sendListSong;
    private SongDatabase mSongDatabase;

    private View mLayoutOpenPlayMusic;

    private String jsonListSongId, tagCheck;
    private TextView mTvNameSong, mTvNameArtist;
    private ImageView mPlayPause;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG_CHECK_ERROR, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing_queue);

        initView();
        getData();
        setUpAdapter();
        setUpToolbar();

    }

    // when create activity or restart will update current song into minicontrol playing
    @Override
    protected void onResume() {
        Log.d(TAG_CHECK_ERROR, "onResume");
        super.onResume();
        miniControlPlay();

    }

    private void initView() {
        mTvNameSong = (TextView) findViewById(R.id.queueNameSong);
        mTvNameArtist = (TextView) findViewById(R.id.queueNameArtist);
        mPlayPause = (ImageView) findViewById(R.id.queuePlayPause);
        mLayoutOpenPlayMusic = findViewById(R.id.queueOpenPlayMusic);

        mPlayPause.setOnClickListener(this);
        mLayoutOpenPlayMusic.setOnClickListener(this);
    }

    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null)
            setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        dataResult();
        finish();
        return true;
    }

    private void dataResult() {
        sendListSong = getDataAfterDragAndSwipe();
        Intent intent = new Intent();
        Bundle sendData = new Bundle();
        sendData.putSerializable(LIST, (Serializable) sendListSong);
        intent.putExtras(sendData);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private void setUpAdapter() {
        QueueAdapter adapter = new QueueAdapter(this, this, mSongList, this);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycleQueue);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);

    }

    private void miniControlPlay() {
        if (MainActivity.musicService.mPlayer != null) {
            if (MainActivity.musicService.mPlayer.isPlaying()) {
                mTvNameSong.setText(MainActivity.musicService.nameSong());
                mTvNameArtist.setText(MainActivity.musicService.nameArtist());
                mPlayPause.setImageResource(R.drawable.playing);
                currentSongPlaying();
            } else {
                mTvNameSong.setText(MainActivity.musicService.nameSong());
                mTvNameArtist.setText(MainActivity.musicService.nameArtist());
                mPlayPause.setImageResource(R.drawable.pause);
                currentSongPlaying();
            }
        }

    }

    private void currentSongPlaying() {
        final Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mHandler.postDelayed(this, 500);
                MainActivity.musicService.setOnComplete();
                mTvNameSong.setText(MainActivity.musicService.nameSong());
                mTvNameArtist.setText(MainActivity.musicService.nameArtist());
            }
        }, 100);
    }

    private void getData() {
        mSongDatabase = new SongDatabase(this);
        if (mSongList == null) {
            mSongList = new ArrayList<>();
        }
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String tag, nameAlbum;
        tag = bundle.getString(TAG);
        if (tag.equals(TAG_SONG)) {
            mSongList = mSongDatabase.getAllListSong();
        } else if (tag.equals(TAG_DETAIL)) {
            nameAlbum = bundle.getString("Name");
            tagCheck = bundle.getString(TAG_ALBUM);
            if (tagCheck.equals(TAG_ALBUM)) {
                mSongList = mSongDatabase.getAllSongFromAlbum(nameAlbum);
            } else if (tagCheck.equals(TAG_ARTIST)) {
                mSongList = mSongDatabase.getAlLSongFromArtist(nameAlbum);
            }
        }
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.queuePlayPause:
                if (MainActivity.musicService.isPlaying()) {
                    MainActivity.musicService.pausePlayer();
                    mPlayPause.setImageResource(R.drawable.ic_play_arrow_white_48dp);
                    PlayMusicActivity.mImgPlayPause.setImageResource(R.drawable.pause);

                } else {
                    MainActivity.musicService.startPlayer();
                    mPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
                    PlayMusicActivity.mImgPlayPause.setImageResource(R.drawable.playing);
                }
                break;

            case R.id.queueOpenPlayMusic:
                finish();
                break;
        }
    }

    @Override
    public void onNoteListChanged(List<Song> mSong) {
        List<Long> listOfSortedCustomerId = new ArrayList<Long>();

        for (Song song : mSongList) {
            listOfSortedCustomerId.add(Long.valueOf(song.getmId()));
        }

        //convert the List of Longs to a JSON string
        Gson gson = new Gson();
        jsonListSongId = gson.toJson(listOfSortedCustomerId);

    }

    private List<Song> getDataAfterDragAndSwipe() {

        List<Song> mSongListReturn = new ArrayList<Song>();


        //check for null
        if (!jsonListSongId.isEmpty()) {

            //convert JSON array into a List<Long>
            Gson gson = new Gson();
            List<Long> listOfSortedCustomersId = gson.fromJson(jsonListSongId, new TypeToken<List<Long>>() {
            }.getType());

            //build sorted list
            if (listOfSortedCustomersId != null && listOfSortedCustomersId.size() > 0) {
                for (Long id : listOfSortedCustomersId) {
                    for (Song mSong : mSongList) {
                        if (mSong.getmId().equals(id)) {
                            mSongListReturn.add(mSong);
                            mSongList.remove(mSong);
                            break;
                        }
                    }
                }
            }
            if (mSongList.size() > 0) {
                mSongListReturn.addAll(mSongList);
            }
            return mSongListReturn;

            //if there are still customers that were not in the sorted list
            //maybe they were added after the last drag and drop
            //add them to the sorted list
        } else {
            return mSongList;
        }
    }

}
