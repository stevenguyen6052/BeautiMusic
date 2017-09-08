package com.example.windows10gamer.beautimusic.view.fragment;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.windows10gamer.beautimusic.R;
import com.example.windows10gamer.beautimusic.view.adapter.SongAdapter;
import com.example.windows10gamer.beautimusic.database.SongDatabase;
import com.example.windows10gamer.beautimusic.model.Song;
import com.example.windows10gamer.beautimusic.view.SendDataPosition;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import me.tankery.lib.circularseekbar.CircularSeekBar;


public class MusicPlay extends Fragment implements View.OnClickListener {
    private static final String POSITION = "POSITION";
    private static final String NAME_ALBUM = "Name Album";
    private static final String NAME_ARTIST = "Name Artist";
    SendDataPosition mSendDataPosition;
    private TextView mTvNameSong, mTvNameSinger, mTvTime;
    private ListView mListView;
    private ImageView mImgNext, mImgPrevious, mImgPlayPause, mShffle, mReppeat, mControlPlayPause;
    private CircularSeekBar mSeekbar1;

    private List<Song> mListSong;
    private SongAdapter mSongAdapter;
    private MediaPlayer mMediaPlayer;
    private SongDatabase mSongDatabase;
    private int mPosition;
    private final static String TAG = "TAG";
    private final static String TAG_SONG = "SONG";
    private final static String TAG_ARTIST = "ARTIST";
    private final static String TAG_ALBUM = "ALBUM";

    private View mRootView1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String tag, nameAlbum, nameArtist;
        mRootView1 = inflater.inflate(R.layout.music_play_frag2, container, false);
        initView();
        Bundle bundle = getArguments();
        if (bundle != null) {
            tag = getArguments().getString(TAG);
            if (tag.equals(TAG_ALBUM)) {
                mPosition = getArguments().getInt(POSITION);
                nameAlbum = getArguments().getString(NAME_ALBUM);
                mSongDatabase = new SongDatabase(getContext());
                mListSong = mSongDatabase.getAllSongFromAlbum(nameAlbum);
                mSongAdapter = new SongAdapter(getContext(), mListSong, R.layout.item_song);
                mListView.setAdapter(mSongAdapter);
                processMediaPlayer();
                updateTimeSong();
                seekBarChange();
            } else if (tag.equals(TAG_SONG)) {
                mSongDatabase = new SongDatabase(getContext());
                mListSong = mSongDatabase.getAllListSong();
                mSongAdapter = new SongAdapter(getContext(), mListSong, R.layout.item_song);
                mListView.setAdapter(mSongAdapter);
                mPosition = getArguments().getInt(POSITION);
                processMediaPlayer();
                updateTimeSong();
                seekBarChange();
            } else if (tag.equals(TAG_ARTIST)) {
                mPosition = getArguments().getInt(POSITION);
                nameArtist = getArguments().getString(NAME_ARTIST);
                mSongDatabase = new SongDatabase(getContext());
                mListSong = mSongDatabase.getAlLSongFromArtist(nameArtist);
                mSongAdapter = new SongAdapter(getContext(), mListSong, R.layout.item_song);
                mListView.setAdapter(mSongAdapter);
                processMediaPlayer();
                updateTimeSong();
                seekBarChange();
            }

        }
        listViewOnItemClick();
        return mRootView1;
    }

    private void listViewOnItemClick() {

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.stop();
                    mMediaPlayer.release();
                    processMediaPlayerInListView(position);
                    if (mPosition > position) {
                        mPosition = mPosition - position;
                    } else if (mPosition < position) {
                        mPosition = position + mPosition;
                    }
                } else {
                    mMediaPlayer.stop();
                    mMediaPlayer.release();
                    processMediaPlayerInListView(position);
                    if (mPosition > position) {
                        mPosition = mPosition - position;
                    } else if (mPosition < position) {
                        mPosition = position + mPosition;
                    }
                }
                updateTimeSong();
            }
        });
    }

    private void processMediaPlayerInListView(int position) {
        mMediaPlayer = MediaPlayer.create(getContext(), Uri.parse(mListSong.get(position).getmFileSong()));
        mMediaPlayer.start();
        mTvNameSong.setText(mListSong.get(position).getmNameSong());
        mTvNameSinger.setText(mListSong.get(position).getmNameArtist());
        mSeekbar1.setMax(mListSong.get(position).getmDuaration());
    }

    private void seekBarChange() {
        mSeekbar1.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, float progress, boolean fromUser) {

            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {
                mMediaPlayer.seekTo((int) mSeekbar1.getProgress());
            }
        });

    }

    // update current time and seekbar
    private void updateTimeSong() {
        final Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat mSimPleDateFormat = new SimpleDateFormat("mm:ss");
                mTvTime.setText(mSimPleDateFormat.format(mMediaPlayer.getCurrentPosition()));
                mSeekbar1.setProgress(mMediaPlayer.getCurrentPosition());
                mHandler.postDelayed(this, 500);
                mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        processNextSong();
                        updateTimeSong();
                        processMediaPlayer();

                    }
                });

            }
        }, 100);

    }

    private void processMediaPlayer() {
        mMediaPlayer = MediaPlayer.create(getContext(), Uri.parse(mListSong.get(mPosition).getmFileSong()));
        mMediaPlayer.start();
        mTvNameSong.setText(mListSong.get(mPosition).getmNameSong());
        mTvNameSinger.setText(mListSong.get(mPosition).getmNameArtist());
        mSeekbar1.setMax(mListSong.get(mPosition).getmDuaration());
        mImgPlayPause.setImageResource(R.drawable.playing);
        mControlPlayPause.setImageResource(R.drawable.playing);
    }

    private void initView() {
        mListSong = new ArrayList<>();
        mSeekbar1 = (CircularSeekBar) mRootView1.findViewById(R.id.pItmSeekbar);
        mListView = (ListView) mRootView1.findViewById(R.id.plistView);
        mTvNameSong = (TextView) mRootView1.findViewById(R.id.itmNameSong);
        mTvNameSinger = (TextView) mRootView1.findViewById(R.id.itmNameSingle);
        mTvTime = (TextView) mRootView1.findViewById(R.id.pTvTime);
        mImgNext = (ImageView) mRootView1.findViewById(R.id.pImgNext);
        mImgPrevious = (ImageView) mRootView1.findViewById(R.id.pImgPrevious);
        mImgPlayPause = (ImageView) mRootView1.findViewById(R.id.pImgPlayPause);
        mShffle = (ImageView) mRootView1.findViewById(R.id.pShuffle);
        mReppeat = (ImageView) mRootView1.findViewById(R.id.pRepeat);
        mControlPlayPause = (ImageView) mRootView1.findViewById(R.id.itmControlPlayPause);
        mControlPlayPause.setOnClickListener(this);
        mImgNext.setOnClickListener(this);
        mImgPrevious.setOnClickListener(this);
        mImgPlayPause.setOnClickListener(this);
        mShffle.setOnClickListener(this);
        mReppeat.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pImgNext:
                processNextSong();
                break;

            case R.id.pImgPrevious:
                processPreviousSong();
                break;

            case R.id.pImgPlayPause:
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                    mImgPlayPause.setImageResource(R.drawable.pause);
                    mControlPlayPause.setImageResource(R.drawable.pause);
                } else {
                    mMediaPlayer.start();
                    mImgPlayPause.setImageResource(R.drawable.playing);
                    mControlPlayPause.setImageResource(R.drawable.playing);
                }
                break;
            case R.id.pShuffle:

                break;
            case R.id.pRepeat:

                break;
            case R.id.itmControlPlayPause:
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                    mImgPlayPause.setImageResource(R.drawable.pause);
                    mControlPlayPause.setImageResource(R.drawable.pause);
                } else {
                    mMediaPlayer.start();
                    mImgPlayPause.setImageResource(R.drawable.playing);
                    mControlPlayPause.setImageResource(R.drawable.playing);
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMediaPlayer.stop();
    }

    private void processNextSong() {
        mPosition = mPosition + 1;
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            if (mPosition >= mListSong.size()) {
                mPosition = 0;
                processMediaPlayer();
            } else {
                processMediaPlayer();
            }
        } else {
            if (mPosition >= mListSong.size()) {
                mPosition = 0;
                processMediaPlayerPause();
            } else {
                processMediaPlayerPause();
            }
        }
    }

    private void processMediaPlayerPause() {
        mMediaPlayer = MediaPlayer.create(getContext(), Uri.parse(mListSong.get(mPosition).getmFileSong()));
        mMediaPlayer.pause();
        mTvNameSong.setText(mListSong.get(mPosition).getmNameSong());
        mTvNameSinger.setText(mListSong.get(mPosition).getmNameArtist());
    }

    private void processPreviousSong() {
        mPosition = mPosition - 1;
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            if (mPosition < 0) {
                mPosition = mListSong.size() - 1;
                processMediaPlayer();
            } else {
                processMediaPlayer();
            }
        } else {
            if (mPosition < 0) {
                mPosition = mListSong.size() - 1;
                processMediaPlayerPause();
            } else {
                processMediaPlayerPause();
            }
        }
    }
}
