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
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.windows10gamer.beautimusic.view.adapter.SongAdapter;
import com.example.windows10gamer.beautimusic.database.SongDatabase;
import com.example.windows10gamer.beautimusic.model.Song;
import com.example.windows10gamer.beautimusic.R;

import java.util.ArrayList;
import java.util.List;

public class DetailArtist extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "TAG";
    private static final String TAG_ARTIST = "ARTIST";
    private static final String POSITION = "POSITION";
    private static final String NAME_ARTIST = "Name Artist";

    private TextView mTvNameSong;
    private TextView mTvNameArtist;
    private ImageView mImgPlayPause;

    private android.media.MediaMetadataRetriever mmr = new MediaMetadataRetriever();
    private SongDatabase mSongDatabase;
    private String nameArtist;
    private View mLayout;

    private List<Song> mSongList;
    private ImageView imgView;
    private ListView mListView;
    private SongAdapter mSongAdapter;

    //cosllap toolbar
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_artist);
        getDataIntent();
        setUpToolBar();
        initView();
        setImage();
        setUpAdapter();
        onItemListViewClick();

        //update current music isplaying in toolbar control
        mTvNameSong.setText(MainActivity.musicService.nameSong());
        mTvNameArtist.setText(MainActivity.musicService.nameArtist());
        updateTimeSong();


    }

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

    private void setUpToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(nameArtist);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        collapsingToolbarLayout.setTitle(nameArtist);
    }

    // if media player isplaying update name song and name artist
    private void toolBarControlPlaying() {
        if (MainActivity.musicService.mPlayer != null) {
            if (MainActivity.musicService.mPlayer.isPlaying()) {
                mTvNameSong.setText(MainActivity.musicService.nameSong());
                mTvNameArtist.setText(MainActivity.musicService.nameArtist());
                mImgPlayPause.setImageResource(R.drawable.playing);
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

    // click item listview
    private void onItemListViewClick() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(DetailArtist.this, PlayMusicActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(TAG, TAG_ARTIST);
                bundle.putString(NAME_ARTIST, nameArtist);
                bundle.putInt(POSITION, position);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void setUpAdapter() {
        mSongAdapter = new SongAdapter(this, mSongList, R.layout.item_song);
        mListView.setAdapter(mSongAdapter);
    }

    private void getDataIntent() {
        Intent mIntent = getIntent();
        Bundle mBundle = mIntent.getExtras();
        nameArtist = mBundle.getString(NAME_ARTIST);
    }

    private void initView() {

        mTvNameSong = (TextView) findViewById(R.id.artistNameSong);
        mTvNameArtist = (TextView) findViewById(R.id.artistNameArtist);
        mImgPlayPause = (ImageView) findViewById(R.id.artistControlPlay);
        imgView = (ImageView) findViewById(R.id.detailAlbumImg);
        mListView = (ListView) findViewById(R.id.detaialbum_listview);

        mSongDatabase = new SongDatabase(getApplication());
        if (mSongList == null) {
            mSongList = new ArrayList<>();
            mSongList = mSongDatabase.getAlLSongFromArtist(nameArtist);
        }
    }

    // set image of artist
    private void setImage() {
        mmr.setDataSource(mSongList.get(0).getmFileSong());
        byte[] dataImageDisc = mmr.getEmbeddedPicture();
        if (dataImageDisc != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(dataImageDisc, 0, dataImageDisc.length);
            imgView.setImageBitmap(bitmap);
        } else {
            imgView.setImageResource(R.drawable.detaialbum);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.artistControlPlay:
        }
    }
}
