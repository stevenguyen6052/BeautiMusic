package com.example.windows10gamer.beautimusic.view.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.windows10gamer.beautimusic.R;
import com.example.windows10gamer.beautimusic.model.Song;
import com.example.windows10gamer.beautimusic.utilities.service.MusicService;
import com.example.windows10gamer.beautimusic.view.adapter.RecyclerItemClickListener;
import com.example.windows10gamer.beautimusic.utilities.Utils;
import com.example.windows10gamer.beautimusic.view.adapter.SongAdapter;
import com.example.windows10gamer.beautimusic.view.adapter.SongAdapterPlaying;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class PlayMusicActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private ImageView mImgPlayPause;
    private final static int REQUEST_LIST = 1;
    private TextView mTvNameSong, mTvNameSinger, mTvTime, mTvSumTime;
    private ImageView mImgBackground, mImgNext, mImgPrevious, mShffle, mReppeat;
    private SeekBar mSeekbar;
    private int mPosition;
    private List<Song> mSongList;
    private List<Song> songList, filteredModelList;
    private SimpleDateFormat mSimPleDateFormat = new SimpleDateFormat("mm:ss");
    private Bundle bundle;
    private SearchView searchView;
    private String check, name = "";
    private Boolean isCheck;
    private RecyclerView lvSongs;
    private SongAdapterPlaying songAdapter;
    private Toolbar toolbar;
    private LinearLayoutManager linearLayoutManager;
    private MusicService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playmusic);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        initView();
        getData();
        lvSongs.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        lvSongs.setLayoutManager(linearLayoutManager);
        songAdapter = new SongAdapterPlaying(mSongList, this);
        lvSongs.setAdapter(songAdapter);

        addItemClick();
        loadStatusShuffleRepeat();

        IntentFilter it = new IntentFilter();
        it.addAction(Utils.PAUSE_KEY);
        it.addAction(Utils.PLAY_KEY);
        registerReceiver(receiver, it);
    }

    private void addItemClick() {
        lvSongs.addOnItemTouchListener(new RecyclerItemClickListener(this, lvSongs,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int i) {
                        if (filteredModelList != null && filteredModelList.size() != 0) {
                            service.setSongList(filteredModelList);
                        }
                        service.setIndexPlay(i);
                        playMusic();
                    }

                    @Override
                    public void onItemLongClick(View view, final int i) {

                    }
                }));
    }

    private void getData() {
        bundle = getIntent().getExtras();
        if (bundle != null) {
            mSongList = bundle.getParcelableArrayList(Utils.LIST_SONG);
            mPosition = bundle.getInt(Utils.POSITION);
            service.setSongList(mSongList);
            service.setIndexPlay(mPosition);
            playMusic();
        } else {
            if (service.mPlayer != null) {
                setDataForView();
                mSongList = service.getSongList();
            }
        }
    }

    private void setDataForView() {
        if (service.isPlaying()) {
            mImgPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
        } else {
            mImgPlayPause.setImageResource(R.drawable.ic_play_arrow_white_48dp);
        }
        mTvNameSong.setText(service.nameSong());
        mTvNameSinger.setText(service.nameArtist());
        mSeekbar.setMax(service.getDuaration());
        mTvSumTime.setText(mSimPleDateFormat.format(service.getDuaration()));
        setImageSong();
        updateTimeSong();
    }

    private void playMusic() {
        mTvNameSong.setText(service.nameSong());
        mTvNameSinger.setText(service.nameArtist());
        mTvSumTime.setText(mSimPleDateFormat.format(service.getDuaration()));
        mImgPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
        mSeekbar.setMax(service.getDuaration());
        setImageSong();
        updateTimeSong();
        service.playSong();
    }

    private void initView() {
        service = HomeActivity.musicService;
        mSongList = new ArrayList<>();
        mSeekbar = (SeekBar) findViewById(R.id.pItmSeekbar);
        mSeekbar.getProgressDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        mSeekbar.getThumb().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        mTvNameSong = (TextView) findViewById(R.id.itmNameSong);
        mTvNameSinger = (TextView) findViewById(R.id.itmNameSingle);
        mTvTime = (TextView) findViewById(R.id.pTvTime);
        mTvSumTime = (TextView) findViewById(R.id.pTvSumTime);
        mImgNext = (ImageView) findViewById(R.id.pImgNext);
        mImgPrevious = (ImageView) findViewById(R.id.pImgPrevious);
        mImgPlayPause = (ImageView) findViewById(R.id.pImgPlayPause);
        mShffle = (ImageView) findViewById(R.id.pShuffle);
        mReppeat = (ImageView) findViewById(R.id.pRepeat);
        mImgBackground = (ImageView) findViewById(R.id.imgSong);
        lvSongs = (RecyclerView) findViewById(R.id.plistView);

        mImgNext.setOnClickListener(this);
        mImgPrevious.setOnClickListener(this);
        mImgPlayPause.setOnClickListener(this);
        mShffle.setOnClickListener(this);
        mReppeat.setOnClickListener(this);
        mSeekbar.setOnSeekBarChangeListener(this);
    }

    private void nextSong() {
        service.playNext();
        setImageSong();
        mTvNameSong.setText(service.nameSong());
        mTvNameSinger.setText(service.nameArtist());
        mTvSumTime.setText(mSimPleDateFormat.format(service.getDuaration()));

    }

    private void previousSong() {
        service.playPrev();
        setImageSong();
        mTvNameSong.setText(service.nameSong());
        mTvNameSinger.setText(service.nameArtist());
        mTvSumTime.setText(mSimPleDateFormat.format(service.getDuaration()));
    }

    private void doPlayPause() {
        if (service.isPlaying()) {
            service.pausePlayer();
            mImgPlayPause.setImageResource(R.drawable.ic_play_arrow_white_48dp);
            sendBroadcast(new Intent().setAction(Utils.PAUSE_KEY));
            sendBroadcast(new Intent().setAction(Utils.NOTIFI));
        } else {
            service.startPlayer();
            mImgPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
            sendBroadcast(new Intent().setAction(Utils.PLAY_KEY));
            sendBroadcast(new Intent().setAction(Utils.NOTIFI));
        }
    }

    // check compleate song and update ui
    private void updateTimeSong() {
        final Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mTvTime.setText(mSimPleDateFormat.format(service.getCurrentPosition()));
                mSeekbar.setProgress(service.getCurrentPosition());
                mHandler.postDelayed(this, 500);
                service.setOnComplete();
                mTvNameSong.setText(service.nameSong());
                mTvNameSinger.setText(service.nameArtist());
                mTvSumTime.setText(mSimPleDateFormat.format(service.getDuaration()));
                setImageSong();
            }
        }, 100);
    }

    private void doShuffle() {
        if (service.isShuffle())
            mShffle.setImageResource(R.drawable.ic_shuffle_black_48dp);
        else mShffle.setImageResource(R.drawable.ic_shuffle_white_48dp);

    }

    private void doRepeat() {
        if (service.isRepeat())
            mReppeat.setImageResource(R.drawable.ic_repeat_black_48dp);
        else mReppeat.setImageResource(R.drawable.ic_repeat_white_48dp);
    }

    private void doQueue() {
        Intent intent = new Intent(this, PlayingQueueActivity.class);
        if (filteredModelList != null && filteredModelList.size() != 0) {
            intent.putParcelableArrayListExtra(Utils.LIST_SONG, (ArrayList<Song>) filteredModelList);
        } else {
            intent.putParcelableArrayListExtra(Utils.LIST_SONG, (ArrayList<Song>) mSongList);
        }
        startActivityForResult(intent, REQUEST_LIST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_LIST) {
            if (resultCode == RESULT_OK) {
                name = service.nameSong();
                isCheck = intent.getBooleanExtra(Utils.TRUE, false);
                check = intent.getExtras().getString(Utils.CHECK);
                if (isCheck) {
                    //check.euqual("click") thì chơi nhạc tại postion
                    if (check.equals(Utils.ITEM_CLICK)) {
                        //th sắp xếp list bài hát đang search
                        if (filteredModelList != null && filteredModelList.size() != 0) {
                            int postion = intent.getIntExtra(Utils.POSITION, 0);
                            songList = intent.getExtras().getParcelableArrayList(Utils.LIST_SONG);
                            filteredModelList.clear();
                            filteredModelList.addAll(songList);
                            songAdapter.setFilter(filteredModelList);
                            service.setSongList(filteredModelList);
                            service.setIndexPlay(postion);
                            playMusic();
                        } else {
                            // th  sắp xếp list nhạc màn hình playmusic
                            int postion = intent.getIntExtra(Utils.POSITION, 0);
                            songList = intent.getExtras().getParcelableArrayList(Utils.LIST_SONG);
                            mSongList.clear();
                            mSongList.addAll(songList);
                            songAdapter.notifyDataSetChanged();
                            service.setIndexPlay(postion);
                            playMusic();
                        }

                        // if check.equals("")-> trường hợp ấn back or ấn vào màn mini control
                    } else if (check.equals("")) {

                        if (filteredModelList != null && filteredModelList.size() != 0) {
                            songList = intent.getExtras().getParcelableArrayList(Utils.LIST_SONG);
                            filteredModelList.clear();
                            filteredModelList.addAll(songList);
                            songAdapter.setFilter(filteredModelList);
                            for (int i = 0; i < filteredModelList.size(); i++) {
                                if (name.equals(filteredModelList.get(i).getNameSong()))
                                    service.setIndexPlay(i);
//
                            }
                        } else {
                            songList = intent.getExtras().getParcelableArrayList(Utils.LIST_SONG);
                            mSongList.clear();
                            mSongList.addAll(songList);
                            songAdapter.notifyDataSetChanged();
                            for (int i = 0; i < mSongList.size(); i++) {
                                if (name.equals(mSongList.get(i).getNameSong()))
                                    service.setIndexPlay(i);
                            }
                        }
                    }
                } else {
                    if (check.equals(Utils.ITEM_CLICK)) {
                        int i = intent.getIntExtra(Utils.POSITION, 0);
                        service.setIndexPlay(i);
                        playMusic();
                    }
                }
            }
        }
    }

    // set image song
    private void setImageSong() {
        Picasso.with(this)
                .load(HomeActivity.musicService.getImageSong())
                .placeholder(R.drawable.ic_empty_music)
                .error(R.drawable.ic_empty_music)
                .into(mImgBackground);
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
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_item, menu);
        MenuItem itemSearch = menu.findItem(R.id.itemSearch);
        searchView = (SearchView) MenuItemCompat.getActionView(itemSearch);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemArrange:
                doQueue();
                break;
            case R.id.itemSearch:
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {

                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        filteredModelList = Utils.filter(mSongList, newText.trim());
                        songAdapter.setFilter(filteredModelList);
                        return true;
                    }
                });
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //save status shuffle or repeat
        SharedPreferences sharedPreferences = getSharedPreferences(Utils.SHUFFLE_REPEAT, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Utils.SHUFFLE, service.getShuffle());
        editor.putBoolean(Utils.REPEAT, service.getRepeat());
        editor.apply();
    }

    private void loadStatusShuffleRepeat() {
        boolean shuffle, repeat;
        SharedPreferences sharedPreferences = this.getSharedPreferences(Utils.SHUFFLE_REPEAT, Context.MODE_PRIVATE);
        if (sharedPreferences != null) {

            shuffle = sharedPreferences.getBoolean(Utils.SHUFFLE, false);
            repeat = sharedPreferences.getBoolean(Utils.REPEAT, false);

            if (shuffle) {
                mShffle.setImageResource(R.drawable.ic_shuffle_black_48dp);
            }
            if (repeat) {
                mReppeat.setImageResource(R.drawable.ic_repeat_black_48dp);
            }
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        service.seek((int) mSeekbar.getProgress());
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Utils.PAUSE_KEY)) {
                mImgPlayPause.setImageResource(R.drawable.ic_play_arrow_white_48dp);
            } else if (intent.getAction().equals(Utils.PLAY_KEY)) {
                mImgPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
            }

        }
    };
}