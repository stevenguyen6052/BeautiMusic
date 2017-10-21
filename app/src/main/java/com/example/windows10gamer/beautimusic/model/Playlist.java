package com.example.windows10gamer.beautimusic.model;

public class Playlist  {
    private int id;
    private String listIdSong;
    private String name;

    public Playlist(int id,String listIdSong,String name) {
        this.id = id;
        this.listIdSong = listIdSong;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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