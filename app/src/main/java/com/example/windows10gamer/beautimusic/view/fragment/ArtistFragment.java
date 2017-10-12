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
    private ArtistAdapter mArtistAdapter;
    private RecyclerView recycArtist;
    SearchView searchView;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_artist, container, false);

        setHasOptionsMenu(true);

        mArtistList = new ArrayList<>();
        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        linearLayoutManager = new LinearLayoutManager(getContext());

        recycArtist = (RecyclerView) view.findViewById(R.id.recycleViewAr);
        recycArtist.setHasFixedSize(true);
        recycArtist.setLayoutManager(gridLayoutManager);
        mArtistAdapter = new ArtistAdapter(mArtistList, getActivity());
        recycArtist.setAdapter(mArtistAdapter);

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
                mArtistAdapter.notifyDataSetChanged();

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
                filteredModelList = Utils.filterArtist(mArtistList, newText.trim());
                mArtistAdapter.setFilter(filteredModelList);
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemList:
                boolean isSwitched = mArtistAdapter.toggleItemViewType();
                recycArtist.setLayoutManager(isSwitched ? new GridLayoutManager(getContext(), 2)
                        : new LinearLayoutManager(getContext()));
                mArtistAdapter.notifyDataSetChanged();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
