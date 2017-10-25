package com.example.windows10gamer.beautimusic.view.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.windows10gamer.beautimusic.R;
import com.example.windows10gamer.beautimusic.utilities.LoadData;
import com.example.windows10gamer.beautimusic.model.Song;
import com.example.windows10gamer.beautimusic.utilities.Utils;
import com.example.windows10gamer.beautimusic.view.adapter.SongAdapter;
import com.example.windows10gamer.beautimusic.view.fragment.FragmentMiniControl;

import java.util.ArrayList;
import java.util.List;

import static com.example.windows10gamer.beautimusic.utilities.Utils.ALBUM_ID;
import static com.example.windows10gamer.beautimusic.utilities.Utils.ARTIST_ID;
import static com.example.windows10gamer.beautimusic.utilities.Utils.LIST_SONG;
import static com.example.windows10gamer.beautimusic.utilities.Utils.NAME_ALBUM;
import static com.example.windows10gamer.beautimusic.utilities.Utils.NAME_ARTIST;
import static com.example.windows10gamer.beautimusic.utilities.Utils.NAME_PLAYLIST;
import static com.example.windows10gamer.beautimusic.utilities.Utils.PLAYLIST;
import static com.example.windows10gamer.beautimusic.utilities.Utils.TAG_ALBUM;
import static com.example.windows10gamer.beautimusic.utilities.Utils.TAG_ARTIST;


public class DetailAlbumArtist extends BaseActivity {
    private ImageView mImgAlbum;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private View mLayoutControl;
    private List<Song> mSongList,songs;
    private String mTitle;
    private int mId;
    private RecyclerView mLvSong;
    private SongAdapter mAdapter;
    private LinearLayoutManager mLinearLayout;
    private FragmentMiniControl mFragmentMiniControl;
    public static int CHECK_PLAED_MUSIC = 0;//check=0 chưa phát nhạc,check=1 đã đc phát

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            CHECK_PLAED_MUSIC = 1;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        collapsingToolbarLayout.setTitle(mTitle);

        registerReceiver(receiver, new IntentFilter(Utils.CHECKED_PLAY));
    }

    @Override
    public void initView() {
        mSongList = new ArrayList<>();
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

    @Override
    public void initData() {
        String tag;
        Bundle bundle = getIntent().getExtras();
        tag = bundle.getString(Utils.TAG);

        switch (tag) {
            case TAG_ALBUM:
                mId = bundle.getInt(ALBUM_ID);
                mTitle = bundle.getString(NAME_ALBUM);
                loadAlbumSong(mId);
                break;

            case TAG_ARTIST:
                mId = bundle.getInt(ARTIST_ID);
                mTitle = bundle.getString(NAME_ARTIST);
                loadArtistSong(mId);
                break;

            case PLAYLIST:
                songs = bundle.getParcelableArrayList(LIST_SONG);
                mTitle = bundle.getString(NAME_PLAYLIST);
                mSongList.clear();
                mSongList.addAll(songs);
                mAdapter.notifyDataSetChanged();

                Glide.with(DetailAlbumArtist.this).load(mSongList.get(0).getImageSong())
                        .placeholder(R.drawable.ic_empty_music2).into(mImgAlbum);
                break;

            default:
                break;
        }
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_detail;
    }

    @Override
    public String titleToolbar() {
        return mTitle;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (CHECK_PLAED_MUSIC == 1) {
            mLayoutControl.setVisibility(View.VISIBLE);
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.album_minicontrol, mFragmentMiniControl, FragmentMiniControl.class.getName()).commit();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void loadAlbumSong(final int id) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            public Void doInBackground(Void... params) {
                mSongList.clear();
                mSongList.addAll(LoadData.getAlbumSongs(id, DetailAlbumArtist.this));
                return null;
            }

            @Override
            public void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                mAdapter.notifyDataSetChanged();
                Glide.with(DetailAlbumArtist.this).load(mSongList.get(0).getImageSong())
                        .placeholder(R.drawable.ic_empty_music2).into(mImgAlbum);
            }
        }.execute();
    }

    private void loadArtistSong(final int id) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            public Void doInBackground(Void... params) {
                mSongList.clear();
                mSongList.addAll(LoadData.getArtistSong(id, DetailAlbumArtist.this));
                return null;
            }

            @Override
            public void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                mAdapter.notifyDataSetChanged();
                Glide.with(DetailAlbumArtist.this).load(mSongList.get(0).getImageSong())
                        .placeholder(R.drawable.ic_empty_music2).into(mImgAlbum);
            }
        }.execute();
    }
}