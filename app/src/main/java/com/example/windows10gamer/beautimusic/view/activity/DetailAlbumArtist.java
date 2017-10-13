package com.example.windows10gamer.beautimusic.view.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.windows10gamer.beautimusic.R;
import com.example.windows10gamer.beautimusic.database.SongDatabase;
import com.example.windows10gamer.beautimusic.model.Song;
import com.example.windows10gamer.beautimusic.utilities.Utils;
import com.example.windows10gamer.beautimusic.view.adapter.RecyclerItemClickListener;
import com.example.windows10gamer.beautimusic.view.adapter.SongAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class DetailAlbumArtist extends AppCompatActivity implements View.OnClickListener {
    private View mLayout;
    private ImageView imgAlbum;
    private CircleImageView imgSong;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private TextView mTvNameSong, mTvNameArtist;
    private ImageView mControlPlayPause;
    public View mLayoutControl;
    private List<Song> mSongList;
    private String nameAlbumArtist, tag;
    private int idAlbumArtist;
    private RecyclerView lvSongs;
    private SongAdapter songAdapter;
    private LinearLayoutManager linearLayoutManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getData();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(nameAlbumArtist);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        collapsingToolbarLayout.setTitle(nameAlbumArtist);
        initView();
        setImageAlbum();

        IntentFilter it = new IntentFilter();
        it.addAction(Utils.PAUSE_KEY);
        it.addAction(Utils.PLAY_KEY);
        registerReceiver(receiver, it);

    }

    @Override
    protected void onResume() {
        super.onResume();


        if (HomeActivity.musicService.mPlayer != null) {
            checkPlayMusic();
            miniControlPlayMusic();

        }
    }

    // update name song,name artist for mini control play music
    private void miniControlPlayMusic() {
        if (HomeActivity.musicService.getSongList() != null) {

            if (HomeActivity.musicService.isPlaying()) {
                mControlPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
            } else {
                mControlPlayPause.setImageResource(R.drawable.ic_play_arrow_white_48dp);
            }
            mTvNameSong.setText(HomeActivity.musicService.nameSong());
            mTvNameArtist.setText(HomeActivity.musicService.nameArtist());
            Picasso.with(this)
                    .load(HomeActivity.musicService.getImageSong())
                    .placeholder(R.drawable.icon_music)
                    .error(R.drawable.icon_music)
                    .into(imgSong);
            currentSongPlaying();
        }

    }

    private void checkPlayMusic() {
        if (HomeActivity.musicService.getSongList() != null)
            mLayoutControl.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        //finish();
        return true;
    }

    // update current song is playing and listener player oncomplete
    private void currentSongPlaying() {
        final Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mHandler.postDelayed(this, 500);
                HomeActivity.musicService.setOnComplete();
                mTvNameSong.setText(HomeActivity.musicService.nameSong());
                mTvNameArtist.setText(HomeActivity.musicService.nameArtist());
            }
        }, 100);
    }

    private void initView() {
        mLayout = findViewById(R.id.album_openplaymusic);
        mTvNameSong = (TextView) findViewById(R.id.albumNameSong);
        mTvNameArtist = (TextView) findViewById(R.id.albumNameArtist);
        mControlPlayPause = (ImageView) findViewById(R.id.albumControlPlayPause);
        imgAlbum = (ImageView) findViewById(R.id.detailAlbumImg);
        imgSong = (CircleImageView) findViewById(R.id.imgSong);
        mLayoutControl = findViewById(R.id.album_minicontrol);
        mLayoutControl.setVisibility(View.GONE);
        lvSongs = (RecyclerView) findViewById(R.id.detaialbum_listview);

        lvSongs.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        lvSongs.setLayoutManager(linearLayoutManager);
        songAdapter = new SongAdapter(mSongList, this);
        lvSongs.setAdapter(songAdapter);

        mLayout.setOnClickListener(this);
        mControlPlayPause.setOnClickListener(this);
        //addItemClick();
    }

    private void addItemClick() {
        lvSongs.addOnItemTouchListener(new RecyclerItemClickListener(this, lvSongs,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        startActivity(new Intent(DetailAlbumArtist.this, PlayMusicActivity.class)
                                .putParcelableArrayListExtra(Utils.LIST_SONG, (ArrayList<Song>) mSongList)
                                .putExtra(Utils.POSITION, position));
                    }

                    @Override
                    public void onItemLongClick(View view, final int position) {
                    }
                }));
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.album_openplaymusic:
                startActivity(new Intent(this, PlayMusicActivity.class));
                break;
            case R.id.albumControlPlayPause:

                if (HomeActivity.musicService.isPlaying()) {
                    mControlPlayPause.setImageResource(R.drawable.ic_play_arrow_white_48dp);
                    sendBroadcast(new Intent().setAction(Utils.PAUSE_KEY));
                    sendBroadcast(new Intent().setAction(Utils.NOTIFI));
                } else {
                    mControlPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
                    sendBroadcast(new Intent().setAction(Utils.PLAY_KEY));
                    sendBroadcast(new Intent().setAction(Utils.NOTIFI));
                }
                break;
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Utils.PAUSE_KEY)) {
                mControlPlayPause.setImageResource(R.drawable.ic_play_arrow_white_48dp);
            } else if (intent.getAction().equals(Utils.PLAY_KEY)) {
                mControlPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
            }

        }
    };

}