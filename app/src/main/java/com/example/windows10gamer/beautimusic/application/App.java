package com.example.windows10gamer.beautimusic.application;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import com.example.windows10gamer.beautimusic.utilities.service.MusicService;

public class App extends Application {

    private boolean isConnected = false;
    private MusicService mService;
    private ServiceConnection serviceConnection;
    public void onCreate() {
        super.onCreate();

        connectServiceAndPlay();

    }

    private void connectServiceAndPlay() {
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                MusicService.MusicBinder musicBinder = (MusicService.MusicBinder) service;
                mService = (MusicService) musicBinder.getService();
                isConnected = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                isConnected = false;
                mService = null;

            }
        };
        Intent it = new Intent(this, MusicService.class);
        bindService(it, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    public MusicService getService() {
        return mService;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        unbindService(serviceConnection);

    }


}