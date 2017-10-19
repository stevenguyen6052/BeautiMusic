package com.example.windows10gamer.beautimusic.view.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.os.IBinder;
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

import com.bumptech.glide.Glide;
import com.example.windows10gamer.beautimusic.R;
import com.example.windows10gamer.beautimusic.application.App;
import com.example.windows10gamer.beautimusic.model.Song;
import com.example.windows10gamer.beautimusic.utilities.service.MusicService;
import com.example.windows10gamer.beautimusic.utilities.RecyclerItemClickListener;
import com.example.windows10gamer.beautimusic.utilities.Utils;
import com.example.windows10gamer.beautimusic.utilities.singleton.SharedPrefs;
import com.example.windows10gamer.beautimusic.view.adapter.SongAdapterPlaying;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class PlayMusicActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private ImageView mImgPlayPause;
    private final static int REQUEST_LIST = 1;
    private TextView mTvNameSong, mTvNameSinger, mTvTime, mTvSumTime;
    private ImageView mImgSong, mImgNext, mImgPrevious, mShffle, mReppeat;
    private SeekBar mSeekbar;
    private int mPosition;
    private List<Song> mSongList;
    private List<Song> songList, mSearchList;
    private SimpleDateFormat mSimPleDateFormat = new SimpleDateFormat("mm:ss");
    private Bundle bundle;
    private SearchView searchView;

    private RecyclerView mLvSong;
    private SongAdapterPlaying mAdapter;
    private Toolbar mToolbar;
    private LinearLayoutManager mLinearLayout;
    private MusicService mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playmusic);

        SharedPrefs.getInstance().put(Utils.STATUS_PLAY, true);
        sendBroadcast(new Intent().setAction(Utils.CHECKED_PLAY));

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        initView();
        getData();

        mLvSong.setHasFixedSize(true);
        mLinearLayout = new LinearLayoutManager(this);

        mLvSong.setLayoutManager(mLinearLayout);
        mAdapter = new SongAdapterPlaying(mSongList, this);
        mLvSong.setAdapter(mAdapter);

        IntentFilter it = new IntentFilter();
        it.addAction(Utils.PAUSE_KEY);
        it.addAction(Utils.PLAY_KEY);
        it.addAction(Utils.PREVIOUS_PLAY);
        it.addAction(Utils.NEXT_PLAY);
        registerReceiver(receiver, it);

        addItemClick();
        loadStatusShuffleRepeat();
    }

    private void addItemClick() {
        mLvSong.addOnItemTouchListener(new RecyclerItemClickListener(this, mLvSong,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int i) {
                        if (mSearchList != null && mSearchList.size() != 0) {
                            mService.setSongList(mSearchList);
                        }
                        mService.setIndexPlay(i);
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
            mService.setSongList(mSongList);
            mService.setIndexPlay(mPosition);
            playMusic();

        } else {
            mSongList = mService.getSongList();
            setDataForView();
        }
    }

    private void setDataForView() {
        setView();
        updateTimeSong();
        if (mService.isPlaying())
            mImgPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
        else
            mImgPlayPause.setImageResource(R.drawable.ic_play_arrow_white_48dp);
    }

    private void playMusic() {
        mService.playSong();
        mImgPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
        setView();
        updateTimeSong();
    }

    private void initView() {
        mService = ((App) getApplicationContext()).getService();
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
        mImgSong = (ImageView) findViewById(R.id.imgSong);
        mLvSong = (RecyclerView) findViewById(R.id.plistView);

        mImgNext.setOnClickListener(this);
        mImgPrevious.setOnClickListener(this);
        mImgPlayPause.setOnClickListener(this);
        mShffle.setOnClickListener(this);
        mReppeat.setOnClickListener(this);
        mSeekbar.setOnSeekBarChangeListener(this);
    }

    private void nextSong() {
        mImgPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
        mService.playNext();
        setView();


    }

    private void previousSong() {
        mImgPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
        mService.playPrev();
        setView();
    }

    private void doPlayPause() {
        if (mService.isPlaying()) {
            mImgPlayPause.setImageResource(R.drawable.ic_play_arrow_white_48dp);
            sendBroadcast(new Intent().setAction(Utils.PAUSE_KEY));
            sendBroadcast(new Intent().setAction(Utils.NOTIFI));

        } else {
            mImgPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
            sendBroadcast(new Intent().setAction(Utils.PLAY_KEY));
            sendBroadcast(new Intent().setAction(Utils.NOTIFI));
        }
    }

    private void setView() {
        mTvNameSong.setText(mService.nameSong());
        mTvNameSinger.setText(mService.nameArtist());
        mTvSumTime.setText(mSimPleDateFormat.format(mService.getDuaration()));
        mSeekbar.setMax(mService.getDuaration());
        Picasso.with(this)
                .load(mService.getImageSong())
                .resizeDimen(R.dimen.art_item_album,R.dimen.art_item_album)
                .placeholder(R.drawable.ic_empty_music)
                .error(R.drawable.ic_empty_music)
                .into(mImgSong);

    }

    // check compleate song and update ui
    private void updateTimeSong() {
        final Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mTvTime.setText(mSimPleDateFormat.format(mService.getCurrentPosition()));
                mSeekbar.setProgress(mService.getCurrentPosition());
                mHandler.postDelayed(this, 500);
                mService.setOnComplete();
                mAdapter.notifyDataSetChanged();
                setView();
            }
        }, 100);
    }

    private void doShuffle() {
        if (mService.isShuffle())
            mShffle.setImageResource(R.drawable.ic_shuffle_black_48dp);
        else
            mShffle.setImageResource(R.drawable.ic_shuffle_white_48dp);

    }

    private void doRepeat() {
        if (mService.isRepeat())
            mReppeat.setImageResource(R.drawable.ic_repeat_black_48dp);
        else
            mReppeat.setImageResource(R.drawable.ic_repeat_white_48dp);
    }

    private void doQueue() {
        Intent intent = new Intent(this, PlayingQueueActivity.class);
        if (mSearchList != null && mSearchList.size() != 0) {
            intent.putParcelableArrayListExtra(Utils.LIST_SONG, (ArrayList<Song>) mSearchList);
        } else {
            intent.putParcelableArrayListExtra(Utils.LIST_SONG, (ArrayList<Song>) mSongList);
        }
        startActivityForResult(intent, REQUEST_LIST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        String nameSong = "";
        Boolean isClick, isQueue;
        if (requestCode == REQUEST_LIST) {
            if (resultCode == RESULT_OK) {
                nameSong = mService.nameSong();//vị trí bài hát hiện tại trước khi sắp xếp
                isQueue = intent.getBooleanExtra(Utils.TRUE, false); // th đã được sắp xếp
                isClick = intent.getExtras().getBoolean(Utils.CHECK); // th click vòa item trong list song
                if (isQueue) {
                    if (isClick) {
                        if (mSearchList != null && mSearchList.size() != 0) {//th sắp xếp list bài hát đang search
                            int postion = intent.getIntExtra(Utils.POSITION, 0);
                            songList = intent.getExtras().getParcelableArrayList(Utils.LIST_SONG);
                            mSearchList.clear();
                            mSearchList.addAll(songList);
                            mAdapter.setFilter(mSearchList);
                            mService.setSongList(mSearchList);
                            mService.setIndexPlay(postion);
                            playMusic();
                        } else {
                            int postion = intent.getIntExtra(Utils.POSITION, 0);// th  sắp xếp list nhạc màn hình playmusic
                            songList = intent.getExtras().getParcelableArrayList(Utils.LIST_SONG);
                            mSongList.clear();
                            mSongList.addAll(songList);
                            mAdapter.notifyDataSetChanged();
                            mService.setIndexPlay(postion);
                            playMusic();
                        }
                    } else { // th click vào mini control bên dưới màn hình or ấn back
                        if (mSearchList != null && mSearchList.size() != 0) {
                            songList = intent.getExtras().getParcelableArrayList(Utils.LIST_SONG);
                            mSearchList.clear();
                            mSearchList.addAll(songList);
                            mAdapter.setFilter(mSearchList);
                            for (int i = 0; i < mSearchList.size(); i++) {
                                if (nameSong.equals(mSearchList.get(i).getNameSong()))
                                    mService.setIndexPlay(i); // cập nhật lại index sau khi sắp xếp
                            }
                        } else {
                            songList = intent.getExtras().getParcelableArrayList(Utils.LIST_SONG);
                            mSongList.clear();
                            mSongList.addAll(songList);
                            mAdapter.notifyDataSetChanged();
                            for (int i = 0; i < mSongList.size(); i++) {
                                if (nameSong.equals(mSongList.get(i).getNameSong()))
                                    mService.setIndexPlay(i);
                            }
                        }
                    }
                } else { // th vô màn hình sx nhưng k sắp xếp
                    if (isClick) {
                        int i = intent.getIntExtra(Utils.POSITION, 0);
                        mService.setIndexPlay(i);
                        playMusic();
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pImgNext:
                nextSong();
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.pImgPrevious:
                previousSong();
                mAdapter.notifyDataSetChanged();
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
                        mSearchList = Utils.filter(mSongList, newText.trim());
                        mAdapter.setFilter(mSearchList);
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
        SharedPrefs.getInstance().put(Utils.SHUFFLE, mService.getShuffle());
        SharedPrefs.getInstance().put(Utils.REPEAT, mService.getRepeat());
    }

    private void loadStatusShuffleRepeat() {
        boolean shuffle, repeat;
        shuffle = SharedPrefs.getInstance().get(Utils.SHUFFLE, Boolean.class, false);
        repeat = SharedPrefs.getInstance().get(Utils.REPEAT, Boolean.class, false);

        if (shuffle) {
            mShffle.setImageResource(R.drawable.ic_shuffle_black_48dp);
            mService.setShuffle(shuffle);
        }

        if (repeat) {
            mReppeat.setImageResource(R.drawable.ic_repeat_black_48dp);
            mService.setRepeat(repeat);
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
        mService.seek((int) mSeekbar.getProgress());
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case Utils.PAUSE_KEY:
                    mImgPlayPause.setImageResource(R.drawable.ic_play_arrow_white_48dp);
                    break;
                case Utils.PLAY_KEY:
                    mImgPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
                    break;
                case Utils.NEXT_PLAY:
                    mImgPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
                    mAdapter.notifyDataSetChanged();
                    break;
                case Utils.PREVIOUS_PLAY:
                    mImgPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
                    mAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };
}