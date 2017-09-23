package com.example.windows10gamer.beautimusic.model;


public class Artist {
    private String nameArtist;
    private int sumSong;
    private int sumAlbum;
    public Artist(){}

    public Artist(String na, int ss, int sa) {
        this.nameArtist = na;
        this.sumSong = ss;
        this.sumAlbum = sa;
    }

    public String getNameArtist() {
        return nameArtist;
    }

    public void setNameArtist(String nameArtist) {
        this.nameArtist = nameArtist;
    }

    public int getSumSong() {
        return sumSong;
    }

    public void setSumSong(int sumSong) {
        this.sumSong = sumSong;
    }

    public int getSumAlbum() {
        return sumAlbum;
    }

    public void setSumAlbum(int sumAlbum) {
        this.sumAlbum = sumAlbum;
    }
}
