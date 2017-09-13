package com.example.windows10gamer.beautimusic.view.helper.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.windows10gamer.beautimusic.R;
import com.example.windows10gamer.beautimusic.view.fragment.MusicPlay;

/**
 * Created by Windows 10 Gamer on 11/09/2017.
 */

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(NotificationGenerator.NOTIFY_PLAY)){
            if (MusicPlay.mMediaPlayer.isPlaying()){
                MusicPlay.mMediaPlayer.pause();
                MusicPlay.mControlPlayPause.setImageResource(R.drawable.pause);
                MusicPlay.mImgPlayPause.setImageResource(R.drawable.pause);
            }else {
                MusicPlay.mMediaPlayer.start();
                MusicPlay.mControlPlayPause.setImageResource(R.drawable.playing);
                MusicPlay.mImgPlayPause.setImageResource(R.drawable.playing);
            }

        } else if (intent.getAction().equals(NotificationGenerator.NOTIFY_PAUSE)){
            Toast.makeText(context, "NOTIFY_PAUSE", Toast.LENGTH_LONG).show();
        }else if (intent.getAction().equals(NotificationGenerator.NOTIFY_NEXT)){
            Toast.makeText(context, "NOTIFY_NEXT", Toast.LENGTH_LONG).show();
        }else if (intent.getAction().equals(NotificationGenerator.NOTIFY_DELETE)){
            Toast.makeText(context, "NOTIFY_DELETE", Toast.LENGTH_LONG).show();
        }else if (intent.getAction().equals(NotificationGenerator.NOTIFY_PREVIOUS)){
            Toast.makeText(context, "NOTIFY_PREVIOUS", Toast.LENGTH_LONG).show();
        }

    }
}
