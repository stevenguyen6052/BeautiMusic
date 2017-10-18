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
    public final static String PLAYLIST = "Playlist";
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
    public static final String SHARE_PREFERENCE = "share";
    public static final String STATUS_PLAY = "StatusPlay";
    public static final String START = "Start";
    public static final String INPUT_NAME_PLAYSLIST = "Please input name playlist !";
    public static final String INPUT_ALL_SPACE = "Input all space, Please Input again!";
    public static final String ADDED_TO_PLAYLIST = "Added into playlist !";
    public static final String EMPTY = "";
    public static final String UPDEATED = "Updated success !";
    public static final String SONGS = "Songs";
    public static final String ALBUMS = "Albums";
    public static final String ALBUMS_ = " Albums |";
    public static final String ARTIST = "Artist";
    public static final String NO_PLAYLIST = "Don't have any playlist!";
    public static final String NOTIFY_PREVIOUS = "com.example.windows10gamer.beautimusic.previous";
    public static final String NOTIFY_PLAY = "com.example.windows10gamer.beautimusic.play";
    public static final String NOTIFY_NEXT = "com.example.windows10gamer.beautimusic.next";
    public static final String CHECKED_PLAY = "CheckedPlay";

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
