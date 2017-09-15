package com.example.windows10gamer.beautimusic.view.helper.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.windows10gamer.beautimusic.R;
import com.example.windows10gamer.beautimusic.view.activity.MainActivity;
import com.example.windows10gamer.beautimusic.view.activity.PlayMusicActivity;

import static com.example.windows10gamer.beautimusic.view.helper.service.MusicService.NOTIFY_NEXT;
import static com.example.windows10gamer.beautimusic.view.helper.service.MusicService.NOTIFY_DELETE;
import static com.example.windows10gamer.beautimusic.view.helper.service.MusicService.NOTIFY_PLAY;
import static com.example.windows10gamer.beautimusic.view.helper.service.MusicService.NOTIFY_PREVIOUS;

/**
 * Created by Windows 10 Gamer on 11/09/2017.
 */

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(NOTIFY_PLAY)){
            if (MainActivity.musicService.isPlaying()){
                MainActivity.musicService.pausePlayer();
                PlayMusicActivity.mImgPlayPause.setImageResource(R.drawable.pause);

                Toast.makeText(context, "PAUSE", Toast.LENGTH_LONG).show();
            }
            else{
                MainActivity.musicService.pausePlayer();
                PlayMusicActivity.mImgPlayPause.setImageResource(R.drawable.playing);

            }
            Toast.makeText(context, "PLAY", Toast.LENGTH_LONG).show();
        }else if (intent.getAction().equals(NOTIFY_NEXT)){

            MainActivity.musicService.playNext();
            PlayMusicActivity.mTvNameSong.setText(MainActivity.musicService.nameSong());
            PlayMusicActivity.mTvNameSinger.setText(MainActivity.musicService.nameArtist());
            Toast.makeText(context, "NEXT", Toast.LENGTH_LONG).show();
        }else if (intent.getAction().equals(NOTIFY_DELETE)){

            Toast.makeText(context, "DELETE", Toast.LENGTH_LONG).show();
        }else if (intent.getAction().equals(NOTIFY_PREVIOUS)){

            MainActivity.musicService.playPrev();
            Toast.makeText(context, "PREVIOUS", Toast.LENGTH_LONG).show();
        }

    }
}
