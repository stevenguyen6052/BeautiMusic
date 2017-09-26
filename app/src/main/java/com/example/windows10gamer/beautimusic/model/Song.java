package com.example.windows10gamer.beautimusic.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Windows 10 Gamer on 21/08/2017.
 */

public class Song implements Parcelable {
    private String id;
    private String nameSong;
    private String nameArtist;
    private String nameAlbum;
    private String fileSong;
    private String duaration;
    private int imageSong;
    private int albumId;
    private int artistId;


    public Song() {

    }

    public Song(String na,String nab,String aid){
        this.nameArtist = na;
        this.nameAlbum = nab;

    }
    public Song(String mId,String mNameSong, String mNameArtist, String mNameAlbum, String mFileSong,String mDuaration,int albumId,int artistId) {
        this.id = mId ;
        this.nameSong = mNameSong;
        this.nameArtist = mNameArtist;
        this.nameAlbum = mNameAlbum;
        this.fileSong = mFileSong;
        this.duaration = mDuaration;
        this.albumId = albumId;
        this.artistId = artistId;

    }

    public Song(String mId,String mNameSong, String mNameArtist, String mNameAlbum, String mFileSong,String mDuaration, int mImage,int albumId,int artistId) {
        this.id = mId ;
        this.nameSong = mNameSong;
        this.nameArtist = mNameArtist;
        this.nameAlbum = mNameAlbum;
        this.fileSong = mFileSong;
        this.duaration = mDuaration;
        this.imageSong = mImage;
        this.albumId = albumId;
        this.artistId = artistId;
    }
    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel source) {
            return new Song(source);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public int getArtistId() {
        return artistId;
    }

    public void setArtistId(int artistId) {
        this.artistId = artistId;
    }

    public Song(Parcel source) {
        id = source.readString();
        nameSong = source.readString();
        nameArtist = source.readString();
        nameAlbum = source.readString();
        fileSong = source.readString();
        duaration = source.readString();
        imageSong = source.readInt();
        albumId = source.readInt();
        artistId = source.readInt();
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameSong() {
        return nameSong;
    }

    public void setNameSong(String nameSong) {
        this.nameSong = nameSong;
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

    public String getFileSong() {
        return fileSong;
    }

    public void setFileSong(String fileSong) {
        this.fileSong = fileSong;
    }

    public String getDuaration() {
        return duaration;
    }

    public void setDuaration(String duaration) {
        this.duaration = duaration;
    }

    public int getImageSong() {
        return imageSong;
    }

    public void setImageSong(int imageSong) {
        this.imageSong = imageSong;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(nameSong);
        dest.writeString(nameArtist);
        dest.writeString(nameAlbum);
        dest.writeString(fileSong);
        dest.writeString(duaration);
        dest.writeInt(imageSong);
        dest.writeInt(albumId);
        dest.writeInt(artistId);
    }
}
