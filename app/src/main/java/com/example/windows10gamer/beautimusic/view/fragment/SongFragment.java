package com.example.windows10gamer.beautimusic.view.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.example.windows10gamer.beautimusic.utilities.LoadData;
import com.example.windows10gamer.beautimusic.model.Song;
import com.example.windows10gamer.beautimusic.R;
import com.example.windows10gamer.beautimusic.view.adapter.SongAdapter;
import com.example.windows10gamer.beautimusic.utilities.Utils;
import java.util.ArrayList;
import java.util.List;


public class SongFragment extends android.support.v4.app.Fragment implements SearchView.OnQueryTextListener {
    private View mRootView;
    private List<Song> mSongList, mSearchList;

    private RecyclerView mLvSong;
    private SongAdapter mAdapter;
    private LinearLayoutManager mLinearLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_song, container, false);
        setHasOptionsMenu(true);

        mSongList = new ArrayList<>();
        mLinearLayout = new LinearLayoutManager(getActivity());
        mLvSong = (RecyclerView) mRootView.findViewById(R.id.mListViewSong);
        mLvSong.setHasFixedSize(true);
        mLvSong.setLayoutManager(mLinearLayout);

        if (mSearchList != null && mSearchList.size() > 0)
            mAdapter = new SongAdapter(mSearchList, getActivity());
        else
            mAdapter = new SongAdapter(mSongList, getActivity());

        mLvSong.setAdapter(mAdapter);

        loadData();

        return mRootView;
    }

    private void loadData() {
        new AsyncTask<String, Void, Void>() {

            @Override
            protected Void doInBackground(String... params) {
                mSongList.clear();
                mSongList.addAll(LoadData.getSongFromDevice(getActivity()));
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                mAdapter.notifyDataSetChanged();
            }
        }.execute("");
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem itemSearch = menu.findItem(R.id.itemSearch);
        SearchView searchView;
        searchView = (SearchView) MenuItemCompat.getActionView(itemSearch);
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mSearchList = Utils.filter(mSongList, newText.trim());
        mAdapter.setFilter(mSearchList);
        return true;
    }
}

