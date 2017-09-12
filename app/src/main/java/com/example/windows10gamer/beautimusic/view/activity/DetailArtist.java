package com.example.windows10gamer.beautimusic.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.windows10gamer.beautimusic.view.adapter.SongAdapter;
import com.example.windows10gamer.beautimusic.view.adapter.SongAdapter1;
import com.example.windows10gamer.beautimusic.database.SongDatabase;
import com.example.windows10gamer.beautimusic.model.Song;
import com.example.windows10gamer.beautimusic.R;
import com.example.windows10gamer.beautimusic.view.fragment.MusicPlay;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Windows 10 Gamer on 31/08/2017.
 */

public class DetailArtist extends AppCompatActivity {
    private static final String TAG = "TAG";
    private static final String TAG_ARTIST = "ARTIST";
    private static final String POSITION = "POSITION";
    private static final String NAME_ARTIST = "Name Artist";

    private android.media.MediaMetadataRetriever mmr = new MediaMetadataRetriever();
    private List<Song> mSongList;
    private SongDatabase mSongDatabase;
    private String nameArtist;
    private View mLayout;
    private static SlidingUpPanelLayout slidingUpPanelLayout;
    private ImageView imgView;
    private ListView mListView;
    private SongAdapter mSongAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_artist);
        getDataIntent();
        initView();
        setImage();
        mSongAdapter = new SongAdapter(this, mSongList, R.layout.item_song);
        mListView.setAdapter(mSongAdapter);
        onItemListViewClick();

    }

    private void onItemListViewClick() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (MusicPlay.mMediaPlayer.isPlaying()) {
                    MusicPlay.mMediaPlayer.pause();
                }
                mLayout.setVisibility(View.VISIBLE);
                DetailArtist.slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                MusicPlay mMusicPlay = new MusicPlay();
                Bundle mBundle = new Bundle();
                mBundle.putString(TAG, TAG_ARTIST);
                mBundle.putInt(POSITION, position);
                mBundle.putString(NAME_ARTIST, mSongList.get(position).getmNameArtist());
                mMusicPlay.setArguments(mBundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.myFrameLayout2, mMusicPlay).commit();
            }
        });
    }

    private void getDataIntent() {
        Intent mIntent = getIntent();
        Bundle mBundle = mIntent.getExtras();
        nameArtist = mBundle.getString(NAME_ARTIST);
    }

    private void initView() {
        imgView = (ImageView) findViewById(R.id.detailArtistImg);
        mLayout = findViewById(R.id.myFrameLayout2);
        slidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout_artist);
        mListView = (ListView) findViewById(R.id.detailArtistRecycle);
        mSongDatabase = new SongDatabase(getApplication());

        if (mSongList == null) {
            mSongList = new ArrayList<>();
            mSongList = mSongDatabase.getAlLSongFromArtist(nameArtist);
        }

    }

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

}
