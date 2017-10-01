package com.example.windows10gamer.beautimusic.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.windows10gamer.beautimusic.R;
import com.example.windows10gamer.beautimusic.view.adapter.SongAdapter;
import com.example.windows10gamer.beautimusic.database.SongDatabase;
import com.example.windows10gamer.beautimusic.model.Song;
import com.example.windows10gamer.beautimusic.view.utilities.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class DetailAlbumArtist extends AppCompatActivity implements View.OnClickListener {
    private SongAdapter mSongAdapter;
    private ListView mListView;
    private View mLayout;
    private ImageView imgAlbum;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private TextView mTvNameSong, mTvNameArtist;
    public static ImageView mControlPlayPause;
    public View mLayoutControl;
    private List<Song> mSongList;
    private String nameAlbumArtist, tag;
    private int idAlbumArtist;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_album);
        getData();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(nameAlbumArtist);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        collapsingToolbarLayout.setTitle(nameAlbumArtist);
        initView();
        onItemClick();
        setImageSong();

    }

    @Override
    protected void onResume() {
        super.onResume();

        checkPlayMusic();

        if (MainActivity.musicService.mPlayer != null) {
            miniControlPlayMusic();

        } else {

        }
    }

    // update name song,name artist for mini control play music
    private void miniControlPlayMusic() {
        if (MainActivity.musicService.mSongList != null) {

            if (MainActivity.musicService.mPlayer.isPlaying()) {
                mTvNameSong.setText(MainActivity.musicService.nameSong());
                mTvNameArtist.setText(MainActivity.musicService.nameArtist());
                mControlPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
                currentSongPlaying();

            } else {
                mTvNameSong.setText(MainActivity.musicService.nameSong());
                mTvNameArtist.setText(MainActivity.musicService.nameArtist());
                mControlPlayPause.setImageResource(R.drawable.ic_play_arrow_white_48dp);
                currentSongPlaying();
            }
        }

    }

    private void checkPlayMusic() {
        if (MainActivity.musicService.mSongList != null) {
            mLayoutControl.setVisibility(View.VISIBLE);
        } else {
            mLayoutControl.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    // update current song is playing and listener player oncomplete
    private void currentSongPlaying() {
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

    // list song item click
    private void onItemClick() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(DetailAlbumArtist.this, PlayMusicActivity.class);
                Bundle b = new Bundle();
                b.putParcelableArrayList(Utils.LIST_SONG, (ArrayList<Song>) mSongList);
                b.putInt(Utils.POSITION, position);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        mLayout = findViewById(R.id.album_openplaymusic);
        mTvNameSong = (TextView) findViewById(R.id.albumNameSong);
        mTvNameArtist = (TextView) findViewById(R.id.albumNameArtist);
        mControlPlayPause = (ImageView) findViewById(R.id.albumControlPlayPause);
        imgAlbum = (ImageView) findViewById(R.id.detailAlbumImg);
        mListView = (ListView) findViewById(R.id.detaialbum_listview);
        mLayoutControl = findViewById(R.id.album_minicontrol);
        mLayoutControl.setVisibility(View.GONE);
        mSongAdapter = new SongAdapter(DetailAlbumArtist.this, mSongList, R.layout.item_song);
        mListView.setAdapter(mSongAdapter);
        mLayout.setOnClickListener(this);
        mControlPlayPause.setOnClickListener(this);
    }

    // set image of album
    private void setImageSong() {
        Picasso.with(this)
                .load(mSongList.get(0).getImageSong())
                .placeholder(R.drawable.detaialbum)
                .error(R.drawable.detaialbum)
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
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.album_openplaymusic:
                startActivity(new Intent(this, PlayMusicActivity.class));
                break;
            case R.id.albumControlPlayPause:

                if (MainActivity.musicService.isPlaying()) {
                    MainActivity.musicService.pausePlayer();
                    mControlPlayPause.setImageResource(R.drawable.ic_play_arrow_white_48dp);
                    MainActivity.mImgContrlPlay.setImageResource(R.drawable.ic_play_arrow_white_48dp);
                    MainActivity.musicService.updateRemoteview();
                } else {
                    MainActivity.musicService.startPlayer();
                    mControlPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
                    MainActivity.mImgContrlPlay.setImageResource(R.drawable.ic_pause_white_48dp);
                    MainActivity.musicService.updateRemoteview();
                }
                break;
        }
    }
}
