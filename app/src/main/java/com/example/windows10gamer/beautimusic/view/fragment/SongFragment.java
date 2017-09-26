package com.example.windows10gamer.beautimusic.view.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.windows10gamer.beautimusic.view.activity.MainActivity;
import com.example.windows10gamer.beautimusic.view.activity.PlayMusicActivity;
import com.example.windows10gamer.beautimusic.view.adapter.SongAdapter;
import com.example.windows10gamer.beautimusic.database.SongDatabase;
import com.example.windows10gamer.beautimusic.model.Song;
import com.example.windows10gamer.beautimusic.R;
import com.example.windows10gamer.beautimusic.view.utilities.InitClass;
import com.example.windows10gamer.beautimusic.view.utilities.SendDataPosition;

import java.util.ArrayList;
import java.util.List;

import static android.support.v4.content.ContextCompat.checkSelfPermission;


public class SongFragment extends android.support.v4.app.Fragment {
    private View mRootView;
    private List<Song> mSongList;
    private SongAdapter mSongAdapter;
    private ListView lvSongs;
    SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.songs_fragment, container, false);
        initView();
        onItemClick();
        return mRootView;
    }
    private void initView() {
        lvSongs = (ListView) mRootView.findViewById(R.id.mListViewSong);
        mSongList = SongDatabase.getSongFromDevice(getContext());
        mSongAdapter = new SongAdapter(getActivity(), mSongList, R.layout.item_song);
        lvSongs.setAdapter(mSongAdapter);
    }
    private void onItemClick() {
        lvSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), PlayMusicActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(InitClass.TAG, InitClass.TAG_SONG);
                bundle.putInt(InitClass.POSITION, position);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
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
}

