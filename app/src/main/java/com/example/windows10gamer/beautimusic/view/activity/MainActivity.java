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
import com.example.windows10gamer.beautimusic.view.fragment.MusicPlay;
import com.example.windows10gamer.beautimusic.view.helper.service.MusicService;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity implements SendDataPosition {
    private static final String POSITION = "POSITION";
    private static final String TAG = "TAG";
    private static final String TAG_SONG = "SONG";
    private ViewPager mViewPager;
    private AdapterTab adapterTab;
    public static int mPosition;
    private View mLayout;
    public static SlidingUpPanelLayout slidingUpPanelLayout;
    public static MusicPlay mMusicPlay ;
    public static MusicService musicService;

    //test
    public static TextView mTvNameSong;
    public static TextView mTvNameArtist;
    public static ImageView mImgContrlPlay;

    private Intent playIntent;
    //binding
    private boolean musicBound = false;

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
    protected void onStart() {
        super.onStart();
        if (playIntent == null) {
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, getMusicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null)
            setSupportActionBar(toolbar);
        initView();
        //toolBarControlPlaying();
        //hideFragmentControl();

    }

    @Override
    protected void onResume() {
        super.onResume();
        toolBarControlPlaying();

    }

    private void toolBarControlPlaying(){
        if (musicService.mPlayer != null){
        if ( musicService.mPlayer.isPlaying()){
            mTvNameSong.setText(musicService.nameSong());
            mTvNameArtist.setText(musicService.nameArtist());
            mImgContrlPlay.setImageResource(R.drawable.playing);
            updateTimeSong();
        }
        }

    }
    private void updateTimeSong() {
        final Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat mSimPleDateFormat = new SimpleDateFormat("mm:ss");
                mHandler.postDelayed(this, 500);
                MainActivity.musicService.setOnComplete();
                mTvNameSong.setText(MainActivity.musicService.nameSong());
                mTvNameArtist.setText(MainActivity.musicService.nameArtist());
            }
        }, 100);
    }

//    private void hideFragmentControl() {
//        if (musicService.mPlayer != null && musicService.mPlayer.isPlaying()) mLayout.setVisibility(View.VISIBLE);
//        else mLayout.setVisibility(View.INVISIBLE);
//
//    }

    private void initView() {
        //mLayout = findViewById(R.id.myFrameLayout);
        //slidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        adapterTab = new AdapterTab(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.setAdapter(adapterTab);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(mViewPager);

        //test
        mTvNameSong = (TextView) findViewById(R.id.mainNameSong);
        mTvNameArtist = (TextView) findViewById(R.id.mainNameSingle);
        mImgContrlPlay = (ImageView) findViewById(R.id.mainControlPlay);

    }

    @Override
    public void SendPosition(int positon) {
//        mLayout.setVisibility(View.VISIBLE);
//        mPosition = positon;
//        mMusicPlay = new MusicPlay();
//        MusicPlay.mPosition = positon;
//        Bundle args = new Bundle();
//        args.putInt(POSITION, mPosition);
//        args.putString(TAG, TAG_SONG);
//        mMusicPlay.setArguments(args);
//        getSupportFragmentManager().beginTransaction().replace(R.id.myFrameLayout,mMusicPlay).commit();
        Intent intent = new Intent(this,PlayMusicActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(TAG, TAG_SONG);
        bundle.putInt(POSITION, mPosition);
        intent.putExtras(bundle);
        startActivity(intent);

    }
}
