package com.example.windows10gamer.beautimusic.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.windows10gamer.beautimusic.R;
import com.example.windows10gamer.beautimusic.database.SongDatabase;
import com.example.windows10gamer.beautimusic.model.Song;
import com.example.windows10gamer.beautimusic.view.adapter.SongAdapter;
import com.example.windows10gamer.beautimusic.view.utilities.Utils;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class PlayMusicActivity extends AppCompatActivity implements View.OnClickListener {
    String nameSong, nameSingle;
    // request result list after queue
    private final static int REQUEST_LIST = 1;
    public static TextView mTvNameSong, mTvNameSinger, mTvTime, mTvSumTime;
    public static ImageView mImgPlayPause;
    private ImageView mImgBackground, mImgNext, mImgPrevious, mShffle, mReppeat;
    private SeekBar mSeekbar;
    public SongAdapter mSongAdapter;
    private ListView mListView;
    private int mPosition;
    private List<Song> mSongList;
    private List<Song> songList, filteredModelList;
    SimpleDateFormat mSimPleDateFormat = new SimpleDateFormat("mm:ss");
    Bundle bundle;
    SearchView searchView;

    //activity and playback pause flags
    private boolean paused = false, playbackPaused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playmusic);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        initView();
        addEvents();
        getData();
        mSongAdapter = new SongAdapter(this, mSongList, R.layout.item_song);
        mListView.setAdapter(mSongAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (filteredModelList != null && filteredModelList.size() != 0) {
                    MainActivity.musicService.mSongList = filteredModelList;
                }
                MainActivity.musicService.mPosition = position;
                playMusic();
            }
        });

        mSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //MainActivity.musicService.seek((int) mSeekbar1.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //MainActivity.musicService.seek((int) mSeekbar.getProgress());
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                MainActivity.musicService.seek((int) mSeekbar.getProgress());
            }
        });
        loadStatusShuffleRepeat();
    }

    //getdata from fragment song,album,artist
    private void getData() {
        bundle = getIntent().getExtras();
        if (bundle != null) {
            mSongList = bundle.getParcelableArrayList(Utils.LIST_SONG);
            mPosition = bundle.getInt(Utils.POSITION);
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
            mImgPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
            mTvNameSong.setText(MainActivity.musicService.nameSong());
            mTvNameSinger.setText(MainActivity.musicService.nameArtist());
            mSeekbar.setMax(MainActivity.musicService.getDuaration());
            mTvSumTime.setText(mSimPleDateFormat.format(MainActivity.musicService.getDuaration()) + "");
            setImageSong();
            updateTimeSong();
        } else {
            mImgPlayPause.setImageResource(R.drawable.ic_play_arrow_white_48dp);
            mTvNameSong.setText(MainActivity.musicService.nameSong());
            mTvNameSinger.setText(MainActivity.musicService.nameArtist());
            mSeekbar.setMax(MainActivity.musicService.getDuaration());
            mTvSumTime.setText(mSimPleDateFormat.format(MainActivity.musicService.getDuaration()) + "");
            setImageSong();
        }
    }

    private void playMusic() {
        mTvNameSong.setText(MainActivity.musicService.nameSong());
        mTvNameSinger.setText(MainActivity.musicService.nameArtist());
        mTvSumTime.setText(mSimPleDateFormat.format(MainActivity.musicService.getDuaration()) + "");
        mImgPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
        mSeekbar.setMax(MainActivity.musicService.getDuaration());
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
    }

    private void initView() {
        mSongList = new ArrayList<>();
        mSeekbar = (SeekBar) findViewById(R.id.pItmSeekbar);
        mListView = (ListView) findViewById(R.id.plistView);
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
    }

    private void nextSong() {
        MainActivity.musicService.playNext();
        setImageSong();
        mTvNameSong.setText(MainActivity.musicService.nameSong());
        mTvNameSinger.setText(MainActivity.musicService.nameArtist());
        mTvSumTime.setText(mSimPleDateFormat.format(MainActivity.musicService.getDuaration()) + "");

    }

    private void previousSong() {
        MainActivity.musicService.playPrev();
        setImageSong();
        mTvNameSong.setText(MainActivity.musicService.nameSong());
        mTvNameSinger.setText(MainActivity.musicService.nameArtist());
        mTvSumTime.setText(mSimPleDateFormat.format(MainActivity.musicService.getDuaration()) + "");
    }

    private void doPlayPause() {
        if (MainActivity.musicService.isPlaying()) {
            MainActivity.musicService.pausePlayer();
            mImgPlayPause.setImageResource(R.drawable.ic_play_arrow_white_48dp);
            MainActivity.musicService.updateRemoteview();
        } else {
            MainActivity.musicService.startPlayer();
            mImgPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
            MainActivity.musicService.updateRemoteview();
        }
    }

    // check compleate song and update ui
    private void updateTimeSong() {
        final Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mTvTime.setText(mSimPleDateFormat.format(MainActivity.musicService.getCurrentPosition()) + "");
                mSeekbar.setProgress(MainActivity.musicService.getCurrentPosition());
                mHandler.postDelayed(this, 500);
                MainActivity.musicService.setOnComplete();
                mTvNameSong.setText(MainActivity.musicService.nameSong());
                mTvNameSinger.setText(MainActivity.musicService.nameArtist());
                mTvSumTime.setText(mSimPleDateFormat.format(MainActivity.musicService.getDuaration()) + "");
                setImageSong();
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

    private void doQueue() {
        Intent intent = new Intent(this, PlayingQueue.class);
        if (filteredModelList!=null && filteredModelList.size() != 0) {
            intent.putParcelableArrayListExtra(Utils.LIST_SONG, (ArrayList<Song>) filteredModelList);
        } else {
            intent.putParcelableArrayListExtra(Utils.LIST_SONG, (ArrayList<Song>) mSongList);
        }
        startActivityForResult(intent, REQUEST_LIST);
    }

    // trả kq về sau từ màn hình sắp xếp nhạc
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Log.d("Main", "Activity for result!");
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_LIST) {
            if (resultCode == RESULT_OK) {
                String name = "";
                name = MainActivity.musicService.nameSong();
                boolean isCheck = intent.getBooleanExtra(Utils.TRUE, false);
                String check = intent.getExtras().getString(Utils.CHECK);
                // update khi ấn onclick item từ màn hình playingqueue
                // check = true thi cho nhac tai vi tri click else update view
                if (isCheck) {
                    //get data khi click item song
                    if (check.equals(Utils.ITEM_CLICK)) {
                        //th sắp xếp list bài hát đang search
                        if (filteredModelList.size() != 0) {
                            int postion = intent.getIntExtra(Utils.POSITION, 0);
                            songList = intent.getExtras().getParcelableArrayList(Utils.LIST_SONG);
                            filteredModelList.clear();
                            filteredModelList.addAll(songList);
                            mSongAdapter.setFilter(filteredModelList);
                            MainActivity.musicService.mPosition = postion;
                            playMusic();
                        } else {
                            int postion = intent.getIntExtra(Utils.POSITION, 0);
                            songList = intent.getExtras().getParcelableArrayList(Utils.LIST_SONG);
                            mSongList.clear();
                            mSongList.addAll(songList);
                            mSongAdapter.notifyDataSetChanged();
                            MainActivity.musicService.mPosition = postion;
                            playMusic();
                        }

                    } else if (check.equals("")) {
                        //get data sau khi sắp xếp nhạc và ấn back
                        if (filteredModelList!=null && filteredModelList.size() != 0) {
                            songList = intent.getExtras().getParcelableArrayList(Utils.LIST_SONG);
                            filteredModelList.clear();
                            filteredModelList.addAll(songList);
                            mSongAdapter.setFilter(filteredModelList);
                            for (int i = 0; i < filteredModelList.size(); i++) {
                                if (name.equals(filteredModelList.get(i).getNameSong()))
                                    MainActivity.musicService.mPosition = i;
                            }
                        } else {
                            songList = intent.getExtras().getParcelableArrayList(Utils.LIST_SONG);
                            mSongList.clear();
                            mSongList.addAll(songList);
                            mSongAdapter.notifyDataSetChanged();
                            for (int i = 0; i < mSongList.size(); i++) {
                                if (name.equals(mSongList.get(i).getNameSong()))
                                    MainActivity.musicService.mPosition = i;
                            }
                        }
                    }
                } else {
                    //khi không sắp xếp
                    if (check.equals(Utils.ITEM_CLICK)) {
                        int postion = intent.getIntExtra(Utils.POSITION, 0);
                        MainActivity.musicService.mPosition = postion;
                        playMusic();
                    }
                }
            }
        }
    }

    // set image song
    private void setImageSong() {
        Picasso.with(this)
                .load(MainActivity.musicService.getImageSong())
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
        nameSong = MainActivity.musicService.nameSong();
        nameSingle = MainActivity.musicService.nameArtist();
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
                        mSongAdapter.setFilter(filteredModelList);

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
        SharedPreferences sharedPreferences = getSharedPreferences("ShuffleRepeat", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("ShuffleOn", MainActivity.musicService.getShuffle());
        editor.putBoolean("RepeatOn", MainActivity.musicService.getRepeat());
        editor.apply();
    }

    private void loadStatusShuffleRepeat() {
        boolean shuffle, repeat;
        SharedPreferences sharedPreferences = this.getSharedPreferences("ShuffleRepeat", Context.MODE_PRIVATE);
        if (sharedPreferences != null) {
            shuffle = sharedPreferences.getBoolean("ShuffleOn", false);
            repeat = sharedPreferences.getBoolean("RepeatOn", false);
            if (shuffle == true) {
                mShffle.setImageResource(R.drawable.ic_shuffle_black_48dp);
            }
            if (repeat == true) {
                mReppeat.setImageResource(R.drawable.ic_repeat_black_48dp);
            }
        }
    }
}
