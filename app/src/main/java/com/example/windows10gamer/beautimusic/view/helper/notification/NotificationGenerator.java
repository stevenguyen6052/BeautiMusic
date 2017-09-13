package com.example.windows10gamer.beautimusic.view.helper.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.example.windows10gamer.beautimusic.R;
import com.example.windows10gamer.beautimusic.model.Song;
import com.example.windows10gamer.beautimusic.view.activity.MainActivity;

import java.util.List;

/**
 * Created by Windows 10 Gamer on 11/09/2017.
 */

public class NotificationGenerator {
    public static final String NOTIFY_PREVIOUS = "com.example.windows10gamer.beautimusic.previous";
    public static final String NOTIFY_DELETE = "com.example.windows10gamer.beautimusic.delete";
    public static final String NOTIFY_PAUSE = "com.example.windows10gamer.beautimusic.pause";
    public static final String NOTIFY_PLAY = "com.example.windows10gamer.beautimusic.play";
    public static final String NOTIFY_NEXT = "com.example.windows10gamer.beautimusic.next";

    private static final int NOTIFICATION_ID_OPEN_ACTIVITY = 9;
    private static final int NOTIFICATION_ID_CUSTOM_BIG = 9;

    public static void openActivityNotification(Context context) {
        NotificationCompat.Builder nc = new NotificationCompat.Builder(context);
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notifyIntent = new Intent(context, MainActivity.class);

        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        nc.setContentIntent(pendingIntent);

        nc.setSmallIcon(R.mipmap.ic_launcher);
        nc.setAutoCancel(true);
        nc.setContentTitle("Notification Demo");
        nc.setContentText("Click please");
        nm.notify(NOTIFICATION_ID_OPEN_ACTIVITY, nc.build());

    }

    public static void customBigNotification(Context context) {
        RemoteViews expandedView = new RemoteViews(context.getPackageName(), R.layout.custom_notification);

        NotificationCompat.Builder nc = new NotificationCompat.Builder(context);
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notifyIntent = new Intent(context, MainActivity.class);

        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        nc.setContentIntent(pendingIntent);
        nc.setSmallIcon(R.drawable.playing);
        nc.setAutoCancel(true);
        nc.setCustomBigContentView(expandedView);
        nc.setContentTitle("Name Song");
        nc.setContentText("Name Artist");
        nc.getBigContentView().setTextViewText(R.id.notifiNameSong, "Adele");

        setListeners(expandedView, context);

        nm.notify(NOTIFICATION_ID_CUSTOM_BIG, nc.build());
    }

    private static void setListeners(RemoteViews view, Context context) {
        Intent previous = new Intent(NOTIFY_PREVIOUS);
        Intent next = new Intent(NOTIFY_NEXT);
        Intent play = new Intent(NOTIFY_PLAY);

//        PendingIntent pPrevious = PendingIntent.getBroadcast(context, 0, previous, PendingIntent.FLAG_UPDATE_CURRENT);
//        view.setOnClickPendingIntent(R.id.notifiPrevious, pPrevious);


        PendingIntent pPlay = PendingIntent.getBroadcast(context, 0, play, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.notifiPlay, pPlay);


//        PendingIntent pNext = PendingIntent.getBroadcast(context, 0, next, PendingIntent.FLAG_UPDATE_CURRENT);
//        view.setOnClickPendingIntent(R.id.notifiNext, pNext);

    }
}
