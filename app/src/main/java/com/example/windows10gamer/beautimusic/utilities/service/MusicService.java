package com.example.windows10gamer.beautimusic.utilities.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcelable;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.windows10gamer.beautimusic.R;
import com.example.windows10gamer.beautimusic.model.Song;
import com.example.windows10gamer.beautimusic.utilities.Utils;
import com.example.windows10gamer.beautimusic.utilities.singleton.SharedPrefs;
import com.example.windows10gamer.beautimusic.view.activity.HomeActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MusicService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {
    private RemoteViews remoteView;
    private RemoteViews remoteViewBig;
    private NotificationCompat.Builder nc;
    private NotificationManager nm;
    // notification
    public static final String NOTIFY_PREVIOUS = "com.example.windows10gamer.beautimusic.previous";
    public static final String NOTIFY_PLAY = "com.example.windows10gamer.beautimusic.play";
    public static final String NOTIFY_NEXT = "com.example.windows10gamer.beautimusic.next";
    private static final int NOTIFICATION_ID_CUSTOM_BIG = 9;

    public MediaPlayer mPlayer;
    public static List<Song> mSongList;
    private int mPosition;
    private String songTitle = "";
    private String artistTitle = "";
    private final IBinder musicBind = new MusicBinder();
    private boolean shuffle = false;
    private boolean repeat = false;
    private Random rand;


    @Override
    public void onCreate() {
        super.onCreate();

        rand = new Random();
        mPlayer = new MediaPlayer();

        initMusicPlayer();
        IntentFilter it = new IntentFilter();
        it.addAction(Utils.PAUSE_KEY);
        it.addAction(Utils.NOTIFI);
        it.addAction(Utils.NEXT_PLAY);
        it.addAction(Utils.PREVIOUS_PLAY);
        it.addAction(Utils.PLAY_KEY);
        registerReceiver(receiver, it);
    }


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case Utils.NOTIFI:
                    updateRemoteview();
                    break;
                case Utils.NEXT_PLAY:
                    playNext();
                    break;
                case Utils.PREVIOUS_PLAY:
                    playPrev();
                    break;
                case Utils.PAUSE_KEY:
                    pausePlayer();
                    break;
                case Utils.PLAY_KEY:
                    startPlayer();
                    break;
            }
        }
    };


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
        Log.d("Main", "disconect");
        return true;
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mPlayer.start();
        initNotification();

    }

    public List<Song> getList() {
        return mSongList;
    }

    private void initNotification() {
        remoteView = new RemoteViews(getApplicationContext().getPackageName(), R.layout.custom_small_notification);
        remoteViewBig = new RemoteViews(getApplicationContext().getPackageName(), R.layout.custom_notification);
        remoteView.setTextViewText(R.id.notifiNameSong, songTitle);
        remoteView.setTextViewText(R.id.notifiNameArtist, artistTitle);
        remoteView.setImageViewResource(R.id.notifiPlay, R.drawable.ic_pause_white_48dp);
        remoteViewBig.setTextViewText(R.id.notifiNameSong, songTitle);
        remoteViewBig.setTextViewText(R.id.notifiNameArtist, artistTitle);
        remoteViewBig.setImageViewResource(R.id.notifiPlay, R.drawable.ic_pause_white_48dp);

        setListener();

        nc = new NotificationCompat.Builder(getApplicationContext());
        nm = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notifyIntent = new Intent(getApplicationContext(), HomeActivity.class);

        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        nc.setOngoing(true);
        nc.setContentIntent(pendingIntent);
        nc.setSmallIcon(R.drawable.ic_play_arrow_white_48dp);
        nc.setCustomContentView(remoteView);
        nc.setCustomBigContentView(remoteViewBig);
        nc.setVisibility(Notification.VISIBILITY_PUBLIC);

        Notification notification = nc.build();
        startForeground(NOTIFICATION_ID_CUSTOM_BIG, notification);
    }

    private void setListener() {
        Intent previous = new Intent(NOTIFY_PREVIOUS);
        Intent next = new Intent(NOTIFY_NEXT);
        Intent play = new Intent(NOTIFY_PLAY);

        PendingIntent pPrevious = PendingIntent.getBroadcast(getApplicationContext(), 0, previous, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteView.setOnClickPendingIntent(R.id.notifiPrevious, pPrevious);
        remoteViewBig.setOnClickPendingIntent(R.id.notifiPrevious, pPrevious);

        PendingIntent pPlay = PendingIntent.getBroadcast(getApplicationContext(), 0, play, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteView.setOnClickPendingIntent(R.id.notifiPlay, pPlay);
        remoteViewBig.setOnClickPendingIntent(R.id.notifiPlay, pPlay);

        PendingIntent pNext = PendingIntent.getBroadcast(getApplicationContext(), 0, next, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteView.setOnClickPendingIntent(R.id.notifiNext, pNext);
        remoteViewBig.setOnClickPendingIntent(R.id.notifiNext, pNext);
    }

    public void updateRemoteview() {
        if (mPlayer.isPlaying()) {
            remoteView.setImageViewResource(R.id.notifiPlay, R.drawable.ic_pause_white_48dp);
            remoteViewBig.setImageViewResource(R.id.notifiPlay, R.drawable.ic_pause_white_48dp);
            Notification notification = nc.build();
            nm.notify(NOTIFICATION_ID_CUSTOM_BIG, notification);
        } else {
            remoteView.setImageViewResource(R.id.notifiPlay, R.drawable.ic_play_arrow_white_48dp);
            remoteViewBig.setImageViewResource(R.id.notifiPlay, R.drawable.ic_play_arrow_white_48dp);
            Notification notification = nc.build();
            nm.notify(NOTIFICATION_ID_CUSTOM_BIG, notification);
        }

    }

    public void setSongList(List<Song> songs) {
        mSongList = songs;
    }

    public List<Song> getSongList() {
        return mSongList;
    }

    public void setIndexPlay(int index) {
        mPosition = index;
    }


    public int getIndexPlay() {
        return mPosition;
    }

    public int getDuaration() {
        return Integer.valueOf(mSongList.get(mPosition).getDuaration());
    }

    public void pausePlayer() {
        mPlayer.pause();
        SharedPrefs.getInstance().put(Utils.STATUS_PLAY, false);

    }

    public void startPlayer() {
        mPlayer.start();
        SharedPrefs.getInstance().put(Utils.STATUS_PLAY, true);
    }

    public int getCurrentPosition() {
        return mPlayer.getCurrentPosition();
    }

    public void seek(int duaration) {
        mPlayer.seekTo(duaration);
    }

    public String nameArtist() {
        return mSongList.get(mPosition).getNameArtist();
    }

    public String nameSong() {
        return mSongList.get(mPosition).getNameSong();
    }

    public String getImageSong() {
        return mSongList.get(mPosition).getImageSong();
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
        songTitle = mSongList.get(mPosition).getNameSong();
        artistTitle = mSongList.get(mPosition).getNameArtist();
        mPlayer.reset();

        try {
            mPlayer.setDataSource(getApplicationContext(), Uri.parse(mSongList.get(mPosition).getFileSong()));
        } catch (Exception e) {

        }

        mPlayer.prepareAsync();

    }
    public void setShuffle(boolean shuffle){
        this.shuffle = shuffle;
    }
    public void setRepeat(boolean repeat){
        this.repeat = repeat;
    }

    public boolean isShuffle() {
        if (shuffle) {
            shuffle = false;
            return false;
        } else shuffle = true;
        return true;
    }

    public boolean isRepeat() {
        if (repeat) {
            repeat = false;
            return false;
        } else repeat = true;
        return true;
    }

    public boolean getRepeat() {
        return repeat;
    }

    public boolean getShuffle() {
        return shuffle;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mPlayer.stop();
        mPlayer.release();
        mPlayer = null;
    }
}
