package com.example.windows10gamer.beautimusic.database;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.MediaStore;

import com.example.windows10gamer.beautimusic.model.Album;
import com.example.windows10gamer.beautimusic.model.Artist;
import com.example.windows10gamer.beautimusic.model.Playlist;
import com.example.windows10gamer.beautimusic.model.Song;
import com.example.windows10gamer.beautimusic.utilities.Utils;

import java.util.ArrayList;
import java.util.List;


public class SongDatabase extends SQLiteOpenHelper {
    private static final String NAME_DATABASE = "Quan ly bai hat";
    private static final int VERSION = 1;

    private static final String TBL_SONG = "DetailSong";
    private static final String TBL_PLAYLIST = "Playlist";

    private static final String PL_ID = "Id";
    private static final String PL_LIST = "ListIdSong";
    private static final String PL_NAME_LIST = "NameList";

    private static final String CL_ID = "Id";
    private static final String CL_NAME_SONG = "Namesong";
    private static final String CL_NAME_ALBUM = "Namealbum";
    private static final String CL_NAME_ARTIST = "Nameartist";
    private static final String CL_IMAGE = "Image";
    private static final String CL_FILE_SONG = "Filesong";
    private static final String CL_DUARATION = "Duaration";
    private static final String CL_ALBUM_ID = "AlbumId";
    private static final String CL_ARTIST_ID = "ArtistId";
    private static final String CREATE_TBL_SOMG = "CREATE TABLE IF NOT EXISTS  " + TBL_SONG + "("
            + CL_ID + " TEXT PRIMARY KEY , "
            + CL_NAME_SONG + " TEXT NOT NULL, "
            + CL_NAME_ALBUM + " TEXT NOT NULL, "
            + CL_NAME_ARTIST + " TEXT NOT NULL, "
            + CL_FILE_SONG + " TEXT NOT NULL, "
            + CL_DUARATION + " TEXT NOT NULL, "
            + CL_IMAGE + " TEXT NOT NULL, "
            + CL_ALBUM_ID + " INTEGER NOT NULL, "
            + CL_ARTIST_ID + " INTEGER NOT NULL "
            + ")";
    private static final String CREATE_TBL_PLAYLIST = "CREATE TABLE IF NOT EXISTS  " + TBL_PLAYLIST + " ("
            + PL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + PL_LIST + " TEXT,  "
            + PL_NAME_LIST + " TEXT"
            + ")";


    public SongDatabase(Context context) {
        super(context, NAME_DATABASE, null, VERSION);
    }

    public void updatePlaylist(String listId, int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(PL_LIST, listId);
        db.update(TBL_PLAYLIST, args, PL_ID + " = ?", new String[]{String.valueOf(id)});

    }

    public void updateNamePlaylist(String namePlaylist, int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(PL_NAME_LIST, namePlaylist);
        db.update(TBL_PLAYLIST, args, PL_ID + " = " + id , null);
    }

    public void deletePlaylist(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TBL_PLAYLIST, PL_ID + " = " + id + "", null);
    }

    public void addPlayList(String nameList, String listId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(PL_LIST, listId);
        values.put(PL_NAME_LIST, nameList);

        db.insert(TBL_PLAYLIST, null, values);
        db.close();
    }

    public List<Playlist> getPlaylist() {
        List<Playlist> playlists = new ArrayList<>();
        String mSelect = "SELECT * FROM " + TBL_PLAYLIST;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mCursor = db.rawQuery(mSelect, null);
        if (mCursor.moveToFirst()) {
            do {
                Playlist playlist = new Playlist();
                playlist.setId(mCursor.getInt(0));
                playlist.setListIdSong(mCursor.getString(1));
                playlist.setName(mCursor.getString(2));
                playlists.add(playlist);
                //playlists.add(new Playlist(mCursor.getInt(0), mCursor.getString(1),mCursor.getString(2)));

            } while (mCursor.moveToNext());
        }
        mCursor.close();
        db.close();
        return playlists;
    }

    public void addNewSong(Song mSong) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(CL_ID, mSong.getId());
        values.put(CL_NAME_SONG, mSong.getNameSong());
        values.put(CL_NAME_ARTIST, mSong.getNameArtist());
        values.put(CL_NAME_ALBUM, mSong.getNameAlbum());
        values.put(CL_FILE_SONG, mSong.getFileSong());
        values.put(CL_DUARATION, mSong.getDuaration());
        values.put(CL_IMAGE, mSong.getImageSong());
        values.put(CL_ALBUM_ID, mSong.getAlbumId());
        values.put(CL_ARTIST_ID, mSong.getArtistId());
        db.insert(TBL_SONG, null, values);
        db.close();
    }

    public void deleteSong(Song mSong) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TBL_SONG, CL_ID + " = ?", new String[]{String.valueOf(mSong.getId())});
    }

    public List<Song> getAllSongFromAlbum(String params) {
        List<Song> mListSong = new ArrayList<>();
        String SELECT_SONGS = "SELECT * FROM " + TBL_SONG + " WHERE " + CL_NAME_ALBUM + " = '" + params + "' ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mCursor = db.rawQuery(SELECT_SONGS, null);
        if (mCursor.moveToFirst()) {
            do {
                Song mSong = new Song();
                mSong.setId(mCursor.getString(0));
                mSong.setNameSong(mCursor.getString(1));
                mSong.setNameAlbum(mCursor.getString(2));
                mSong.setNameArtist(mCursor.getString(3));
                mSong.setFileSong(mCursor.getString(4));
                mSong.setDuaration(mCursor.getString(5));
                mSong.setImageSong(mCursor.getString(6));
                mListSong.add(mSong);
            } while (mCursor.moveToNext());
        }
        Utils.sortCollection(mListSong);

        mCursor.close();
        db.close();
        return mListSong;
    }

    public static List<Album> getAlbumFromDevice(Context context) {
        List<Album> albumList = new ArrayList<>();
        Uri ART_CONTENT_URI = Uri.parse("content://media/external/audio/albumart");
        Cursor mCursor = context.getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[]{
                        BaseColumns._ID,
                        MediaStore.Audio.AlbumColumns.ALBUM,
                        MediaStore.Audio.AlbumColumns.ARTIST,

                }, null, null, android.provider.MediaStore.Audio.Albums.DEFAULT_SORT_ORDER);
        mCursor.moveToFirst();
        while (mCursor.moveToNext()) {
            int id = mCursor.getInt(0);
            String albumName = mCursor.getString(1);
            String artist = mCursor.getString(2);
            String image = ContentUris.withAppendedId(ART_CONTENT_URI, id).toString();
            Album album = new Album(id, albumName, artist, image);
            albumList.add(album);
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
        c.moveToFirst();
        while (c.moveToNext()) {
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
        }
        //Utils.sortCollection(mListSong);
        c.close();
        return mListSong;
    }

    public static ArrayList<Song> getAlbumSongs(int albumId, Context context) {
        Uri ART_CONTENT_URI = Uri.parse("content://media/external/audio/albumart");
        ArrayList<Song> mSongList = new ArrayList<Song>();
        final StringBuilder selection = new StringBuilder();
        Cursor mCursor;
        selection.append(MediaStore.Audio.AudioColumns.IS_MUSIC + "=1");
        selection.append(" AND " + MediaStore.Audio.AudioColumns.TITLE + " != ''");
        selection.append(" AND " + MediaStore.Audio.AudioColumns.ALBUM_ID + "=" + albumId);
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

        return mSongList;
    }

    public static List<Song> getArtistSong(int artistId, Context context) {
        Uri ART_CONTENT_URI = Uri.parse("content://media/external/audio/albumart");
        ArrayList<Song> mSongList = new ArrayList<Song>();
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
        return atristList;
    }


    public List<Album> getAllAlbum1() {
        List<Album> mAlbumList = new ArrayList<>();
        String mSelect = " SELECT " + CL_NAME_ALBUM + ", " + CL_NAME_ARTIST + " ," + CL_FILE_SONG + " FROM " + TBL_SONG + " GROUP BY " + CL_NAME_ARTIST + "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mCursor = db.rawQuery(mSelect, null);
        if (mCursor.moveToFirst()) {
            do {
                Album mAlbum = new Album();
                mAlbum.setNameAlbum(mCursor.getString(0));
                mAlbum.setNameArtist(mCursor.getString(1));
                mAlbum.setImage(mCursor.getString(2));
                mAlbumList.add(mAlbum);
            } while (mCursor.moveToNext());
        }
        Utils.sortCollectionAlbum(mAlbumList);
        mCursor.close();
        db.close();
        return mAlbumList;
    }

    public List<Artist> getAllArtist() {
        List<Artist> mArtistList = new ArrayList<>();
        String mSelect = " SELECT " + CL_NAME_ARTIST + ", COUNT(" + CL_ALBUM_ID + "),COUNT(" + CL_NAME_SONG + ")  FROM " + TBL_SONG + " GROUP BY " + CL_NAME_ARTIST + "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mCursor = db.rawQuery(mSelect, null);
        if (mCursor.moveToFirst()) {
            do {
                Artist mArtist = new Artist();
                mArtist.setNameArtist(mCursor.getString(0));
                mArtist.setSumSong(mCursor.getInt(1));
                mArtist.setSumAlbum(mCursor.getInt(2));
                mArtistList.add(mArtist);
            } while (mCursor.moveToNext());
        }
        Utils.sortCollectionArtist(mArtistList);
        mCursor.close();
        db.close();
        return mArtistList;
    }


    public List<Song> getAlLSongFromArtist(String params) {
        List<Song> mListSong = new ArrayList<>();
        String mSelect = "Select * from " + TBL_SONG + " WHERE " + CL_NAME_ARTIST + "='" + params + "' ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mCursor = db.rawQuery(mSelect, null);
        if (mCursor.moveToFirst()) {
            do {
                Song mSong = new Song();
                mSong.setId(mCursor.getString(0));
                mSong.setNameSong(mCursor.getString(1));
                mSong.setNameAlbum(mCursor.getString(2));
                mSong.setNameArtist(mCursor.getString(3));
                mSong.setFileSong(mCursor.getString(4));
                mSong.setDuaration(mCursor.getString(5));
                mSong.setImageSong(mCursor.getString(6));
                mListSong.add(mSong);
            } while (mCursor.moveToNext());
        }
        Utils.sortCollection(mListSong);
        mCursor.close();
        db.close();
        return mListSong;
    }

    public List<Song> getAllListSong() {
        List<Song> mSongList = new ArrayList<>();
        String mSelect = "SELECT * FROM " + TBL_SONG;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mCursor = db.rawQuery(mSelect, null);
        if (mCursor.moveToFirst()) {
            do {
                Song mSong = new Song();
                mSong.setId(mCursor.getString(0));
                mSong.setNameSong(mCursor.getString(1));
                mSong.setNameArtist(mCursor.getString(2));
                mSong.setNameAlbum(mCursor.getString(3));
                mSong.setFileSong(mCursor.getString(4));
                mSong.setDuaration(mCursor.getString(5));
                mSong.setImageSong(mCursor.getString(6));
                mSong.setAlbumId(mCursor.getInt(7));
                mSong.setArtistId(mCursor.getInt(8));
                mSongList.add(mSong);
            } while (mCursor.moveToNext());
        }
        Utils.sortCollection(mSongList);
        mCursor.close();
        db.close();
        return mSongList;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TBL_SOMG);
        db.execSQL(CREATE_TBL_PLAYLIST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
