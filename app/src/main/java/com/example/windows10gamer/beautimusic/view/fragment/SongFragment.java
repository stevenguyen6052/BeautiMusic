package com.example.windows10gamer.beautimusic.view.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.windows10gamer.beautimusic.view.activity.PlayMusicActivity;
import com.example.windows10gamer.beautimusic.database.SongDatabase;
import com.example.windows10gamer.beautimusic.model.Song;
import com.example.windows10gamer.beautimusic.R;
import com.example.windows10gamer.beautimusic.view.adapter.RecyclerItemClickListener;
import com.example.windows10gamer.beautimusic.view.adapter.SongAdapter;
import com.example.windows10gamer.beautimusic.utilities.Utils;

import java.util.ArrayList;
import java.util.List;


public class SongFragment extends android.support.v4.app.Fragment {
    private View view;
    private List<Song> mSongList, filteredModelList;
    private SearchView searchView;
    private SongDatabase mSongDatabase;
    private RecyclerView lvSongs;
    private SongAdapter mSongAdapter;
    private LinearLayoutManager linearLayoutManager;
    private Dialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_song, container, false);
        setHasOptionsMenu(true);

        mSongList = new ArrayList<>();
        mSongDatabase = new SongDatabase(getContext());
        lvSongs = (RecyclerView) view.findViewById(R.id.mListViewSong);

        lvSongs.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getContext());
        lvSongs.setLayoutManager(linearLayoutManager);
        if (filteredModelList!=null && filteredModelList.size()>0){
            mSongAdapter = new SongAdapter(filteredModelList, getActivity());
        }else {
            mSongAdapter = new SongAdapter(mSongList, getActivity());
        }
        lvSongs.setAdapter(mSongAdapter);

        loadData();
        return view;
    }

    private void loadData(){
        new AsyncTask<String, Void, Void>() {

            @Override
            protected Void doInBackground(String... params) {
                mSongList.clear();
                mSongList.addAll(SongDatabase.getSongFromDevice(getContext()));

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                mSongAdapter.notifyDataSetChanged();

            }
        }.execute("");
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem itemSearch = menu.findItem(R.id.itemSearch);

        searchView = (SearchView) MenuItemCompat.getActionView(itemSearch);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filteredModelList = Utils.filter(mSongList, newText.trim());
                mSongAdapter.setFilter(filteredModelList);
                return true;
            }
        });
    }


}

