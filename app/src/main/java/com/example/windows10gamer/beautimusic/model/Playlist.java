package com.example.windows10gamer.beautimusic.model;

/**
 * Created by Windows 10 Gamer on 06/10/2017.
 */

public class Playlist {
    private int idPlaylist;
    private String listIdSong;
    private String name;

    public Playlist(){

    }

    public Playlist(String listIdSong, String name) {
        this.listIdSong = listIdSong;
        this.name = name;
    }

    public Playlist(int idPlaylist, String listIdSong, String name) {
        this.idPlaylist = idPlaylist;
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

    public int getIdPlaylist() {
        return idPlaylist;
    }

    public void setIdPlaylist(int idPlaylist) {
        this.idPlaylist = idPlaylist;
    }
}