package com.example.windows10gamer.beautimusic.view.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.windows10gamer.beautimusic.R;
import com.example.windows10gamer.beautimusic.model.Song;
import com.example.windows10gamer.beautimusic.view.fragment.AdapterTab;
import com.example.windows10gamer.beautimusic.utilities.Utils;
import com.example.windows10gamer.beautimusic.utilities.service.MusicService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager mViewPager;
    private AdapterTab adapterTab;
    private View mLayoutControl;
    private View mMiniControl;
    public static MusicService musicService;
    private TextView mTvNameSong;
    private TextView mTvNameArtist;
    public static ImageView mImgPlayPause;
    private CircleImageView mImgSong;
    private Intent playIntent;
    private boolean musicBound = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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

    private void addPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(HomeActivity.this
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
        if (musicService.mSongList != null) {
            changeLayout();
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
                mImgPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
                currentSongPlaying();
            } else {
                mTvNameSong.setText(musicService.nameSong());
                mTvNameArtist.setText(musicService.nameArtist());
                mImgPlayPause.setImageResource(R.drawable.ic_play_arrow_white_48dp);
            }
            Picasso.with(this)
                    .load(musicService.getImageSong())
                    .placeholder(R.drawable.dianhac)
                    .error(R.drawable.dianhac)
                    .into(mImgSong);
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

    private void initView() {
        mLayoutControl = findViewById(R.id.mainLayoutControl);
        mTvNameSong = (TextView) findViewById(R.id.mainNameSong);
        mTvNameArtist = (TextView) findViewById(R.id.mainNameSingle);
        mImgPlayPause = (ImageView) findViewById(R.id.mainControlPlay);
        mImgSong = (CircleImageView) findViewById(R.id.mainImgSong);
        mMiniControl = findViewById(R.id.mainMiniControl);
        mMiniControl.setOnClickListener(this);
        mImgPlayPause.setOnClickListener(this);

    }

    private void addTabFragment() {
        adapterTab = new AdapterTab(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.setAdapter(adapterTab);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_music_note_white_48dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_album_white_48dp1);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_person_white_48dp1);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_favorite_white_48dp);
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
                    mImgPlayPause.setImageResource(R.drawable.ic_play_arrow_white_48dp);
                    musicService.updateRemoteview();
                } else {
                    musicService.startPlayer();
                    mImgPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
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

    private void changeLayout() {
        if (Utils.getCurrentScreen(this).equals(Utils.HDPI)) {
            mLayoutControl.getLayoutParams().height = 90;
            mLayoutControl.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        } else if (Utils.getCurrentScreen(this).equals(Utils.XHDPI)) {
            mLayoutControl.getLayoutParams().height = 120;
            mLayoutControl.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        } else if (Utils.getCurrentScreen(this).equals(Utils.XXHDPI)) {
            mLayoutControl.getLayoutParams().height = 180;
            mLayoutControl.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        }
    }
}
