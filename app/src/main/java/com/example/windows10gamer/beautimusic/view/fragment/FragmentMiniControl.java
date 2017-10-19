package com.example.windows10gamer.beautimusic.view.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.windows10gamer.beautimusic.R;
import com.example.windows10gamer.beautimusic.application.App;
import com.example.windows10gamer.beautimusic.utilities.Utils;
import com.example.windows10gamer.beautimusic.utilities.service.MusicService;
import com.example.windows10gamer.beautimusic.utilities.singleton.SharedPrefs;
import com.example.windows10gamer.beautimusic.view.activity.HomeActivity;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

public class FragmentMiniControl extends android.support.v4.app.Fragment implements View.OnClickListener {
    private View mRootView;
    private TextView mTvNameSong;
    private TextView mTvNameArtist;
    private ImageView mPlayPause;
    private CircleImageView mImgSong;
    private MusicService mService;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case Utils.PAUSE_KEY:
                    mPlayPause.setImageResource(R.drawable.ic_play_arrow_white_48dp);
                    break;
                case Utils.PLAY_KEY:
                    mPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
                    break;
                case Utils.PREVIOUS_PLAY:
                    mPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
                    break;
                case Utils.NEXT_PLAY:
                    mPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_mini_control, container, false);

        mService = ((App) getContext().getApplicationContext()).getService();

        IntentFilter it = new IntentFilter();
        it.addAction(Utils.PAUSE_KEY);
        it.addAction(Utils.PLAY_KEY);
        it.addAction(Utils.NEXT_PLAY);
        it.addAction(Utils.PREVIOUS_PLAY);
        getContext().registerReceiver(receiver, it);
        initView();

        miniControlPlayMusic();
        return mRootView;
    }

    private void initView() {
        mTvNameSong = (TextView) mRootView.findViewById(R.id.mainNameSong);
        mTvNameArtist = (TextView) mRootView.findViewById(R.id.mainNameSingle);
        mPlayPause = (ImageView) mRootView.findViewById(R.id.mainControlPlay);
        mImgSong = (CircleImageView) mRootView.findViewById(R.id.mainImgSong);
        mPlayPause.setOnClickListener(this);

    }

    private void miniControlPlayMusic() {

        if (SharedPrefs.getInstance().get(Utils.STATUS_PLAY, Boolean.class, false)) {
            mPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
            currentSongPlaying();
        } else {
            mPlayPause.setImageResource(R.drawable.ic_play_arrow_white_48dp);
        }
        mTvNameSong.setText(mService.nameSong());
        mTvNameArtist.setText(mService.nameArtist());
        Picasso.with(getContext())
                .load(mService.getImageSong())
                .resizeDimen(R.dimen.artItem,R.dimen.artItem)
                .placeholder(R.drawable.dianhac)
                .error(R.drawable.dianhac)
                .into(mImgSong);

    }


    private void currentSongPlaying() {
        final Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mHandler.postDelayed(this, 500);
                mService.setOnComplete();
                mTvNameSong.setText(mService.nameSong());
                mTvNameArtist.setText(mService.nameArtist());
                Picasso.with(getContext())
                        .load(mService.getImageSong())
                        .resizeDimen(R.dimen.artItem,R.dimen.artItem)
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
                if (mService.isPlaying()) {
                    getContext().sendBroadcast(new Intent().setAction(Utils.PAUSE_KEY));
                    mPlayPause.setImageResource(R.drawable.ic_play_arrow_white_48dp);
                } else {
                    getContext().sendBroadcast(new Intent().setAction(Utils.PLAY_KEY));
                    mPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
                }
                getContext().sendBroadcast(new Intent().setAction(Utils.NOTIFI));
                break;
        }
    }
}
