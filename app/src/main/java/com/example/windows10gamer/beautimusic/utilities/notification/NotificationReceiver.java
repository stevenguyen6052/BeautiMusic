package com.example.windows10gamer.beautimusic.utilities.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


import com.example.windows10gamer.beautimusic.R;
import com.example.windows10gamer.beautimusic.view.activity.DetailAlbumArtist;
import com.example.windows10gamer.beautimusic.view.activity.HomeActivity;
import com.example.windows10gamer.beautimusic.view.activity.PlayMusicActivity;
import com.example.windows10gamer.beautimusic.view.activity.PlayingQueueActivity;

import static com.example.windows10gamer.beautimusic.utilities.service.MusicService.NOTIFY_NEXT;
import static com.example.windows10gamer.beautimusic.utilities.service.MusicService.NOTIFY_PLAY;
import static com.example.windows10gamer.beautimusic.utilities.service.MusicService.NOTIFY_PREVIOUS;


public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(NOTIFY_PLAY)) {
            if (HomeActivity.musicService.isPlaying()) {
                HomeActivity.musicService.pausePlayer();
                PlayMusicActivity.mImgPlayPause.setImageResource(R.drawable.ic_play_arrow_white_48dp);
                HomeActivity.mImgPlayPause.setImageResource(R.drawable.ic_play_arrow_white_48dp);
                if (DetailAlbumArtist.mControlPlayPause != null && PlayingQueueActivity.mPlayPause != null) {
                    DetailAlbumArtist.mControlPlayPause.setImageResource(R.drawable.ic_play_arrow_white_48dp);
                    PlayingQueueActivity.mPlayPause.setImageResource(R.drawable.ic_play_arrow_white_48dp);
                }
                HomeActivity.musicService.updateRemoteview();

            } else {
                HomeActivity.musicService.startPlayer();
                PlayMusicActivity.mImgPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
                HomeActivity.mImgPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
                if (DetailAlbumArtist.mControlPlayPause != null && PlayingQueueActivity.mPlayPause != null) {
                    DetailAlbumArtist.mControlPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
                    PlayingQueueActivity.mPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
                }
                HomeActivity.musicService.updateRemoteview();
            }


        } else if (intent.getAction().equals(NOTIFY_NEXT)) {
            HomeActivity.musicService.playNext();
            if (HomeActivity.musicService.isPlaying()) {

            } else {
                PlayMusicActivity.mImgPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
                HomeActivity.mImgPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
                if (DetailAlbumArtist.mControlPlayPause != null && PlayingQueueActivity.mPlayPause != null) {
                    DetailAlbumArtist.mControlPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
                    PlayingQueueActivity.mPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
                }
            }

        } else if (intent.getAction().equals(NOTIFY_PREVIOUS)) {
            HomeActivity.musicService.playPrev();
            if (HomeActivity.musicService.isPlaying()) {
            } else {
                PlayMusicActivity.mImgPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
                HomeActivity.mImgPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
                if (DetailAlbumArtist.mControlPlayPause != null && PlayingQueueActivity.mPlayPause != null) {
                    DetailAlbumArtist.mControlPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
                    PlayingQueueActivity.mPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
                }
            }
        }
    }
}
