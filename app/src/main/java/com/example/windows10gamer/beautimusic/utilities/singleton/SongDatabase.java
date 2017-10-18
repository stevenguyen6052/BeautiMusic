package com.example.windows10gamer.beautimusic.utilities.singleton;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.windows10gamer.beautimusic.model.Playlist;
import java.util.ArrayList;
import java.util.List;


public class SongDatabase extends SQLiteOpenHelper {
    private static final String NAME_DATABASE = "Quan ly bai hat";
    private static final int VERSION = 1;

    private static final String TBL_PLAYLIST = "Playlist";

    private static final String PL_ID = "Id";
    private static final String PL_LIST = "ListIdSong";
    private static final String PL_NAME_LIST = "NameList";

    private static final String CREATE_TBL_PLAYLIST = "CREATE TABLE IF NOT EXISTS  " + TBL_PLAYLIST + " ("
            + PL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + PL_LIST + " TEXT,  "
            + PL_NAME_LIST + " TEXT"
            + ")";

    private static SongDatabase instance;

    public static SongDatabase getInstance(Context context) {
        if (instance == null) {
            instance = new SongDatabase(context.getApplicationContext());
        }
        return instance;
    }

    private SongDatabase(Context context) {
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
        db.update(TBL_PLAYLIST, args, PL_ID + " = " + id, null);
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
                playlists.add(new Playlist(mCursor.getInt(0), mCursor.getString(1), mCursor.getString(2)));

            } while (mCursor.moveToNext());
        }
        mCursor.close();
        db.close();
        return playlists;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TBL_PLAYLIST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
