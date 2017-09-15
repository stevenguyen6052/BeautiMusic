package com.example.windows10gamer.beautimusic.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.windows10gamer.beautimusic.R;
import com.example.windows10gamer.beautimusic.database.SongDatabase;
import com.example.windows10gamer.beautimusic.model.Song;
import com.example.windows10gamer.beautimusic.view.adapter.SongAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import me.tankery.lib.circularseekbar.CircularSeekBar;

public class PlayMusicActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String POSITION = "POSITION";
    private static final String NAME_ALBUM = "Name Album";
    private static final String NAME_ARTIST = "Name Artist";
    private final static String TAG = "TAG";
    private final static String TAG_SONG = "SONG";
    private final static String TAG_ARTIST = "ARTIST";
    private final static String TAG_ALBUM = "ALBUM";
    private final static String LIST = "List";
    private final static int REQUEST_LIST = 1;

    public static final String NOTIFY_PREVIOUS = "com.example.windows10gamer.beautimusic.previous";
    public static final String NOTIFY_PLAY = "com.example.windows10gamer.beautimusic.play";
    public static final String NOTIFY_NEXT = "com.example.windows10gamer.beautimusic.next";
    private String tag, nameAlbum, nameArtist;

    public static TextView mTvNameSong, mTvNameSinger, mTvTime;
    private ListView mListView;
    private ImageView mImgBackground, mImgNext, mImgPrevious, mShffle, mReppeat, mImgQueue;
    public static ImageView mControlPlayPause, mImgPlayPause;
    private CircularSeekBar mSeekbar1;
    private android.media.MediaMetadataRetriever mmr = new MediaMetadataRetriever();

    private SongAdapter mSongAdapter;
    private SongDatabase mSongDatabase;
    public static int mPosition;


    public static List<Song> mSongList;
    private ListView songView;

    //activity and playback pause flags
    private boolean paused = false, playbackPaused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_play_frag2);
        initView();
        addEvents();
        getData();
        listViewClick();

    }

    private void listViewClick() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                playMusicItem(position);
            }
        });
    }

    //play music in item click
    private void playMusicItem(int position) {
        if (MainActivity.musicService.mPosition < position)
            MainActivity.musicService.mPosition += position;
        else MainActivity.musicService.mPosition = position;
        playMusic();

    }

    //get agurment from activity
    private void getData() {
//        if (MainActivity.musicService.mPlayer == null){

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            tag = bundle.getString(TAG);
            if (tag.equals(TAG_ALBUM)) {

                mPosition = bundle.getInt(POSITION);
                nameAlbum = bundle.getString(NAME_ALBUM);
                mSongDatabase = new SongDatabase(this);
                mSongList = mSongDatabase.getAllSongFromAlbum(nameAlbum);

            } else if (tag.equals(TAG_SONG)) {

                mSongDatabase = new SongDatabase(this);
                mSongList = mSongDatabase.getAllListSong();
                mPosition = bundle.getInt(POSITION);

            } else if (tag.equals(TAG_ARTIST)) {

                mPosition = bundle.getInt(POSITION);
                nameArtist = bundle.getString(NAME_ARTIST);
                mSongDatabase = new SongDatabase(this);
                mSongList = mSongDatabase.getAlLSongFromArtist(nameArtist);
            }
            mSongAdapter = new SongAdapter(this, mSongList, R.layout.item_song);
            mListView.setAdapter(mSongAdapter);
            setDataSource();
            playMusic();

        }

//        }else if (MainActivity.musicService.mPlayer.isPlaying()){
//            mTvNameSong.setText(MainActivity.musicService.nameSong());
//            mTvNameSinger.setText(MainActivity.musicService.nameArtist());
//            mImgPlayPause.setImageResource(R.drawable.playing);
//            mControlPlayPause.setImageResource(R.drawable.playing);
//            mSeekbar1.setMax(MainActivity.musicService.getDuaration());
//            setImageSong();
//            updateTimeSong();
//
//        }

        listenerSeekbarChange();
    }


    // set datasource for service
    private void setDataSource() {
        MainActivity.musicService.mSongList = mSongList;
        MainActivity.musicService.mPosition = mPosition;
    }


    private void playMusic() {
        mTvNameSong.setText(MainActivity.musicService.nameSong());
        mTvNameSinger.setText(MainActivity.musicService.nameArtist());
        mImgPlayPause.setImageResource(R.drawable.playing);
        mControlPlayPause.setImageResource(R.drawable.playing);
        mSeekbar1.setMax(MainActivity.musicService.getDuaration());
        setImageSong();
        updateTimeSong();
        MainActivity.musicService.playSong();

    }

    // seekbar change listener
    private void listenerSeekbarChange() {
        mSeekbar1.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {

            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, float progress, boolean fromUser) {
            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {
                MainActivity.musicService.seek((int) mSeekbar1.getProgress());
            }
        });

    }

    private void addEvents() {
        mControlPlayPause.setOnClickListener(this);
        mImgNext.setOnClickListener(this);
        mImgPrevious.setOnClickListener(this);
        mImgPlayPause.setOnClickListener(this);
        mShffle.setOnClickListener(this);
        mReppeat.setOnClickListener(this);
        mImgQueue.setOnClickListener(this);
    }

    private void initView() {
        mSongList = new ArrayList<>();
        mSeekbar1 = (CircularSeekBar) findViewById(R.id.pItmSeekbar);
        mListView = (ListView) findViewById(R.id.plistView);
        mTvNameSong = (TextView) findViewById(R.id.itmNameSong);
        mTvNameSinger = (TextView) findViewById(R.id.itmNameSingle);
        mTvTime = (TextView) findViewById(R.id.pTvTime);
        mImgNext = (ImageView) findViewById(R.id.pImgNext);
        mImgPrevious = (ImageView) findViewById(R.id.pImgPrevious);
        mImgPlayPause = (ImageView) findViewById(R.id.pImgPlayPause);
        mShffle = (ImageView) findViewById(R.id.pShuffle);
        mReppeat = (ImageView) findViewById(R.id.pRepeat);
        mControlPlayPause = (ImageView) findViewById(R.id.itmControlPlayPause);
        mImgBackground = (ImageView) findViewById(R.id.background_playmusic);
        mImgQueue = (ImageView) findViewById(R.id.pImgQueue);
    }

    private void nextSong() {
        MainActivity.musicService.playNext();
        setImageSong();
        mTvNameSong.setText(MainActivity.musicService.nameSong());
        mTvNameSinger.setText(MainActivity.musicService.nameArtist());
    }

    private void previousSong() {
        MainActivity.musicService.playPrev();
        setImageSong();
        mTvNameSong.setText(MainActivity.musicService.nameSong());
        mTvNameSinger.setText(MainActivity.musicService.nameArtist());
    }

    private void doPlayPause() {
        if (MainActivity.musicService.isPlaying()) {
            MainActivity.musicService.pausePlayer();
            mImgPlayPause.setImageResource(R.drawable.pause);
            mControlPlayPause.setImageResource(R.drawable.pause);
        } else {
            MainActivity.musicService.startPlayer();
            mImgPlayPause.setImageResource(R.drawable.playing);
            mControlPlayPause.setImageResource(R.drawable.playing);
        }
    }

    // update current time and seekbar of song
    private void updateTimeSong() {
        final Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat mSimPleDateFormat = new SimpleDateFormat("mm:ss");
                mTvTime.setText(mSimPleDateFormat.format(MainActivity.musicService.getCurrentPosition()));
                mSeekbar1.setProgress(MainActivity.musicService.getCurrentPosition());
                mHandler.postDelayed(this, 500);
                MainActivity.musicService.setOnComplete();
                mTvNameSong.setText(MainActivity.musicService.nameSong());
                mTvNameSinger.setText(MainActivity.musicService.nameArtist());
            }
        }, 100);
    }

    private void doShuffle() {
        if (MainActivity.musicService.setShuffle() == true)
            mShffle.setImageResource(R.drawable.ic_shuffle_black_48dp);
        else mShffle.setImageResource(R.drawable.ic_shuffle_white_48dp);

    }

    private void doRepeat() {
        if (MainActivity.musicService.setRepeat() == true)
            mReppeat.setImageResource(R.drawable.ic_repeat_black_48dp);
        else mReppeat.setImageResource(R.drawable.ic_repeat_white_48dp);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pImgNext:
                nextSong();
                break;

            case R.id.pImgPrevious:
                previousSong();
                break;

            case R.id.pImgPlayPause:
                doPlayPause();
                break;
            case R.id.pShuffle:
                doShuffle();
                break;

            case R.id.pRepeat:
                doRepeat();

                break;
            case R.id.itmControlPlayPause:
                doControlPlay();

                break;
            case R.id.pImgQueue:
                doQueue();

                break;
        }
    }

    // queue song playing
    private void doQueue() {
        Intent intent = new Intent(this, PlayingQueue.class);
        Bundle bundle = new Bundle();
        if (tag.equals(TAG_SONG)) {

            bundle.putString(TAG, TAG_SONG);
        } else if (tag.equals(TAG_ARTIST)) {

            bundle.putString(TAG, TAG_ARTIST);
            bundle.putString(NAME_ARTIST, nameArtist);
        } else if (tag.equals(TAG_ALBUM)) {

            bundle.putString(TAG, TAG_ALBUM);
            bundle.putString(NAME_ALBUM, nameAlbum);
        }
        intent.putExtras(bundle);
        startActivityForResult(intent, REQUEST_LIST);
    }

    // set image of song
    private void setImageSong() {
        mmr.setDataSource(MainActivity.musicService.pathSong());
        byte[] dataImageDisc = mmr.getEmbeddedPicture();
        if (dataImageDisc != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(dataImageDisc, 0, dataImageDisc.length);
            mImgBackground.setImageBitmap(bitmap);
        } else {
            mImgBackground.setImageResource(R.drawable.detaialbum);
        }
    }


    private void doControlPlay() {
        if (MainActivity.musicService.mPlayer.isPlaying()) {
            MainActivity.musicService.pausePlayer();
            mImgPlayPause.setImageResource(R.drawable.pause);
            mControlPlayPause.setImageResource(R.drawable.pause);
        } else {
            MainActivity.musicService.mPlayer.start();
            mImgPlayPause.setImageResource(R.drawable.playing);
            mControlPlayPause.setImageResource(R.drawable.playing);
        }
    }
}
