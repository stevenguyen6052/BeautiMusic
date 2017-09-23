package com.example.windows10gamer.beautimusic.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
                mSong1.setmId(mCursor.getString(0));
                mSong1.setmNameSong(mCursor.getString(1));
                mSong1.setmNameAlbum(mCursor.getString(2));
                mSong1.setmNameArtist(mCursor.getString(3));
                mSong1.setmFileSong(mCursor.getString(4));
                mSong1.setmDuaration(mCursor.getString(5));
                mSong1.setmAlbumId(mCursor.getString(6));
                mSong1.setmArtistId(mCursor.getString(7));
                mSong1.setmAlbumKey(mCursor.getString(8));
                mSong1.setmImage(mCursor.getInt(9));

            } while (mCursor.moveToNext());
        }
        if (mSong1.getmId() != mSong.getmId()) {
            ContentValues values = new ContentValues();
            values.put(CL_ID, mSong.getmId());
            values.put(CL_NAME_SONG, mSong.getmNameSong());
            values.put(CL_NAME_ALBUM, mSong.getmNameAlbum());
            values.put(CL_NAME_ARTIST, mSong.getmNameArtist());
            values.put(CL_FILE_SONG, mSong.getmFileSong());
            values.put(CL_DUARATION, mSong.getmDuaration());
            values.put(CL_ALBUM_ID, mSong.getmAlbumId());
            values.put(CL_ARTIST_ID, mSong.getmArtistId());
            values.put(CL_ALBUM_KEY, mSong.getmAlbumKey());
            values.put(CL_IMAGE, mSong.getmImage());
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
                mSong.setmId(mCursor.getString(0));
                mSong.setmNameSong(mCursor.getString(1));
                mSong.setmNameAlbum(mCursor.getString(2));
                mSong.setmNameArtist(mCursor.getString(3));
                mSong.setmFileSong(mCursor.getString(4));
                mSong.setmDuaration(mCursor.getString(5));
                mSong.setmAlbumId(mCursor.getString(6));
                mSong.setmArtistId(mCursor.getString(7));
                mSong.setmAlbumKey(mCursor.getString(8));
                mSong.setmImage(mCursor.getInt(9));
                mListSong.add(mSong);
            } while (mCursor.moveToNext());
        }
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
                mSong.setmId(mCursor.getString(0));
                mSong.setmNameSong(mCursor.getString(1));
                mSong.setmNameAlbum(mCursor.getString(2));
                mSong.setmNameArtist (mCursor.getString(3));
                mSong.setmFileSong(mCursor.getString(4));
                mSong.setmDuaration(mCursor.getString(5));
                mSong.setmAlbumId(mCursor.getString(6));
                mSong.setmArtistId(mCursor.getString(7));
                mSong.setmAlbumKey(mCursor.getString(8));
                mSong.setmImage(mCursor.getInt(9));
                mListSong.add(mSong);
            } while (mCursor.moveToNext());
        }
        InitClass.sortCollection(mListSong);

        mCursor.close();
        db.close();
        return mListSong;
    }



    public List<Album> getAllAlbum1() {
        List<Album> mAlbumList = new ArrayList<>();
        String mSelect = " SELECT " + CL_NAME_ALBUM + ", " + CL_NAME_ARTIST + " ,COUNT(" + CL_ID + ") FROM " + TBL_SONG + " GROUP BY " + CL_NAME_ARTIST + "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mCursor = db.rawQuery(mSelect, null);
        if (mCursor.moveToFirst()) {
            do {
                Album mAlbum = new Album();
                mAlbum.setNameAlbum(mCursor.getString(0));
                mAlbum.setNameArtist(mCursor.getString(1));
                mAlbum.setSumSong(mCursor.getInt(2));
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
                mSong.setmId(mCursor.getString(0));
                mSong.setmNameSong(mCursor.getString(1));
                mSong.setmNameAlbum(mCursor.getString(2));
                mSong.setmNameArtist(mCursor.getString(3));
                mSong.setmFileSong(mCursor.getString(4));
                mSong.setmDuaration(mCursor.getString(5));
                mSong.setmAlbumId(mCursor.getString(6));
                mSong.setmArtistId(mCursor.getString(7));
                mSong.setmAlbumKey(mCursor.getString(8));
                mSong.setmImage(mCursor.getInt(9));
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
                mSong.setmId(mCursor.getString(0));
                mSong.setmNameSong(mCursor.getString(1));
                mSong.setmNameAlbum(mCursor.getString(2));
                mSong.setmNameArtist(mCursor.getString(3));
                mSong.setmFileSong(mCursor.getString(4));
                mSong.setmDuaration(mCursor.getString(5));
                mSong.setmAlbumId(mCursor.getString(6));
                mSong.setmArtistId(mCursor.getString(7));
                mSong.setmAlbumKey(mCursor.getString(8));
                mSong.setmImage(mCursor.getInt(9));
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
