package com.example.windows10gamer.beautimusic.view.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.windows10gamer.beautimusic.R;
import com.example.windows10gamer.beautimusic.database.SongDatabase;
import com.example.windows10gamer.beautimusic.model.Album;
import com.example.windows10gamer.beautimusic.model.Artist;
import com.example.windows10gamer.beautimusic.model.Song;
import com.example.windows10gamer.beautimusic.view.utilities.Utils;
import com.example.windows10gamer.beautimusic.view.fragment.AdapterTab;
import com.example.windows10gamer.beautimusic.view.utilities.service.MusicService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager mViewPager;
    private AdapterTab adapterTab;
    // layout contain toolbar control play music
    private View mLayoutControl;
    private View mMiniControl;

    private List<Song> mSongList = new ArrayList<>();
    public static MusicService musicService;
    public static TextView mTvNameSong;
    public static TextView mTvNameArtist;
    public static ImageView mImgContrlPlay, mOpenMusicPlay;
    private Intent playIntent;
    private boolean musicBound = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (playIntent == null) {
            playIntent = new Intent(this, MusicService.class);
            startService(playIntent);
            doBindService();
        }
        addPermission();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) setSupportActionBar(toolbar);

        initView();
    }

    // add permission for android >=6.0
    private void addPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(MainActivity.this
                    , Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                addTabFragment();
            } else {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            addTabFragment();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                addTabFragment();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onResume() {

        super.onResume();
        checkPlayMusic();
        if (musicService.mSongList != null) {
            miniControlPlayMusic();
        }
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        if (musicService.mPlayer.isPlaying()) {

        } else {
            musicService.stopForeground(true);
            doUnbindService();
        }

    }

    // update current nameSong,nameArtist of song isplaying
    private void miniControlPlayMusic() {
        if (musicService.mSongList.size() > 0) {

            if (musicService.mPlayer.isPlaying()) {
                mTvNameSong.setText(musicService.nameSong());
                mTvNameArtist.setText(musicService.nameArtist());
                mImgContrlPlay.setImageResource(R.drawable.ic_pause_white_48dp);
                currentSongPlaying();
            } else {
                mTvNameSong.setText(musicService.nameSong());
                mTvNameArtist.setText(musicService.nameArtist());
                mImgContrlPlay.setImageResource(R.drawable.ic_play_arrow_white_48dp);
            }
        }
    }

    // update view when song complete
    private void currentSongPlaying() {
        final Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mHandler.postDelayed(this, 500);
                musicService.setOnComplete();
                mTvNameSong.setText(musicService.nameSong());
                mTvNameArtist.setText(musicService.nameArtist());
            }
        }, 100);
    }

    // check if music is playing display mini control music
    private void checkPlayMusic() {
        if (musicService.mSongList != null) {
            mLayoutControl.setVisibility(View.VISIBLE);
        } else {
            mLayoutControl.setVisibility(View.INVISIBLE);
        }
    }

    private void initView() {
        mLayoutControl = findViewById(R.id.mainLayoutControl);
        mLayoutControl.setVisibility(View.GONE);
        mTvNameSong = (TextView) findViewById(R.id.mainNameSong);
        mTvNameArtist = (TextView) findViewById(R.id.mainNameSingle);
        mImgContrlPlay = (ImageView) findViewById(R.id.mainControlPlay);
        mOpenMusicPlay = (ImageView) findViewById(R.id.mainOpenPlayMusic);
        mMiniControl = findViewById(R.id.mainMiniControl);
        mMiniControl.setOnClickListener(this);
        mImgContrlPlay.setOnClickListener(this);
        mOpenMusicPlay.setOnClickListener(this);
    }

    private void addTabFragment() {
        adapterTab = new AdapterTab(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.setAdapter(adapterTab);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(mViewPager);
    }

    // set connection to service
    private ServiceConnection getMusicConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder musicBinder = (MusicService.MusicBinder) service;
            musicService = musicBinder.getService();
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.mainMiniControl:
                startActivity(new Intent(this, PlayMusicActivity.class));
                break;

            case R.id.mainControlPlay:
                if (musicService.isPlaying()) {
                    musicService.pausePlayer();
                    mImgContrlPlay.setImageResource(R.drawable.ic_play_arrow_white_48dp);
                    musicService.updateRemoteview();
                } else {
                    musicService.startPlayer();
                    mImgContrlPlay.setImageResource(R.drawable.ic_pause_white_48dp);
                    musicService.updateRemoteview();
                }
                break;
        }
    }

    public void doBindService() {
        bindService(playIntent, getMusicConnection, Context.BIND_AUTO_CREATE);
        musicBound = true;
    }

    public void doUnbindService() {
        if (musicBound) {
            unbindService(getMusicConnection);
            musicBound = false;
        }
    }

}
