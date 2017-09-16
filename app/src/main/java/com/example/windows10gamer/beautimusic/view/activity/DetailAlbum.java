package com.example.windows10gamer.beautimusic.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.windows10gamer.beautimusic.R;
import com.example.windows10gamer.beautimusic.view.adapter.SongAdapter;
import com.example.windows10gamer.beautimusic.database.SongDatabase;
import com.example.windows10gamer.beautimusic.model.Song;

import java.util.ArrayList;
import java.util.List;



public class DetailAlbum extends AppCompatActivity implements View.OnClickListener{
    private static final String POSITION = "POSITION";
    private static final String NAME_ALBUM = "Name Album";
    private static final String NAME_ARTIST = "Name Artist";
    private static final String TAG = "TAG";
    private static final String TAG_ALBUM = "ALBUM";

    private SongAdapter mSongAdapter;
    private ListView mListView;
    private View mLayout;
    private ImageView imgAlbum;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    private TextView mTvNameSong, mTvNameArtist;
    private ImageView mControlPlayPause,mOpenPlayMusic;

    private List<Song> mSongList;
    private String nameAlbum;

    private SongDatabase mSongDatabase;
    private android.media.MediaMetadataRetriever mmr = new MediaMetadataRetriever();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_album);
        getDataIntent();
        setUpToolBar();
        initView();
        setUpAdapter();
        onItemClick();
        setImageSong();

    }

    private void setUpToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(nameAlbum);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        collapsingToolbarLayout.setTitle(nameAlbum);

    }

    private void setUpAdapter() {
        mSongAdapter = new SongAdapter(DetailAlbum.this, mSongList, R.layout.item_song);
        mListView.setAdapter(mSongAdapter);
    }

    // if media player isplaying set update name song,name artist
    private void toolBarControlPlaying() {
        if (MainActivity.musicService.mPlayer != null) {
            if (MainActivity.musicService.mPlayer.isPlaying()) {
                mTvNameSong.setText(MainActivity.musicService.nameSong());
                mTvNameArtist.setText(MainActivity.musicService.nameArtist());
                mControlPlayPause.setImageResource(R.drawable.playing);
                updateTimeSong();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        toolBarControlPlaying();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    // update current song and listener player oncomplete
    private void updateTimeSong() {
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
                Intent intent = new Intent(DetailAlbum.this, PlayMusicActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(TAG, TAG_ALBUM);
                bundle.putString(NAME_ALBUM, nameAlbum);
                bundle.putInt(POSITION, position);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
    }

    private void initView() {

        mTvNameSong = (TextView) findViewById(R.id.albumNameSong);
        mTvNameArtist = (TextView) findViewById(R.id.albumNameArtist);
        mControlPlayPause = (ImageView) findViewById(R.id.albumControlPlayPause);
        mOpenPlayMusic = (ImageView) findViewById(R.id.album_openplaymusic);
        imgAlbum = (ImageView) findViewById(R.id.detailAlbumImg);
        mListView = (ListView) findViewById(R.id.detaialbum_listview);

        mSongDatabase = new SongDatabase(getApplication());
        if (mSongList == null) {
            mSongList = new ArrayList<>();
            mSongList = mSongDatabase.getAllSongFromAlbum(nameAlbum);
        }

        mControlPlayPause.setOnClickListener(this);
        mOpenPlayMusic.setOnClickListener(this);

    }
    // set image of album
    private void setImageSong() {
        mmr.setDataSource(mSongList.get(0).getmFileSong());
        byte[] dataImageDisc = mmr.getEmbeddedPicture();
        if (dataImageDisc != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(dataImageDisc, 0, dataImageDisc.length);
            imgAlbum.setImageBitmap(bitmap);
        } else {
            imgAlbum.setImageResource(R.drawable.detaialbum);
        }
    }

    private void getDataIntent() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        nameAlbum = bundle.getString(NAME_ALBUM);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.album_openplaymusic:
                Intent intent = new Intent(this,PlayMusicActivity.class);
                startActivity(intent);
                break;
            case R.id.albumControlPlayPause:
                if (MainActivity.musicService.isPlaying()){
                    MainActivity.musicService.pausePlayer();
                    mControlPlayPause.setImageResource(R.drawable.pause);
                }else {
                    mControlPlayPause.setImageResource(R.drawable.playing);
                }
                break;
        }
    }
}
