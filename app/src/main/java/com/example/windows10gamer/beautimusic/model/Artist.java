package com.example.windows10gamer.beautimusic.model;


public class Artist {
    private String nameArtist;
    private int sumSong;
    private int sumAlbum;
    public Artist(){}

    public Artist(String nameArtist, int sumSong, int sumAlbum) {
        this.nameArtist = nameArtist;
        this.sumSong = sumSong;
        this.sumAlbum = sumAlbum;
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
