package com.example.windows10gamer.beautimusic.view.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.windows10gamer.beautimusic.R;
import com.example.windows10gamer.beautimusic.view.SendDataPosition;
import com.example.windows10gamer.beautimusic.view.fragment.AdapterTab;
import com.example.windows10gamer.beautimusic.view.helper.service.MusicService;

public class MainActivity extends AppCompatActivity implements SendDataPosition,View.OnClickListener {
    // tag of current song
    private static final String POSITION = "POSITION";
    private int mPosition;

    private static final String TAG = "TAG";
    private static final String TAG_SONG = "SONG";

    private ViewPager mViewPager;
    private AdapterTab adapterTab;

    // layout contain toolbar control play music
    private View mLayoutControl;

    // service
    public static MusicService musicService;

    public static TextView mTvNameSong;
    public static TextView mTvNameArtist;
    public static ImageView mImgContrlPlay,mOpenMusicPlay;

    private Intent playIntent;

    private boolean musicBound = false;

    @Override
    protected void onStart() {
        super.onStart();
        // bind service
        if (playIntent == null) {
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, getMusicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
        checkPlayMusic();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null)
            setSupportActionBar(toolbar);
        initView();

    }



    @Override
    protected void onResume() {
        super.onResume();
        toolBarControlPlaying();
    }

    // update current nameSong,nameArtist of song isplaying
    private void toolBarControlPlaying(){
        if (musicService.mPlayer != null){
        if ( musicService.mPlayer.isPlaying()){
            mTvNameSong.setText(musicService.nameSong());
            mTvNameArtist.setText(musicService.nameArtist());
            mImgContrlPlay.setImageResource(R.drawable.playing);
            updateToolbarControl();
        }
        }
    }

    // update view when song complete
    private void updateToolbarControl() {
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

    // check if music is playing display toolbar mini control music
    private void checkPlayMusic(){
//        if (musicService.mPlayer == null){
//            mLayoutControl.setVisibility(View.INVISIBLE);
//        }else {
//            mLayoutControl.setVisibility(View.VISIBLE);
//        }
        if (musicService.mPlayer != null){
            mLayoutControl.setVisibility(View.VISIBLE);
        }
    }


    private void initView() {

        adapterTab = new AdapterTab(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.setAdapter(adapterTab);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(mViewPager);

        mLayoutControl = findViewById(R.id.mainLayoutControl);
        mLayoutControl.setVisibility(View.GONE);
        mTvNameSong = (TextView) findViewById(R.id.mainNameSong);
        mTvNameArtist = (TextView) findViewById(R.id.mainNameSingle);
        mImgContrlPlay = (ImageView) findViewById(R.id.mainControlPlay);
        mOpenMusicPlay = (ImageView) findViewById(R.id.mainOpenPlayMusic);
        mImgContrlPlay.setOnClickListener(this);
        mOpenMusicPlay.setOnClickListener(this);

    }

    // when click item in song fragment send index of item to activity and activity send to playmusicactivity
    @Override
    public void SendPosition(int positon) {
        mPosition = positon;
        Intent intent = new Intent(this,PlayMusicActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(TAG, TAG_SONG);
        bundle.putInt(POSITION, mPosition);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(getMusicConnection);

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
        switch (v.getId()){
            case R.id.mainOpenPlayMusic:
                Intent intent = new Intent(this,PlayMusicActivity.class);
                startActivity(intent);
                break;
            case R.id.mainControlPlay:
                if (musicService.isPlaying()){
                    musicService.pausePlayer();
                    mImgContrlPlay.setImageResource(R.drawable.pause);
                }else {
                    mImgContrlPlay.setImageResource(R.drawable.playing);
                }
                break;
        }
    }
}
