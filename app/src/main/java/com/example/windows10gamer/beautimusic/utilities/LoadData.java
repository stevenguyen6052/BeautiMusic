package com.example.windows10gamer.beautimusic.utilities;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import com.example.windows10gamer.beautimusic.model.Album;
import com.example.windows10gamer.beautimusic.model.Artist;
import com.example.windows10gamer.beautimusic.model.Song;
import java.util.ArrayList;
import java.util.List;

public class LoadData {
    public static final String AND = " AND ";

    public static List<Album> getAlbumFromDevice(Context context) {
        List<Album> albumList = new ArrayList<>();
        Uri ART_CONTENT_URI = Uri.parse("content://media/external/audio/albumart");
        Cursor mCursor = context.getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[]{
                        BaseColumns._ID,
                        MediaStore.Audio.AlbumColumns.ALBUM,
                        MediaStore.Audio.AlbumColumns.ARTIST,

                }, null, null, android.provider.MediaStore.Audio.Albums.DEFAULT_SORT_ORDER);

        if (mCursor != null && mCursor.moveToNext()) {
            do {
                int id = mCursor.getInt(0);
                String albumName = mCursor.getString(1);
                String artist = mCursor.getString(2);
                String image = ContentUris.withAppendedId(ART_CONTENT_URI, id).toString();
                Album album = new Album(id, albumName, artist, image);
                albumList.add(album);
            } while (mCursor.moveToNext());
        }
        mCursor.close();
        return albumList;
    }

    public static List<Song> getSongFromDevice(Context context) {
        Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Uri ART_CONTENT_URI = Uri.parse("content://media/external/audio/albumart");
        List<Song> mListSong = new ArrayList<>();
        String[] m_data = {MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.ARTIST_ID,
        };

        Cursor c = context.getContentResolver().query(uri, m_data, android.provider.MediaStore.Audio.Media.IS_MUSIC + "=1", null, android.provider.MediaStore.Audio.Media.TITLE + " ASC");

        if (c != null && c.moveToNext()) {
            do {
                String name, album, artist, path, id, duration;
                int albumId, artistId;

                id = c.getString(0);
                name = c.getString(1);
                album = c.getString(2);
                artist = c.getString(3);
                duration = c.getString(4);
                path = c.getString(5);
                albumId = c.getInt(6);
                artistId = c.getInt(7);
                String image = ContentUris.withAppendedId(ART_CONTENT_URI, albumId).toString();

                Song song = new Song(id, name, artist, album, path, duration, image, albumId, artistId);
                mListSong.add(song);
            } while (c.moveToNext());
        }
        //Utils.sortCollection(mListSong);
        c.close();
        return mListSong;
    }

    public static ArrayList<Song> getAlbumSongs(int albumId, Context context) {
        Uri ART_CONTENT_URI = Uri.parse("content://media/external/audio/albumart");
        ArrayList<Song> mSongList = new ArrayList<>();
        final StringBuilder selection = new StringBuilder();
        Cursor mCursor;
        selection.append(MediaStore.Audio.AudioColumns.IS_MUSIC + "=1");
        selection.append(AND + MediaStore.Audio.AudioColumns.TITLE + " != ''");
        selection.append(AND + MediaStore.Audio.AudioColumns.ALBUM_ID + "=" + albumId);
        mCursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Media._ID,
                        MediaStore.Audio.Media.TITLE,
                        MediaStore.Audio.Media.ALBUM,
                        MediaStore.Audio.Media.ARTIST,
                        MediaStore.Audio.Media.DURATION,
                        MediaStore.Audio.Media.DATA,
                        MediaStore.Audio.Media.ALBUM_ID,
                        MediaStore.Audio.Media.ARTIST_ID,},
                selection.toString(), null, android.provider.MediaStore.Audio.Media.TRACK + ", " +
                        android.provider.MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        if (mCursor != null && mCursor.moveToFirst()) {
            do {
                String name, album, artist, path, id, duration;
                int albumid, artistId;

                id = mCursor.getString(0);
                name = mCursor.getString(1);
                album = mCursor.getString(2);
                artist = mCursor.getString(3);
                duration = mCursor.getString(4);
                path = mCursor.getString(5);
                albumid = mCursor.getInt(6);
                artistId = mCursor.getInt(7);
                String image = ContentUris.withAppendedId(ART_CONTENT_URI, albumId).toString();

                Song song = new Song(id, name, artist, album, path, duration, image, albumid, artistId);
                mSongList.add(song);
            } while (mCursor.moveToNext());
        }
        mCursor.close();

        return mSongList;
    }

    public static List<Song> getArtistSong(int artistId, Context context) {
        Uri ART_CONTENT_URI = Uri.parse("content://media/external/audio/albumart");
        ArrayList<Song> mSongList = new ArrayList<>();
        Cursor mCursor;
        final StringBuilder selection = new StringBuilder();
        selection.append(android.provider.MediaStore.Audio.AudioColumns.IS_MUSIC + "=1");
        selection.append(" AND " + android.provider.MediaStore.Audio.AudioColumns.TITLE + " != ''");
        selection.append(" AND " + android.provider.MediaStore.Audio.AudioColumns.ARTIST_ID + "=" + artistId);

        mCursor = context.getContentResolver().query(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Media._ID,
                        MediaStore.Audio.Media.TITLE,
                        MediaStore.Audio.Media.ALBUM,
                        MediaStore.Audio.Media.ARTIST,
                        MediaStore.Audio.Media.DURATION,
                        MediaStore.Audio.Media.DATA,
                        MediaStore.Audio.Media.ALBUM_ID,
                        MediaStore.Audio.Media.ARTIST_ID,
                },
                selection.toString(), null, android.provider.MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        if (mCursor != null && mCursor.moveToFirst()) {
            do {
                String name, album, artist, path, id, duration;
                int albumid, artistId1;

                id = mCursor.getString(0);
                name = mCursor.getString(1);
                album = mCursor.getString(2);
                artist = mCursor.getString(3);
                duration = mCursor.getString(4);
                path = mCursor.getString(5);
                albumid = mCursor.getInt(6);
                artistId1 = mCursor.getInt(7);
                String image = ContentUris.withAppendedId(ART_CONTENT_URI, albumid).toString();

                Song song = new Song(id, name, artist, album, path, duration, image, albumid, artistId1);
                mSongList.add(song);
            } while (mCursor.moveToNext());
        }
        mCursor.close();
        return mSongList;

    }

    public static List<Artist> getArtistFromDevice(Context context) {
        List<Artist> atristList = new ArrayList<>();
        Uri uri = android.provider.MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
        String[] mdata = new String[]{
          /* 0 */ BaseColumns._ID,
          /* 1 */ android.provider.MediaStore.Audio.ArtistColumns.ARTIST,
          /* 2 */ android.provider.MediaStore.Audio.ArtistColumns.NUMBER_OF_ALBUMS,
          /* 3 */ android.provider.MediaStore.Audio.ArtistColumns.NUMBER_OF_TRACKS
        };
        Cursor cursor = context.getContentResolver().query(uri, mdata, null, null, MediaStore.Audio.Artists.DEFAULT_SORT_ORDER);
        while (cursor.moveToNext()) {
            Artist atrist = new Artist(cursor.getInt(0), cursor.getString(1), cursor.getInt(3), cursor.getInt(2));
            atristList.add(atrist);
        }
        cursor.close();
        return atristList;
    }
}
