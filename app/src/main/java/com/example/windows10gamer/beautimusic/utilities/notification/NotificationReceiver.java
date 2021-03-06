package com.example.windows10gamer.beautimusic.utilities.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.example.windows10gamer.beautimusic.utilities.Utils;
import com.example.windows10gamer.beautimusic.utilities.singleton.SharedPrefs;
import static com.example.windows10gamer.beautimusic.utilities.Utils.NOTIFY_NEXT;
import static com.example.windows10gamer.beautimusic.utilities.Utils.NOTIFY_PLAY;
import static com.example.windows10gamer.beautimusic.utilities.Utils.NOTIFY_PREVIOUS;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        switch (intent.getAction()) {
            case NOTIFY_PLAY:
                if (SharedPrefs.getInstance().get(Utils.STATUS_PLAY, Boolean.class, false)) {
                    context.sendBroadcast(new Intent().setAction(Utils.PAUSE_KEY));
                    context.sendBroadcast(new Intent().setAction(Utils.NOTIFI));
                } else {
                    context.sendBroadcast(new Intent().setAction(Utils.PLAY_KEY));
                    context.sendBroadcast(new Intent().setAction(Utils.NOTIFI));
                }
                break;

            case NOTIFY_NEXT:
                context.sendBroadcast(new Intent().setAction(Utils.NEXT_PLAY));
                break;

            case NOTIFY_PREVIOUS:
                context.sendBroadcast(new Intent().setAction(Utils.PREVIOUS_PLAY));
                break;


        }
    }
}
