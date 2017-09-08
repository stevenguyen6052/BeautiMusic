package com.example.windows10gamer.beautimusic;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.windows10gamer.beautimusic.view.adapter.SongAdapter;
import com.example.windows10gamer.beautimusic.database.SongDatabase;
import com.example.windows10gamer.beautimusic.model.Song;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import me.tankery.lib.circularseekbar.CircularSeekBar;

/**
 * Created by Windows 10 Gamer on 23/08/2017.
 */

public class PlayMusic extends Activity implements View.OnClickListener {
    private TextView mTvNameSong, mTvNameSinger, mTvTime;
    private ListView mListView;
    private ImageButton mImgNext, mImgPrevious, mImgPlayPause;
    private CircularSeekBar mSeekbar1;

    private List<Song> mListSong;
    private SongAdapter mSongAdapter;
    private MediaPlayer mMediaPlayer;
    private SongDatabase mSongDatabase;
    private int mPosition;

    private static final String POSITION = "POSITION";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_play_music);
        initView();
        getData();
        processMediaPlayer();
        updateTimeSong();
        listViewOnItemClick();
        seekBarChangeListener();


    }
    private void seekBarChangeListener(){
        mSeekbar1.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, float progress, boolean fromUser) {

            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {
                mMediaPlayer.seekTo((int) mSeekbar1.getProgress());
            }
        });
    }
    private void listViewOnItemClick() {

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.stop();
                    mMediaPlayer.release();
                    processMediaPlayerInListView(position);
                    if (mPosition > position) {
                        mPosition = mPosition - position;
                    } else if (mPosition < position) {
                        mPosition = position + mPosition;
                    }
                } else {
                    mMediaPlayer.stop();
                    mMediaPlayer.release();
                    processMediaPlayerInListView(position);
                    if (mPosition > position) {
                        mPosition = mPosition - position;
                    } else if (mPosition < position) {
                        mPosition = position + mPosition;
                    }
                }
                updateTimeSong();
            }
        });
    }

    /**
     * Update current time song and seekbar
     */
    private void updateTimeSong() {
        final Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat mSimPleDateFormat = new SimpleDateFormat("mm:ss");
                mTvTime.setText(mSimPleDateFormat.format(mMediaPlayer.getCurrentPosition()));
                mSeekbar1.setProgress(mMediaPlayer.getCurrentPosition());
                mHandler.postDelayed(this, 500);
                mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        processNextSong();
                        updateTimeSong();
                        processMediaPlayer();

                    }
                });

            }
        }, 100);
    }

    private void getData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mPosition = bundle.getInt(POSITION);
        mSeekbar1.setMax(mListSong.get(mPosition).getmDuaration());
    }

    private void processMediaPlayerInListView(int positon) {
        mMediaPlayer = MediaPlayer.create(this, Uri.parse(mListSong.get(positon).getmFileSong()));
        mMediaPlayer.start();
        mTvNameSong.setText(mListSong.get(positon).getmNameSong());
        mTvNameSinger.setText(mListSong.get(positon).getmNameArtist());
        mSeekbar1.setMax(mListSong.get(positon).getmDuaration());
    }

    private void initView() {
        mSongDatabase = new SongDatabase(this);
        mListSong = mSongDatabase.getAllListSong();
        mSongAdapter = new SongAdapter(getApplication(), mListSong, R.layout.item_song);
        mListView.setAdapter(mSongAdapter);
        mListSong = new ArrayList<>();
        mSeekbar1 = (CircularSeekBar) findViewById(R.id.pItmSeekbar);
        mListView = (ListView) findViewById(R.id.plistView);
        mTvNameSong = (TextView) findViewById(R.id.pNameSong);
        mTvNameSinger = (TextView) findViewById(R.id.pNameSinger);
        mTvTime = (TextView) findViewById(R.id.pTvTime);
        mImgNext = (ImageButton) findViewById(R.id.pImgNext);
        mImgPrevious = (ImageButton) findViewById(R.id.pImgPrevious);
        mImgPlayPause = (ImageButton) findViewById(R.id.pImgPlayPause);
        mImgNext.setOnClickListener(this);
        mImgPrevious.setOnClickListener(this);
        mImgPlayPause.setOnClickListener(this);
    }

    /**
     * process song from list
     */
    private void processMediaPlayer() {
        mMediaPlayer = MediaPlayer.create(this, Uri.parse(mListSong.get(mPosition).getmFileSong()));
        mMediaPlayer.start();
        mTvNameSong.setText(mListSong.get(mPosition).getmNameSong());
        mTvNameSinger.setText(mListSong.get(mPosition).getmNameArtist());
    }

    private void processMediaPlayerPause() {
        mMediaPlayer = MediaPlayer.create(this, Uri.parse(mListSong.get(mPosition).getmFileSong()));
        mMediaPlayer.pause();
        mTvNameSong.setText(mListSong.get(mPosition).getmNameSong());
        mTvNameSinger.setText(mListSong.get(mPosition).getmNameArtist());
    }

    private void processNextSong() {
        mPosition = mPosition + 1;
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            if (mPosition >= mListSong.size()) {
                mPosition = 0;
                processMediaPlayer();
            } else {
                processMediaPlayer();
            }
        } else {
            if (mPosition >= mListSong.size()) {
                mPosition = 0;
                processMediaPlayerPause();
            } else {
                processMediaPlayerPause();
            }
        }
    }

    private void processPreviousSong() {
        mPosition = mPosition - 1;
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            if (mPosition >= mListSong.size()) {
                mPosition = 0;
                processMediaPlayer();
            } else {
                processMediaPlayer();
            }
        } else {
            if (mPosition < 0) {
                mPosition = mListSong.size() - 1;
                processMediaPlayerPause();
            } else {
                processMediaPlayerPause();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pImgNext:
                processNextSong();
                break;

            case R.id.pImgPrevious:
                processPreviousSong();
                break;

            case R.id.pImgPlayPause:
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                } else {
                    mMediaPlayer.start();
                    mImgPlayPause.setImageResource(R.drawable.playing);
                }
                break;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);
        MenuItem itemSearch = menu.findItem(R.id.itemSearch);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int idItem = item.getItemId();
        switch (idItem) {
            case R.id.itemSearch:

                break;
            case R.id.itemArrange:

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
