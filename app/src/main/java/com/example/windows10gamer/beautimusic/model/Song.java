package com.example.windows10gamer.beautimusic.model;

import java.io.Serializable;

/**
 * Created by Windows 10 Gamer on 21/08/2017.
 */

public class Song implements Serializable {
    private String mId;
    private String mNameSong;
    private String mNameArtist;
    private String mNameAlbum;
    private String mFileSong;
    private String mDuaration;
    private String mAlbumId;
    private String mArtistId;
    private String mAlbumKey;
    private int mImage;

    public Song() {

    }

    public Song(String na,String nab,String aid){
        this.mNameArtist = na;
        this.mNameAlbum = nab;
        this.mAlbumId = aid;
    }

    public Song(String mId,String mNameSong, String mNameArtist, String mNameAlbum, String mFileSong,String mDuaration,String mAlbumId,String mArtistId,String mAlbumKey, int mImage) {
        this.mId = mId ;
        this.mNameSong = mNameSong;
        this.mNameArtist = mNameArtist;
        this.mNameAlbum = mNameAlbum;
        this.mFileSong = mFileSong;
        this.mDuaration = mDuaration;
        this.mAlbumId = mAlbumId;
        this.mArtistId = mArtistId;
        this.mAlbumKey = mAlbumKey;
        this.mImage = mImage;
    }

    public String getmAlbumKey() {
        return mAlbumKey;
    }

    public void setmAlbumKey(String mAlbumKey) {
        this.mAlbumKey = mAlbumKey;
    }

    public String getmAlbumId() {
        return mAlbumId;
    }

    public void setmAlbumId(String mAlbumId) {
        this.mAlbumId = mAlbumId;
    }

    public String getmArtistId() {
        return mArtistId;
    }

    public void setmArtistId(String mArtistId) {
        this.mArtistId = mArtistId;
    }

    public String getmDuaration() {
        return mDuaration;
    }

    public void setmDuaration(String mDuaration) {
        this.mDuaration = mDuaration;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmNameSong() {
        return mNameSong;
    }

    public void setmNameSong(String mNameSong) {
        this.mNameSong = mNameSong;
    }

    public String getmNameArtist() {
        return mNameArtist;
    }

    public void setmNameArtist(String mNameArtist) {
        this.mNameArtist = mNameArtist;
    }

    public String getmNameAlbum() {
        return mNameAlbum;
    }

    public void setmNameAlbum(String mNameAlbum) {
        this.mNameAlbum = mNameAlbum;
    }

    public String getmFileSong() {
        return mFileSong;
    }

    public void setmFileSong(String mFileSong) {
        this.mFileSong = mFileSong;
    }

    public int getmImage() {
        return mImage;
    }

    public void setmImage(int mImage) {
        this.mImage = mImage;
    }
}
