package com.example.windows10gamer.beautimusic.view.fragment;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.windows10gamer.beautimusic.view.adapter.SongAdapter;
import com.example.windows10gamer.beautimusic.database.SongDatabase;
import com.example.windows10gamer.beautimusic.model.Song;
import com.example.windows10gamer.beautimusic.R;
import com.example.windows10gamer.beautimusic.view.SendDataPosition;
import com.example.windows10gamer.beautimusic.view.activity.MainActivity;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.List;

import static android.support.v4.app.ActivityCompat.invalidateOptionsMenu;


public class SongFragment extends android.support.v4.app.Fragment {
    private View mRootView;
    private SongDatabase mSongDatabase;
    private List<Song> mSongList;
    public static List<Song> mSongList1;
    private SongAdapter mSongAdapter;
    private ListView mListView;
    private SendDataPosition mSendDataPosition;
    private Context mContext;
    SearchView searchView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRetainInstance(true);
        //setHasOptionsMenu(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mSendDataPosition = (SendDataPosition) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.songs_fragment, container, false);
        initView();
        onItemClick();
        return mRootView;
    }

    private void setListForAdapter(List<Song> mSongList) {
        mSongAdapter = new SongAdapter(getActivity(), mSongList, R.layout.item_song);
        mListView.setAdapter(mSongAdapter);
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        menu.clear();
//        inflater.inflate(R.menu.menu_item, menu);
//        MenuItem itemSearch = menu.findItem(R.id.itemSearch);
//        searchView = (SearchView) MenuItemCompat.getActionView(itemSearch);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int idItem = item.getItemId();
//        switch (idItem) {
//            case R.id.itemSearch:
//                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//                    @Override
//                    public boolean onQueryTextSubmit(String query) {
//
//                        return false;
//                    }
//                    @Override
//                    public boolean onQueryTextChange(String newText) {
//                        mSongList = mSongDatabase.getSongFromNameSong(newText);
//                        //MainActivity.musicService.mSongList = mSongList;
//                        setListForAdapter(mSongList);
//                        mSongAdapter.notifyDataSetChanged();
//                        return true;
//                    }
//                });
//
//                break;
//            case R.id.itemArrange:
//        }
//        return super.onOptionsItemSelected(item);
//    }

    private void initView() {
        mSongDatabase = new SongDatabase(getActivity());
        mSendDataPosition = (SendDataPosition) getActivity();

        mListView = (ListView) mRootView.findViewById(R.id.mListViewSong);
        if (mSongList == null && mSongList1 == null) {
            mSongList = new ArrayList<>();
            mSongList1 = new ArrayList<>();
            mSongList = getAllMp3FromDevice(getActivity());

            for (Song song: mSongList){
                mSongDatabase.addNewSong(song);
            }
            mSongList1 = mSongDatabase.getAllListSong();
            //Log.d("DATABASE", String.valueOf(mSongList1.size()));
        }
        setListForAdapter(mSongList1);
    }

    private void onItemClick() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSendDataPosition.SendPosition(position);
            }
        });
    }

    public List<Song> getAllMp3FromDevice(final Context context) {

        mSongList = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.AudioColumns.DATA, MediaStore.Audio.AudioColumns.ALBUM,
                MediaStore.Audio.ArtistColumns.ARTIST, MediaStore.Audio.AudioColumns._ID,
                MediaStore.Audio.AudioColumns.DURATION, MediaStore.Audio.AudioColumns.ALBUM_ID,
                MediaStore.Audio.AudioColumns.ARTIST_ID, MediaStore.Audio.AudioColumns.ALBUM_KEY,
        };
        Cursor c = context.getContentResolver().query(uri, projection, null, null, null);
        if (c != null) {
            while (c.moveToNext()) {
                String mPath, mAlbum, mArtist, mName, mId, mDuaration, mAlbumId, mArtistId, mAlbumKey;

                Song mSong = new Song();
                mPath = c.getString(0);
                mAlbum = c.getString(1);
                mArtist = c.getString(2);
                mId = c.getString(3);
                mDuaration = c.getString(4);
                mAlbumId = c.getString(5);
                mArtistId = c.getString(6);
                mAlbumKey = c.getString(7);
                mName = mPath.substring(mPath.lastIndexOf("/") + 1);

                mSong.setmId(mId);
                mSong.setmNameSong(mName);
                mSong.setmNameAlbum(mAlbum);
                mSong.setmNameArtist(mArtist);
                mSong.setmFileSong(mPath);
                mSong.setmDuaration(mDuaration);
                mSong.setmAlbumId(mAlbumId);
                mSong.setmArtistId(mArtistId);
                mSong.setmAlbumKey(mAlbumKey);
                mSongList.add(mSong);
            }
            c.close();
        }
        return mSongList;
    }
}

