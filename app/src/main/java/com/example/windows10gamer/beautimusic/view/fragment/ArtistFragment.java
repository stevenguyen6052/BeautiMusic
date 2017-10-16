package com.example.windows10gamer.beautimusic.view.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.windows10gamer.beautimusic.view.adapter.ArtistAdapter;
import com.example.windows10gamer.beautimusic.database.SongDatabase;
import com.example.windows10gamer.beautimusic.model.Artist;
import com.example.windows10gamer.beautimusic.R;
import com.example.windows10gamer.beautimusic.utilities.Utils;

import java.util.ArrayList;
import java.util.List;


public class ArtistFragment extends android.support.v4.app.Fragment {
    private View view;
    private List<Artist> mArtistList, filteredModelList;
    private ArtistAdapter mAdapter;
    private RecyclerView mLvArtist;
    private GridLayoutManager gridLayoutManager;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_artist, container, false);

        setHasOptionsMenu(true);

        mArtistList = new ArrayList<>();

        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        mLvArtist = (RecyclerView) view.findViewById(R.id.recycleViewAr);
        mLvArtist.setHasFixedSize(true);
        mLvArtist.setLayoutManager(gridLayoutManager);
        mAdapter = new ArtistAdapter(mArtistList, getActivity());
        mLvArtist.setAdapter(mAdapter);

        loadData();

        return view;
    }

    private void loadData() {
        new AsyncTask<String, Void, Void>() {

            @Override
            protected Void doInBackground(String... params) {
                mArtistList.clear();
                mArtistList.addAll(SongDatabase.getArtistFromDevice(getContext()));

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
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filteredModelList = Utils.filterArtist(mArtistList, newText.trim());
                mAdapter.setFilter(filteredModelList);
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemList:
                boolean isSwitched = mAdapter.toggleItemViewType();
                mLvArtist.setLayoutManager(isSwitched ? new GridLayoutManager(getContext(), 2)
                        : new LinearLayoutManager(getContext()));
                mAdapter.notifyDataSetChanged();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
