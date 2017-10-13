package com.example.windows10gamer.beautimusic.view.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.example.windows10gamer.beautimusic.view.fragment.FragmentMiniControl;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity  {

    private ViewPager mViewPager;
    private AdapterTab adapterTab;
    private View mLayoutControl;
    public static MusicService musicService;
    private Intent playIntent;
    private boolean musicBound = false;
    private FragmentMiniControl mFragmentMiniControl;


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
            mLayoutControl.setVisibility(View.VISIBLE);
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.mainLayoutControl, mFragmentMiniControl, FragmentMiniControl.class.getName()).commit();

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

    private void initView() {
        mFragmentMiniControl = new FragmentMiniControl();
        mLayoutControl = findViewById(R.id.mainLayoutControl);
        mLayoutControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,PlayMusicActivity.class));
            }
        });
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