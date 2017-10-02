package com.example.windows10gamer.beautimusic.view.activity;

import android.app.Activity;
import android.content.Intent;
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
import com.example.windows10gamer.beautimusic.view.utilities.Utils;
import com.example.windows10gamer.beautimusic.view.utilities.dragandswipe.ListChangedListener;
import com.example.windows10gamer.beautimusic.view.utilities.dragandswipe.QueueAdapter;
import com.example.windows10gamer.beautimusic.view.utilities.dragandswipe.SimpleItemTouchHelperCallback;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class PlayingQueue extends AppCompatActivity implements QueueAdapter.OnStartDragListener, View.OnClickListener, ListChangedListener {

    private ItemTouchHelper mItemTouchHelper;
    public List<Song> mSongList;
    public int mPostion;
    public String check = "";
    private View mLayoutOpenPlayMusic;
    private TextView mTvNameSong, mTvNameArtist;
    public static ImageView mPlayPause;
    private List<Song> getListSong;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing_queue);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null)
            setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initView();
        mSongList = getIntent().getExtras().getParcelableArrayList(Utils.LIST_SONG);
        setUpAdapter();

    }

    @Override
    protected void onResume() {
        super.onResume();
        miniControlPlay();
    }

    private void initView() {
        mSongList = new ArrayList<>();
        mTvNameSong = (TextView) findViewById(R.id.queueNameSong);
        mTvNameArtist = (TextView) findViewById(R.id.queueNameArtist);
        mPlayPause = (ImageView) findViewById(R.id.queuePlayPause);
        mLayoutOpenPlayMusic = findViewById(R.id.queueOpenPlayMusic);
        mPlayPause.setOnClickListener(this);
        mLayoutOpenPlayMusic.setOnClickListener(this);
    }

    public void dataResult() {
        Intent intent = new Intent();
        Bundle b = new Bundle();
        boolean isCheckChange = false;
        // nếu đã sx thì trả về list sau khi sx còn k thì trả về list ban đầu
        // string check để kiểm tra xem click vào item song or back playmusicactivity
        if (getListSong != null) {
            isCheckChange = true;
            //sendListSong = getDataAfterDragAndSwipe();
            b.putParcelableArrayList(Utils.LIST_SONG, (ArrayList<Song>) getListSong);
            b.putInt(Utils.POSITION, mPostion);
            b.putBoolean(Utils.TRUE, isCheckChange);
            b.putString(Utils.CHECK, check);
        } else {

            isCheckChange = false;
            b.putInt(Utils.POSITION, mPostion);
            b.putBoolean(Utils.TRUE, isCheckChange);
            b.putString(Utils.CHECK, check);

        }
        intent.putExtras(b);
        setResult(Activity.RESULT_OK, intent);
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
        if (MainActivity.musicService.mPlayer.isPlaying()) {
            mTvNameSong.setText(MainActivity.musicService.nameSong());
            mTvNameArtist.setText(MainActivity.musicService.nameArtist());
            mPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
            currentSongPlaying();
        } else {
            mTvNameSong.setText(MainActivity.musicService.nameSong());
            mTvNameArtist.setText(MainActivity.musicService.nameArtist());
            mPlayPause.setImageResource(R.drawable.ic_play_arrow_white_48dp);
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
                    PlayMusicActivity.mImgPlayPause.setImageResource(R.drawable.ic_play_arrow_white_48dp);
                    MainActivity.musicService.updateRemoteview();

                } else {
                    MainActivity.musicService.startPlayer();
                    mPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
                    PlayMusicActivity.mImgPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
                    MainActivity.musicService.updateRemoteview();
                }
                break;

            case R.id.queueOpenPlayMusic:
                dataResult();
                finish();
                break;
        }
    }

    @Override
    public void onNoteListChanged(List<Song> mSongs) {
        if (mSongs != null) {
            getListSong = mSongs;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        dataResult();
        finish();
        return true;
    }

}
