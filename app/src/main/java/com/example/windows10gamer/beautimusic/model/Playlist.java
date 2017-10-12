package com.example.windows10gamer.beautimusic.model;

import android.print.PageRange;

/**
 * Created by Windows 10 Gamer on 06/10/2017.
 */

public class Playlist {
    private int id;
    private String listIdSong;
    private String name;

    public Playlist(){

    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Playlist(int  id, String listIdSong, String name) {
        this.id = id;
        this.listIdSong = listIdSong;
        this.name = name;
    }

    public String getListIdSong() {
        return listIdSong;
    }

    public void setListIdSong(String listIdSong) {
        this.listIdSong = listIdSong;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



}