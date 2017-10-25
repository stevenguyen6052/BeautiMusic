package com.example.windows10gamer.beautimusic.view.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import com.example.windows10gamer.beautimusic.R;
import com.example.windows10gamer.beautimusic.application.App;
import com.example.windows10gamer.beautimusic.utilities.singleton.SharedPrefs;
import com.example.windows10gamer.beautimusic.view.fragment.AdapterTab;
import com.example.windows10gamer.beautimusic.utilities.Utils;
import com.example.windows10gamer.beautimusic.view.fragment.FragmentMiniControl;
import static com.example.windows10gamer.beautimusic.utilities.Utils.HOME;

public class HomeActivity extends BaseActivity {
    private ViewPager mViewPager;
    private AdapterTab mTab;
    private View mLayoutControl;
    private FragmentMiniControl mFragmentMiniControl;
    private static int CHECK_PLAYED_MUSIC = 0; //check=0 un play, check=1 played

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            DetailAlbumArtist.CHECK_PLAED_MUSIC = 1;
            mLayoutControl.setVisibility(View.VISIBLE);
            CHECK_PLAYED_MUSIC = 1;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPermission();
        registerReceiver(receiver, new IntentFilter(Utils.CHECKED_PLAY));
    }

    @Override
    public void initView() {
        mFragmentMiniControl = new FragmentMiniControl();
        mLayoutControl = findViewById(R.id.mainLayoutControl);
        mLayoutControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, PlayMusicActivity.class));
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_home;
    }

    @Override
    public String titleToolbar() {
        return HOME;
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

        if (CHECK_PLAYED_MUSIC == 1) {
            mLayoutControl.setVisibility(View.VISIBLE);
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.mainLayoutControl, mFragmentMiniControl, FragmentMiniControl.class.getName()).commit();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!SharedPrefs.getInstance().get(Utils.STATUS_PLAY, Boolean.class, false))
            ((App) getApplicationContext()).getService().stopForeground(true);

        unregisterReceiver(receiver);
    }

    private void addTabFragment() {
        mTab = new AdapterTab(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.setAdapter(mTab);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_music_note_white_48dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_album_white_48dp1);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_person_white_48dp1);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_favorite_white_48dp);
    }
}