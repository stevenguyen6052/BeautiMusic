package com.example.windows10gamer.beautimusic.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.example.windows10gamer.beautimusic.R;
import com.example.windows10gamer.beautimusic.application.App;
import com.example.windows10gamer.beautimusic.database.SongDatabase;
import com.example.windows10gamer.beautimusic.model.Song;
import com.example.windows10gamer.beautimusic.utilities.Utils;
import com.example.windows10gamer.beautimusic.utilities.service.MusicService;
import com.example.windows10gamer.beautimusic.view.adapter.SongAdapter;
import com.example.windows10gamer.beautimusic.view.fragment.FragmentMiniControl;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class DetailAlbumArtist extends AppCompatActivity {
    private ImageView imgAlbum;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    public View mLayoutControl;
    private List<Song> mSongList;
    private String nameAlbumArtist, tag;
    private int idAlbumArtist;
    private RecyclerView lvSongs;
    private SongAdapter songAdapter;
    private LinearLayoutManager linearLayoutManager;
    private FragmentMiniControl mFragmentMiniControl;
    private SharedPreferences sharedPreferences;
    private MusicService service;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        service = ((App) getApplicationContext()).getService();
        getData();

        sharedPreferences = this.getSharedPreferences(Utils.SHARE_PREFERENCE, MODE_PRIVATE);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(nameAlbumArtist);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        collapsingToolbarLayout.setTitle(nameAlbumArtist);

        initView();
        setImageAlbum();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (service.mSongList !=null) {
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

    private void initView() {
        mFragmentMiniControl = new FragmentMiniControl();
        imgAlbum = (ImageView) findViewById(R.id.detailAlbumImg);
        lvSongs = (RecyclerView) findViewById(R.id.detaialbum_listview);

        lvSongs.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        lvSongs.setLayoutManager(linearLayoutManager);
        songAdapter = new SongAdapter(mSongList, this);
        lvSongs.setAdapter(songAdapter);

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
        Picasso.with(this)
                .load(mSongList.get(0).getImageSong())
                .placeholder(R.drawable.ic_empty_music)
                .error(R.drawable.ic_empty_music)
                .into(imgAlbum);
    }

    private void getData() {

        mSongList = new ArrayList<>();
        Bundle b = getIntent().getExtras();
        tag = b.getString(Utils.TAG);

        if (tag.equals(Utils.TAG_ALBUM)) {
            nameAlbumArtist = b.getString(Utils.NAME_ALBUM);
            idAlbumArtist = b.getInt(Utils.ALBUM_ID);
            mSongList = SongDatabase.getAlbumSongs(idAlbumArtist, this);

        } else if (tag.equals(Utils.TAG_ARTIST)) {
            nameAlbumArtist = b.getString(Utils.NAME_ARTIST);
            idAlbumArtist = b.getInt(Utils.ARTIST_ID);
            mSongList = SongDatabase.getArtistSong(idAlbumArtist, this);

        } else if (tag.equals(Utils.PLAYLIST)) {
            nameAlbumArtist = b.getString(Utils.NAME_PLAYLIST);
            mSongList = b.getParcelableArrayList(Utils.LIST_SONG);
        }
    }
}