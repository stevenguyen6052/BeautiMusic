package com.example.windows10gamer.beautimusic.view.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.windows10gamer.beautimusic.R;
import com.example.windows10gamer.beautimusic.utilities.Utils;
import com.example.windows10gamer.beautimusic.utilities.service.MusicService;
import com.example.windows10gamer.beautimusic.view.activity.HomeActivity;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Windows 10 Gamer on 13/10/2017.
 */

public class FragmentMiniControl extends android.support.v4.app.Fragment implements View.OnClickListener {
    private View mRootView;
    private TextView mTvNameSong;
    private TextView mTvNameArtist;
    private ImageView mPlayPause;
    private CircleImageView mImgSong;
    private MusicService service;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Utils.PAUSE_KEY)) {
                mPlayPause.setImageResource(R.drawable.ic_play_arrow_white_48dp);
            } else if (intent.getAction().equals(Utils.PLAY_KEY)) {
                mPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_mini_control, container, false);
        IntentFilter it = new IntentFilter();
        it.addAction(Utils.PAUSE_KEY);
        it.addAction(Utils.PLAY_KEY);
        getContext().registerReceiver(receiver, it);
        initView();
        if (HomeActivity.musicService.mSongList != null) {
            miniControlPlayMusic();
        }
        return mRootView;
    }

    private void initView() {
        service = HomeActivity.musicService;
        mTvNameSong = (TextView) mRootView.findViewById(R.id.mainNameSong);
        mTvNameArtist = (TextView) mRootView.findViewById(R.id.mainNameSingle);
        mPlayPause = (ImageView) mRootView.findViewById(R.id.mainControlPlay);
        mImgSong = (CircleImageView) mRootView.findViewById(R.id.mainImgSong);
        mPlayPause.setOnClickListener(this);

    }

    private void miniControlPlayMusic() {
        if (service.mSongList.size() > 0) {

            if (service.mPlayer.isPlaying()) {
                mTvNameSong.setText(service.nameSong());
                mTvNameArtist.setText(service.nameArtist());
                mPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
                currentSongPlaying();
            } else {
                mTvNameSong.setText(service.nameSong());
                mTvNameArtist.setText(service.nameArtist());
                mPlayPause.setImageResource(R.drawable.ic_play_arrow_white_48dp);
            }
            Picasso.with(getContext())
                    .load(service.getImageSong())
                    .placeholder(R.drawable.dianhac)
                    .error(R.drawable.dianhac)
                    .into(mImgSong);
        }
    }

    private void currentSongPlaying() {
        final Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mHandler.postDelayed(this, 500);
                service.setOnComplete();
                mTvNameSong.setText(service.nameSong());
                mTvNameArtist.setText(service.nameArtist());
                Picasso.with(getContext())
                        .load(service.getImageSong())
                        .placeholder(R.drawable.dianhac)
                        .error(R.drawable.dianhac)
                        .into(mImgSong);
            }
        }, 100);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mainControlPlay:
                if (service.isPlaying()) {
                    service.pausePlayer();
                    mPlayPause.setImageResource(R.drawable.ic_play_arrow_white_48dp);
                } else {
                    service.startPlayer();
                    mPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
                }
                getContext().sendBroadcast(new Intent().setAction(Utils.NOTIFI));
                break;
        }
    }
}
