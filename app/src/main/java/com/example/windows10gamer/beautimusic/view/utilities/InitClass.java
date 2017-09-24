package com.example.windows10gamer.beautimusic.view.utilities;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.example.windows10gamer.beautimusic.model.Album;
import com.example.windows10gamer.beautimusic.model.Artist;
import com.example.windows10gamer.beautimusic.model.Song;

import java.text.Normalizer;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;


public class InitClass {
    public static final String POSITION = "POSITION";
    // check to get data from fragment song,album,artist
    public final static String TAG = "TAG";
    public final static String TAG_SONG = "SONG";
    public final static String TAG_ARTIST = "ARTIST";
    public final static String TAG_ALBUM = "ALBUM";
    public static final String TAG_DETAIL = "DETAIL";
    public static final String LIST_SONG = "LISTSONG";
    public static final String SEARCH = "SEARCH";
    public static final String NAMEALBUM_ARTIST = "Name";
    public static final String NAME_ALBUM = "Name Album";
    public static final String NAME_ARTIST = "Name Artist";

    public static void sortCollection(List<Song> mSongList){
        Collections.sort(mSongList, new Comparator<Song>() {
            @Override
            public int compare(Song o1, Song o2) {
                return o1.getNameSong().compareTo(o2.getNameSong());
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

    //resize bitmap
    public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }
    public static String unAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("").replaceAll("Đ", "D").replaceAll("đ", "d");
    }
}
