package com.example.windows10gamer.beautimusic.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.windows10gamer.beautimusic.R;
import com.example.windows10gamer.beautimusic.database.SongDatabase;
import com.example.windows10gamer.beautimusic.model.Song;
import com.example.windows10gamer.beautimusic.view.adapter.SongAdapter;
import com.example.windows10gamer.beautimusic.view.utilities.InitClass;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import me.tankery.lib.circularseekbar.CircularSeekBar;

public class PlayMusicActivity extends AppCompatActivity implements View.OnClickListener {
    // request result list after queue
    private final static String TAG_REQUEST = "LIST";
    private final static int REQUEST_LIST = 1;

    private String tag, nameAlbum, tagCheck;

    public static TextView mTvNameSong, mTvNameSinger, mTvTime;
    public static ImageView mImgPlayPause;

    private ImageView mImgBackground, mImgNext, mImgPrevious, mShffle, mReppeat, mQueue;
    private CircularSeekBar mSeekbar1;
    private android.media.MediaMetadataRetriever mmr = new MediaMetadataRetriever();

    private SongDatabase mSongDatabase;
    public SongAdapter mSongAdapter;
    private ListView mListView;

    private int mPosition;
    public static List<Song> mSongList;
    SearchView searchView;

    //activity and playback pause flags
    private boolean paused = false, playbackPaused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_play_frag2);
        setUpToolbar();
        initView();
        addEvents();
        getData();
        mSongAdapter = new SongAdapter(this, mSongList, R.layout.item_song);
        mListView.setAdapter(mSongAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                MainActivity.musicService.mPosition = position;
                playMusic();
            }
        });
        mSeekbar1.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {

            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, float progress, boolean fromUser) {
            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {
                MainActivity.musicService.seek((int) mSeekbar1.getProgress());
            }
        });

    }

    //getdata from fragment song,album,artist
    private void getData() {
        mSongDatabase = new SongDatabase(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {

            tag = bundle.getString(InitClass.TAG);
            //if tag = detail -> tagcheck = album get song from album or tagcheck = artist get song from artist
            if (tag.equals(InitClass.TAG_DETAIL)) {
                nameAlbum = bundle.getString(InitClass.NAMEALBUM_ARTIST);
                mPosition = bundle.getInt(InitClass.POSITION);
                tagCheck = bundle.getString(InitClass.TAG_ALBUM);

                if (tagCheck.equals(InitClass.TAG_ALBUM)) {
                    mSongList = mSongDatabase.getAllSongFromAlbum(nameAlbum);

                } else if (tagCheck.equals(InitClass.TAG_ARTIST)) {
                    mSongList = mSongDatabase.getAlLSongFromArtist(nameAlbum);

                }
            } else if (tag.equals(InitClass.TAG_SONG)) {

                mSongList = mSongDatabase.getAllListSong();
                mPosition = bundle.getInt(InitClass.POSITION);

            } else if (tag.equals(InitClass.SEARCH)) {
                mPosition = bundle.getInt(InitClass.POSITION);
                mSongList = bundle.getParcelableArrayList(InitClass.LIST_SONG);
            }
            MainActivity.musicService.mSongList = mSongList;
            MainActivity.musicService.mPosition = mPosition;
            playMusic();
        } else {
            if (MainActivity.musicService.mPlayer != null) {
                setDataForView();
                mSongList = MainActivity.musicService.mSongList;
            }
        }
    }

    private void setDataForView() {
        if (MainActivity.musicService.isPlaying()) {
            mImgPlayPause.setImageResource(R.drawable.playing);
            mTvNameSong.setText(MainActivity.musicService.nameSong());
            mTvNameSinger.setText(MainActivity.musicService.nameArtist());
            mSeekbar1.setMax(MainActivity.musicService.getDuaration());
            setImageSong();
            updateTimeSong();

        } else {
            mImgPlayPause.setImageResource(R.drawable.pause);
            mTvNameSong.setText(MainActivity.musicService.nameSong());
            mTvNameSinger.setText(MainActivity.musicService.nameArtist());
            mSeekbar1.setMax(MainActivity.musicService.getDuaration());
            setImageSong();
        }
    }

    private void playMusic() {
        mTvNameSong.setText(MainActivity.musicService.nameSong());
        mTvNameSinger.setText(MainActivity.musicService.nameArtist());
        mImgPlayPause.setImageResource(R.drawable.playing);
        mSeekbar1.setMax(MainActivity.musicService.getDuaration());
        setImageSong();
        updateTimeSong();
        MainActivity.musicService.playSong();
    }


    private void addEvents() {
        mImgNext.setOnClickListener(this);
        mImgPrevious.setOnClickListener(this);
        mImgPlayPause.setOnClickListener(this);
        mShffle.setOnClickListener(this);
        mReppeat.setOnClickListener(this);
        mQueue.setOnClickListener(this);
    }

    private void initView() {
        mSongList = new ArrayList<>();
        mSeekbar1 = (CircularSeekBar) findViewById(R.id.pItmSeekbar);
        mListView = (ListView) findViewById(R.id.plistView);
        mTvNameSong = (TextView) findViewById(R.id.itmNameSong);
        mTvNameSinger = (TextView) findViewById(R.id.itmNameSingle);
        mTvTime = (TextView) findViewById(R.id.pTvTime);
        mImgNext = (ImageView) findViewById(R.id.pImgNext);
        mImgPrevious = (ImageView) findViewById(R.id.pImgPrevious);
        mImgPlayPause = (ImageView) findViewById(R.id.pImgPlayPause);
        mShffle = (ImageView) findViewById(R.id.pShuffle);
        mReppeat = (ImageView) findViewById(R.id.pRepeat);
        mQueue = (ImageView) findViewById(R.id.pImgQueue);
        mImgBackground = (ImageView) findViewById(R.id.background_playmusic);
        // set animition for text;
        Animation animationToLeft = new TranslateAnimation(400, -400, 0, 0);
        animationToLeft.setDuration(12000);
        animationToLeft.setRepeatMode(Animation.RESTART);
        animationToLeft.setRepeatCount(Animation.INFINITE);

        Animation animationToRight = new TranslateAnimation(-400, 400, 0, 0);
        animationToRight.setDuration(12000);
        animationToRight.setRepeatMode(Animation.RESTART);
        animationToRight.setRepeatCount(Animation.INFINITE);

        mTvNameSong.setAnimation(animationToRight);

    }

    private void nextSong() {
        MainActivity.musicService.playNext();
        setImageSong();
        mTvNameSong.setText(MainActivity.musicService.nameSong());
        mTvNameSinger.setText(MainActivity.musicService.nameArtist());
    }

    private void previousSong() {
        MainActivity.musicService.playPrev();
        setImageSong();
        mTvNameSong.setText(MainActivity.musicService.nameSong());
        mTvNameSinger.setText(MainActivity.musicService.nameArtist());
    }

    private void doPlayPause() {
        if (MainActivity.musicService.isPlaying()) {
            MainActivity.musicService.pausePlayer();
            mImgPlayPause.setImageResource(R.drawable.pause);

        } else {
            MainActivity.musicService.startPlayer();
            mImgPlayPause.setImageResource(R.drawable.playing);
        }
    }

    // check compleate song and update ui
    private void updateTimeSong() {
        final Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat mSimPleDateFormat = new SimpleDateFormat("mm:ss");
                mTvTime.setText(mSimPleDateFormat.format(MainActivity.musicService.getCurrentPosition()));
                mSeekbar1.setProgress(MainActivity.musicService.getCurrentPosition());
                mHandler.postDelayed(this, 500);
                MainActivity.musicService.setOnComplete();
                mTvNameSong.setText(MainActivity.musicService.nameSong());
                mTvNameSinger.setText(MainActivity.musicService.nameArtist());
            }
        }, 100);
    }

    private void doShuffle() {
        if (MainActivity.musicService.setShuffle() == true)
            mShffle.setImageResource(R.drawable.ic_shuffle_black_48dp);
        else mShffle.setImageResource(R.drawable.ic_shuffle_white_48dp);

    }

    private void doRepeat() {
        if (MainActivity.musicService.setRepeat() == true)
            mReppeat.setImageResource(R.drawable.ic_repeat_black_48dp);
        else mReppeat.setImageResource(R.drawable.ic_repeat_white_48dp);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pImgNext:
                nextSong();
                break;

            case R.id.pImgPrevious:
                previousSong();
                break;

            case R.id.pImgPlayPause:
                doPlayPause();
                break;

            case R.id.pShuffle:
                doShuffle();
                break;

            case R.id.pRepeat:
                doRepeat();
                break;

            case R.id.pImgQueue:
                doQueue();
                break;
        }
    }

    private void doQueue() {
        Intent intent = new Intent(this, PlayingQueue.class);
        Bundle bundle = new Bundle();
        if (tag.equals(InitClass.TAG_SONG)) {
            bundle.putString(InitClass.TAG, InitClass.TAG_SONG);

        } else if (tag.equals(InitClass.TAG_DETAIL)) {
            bundle.putString(InitClass.TAG, InitClass.TAG_DETAIL);
            bundle.putString(InitClass.NAMEALBUM_ARTIST, nameAlbum);

            if (tagCheck.equals(InitClass.TAG_ALBUM)) {
                bundle.putString(InitClass.TAG_ALBUM, InitClass.TAG_ALBUM);

            } else if (tagCheck.equals(InitClass.TAG_ARTIST)) {
                bundle.putString(InitClass.TAG_ALBUM, InitClass.TAG_ARTIST);
            }
        }
        intent.putExtras(bundle);
        startActivityForResult(intent, REQUEST_LIST);

    }

    // trả về list sau khi sắp xếp
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LIST) {
            if (resultCode == RESULT_OK) {
                String name = "";
                name = MainActivity.musicService.nameSong();
                Bundle getData = data.getExtras();
                mSongList.clear();
                mSongList.addAll((List<Song>) getData.getSerializable(TAG_REQUEST));
                mSongAdapter.notifyDataSetChanged();
                // update position after queue
                for (int i = 0; i < mSongList.size(); i++) {
                    if (name.equals(mSongList.get(i).getNameSong()))
                        MainActivity.musicService.mPosition = i;
                }
            }
        }
    }

    // set image song
    private void setImageSong() {
        mmr.setDataSource(MainActivity.musicService.pathSong());
        byte[] dataImageDisc = mmr.getEmbeddedPicture();
        if (dataImageDisc != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(dataImageDisc, 0, dataImageDisc.length);
            mImgBackground.setImageBitmap(bitmap);
        } else {
            mImgBackground.setImageResource(R.drawable.ic_empty_music2);
        }
    }

    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null)
            setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemSearch:
                doSearch();

                break;
            case R.id.itemArrange:
                doQueue();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void doSearch() {
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putParcelableArrayListExtra(InitClass.LIST_SONG, (ArrayList<Song>) mSongList);
        startActivity(intent);
    }
}
