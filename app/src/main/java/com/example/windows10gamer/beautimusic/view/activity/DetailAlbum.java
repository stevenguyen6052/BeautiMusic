package com.example.windows10gamer.beautimusic.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.windows10gamer.beautimusic.R;
import com.example.windows10gamer.beautimusic.view.adapter.SongAdapter;
import com.example.windows10gamer.beautimusic.database.SongDatabase;
import com.example.windows10gamer.beautimusic.model.Song;
import com.example.windows10gamer.beautimusic.view.fragment.MusicPlay;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.List;

import static com.example.windows10gamer.beautimusic.view.activity.MainActivity.mMusicPlay;


public class DetailAlbum extends AppCompatActivity {
    private static final String POSITION = "POSITION";
    private static final String NAME_ALBUM = "Name Album";
    private static final String NAME_ARTIST = "Name Artist";
    private static final String TAG = "TAG";
    private static final String TAG_ALBUM = "ALBUM";

    private SongAdapter mSongAdapter;
    private ListView mListView;
    private View mLayout;
    private TextView mTvNameArtist;
    private ImageView imgAlbum;

    private List<Song> mSongList;
    private String nameAlbum, nameArtist;
    private SongDatabase mSongDatabase;
    private SlidingUpPanelLayout slidingUpPanelLayout;
    private android.media.MediaMetadataRetriever mmr = new MediaMetadataRetriever();
    MusicPlay musicPlay = new MusicPlay();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_album);
        getDataIntent();
        initView();
        setUpAdapter();
        onItemClick();
        setImageSong();

    }

    private void setUpAdapter() {
        mSongAdapter = new SongAdapter(DetailAlbum.this, mSongList, R.layout.item_song);
        mListView.setAdapter(mSongAdapter);
    }

    private void onItemClick() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (MusicPlay.mMediaPlayer.isPlaying()) {

                    MusicPlay.mMediaPlayer.stop();
                    slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                    MusicPlay mMusicPlay = new MusicPlay();
                    Bundle args = new Bundle();
                    args.putString(TAG, TAG_ALBUM);
                    args.putInt(POSITION, position);
                    args.putString(NAME_ALBUM, mSongList.get(position).getmNameAlbum());
                    mMusicPlay.setArguments(args);
                    getSupportFragmentManager().beginTransaction().replace(R.id.myFrameLayout1, mMusicPlay).commit();

                } else {

                    slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                    MusicPlay mMusicPlay = new MusicPlay();
                    Bundle args = new Bundle();
                    args.putString(TAG, TAG_ALBUM);
                    args.putInt(POSITION, position);
                    args.putString(NAME_ALBUM, mSongList.get(position).getmNameAlbum());
                    mMusicPlay.setArguments(args);
                    getSupportFragmentManager().beginTransaction().replace(R.id.myFrameLayout1, mMusicPlay).commit();

                }
            }
        });
    }

    private void initView() {

        getSupportFragmentManager().beginTransaction().add(R.id.myFrameLayout1,musicPlay).commit();

        imgAlbum = (ImageView) findViewById(R.id.detailAlbumImg);
        mLayout = findViewById(R.id.myFrameLayout1);
        slidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout1);
        mListView = (ListView) findViewById(R.id.detailAlbumListView);
        mTvNameArtist = (TextView) findViewById(R.id.detailAlbumTvNameArtist);

        mSongDatabase = new SongDatabase(getApplication());
        if (mSongList == null) {
            mSongList = new ArrayList<>();
            mSongList = mSongDatabase.getAllSongFromAlbum(nameAlbum);
        }
        mTvNameArtist.setText(nameArtist);
    }

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
        nameArtist = bundle.getString(NAME_ARTIST);

    }
}
