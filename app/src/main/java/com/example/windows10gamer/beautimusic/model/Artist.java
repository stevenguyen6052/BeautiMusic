package com.example.windows10gamer.beautimusic.model;


public class Artist extends BaseModel {
    private int sumSong;
    private int sumAlbum;

    public Artist(int id, String nameArtist, int sumSong, int sumAlbum) {
        super(id, nameArtist);
        this.sumSong = sumSong;
        this.sumAlbum = sumAlbum;
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
