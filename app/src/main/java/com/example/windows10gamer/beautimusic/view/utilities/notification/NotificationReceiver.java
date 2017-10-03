package com.example.windows10gamer.beautimusic.view.utilities.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;


import com.example.windows10gamer.beautimusic.R;
import com.example.windows10gamer.beautimusic.view.activity.DetailAlbumArtist;
import com.example.windows10gamer.beautimusic.view.activity.MainActivity;
import com.example.windows10gamer.beautimusic.view.activity.PlayMusicActivity;
import com.example.windows10gamer.beautimusic.view.activity.PlayingQueue;
import com.example.windows10gamer.beautimusic.view.utilities.service.MusicService;

import static com.example.windows10gamer.beautimusic.view.utilities.service.MusicService.NOTIFY_NEXT;
import static com.example.windows10gamer.beautimusic.view.utilities.service.MusicService.NOTIFY_PLAY;
import static com.example.windows10gamer.beautimusic.view.utilities.service.MusicService.NOTIFY_PREVIOUS;
import static com.example.windows10gamer.beautimusic.view.utilities.service.MusicService.notification;


public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(NOTIFY_PLAY)) {
            if (MainActivity.musicService.isPlaying()) {

                MainActivity.musicService.pausePlayer();
                PlayMusicActivity.mImgPlayPause.setImageResource(R.drawable.ic_play_arrow_white_48dp);
                MainActivity.mImgContrlPlay.setImageResource(R.drawable.ic_play_arrow_white_48dp);
                if (DetailAlbumArtist.mControlPlayPause != null && PlayingQueue.mPlayPause != null) {
                    DetailAlbumArtist.mControlPlayPause.setImageResource(R.drawable.ic_play_arrow_white_48dp);
                    PlayingQueue.mPlayPause.setImageResource(R.drawable.ic_play_arrow_white_48dp);
                }

                MusicService.updateRemoteview();

            } else {
                MainActivity.musicService.startPlayer();
                PlayMusicActivity.mImgPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
                MainActivity.mImgContrlPlay.setImageResource(R.drawable.ic_pause_white_48dp);
                if (DetailAlbumArtist.mControlPlayPause != null && PlayingQueue.mPlayPause != null) {
                    DetailAlbumArtist.mControlPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
                    PlayingQueue.mPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
                }
                MusicService.updateRemoteview();

            }


        } else if (intent.getAction().equals(NOTIFY_NEXT)) {

            MainActivity.musicService.playNext();
            if (MainActivity.musicService.isPlaying()) {

            } else {
                PlayMusicActivity.mImgPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
                MainActivity.mImgContrlPlay.setImageResource(R.drawable.ic_pause_white_48dp);
                if (DetailAlbumArtist.mControlPlayPause != null && PlayingQueue.mPlayPause != null) {
                    DetailAlbumArtist.mControlPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
                    PlayingQueue.mPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
                }
            }

        } else if (intent.getAction().equals(NOTIFY_PREVIOUS)) {
            MainActivity.musicService.playPrev();
            if (MainActivity.musicService.isPlaying()) {

            } else {
                PlayMusicActivity.mImgPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
                MainActivity.mImgContrlPlay.setImageResource(R.drawable.ic_pause_white_48dp);
                if (DetailAlbumArtist.mControlPlayPause != null && PlayingQueue.mPlayPause != null) {
                    DetailAlbumArtist.mControlPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
                    PlayingQueue.mPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
                }
            }

        }else {
            Toast.makeText(context,"Tai nghe đã được cắm!",Toast.LENGTH_LONG).show();
        }

    }
}
