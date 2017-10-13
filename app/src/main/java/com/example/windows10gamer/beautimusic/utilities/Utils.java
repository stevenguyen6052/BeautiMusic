package com.example.windows10gamer.beautimusic.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.example.windows10gamer.beautimusic.model.Album;
import com.example.windows10gamer.beautimusic.model.Artist;
import com.example.windows10gamer.beautimusic.model.Playlist;
import com.example.windows10gamer.beautimusic.model.Song;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;


public class Utils {
    public static final String POSITION = "POSITION";
    // check to get data from fragment song,album,artist
    public final static String TAG = "TAG";
    public final static String PLAYLIST = "PLAYLIST";
    public final static String NAME_PLAYLIST = "NamePlaylist";
    public final static String TAG_SONG = "SONG";
    public final static String TAG_ARTIST = "ARTIST";
    public final static String TAG_ALBUM = "ALBUM";
    public static final String TAG_DETAIL = "DETAIL";
    public static final String LIST_SONG = "LISTSONG";
    public static final String SEARCH = "SEARCH";
    public static final String NAMEALBUM_ARTIST = "Name";
    public static final String NAME_ALBUM = "Name Album";
    public static final String NAME_ARTIST = "Name Artist";
    public static final String ALBUM_ID = "ALBUMID";
    public static final String ARTIST_ID = "ARTISTID";
    public static final String CHECK = "Check";
    public static final String EMPTY = "EMPTY";
    public static final String TRUE = "True";
    public static final String ITEM_CLICK = "Click";
    public static final String SHUFFLE_REPEAT = "ShuffleRepeat";
    public static final String SHUFFLE = "ShuffleOn";
    public static final String REPEAT = "RepeatOn";
    public static final String PLAY_KEY = "play";
    public static final String PAUSE_KEY = "pause";
    public static final String NOTIFI = "update.notifi";
    public static final String NEXT_PLAY = "nextplay";
    public static final String PREVIOUS_PLAY = "previousplay";

    public static boolean checkString(String s) {
        int sum = 0;

        String[] separated = s.split(" ");
        for (int i = 0; i > separated.length; i++) {
            if (separated[i].equals(" ")) {
                sum = sum + 1;
            }
        }
        if (sum == separated.length) {
            return true;
        } else {
            return false;
        }

    }


    public static void sortCollection(List<Song> mSongList) {
        Collections.sort(mSongList, new Comparator<Song>() {
            @Override
            public int compare(Song o1, Song o2) {
                return o1.getNameSong().compareTo(o2.getNameSong());
            }
        });
    }

    public static void sortCollectionAlbum(List<Album> mSongAlbum) {
        Collections.sort(mSongAlbum, new Comparator<Album>() {
            @Override
            public int compare(Album o1, Album o2) {
                return o1.getNameAlbum().compareTo(o2.getNameAlbum());
            }
        });
    }

    public static void sortCollectionArtist(List<Artist> mArtist) {
        Collections.sort(mArtist, new Comparator<Artist>() {
            @Override
            public int compare(Artist o1, Artist o2) {
                return o1.getNameArtist().compareTo(o2.getNameArtist());
            }
        });
    }


    public static String unAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("").replaceAll("Đ", "D").replaceAll("đ", "d");
    }

    public static List<Song> filter(List<Song> mlistSong, String query) {
        String s = Utils.unAccent(query.toLowerCase());
        List<Song> filteredModelList = new ArrayList<>();

        for (Song song : mlistSong) {
            String text = Utils.unAccent(song.getNameSong().toLowerCase());
            if (text.contains(s)) {
                filteredModelList.add(song);
            }
        }
        return filteredModelList;
    }

    public static List<Album> filterAlbum(List<Album> mAlbumList, String query) {
        String s = Utils.unAccent(query.toLowerCase());
        List<Album> filteredModelList = new ArrayList<>();

        for (Album album : mAlbumList) {
            String text = Utils.unAccent(album.getNameAlbum().toLowerCase());
            if (text.contains(s)) {
                filteredModelList.add(album);
            }
        }
        return filteredModelList;
    }
    public static List<Playlist> filterPlaylist(List<Playlist> playlists, String query) {
        String s = Utils.unAccent(query.toLowerCase());
        List<Playlist> filteredModelList = new ArrayList<>();

        for (Playlist playlist : playlists) {
            String text = Utils.unAccent(playlist.getName().toLowerCase());
            if (text.contains(s)) {
                filteredModelList.add(playlist);
            }
        }
        return filteredModelList;
    }

    public static List<Artist> filterArtist(List<Artist> mArtistList, String query) {
        String s = Utils.unAccent(query.toLowerCase());
        List<Artist> filteredModelList = new ArrayList<>();

        for (Artist artist : mArtistList) {
            String text = Utils.unAccent(artist.getNameArtist().toLowerCase());
            if (text.contains(s)) {
                filteredModelList.add(artist);
            }
        }
        return filteredModelList;
    }
}
