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
import com.example.windows10gamer.beautimusic.view.utilities.InitClass;

import java.util.ArrayList;
import java.util.List;


public class DetailAlbumArtist extends AppCompatActivity implements View.OnClickListener {
    //tag check debug
    private static final String TAG_CHECK_ERROR = "DetailAlbumArtist";
    // tag for get data
    private static final String POSITION = "POSITION";
    private static final String NAME_ALBUM = "Name Album";
    private static final String TAG = "TAG";
    private static final String TAG_DETAIL = "DETAIL";
    private static final String TAG_ARTIST = "ARTIST";
    private static final String TAG_ALBUM = "ALBUM";
    private static final String NAME_ARTIST = "Name Artist";

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

    private SongDatabase mSongDatabase;
    private android.media.MediaMetadataRetriever mmr = new MediaMetadataRetriever();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG_CHECK_ERROR, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_album);

        getDataIntent();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(nameAlbumArtist);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        collapsingToolbarLayout.setTitle(nameAlbumArtist);

        initView();
        setUpAdapter();
        onItemClick();
        setImageSong();

    }

    @Override
    protected void onResume() {
        Log.d(TAG_CHECK_ERROR, "onResume");
        super.onResume();

        checkPlayMusic();

        if (MainActivity.musicService.mPlayer != null) {
            miniControlPlayMusic();

        } else {

        }
    }

    private void setUpAdapter() {
        mSongAdapter = new SongAdapter(DetailAlbumArtist.this, mSongList, R.layout.item_song);
        mListView.setAdapter(mSongAdapter);
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
        }else {
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
                Bundle bundle = new Bundle();
                bundle.putString(TAG, TAG_DETAIL);

                if (tag.equals(TAG_ALBUM)) {
                    bundle.putString(TAG_ALBUM, TAG_ALBUM);
                } else if (tag.equals(TAG_ARTIST)) {
                    bundle.putString(TAG_ALBUM, TAG_ARTIST);
                }
                bundle.putString("Name", nameAlbumArtist);
                bundle.putInt(POSITION, position);
                intent.putExtras(bundle);
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

        mSongDatabase = new SongDatabase(getApplication());
        mSongList = new ArrayList<>();
        if (tag.equals(TAG_ALBUM)) {
            mSongList = mSongDatabase.getAllSongFromAlbum(nameAlbumArtist);
        } else if (tag.equals(TAG_ARTIST)) {
            mSongList = mSongDatabase.getAlLSongFromArtist(nameAlbumArtist);
        }

        mLayout.setOnClickListener(this);
        mControlPlayPause.setOnClickListener(this);

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
        tag = bundle.getString("TAG");
        if (tag.equals(TAG_ALBUM)) {
            nameAlbumArtist = bundle.getString(NAME_ALBUM);
        } else if (tag.equals(TAG_ARTIST)) {
            nameAlbumArtist = bundle.getString(NAME_ARTIST);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.album_openplaymusic:
                Intent intent = new Intent(this, PlayMusicActivity.class);
                startActivity(intent);
                break;
            case R.id.albumControlPlayPause:

                if (MainActivity.musicService.isPlaying()) {
                    MainActivity.musicService.pausePlayer();
                    mControlPlayPause.setImageResource(R.drawable.ic_play_arrow_white_48dp);
                    MainActivity.mImgContrlPlay.setImageResource(R.drawable.ic_play_arrow_white_48dp);
                } else {
                    MainActivity.musicService.startPlayer();
                    mControlPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
                    MainActivity.mImgContrlPlay.setImageResource(R.drawable.ic_pause_white_48dp);
                }
                break;
        }
    }
}
