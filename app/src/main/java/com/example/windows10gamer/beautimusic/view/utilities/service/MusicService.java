package com.example.windows10gamer.beautimusic.view.utilities.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.windows10gamer.beautimusic.R;
import com.example.windows10gamer.beautimusic.model.Song;
import com.example.windows10gamer.beautimusic.view.activity.MainActivity;

import java.util.List;
import java.util.Random;


public class MusicService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {
    // tag check playmusic error
    private static final String TAG_CHECK_BUG = "MainActivity";
    public static String TAG = "";
    // notification
    public static final String NOTIFY_PREVIOUS = "com.example.windows10gamer.beautimusic.previous";
    public static final String NOTIFY_PAUSE = "com.example.windows10gamer.beautimusic.pause";
    public static final String NOTIFY_PLAY = "com.example.windows10gamer.beautimusic.play";
    public static final String NOTIFY_NEXT = "com.example.windows10gamer.beautimusic.next";
    public static final String NOTIFI_STOP = "com.example.windows10gamer.beautimusic.stop";
    // notification lock screen
    private MediaSession mediaSession;
    private MediaController mediaController;


    private static final int NOTIFICATION_ID_OPEN_ACTIVITY = 9;
    private static final int NOTIFICATION_ID_CUSTOM_BIG = 9;

    //media player
    public static MediaPlayer mPlayer;
    //song list
    public static List<Song> mSongList;
    //current position
    public static int mPosition;

    private String songTitle = "";
    private String artistTitle = "";

    private final IBinder musicBind = new MusicBinder();

    private static final int NOTIFY_ID = 1;

    //shuffle flag and random
    private boolean shuffle = false;
    private boolean repeat = false;
    private Random rand;

    @Override
    public void onCreate() {
        super.onCreate();
        rand = new Random();
        //create player
        mPlayer = new MediaPlayer();
        initMusicPlayer();
    }

    private void initMusicPlayer() {
        mPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        //set listeners
        mPlayer.setOnPreparedListener(this);
        mPlayer.setOnErrorListener(this);
    }

    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

        @Override
    public boolean onUnbind(Intent intent) {

        return super.onUnbind(intent);
    }
    public void stopMusic() {
        mPlayer.stop();
        mPlayer.release();
        mPlayer = null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.d(TAG_CHECK_BUG, "Playback Error");
        if (mPlayer != null) {
            try {
                mPlayer.stop();
                mPlayer.release();
            } finally {
                mPlayer = null;
            }
        }
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mPlayer.start();
        // create notification
        RemoteViews remoteView = new RemoteViews(getApplicationContext().getPackageName(), R.layout.custom_notification);
        setListeners(remoteView, getApplicationContext());


        NotificationCompat.Builder nc = new NotificationCompat.Builder(getApplicationContext());
        NotificationManager nm = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notifyIntent = new Intent(getApplicationContext(), MainActivity.class);

        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        nc.setVisibility(Notification.VISIBILITY_PUBLIC);
        nc.setOngoing(true);
        nc.setContentIntent(pendingIntent);
        nc.setSmallIcon(R.drawable.ic_play_arrow_white_48dp);
        nc.setAutoCancel(true);
        nc.setCustomBigContentView(remoteView);
        nc.setContentTitle("Playing");
        nc.setContentText(songTitle);
        remoteView.setTextViewText(R.id.notifiNameSong, songTitle);
        remoteView.setTextViewText(R.id.notifiNameArtist, artistTitle);

        Notification notification = nc.build();
        startForeground(NOTIFICATION_ID_CUSTOM_BIG, notification);

        // create notification in lockscree

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initMediSession() {
        mediaSession = new MediaSession(getApplicationContext(), "EXAMPLE");

        mediaSession.setCallback(new MediaSession.Callback() {
            @Override
            public void onPlay() {
                super.onPlay();
                //buildNotification();
            }
        });
    }

    // create actions listener service from notification
    private void setListeners(RemoteViews expandedView, Context applicationContext) {
        int playButtonResId = mPlayer.isPlaying()
                ? R.drawable.playing : R.drawable.pause;
        Intent previous = new Intent(NOTIFY_PREVIOUS);
        Intent next = new Intent(NOTIFY_NEXT);
        Intent play = new Intent(NOTIFY_PLAY);
        PendingIntent pPrevious = PendingIntent.getBroadcast(applicationContext, 0, previous, PendingIntent.FLAG_UPDATE_CURRENT);
        expandedView.setOnClickPendingIntent(R.id.notifiPrevious, pPrevious);

        PendingIntent pPlay = PendingIntent.getBroadcast(applicationContext, 0, play, PendingIntent.FLAG_UPDATE_CURRENT);
        expandedView.setOnClickPendingIntent(R.drawable.playing, pPlay);

        PendingIntent pNext = PendingIntent.getBroadcast(applicationContext, 0, next, PendingIntent.FLAG_UPDATE_CURRENT);
        expandedView.setOnClickPendingIntent(R.id.notifiNext, pNext);
    }


    public int getDuaration() {
        return Integer.valueOf(mSongList.get(mPosition).getmDuaration());
    }

    public void pausePlayer() {
        mPlayer.pause();
    }

    public void startPlayer() {
        mPlayer.start();
    }

    public int getCurrentPosition() {
        return mPlayer.getCurrentPosition();
    }

    public void seek(int duaration) {
        mPlayer.seekTo(duaration);
    }


    public void playPrev() {
        if (repeat) {

        } else {
            if (shuffle) {
                int newSong = mPosition;
                while (newSong == mPosition) {
                    newSong = rand.nextInt(mSongList.size());
                }
                mPosition = newSong;
            } else {
                mPosition--;
                if (mPosition < 0) mPosition = mSongList.size() - 1;
            }
        }
        playSong();
    }

    public String pathSong() {
        return mSongList.get(mPosition).getmFileSong();
    }

    public String nameArtist() {
        return mSongList.get(mPosition).getmNameArtist();
    }

    public String nameSong() {
        return mSongList.get(mPosition).getmNameSong();
    }

    public void setOnComplete() {
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playNext();
            }
        });
    }

    public boolean isPlaying() {
        return mPlayer.isPlaying();
    }

    public void playNext() {
        if (repeat) {

        } else {
            if (shuffle) {
                int newSong = mPosition;
                while (newSong == mPosition) {
                    newSong = rand.nextInt(mSongList.size());
                }
                mPosition = newSong;
            } else {
                mPosition++;
                if (mPosition >= mSongList.size()) mPosition = 0;
            }
        }
        playSong();
    }

    public void playSong() {
        songTitle = mSongList.get(mPosition).getmNameSong();
        artistTitle = mSongList.get(mPosition).getmNameArtist();
        mPlayer.reset();

        try {
            mPlayer.setDataSource(getApplicationContext(), Uri.parse(mSongList.get(mPosition).getmFileSong()));
        } catch (Exception e) {
            Log.d(TAG_CHECK_BUG, "Error setting data source", e);
        }

        mPlayer.prepareAsync();
    }

    public boolean setShuffle() {

        if (shuffle) {
            shuffle = false;
            return false;
        } else shuffle = true;
        return true;
    }

    public boolean setRepeat() {
        if (repeat) {
            repeat = false;
            return false;
        } else repeat = true;
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void handleIntent(Intent intent) {
        if (intent == null) {
            return;
        }
        String action = intent.getAction();
        if (action.equalsIgnoreCase(NOTIFY_PLAY)) {
            mediaController.getTransportControls().play();
        } else if (action.equalsIgnoreCase(NOTIFY_PAUSE)) {
            mediaController.getTransportControls().pause();
        } else if (action.equalsIgnoreCase(NOTIFY_NEXT)) {
            mediaController.getTransportControls().skipToNext();
        } else if (action.equalsIgnoreCase(NOTIFY_PREVIOUS)) {
            mediaController.getTransportControls().skipToPrevious();
        } else if (action.equalsIgnoreCase(NOTIFI_STOP)) {
            mediaController.getTransportControls().stop();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void buildNotification(Notification.Action action) {
        Notification.MediaStyle style = new Notification.MediaStyle();
        Intent intent = new Intent(getApplicationContext(), MusicService.class);
        intent.setAction(NOTIFI_STOP);
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 1, intent, 0);
        Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.playing)
                .setContentTitle("Lock screen")
                .setContentText(" la day")
                .setDeleteIntent(pendingIntent)
                .setStyle(style);
        // builder.addAction((android.R.drawable.ic_media_previous,"Previous",NOTIFY_PREVIOUS );
        builder.addAction(action);
        //builder.addAction((android.R.drawable.ic_media_next,"Previous",NOTIFY_NEXT );
        style.setShowActionsInCompactView(0, 1, 2);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG_CHECK_BUG, "Service is destroyed");
        stopForeground(true);
        mPlayer.stop();
        mPlayer.release();
        mPlayer = null;

    }
}
