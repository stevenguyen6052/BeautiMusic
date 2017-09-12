package com.example.windows10gamer.beautimusic.view.fragment;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.example.windows10gamer.beautimusic.R;
import com.example.windows10gamer.beautimusic.view.adapter.SongAdapter;
import com.example.windows10gamer.beautimusic.database.SongDatabase;
import com.example.windows10gamer.beautimusic.model.Song;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.tankery.lib.circularseekbar.CircularSeekBar;


public class MusicPlay extends Fragment implements View.OnClickListener {
    private static final String POSITION = "POSITION";
    private static final String NAME_ALBUM = "Name Album";
    private static final String NAME_ARTIST = "Name Artist";
    private final static String TAG = "TAG";
    private final static String TAG_SONG = "SONG";
    private final static String TAG_ARTIST = "ARTIST";
    private final static String TAG_ALBUM = "ALBUM";

    private TextView mTvNameSong, mTvNameSinger, mTvTime;
    private ListView mListView;
    private ImageView mImgBackground, mImgNext, mImgPrevious, mImgPlayPause, mShffle, mReppeat, mControlPlayPause;
    private CircularSeekBar mSeekbar1;
    private android.media.MediaMetadataRetriever mmr = new MediaMetadataRetriever();

    private List<Song> mListSong;
    private SongAdapter mSongAdapter;
    public static MediaPlayer mMediaPlayer;
    private SongDatabase mSongDatabase;
    private int mPosition;
    private Random mRandom;
    private boolean mShuffleOn = false;
    private boolean mRepeat = false;
    private View mRootView1;

    //notification
    private NotificationCompat.Builder builder;
    private NotificationManager notificationManager;
    private int notification_id;
    private RemoteViews remoteViews;
    private Context mContext;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView1 = inflater.inflate(R.layout.music_play_frag2, container, false);
        String tag, nameAlbum, nameArtist;
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
                createNotification();

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

    private void createNotification() {
        notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        RemoteViews contentView = new RemoteViews(mRootView1.getContext().getPackageName(), R.layout.custom_notification);
        contentView.setTextViewText(R.id.notifiNameSong, mListSong.get(mPosition).getmNameSong());
        contentView.setTextViewText(R.id.notifiNameArtist, mListSong.get(mPosition).getmNameArtist());
        contentView.setImageViewResource(R.id.notifiPrevious, R.drawable.custom_previous);
        contentView.setImageViewResource(R.id.notifiPlay, R.drawable.playing);
        contentView.setImageViewResource(R.id.notifiNext, R.drawable.custom_next);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getContext())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContent(contentView);

        Notification notification = mBuilder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(1, notification);
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
                        mPosition =  position;
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
        setImageSong();
    }

    private void setImageSong() {
        mmr.setDataSource(mListSong.get(mPosition).getmFileSong());
        byte[] dataImageDisc = mmr.getEmbeddedPicture();
        if (dataImageDisc != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(dataImageDisc, 0, dataImageDisc.length);
            mImgBackground.setImageBitmap(bitmap);
        } else {
            mImgBackground.setImageResource(R.drawable.detaialbum);
        }
    }

    private void initView() {
        mRandom = new Random();
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
        mImgBackground = (ImageView) mRootView1.findViewById(R.id.background_playmusic);
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
                if (mShuffleOn == false) {
                    mShuffleOn = true;
                    mShffle.setImageResource(R.drawable.ic_shuffle_black_48dp);
                } else if (mShuffleOn == true) {
                    mShffle.setImageResource(R.drawable.ic_shuffle_white_48dp);
                    mShuffleOn = false;
                }


                break;
            case R.id.pRepeat:
                if (mRepeat == false){
                    mRepeat = true;
                    mReppeat.setImageResource(R.drawable.ic_repeat_black_48dp);
                }else {
                    mRepeat = false;
                    mReppeat.setImageResource(R.drawable.ic_repeat_white_48dp);
                }

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

    private void processNextSong() {
        if (mRepeat==true){
            mMediaPlayer.stop();
            mMediaPlayer.release();
            processMediaPlayer();
        }if (mRepeat==false){
            if (mShuffleOn == true) {
                mMediaPlayer.stop();
                mMediaPlayer.release();
                int newSong = mPosition;
                while (newSong == mPosition) {
                    newSong = mRandom.nextInt(mListSong.size());
                }
                mPosition = newSong;
                processMediaPlayer();
            } else if (mShuffleOn == false) {
                mPosition = mPosition + 1;
                mMediaPlayer.stop();
                mMediaPlayer.release();
                if (mPosition >= mListSong.size()) {
                    mPosition = 0;
                    processMediaPlayer();
                } else {
                    processMediaPlayer();
                }
            }
        }

    }

    private void processPreviousSong() {
        if (mRepeat==true){
            mMediaPlayer.stop();
            mMediaPlayer.release();
            processMediaPlayer();
        }else {
            if (mShuffleOn == true) {
                mMediaPlayer.stop();
                mMediaPlayer.release();
                int newSong = mPosition;
                while (newSong == mPosition) {
                    newSong = mRandom.nextInt(mListSong.size());
                }
                mPosition = newSong;
                processMediaPlayer();
            } else if (mShuffleOn == false) {
                mPosition = mPosition - 1;
                mMediaPlayer.stop();
                mMediaPlayer.release();
                if (mPosition < 0) {
                    mPosition = mListSong.size() - 1;
                    processMediaPlayer();
                } else {
                    processMediaPlayer();
                }
            }
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMediaPlayer.stop();
    }
}
