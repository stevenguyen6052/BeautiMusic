package com.example.windows10gamer.beautimusic.model;



public class Album {
    private String nameAlbum;
    private String nameArtist;
    private int sumSong;

    public Album(String ns,String na, int ss) {
        this.nameAlbum = ns;
        this.nameArtist = na;
        this.sumSong = ss;
    }
    public Album(){

    }

    public String getNameArtist() {
        return nameArtist;
    }

    public void setNameArtist(String nameArtist) {
        this.nameArtist = nameArtist;
    }

    public String getNameAlbum() {
        return nameAlbum;
    }

    public void setNameAlbum(String nameAlbum) {
        this.nameAlbum = nameAlbum;
    }

    public int getSumSong() {
        return sumSong;
    }

    public void setSumSong(int sumSong) {
        this.sumSong = sumSong;
    }
}
