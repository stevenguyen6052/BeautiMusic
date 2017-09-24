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
import com.example.windows10gamer.beautimusic.model.Song;
import com.example.windows10gamer.beautimusic.view.utilities.InitClass;

import java.util.ArrayList;
import java.util.List;


public class SongDatabase extends SQLiteOpenHelper {
    private static final String NAME_DATABASE = "Quan ly bai hat";
    private static final int VERSION = 1;

    private static final String TBL_SONG = "DetailSong";

    private static final String CL_ID = "Id";
    private static final String CL_NAME_SONG = "Namesong";
    private static final String CL_NAME_ALBUM = "Namealbum";
    private static final String CL_NAME_ARTIST = "Nameartist";
    private static final String CL_IMAGE = "Image";
    private static final String CL_FILE_SONG = "Filesong";
    private static final String CL_DUARATION = "Duaration";
    private static final String CL_ALBUM_ID = "AlbumId";
    private static final String CL_ARTIST_ID = "ArtistId";
    private static final String CL_ALBUM_KEY = "AlbumKey";
    private static final String CREATE_TBL_SOMG = "CREATE TABLE " + TBL_SONG + "("
            + CL_ID + " TEXT PRIMARY KEY , "
            + CL_NAME_SONG + " TEXT NOT NULL, "
            + CL_NAME_ALBUM + " TEXT NOT NULL, "
            + CL_NAME_ARTIST + " TEXT NOT NULL, "
            + CL_FILE_SONG + " TEXT NOT NULL, "
            + CL_DUARATION + " INTEGER NOT NULL, "
            + CL_ALBUM_ID + " TEXT NOT NULL, "
            + CL_ARTIST_ID + " TEXT NOT NULL, "
            + CL_ALBUM_KEY + " TEXT NOT NULL, "
            + CL_IMAGE + " INTEGER NOT NULL "
            + ")";

    public SongDatabase(Context context) {
        super(context, NAME_DATABASE, null, VERSION);
    }

    public void addNewSong(Song mSong) {
        Song mSong1 = new Song();
        SQLiteDatabase db = this.getWritableDatabase();
        SQLiteDatabase db1 = this.getReadableDatabase();
        String mSelect = "Select * from " + TBL_SONG + "";
        Cursor mCursor = db1.rawQuery(mSelect, null);
        if (mCursor.moveToFirst()) {
            do {
                mSong1.setId(mCursor.getString(0));

            } while (mCursor.moveToNext());
        }
        if (mSong1.getId() != mSong.getId()) {
            ContentValues values = new ContentValues();
            values.put(CL_ID, mSong.getId());
            values.put(CL_NAME_SONG, mSong.getNameSong());
            values.put(CL_NAME_ALBUM, mSong.getNameAlbum());
            values.put(CL_NAME_ARTIST, mSong.getNameArtist());
            values.put(CL_FILE_SONG, mSong.getFileSong());
            values.put(CL_DUARATION, mSong.getDuaration());
            values.put(CL_IMAGE, mSong.getImageSong());
            db.insert(TBL_SONG, null, values);
            db.close();
        }

    }

    public List<Song> getSongFromNameSong(String params) {
        List<Song> mListSong = new ArrayList<>();
        String SELECT_SONGS = "SELECT * FROM " + TBL_SONG + " WHERE " + CL_NAME_ALBUM + " LIKE  '%" + params + "%' ";
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
                mSong.setImageSong(mCursor.getInt(6));
                mListSong.add(mSong);
            } while (mCursor.moveToNext());
        }
        InitClass.sortCollection(mListSong);
        mCursor.close();
        db.close();
        return mListSong;
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
                mSong.setImageSong(mCursor.getInt(6));
                mListSong.add(mSong);
            } while (mCursor.moveToNext());
        }
        InitClass.sortCollection(mListSong);

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
            Artist atrist = new Artist(cursor.getInt(0),cursor.getString(1),cursor.getInt(2),cursor.getInt(3));
            atristList.add(atrist);
        }
        return atristList;
    }




    public List<Album> getAllAlbum1() {
        List<Album> mAlbumList = new ArrayList<>();
        String mSelect = " SELECT " + CL_NAME_ALBUM + ", " + CL_NAME_ARTIST + " ,"+CL_FILE_SONG+" FROM " + TBL_SONG + " GROUP BY " + CL_NAME_ARTIST + "";
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
        InitClass.sortCollectionAlbum(mAlbumList);
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
        InitClass.sortCollectionArtist(mArtistList);
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
                mSong.setImageSong(mCursor.getInt(6));
                mListSong.add(mSong);
            } while (mCursor.moveToNext());
        }
        InitClass.sortCollection(mListSong);
        mCursor.close();
        db.close();
        return mListSong;
    }

    public List<Song> getAllListSong() {
        List<Song> mListSong = new ArrayList<>();
        String mSelect = "Select * from " + TBL_SONG + "";
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
                mSong.setImageSong(mCursor.getInt(6));
                mListSong.add(mSong);
            } while (mCursor.moveToNext());
        }
        InitClass.sortCollection(mListSong);
        mCursor.close();
        db.close();
        return mListSong;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TBL_SOMG);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
