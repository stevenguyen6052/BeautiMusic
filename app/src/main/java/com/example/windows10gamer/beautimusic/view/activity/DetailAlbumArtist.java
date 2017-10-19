package com.example.windows10gamer.beautimusic.view.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.windows10gamer.beautimusic.R;
import com.example.windows10gamer.beautimusic.application.App;
import com.example.windows10gamer.beautimusic.utilities.LoadData;
import com.example.windows10gamer.beautimusic.utilities.singleton.SharedPrefs;
import com.example.windows10gamer.beautimusic.utilities.singleton.SongDatabase;
import com.example.windows10gamer.beautimusic.model.Song;
import com.example.windows10gamer.beautimusic.utilities.Utils;
import com.example.windows10gamer.beautimusic.utilities.service.MusicService;
import com.example.windows10gamer.beautimusic.view.adapter.SongAdapter;
import com.example.windows10gamer.beautimusic.view.fragment.FragmentMiniControl;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.example.windows10gamer.beautimusic.utilities.Utils.CHECKED_PLAY;


public class DetailAlbumArtist extends AppCompatActivity {
    private ImageView mImgAlbum;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    public View mLayoutControl;
    private List<Song> mSongList = new ArrayList<>();
    private String mTitile;
    private RecyclerView mLvSong;
    private SongAdapter mAdapter;
    private LinearLayoutManager mLinearLayout;
    private FragmentMiniControl mFragmentMiniControl;
    private static int CHECK_PLAED_MUSIC = 0;//check=0 chưa phát nhạc,check=1 đã đc phát
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            CHECK_PLAED_MUSIC = 1;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getData();

        registerReceiver(receiver, new IntentFilter(Utils.CHECKED_PLAY));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(mTitile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        collapsingToolbarLayout.setTitle(mTitile);

        initView();
        setImageAlbum();


    }

    @Override
    protected void onResume() {
        super.onResume();

        if (SharedPrefs.getInstance().get(CHECKED_PLAY, Boolean.class,false)) {
            CHECK_PLAED_MUSIC = 1;
        }

        if (CHECK_PLAED_MUSIC == 1) {
            mLayoutControl.setVisibility(View.VISIBLE);
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.album_minicontrol, mFragmentMiniControl, FragmentMiniControl.class.getName()).commit();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPrefs.getInstance().remove(CHECKED_PLAY);

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initView() {

        mFragmentMiniControl = new FragmentMiniControl();
        mImgAlbum = (ImageView) findViewById(R.id.detailAlbumImg);
        mLvSong = (RecyclerView) findViewById(R.id.detaialbum_listview);

        mLvSong.setHasFixedSize(true);
        mLinearLayout = new LinearLayoutManager(this);
        mLvSong.setLayoutManager(mLinearLayout);
        mAdapter = new SongAdapter(mSongList, this);
        mLvSong.setAdapter(mAdapter);

        mLayoutControl = findViewById(R.id.album_minicontrol);
        mLayoutControl.setVisibility(View.GONE);
        mLayoutControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DetailAlbumArtist.this, PlayMusicActivity.class));
            }
        });
    }

    private void setImageAlbum() {
        Glide.with(this).load(mSongList.get(0).getImageSong()).placeholder(R.drawable.ic_empty_music).into(mImgAlbum);
    }

    private void getData() {
        String tag;
        int id;
        Bundle b = getIntent().getExtras();
        tag = b.getString(Utils.TAG);

        if (tag.equals(Utils.TAG_ALBUM)) {
            id = b.getInt(Utils.ALBUM_ID);
            mTitile = b.getString(Utils.NAME_ALBUM);
            mSongList = LoadData.getAlbumSongs(id, this);

        } else if (tag.equals(Utils.TAG_ARTIST)) {
            id = b.getInt(Utils.ARTIST_ID);
            mTitile = b.getString(Utils.NAME_ARTIST);
            mSongList = LoadData.getArtistSong(id, this);

        } else if (tag.equals(Utils.PLAYLIST)) {
            mTitile = b.getString(Utils.NAME_PLAYLIST);
            mSongList = b.getParcelableArrayList(Utils.LIST_SONG);
        }
    }
}