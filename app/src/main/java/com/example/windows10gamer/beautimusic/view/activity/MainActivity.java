package com.example.windows10gamer.beautimusic.view.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.windows10gamer.beautimusic.R;
import com.example.windows10gamer.beautimusic.view.SendDataPosition;
import com.example.windows10gamer.beautimusic.view.fragment.AdapterTab;
import com.example.windows10gamer.beautimusic.view.fragment.MusicPlay;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

public class MainActivity extends AppCompatActivity implements SendDataPosition {
    private static final String POSITION = "POSITION";
    private static final String TAG = "TAG";
    private static final String TAG_SONG = "SONG";
    private ViewPager mViewPager;
    private AdapterTab adapterTab;
    private int mPosition;
    private View mLayout;
    public static SlidingUpPanelLayout slidingUpPanelLayout;
    public static MusicPlay mMusicPlay ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        hideFragmentControl();

    }

    private void hideFragmentControl() {
        mLayout = findViewById(R.id.myFrameLayout);
        mLayout.setVisibility(View.INVISIBLE);
    }

    private void initView() {
        slidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        adapterTab = new AdapterTab(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.setAdapter(adapterTab);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void SendPosition(int positon) {
        mLayout.setVisibility(View.VISIBLE);
        mPosition = positon;
        mMusicPlay = new MusicPlay();
        Bundle args = new Bundle();
        args.putInt(POSITION, mPosition);
        args.putString(TAG, TAG_SONG);
        mMusicPlay.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.myFrameLayout , mMusicPlay).commit();
    }
}
