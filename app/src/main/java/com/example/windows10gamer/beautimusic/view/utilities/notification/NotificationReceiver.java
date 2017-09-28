package com.example.windows10gamer.beautimusic.view.utilities.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;


import com.example.windows10gamer.beautimusic.R;
import com.example.windows10gamer.beautimusic.view.activity.MainActivity;
import com.example.windows10gamer.beautimusic.view.activity.PlayMusicActivity;
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


                Toast.makeText(context, "PAUSE", Toast.LENGTH_LONG).show();

            } else {
                MainActivity.musicService.startPlayer();
                PlayMusicActivity.mImgPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
                MainActivity.mImgContrlPlay.setImageResource(R.drawable.ic_pause_white_48dp);
                Toast.makeText(context, "PLAY", Toast.LENGTH_LONG).show();
            }


        } else if (intent.getAction().equals(NOTIFY_NEXT)) {
            MainActivity.musicService.playNext();

            Toast.makeText(context, "NEXT", Toast.LENGTH_LONG).show();

        } else if (intent.getAction().equals(NOTIFY_PREVIOUS)) {
            MainActivity.musicService.playPrev();
            Toast.makeText(context, "PREVIOUS", Toast.LENGTH_LONG).show();

        }

    }
}
