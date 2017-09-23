package com.example.windows10gamer.beautimusic.view.utilities;

import com.example.windows10gamer.beautimusic.model.Album;
import com.example.windows10gamer.beautimusic.model.Artist;
import com.example.windows10gamer.beautimusic.model.Song;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class InitClass {
    public static void sortCollection(List<Song> mSongList){
        Collections.sort(mSongList, new Comparator<Song>() {
            @Override
            public int compare(Song o1, Song o2) {
                return o1.getmNameSong().compareTo(o2.getmNameSong());
            }
        });
    }
    public static void sortCollectionAlbum(List<Album> mSongAlbum){
        Collections.sort(mSongAlbum, new Comparator<Album>() {
            @Override
            public int compare(Album o1, Album o2) {
                return o1.getNameAlbum().compareTo(o2.getNameAlbum());
            }
        });
    }
    public static void sortCollectionArtist(List<Artist> mArtist){
        Collections.sort(mArtist, new Comparator<Artist>() {
            @Override
            public int compare(Artist o1, Artist o2) {
                return o1.getNameArtist().compareTo(o2.getNameArtist());
            }
        });
    }
}
