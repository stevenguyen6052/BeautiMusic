package com.example.windows10gamer.beautimusic.view.fragment;


import android.content.Intent;
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

import com.example.windows10gamer.beautimusic.view.activity.DetailAlbumArtist;
import com.example.windows10gamer.beautimusic.view.adapter.AlbumAdapter;
import com.example.windows10gamer.beautimusic.database.SongDatabase;
import com.example.windows10gamer.beautimusic.model.Album;
import com.example.windows10gamer.beautimusic.R;
import com.example.windows10gamer.beautimusic.view.adapter.RecyclerItemClickListener;
import com.example.windows10gamer.beautimusic.utilities.Utils;

import java.util.ArrayList;
import java.util.List;

import static com.example.windows10gamer.beautimusic.utilities.Utils.NAME_ALBUM;


public class AlbumFragment extends android.support.v4.app.Fragment {
    private View view;
    private List<Album> mAlbumList, filteredModelList;
    private RecyclerView lvAlbums;
    private AlbumAdapter adapter;
    private GridLayoutManager gridLayoutManager;
    private SearchView searchView;
    private MenuItem itemSearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_album, container, false);
        setHasOptionsMenu(true);

        mAlbumList = new ArrayList<>();
        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        lvAlbums = (RecyclerView) view.findViewById(R.id.recycleViewAl);
        lvAlbums.setHasFixedSize(true);
        lvAlbums.setLayoutManager(gridLayoutManager);

        adapter = new AlbumAdapter(getContext(), mAlbumList);
        lvAlbums.setAdapter(adapter);

        loadData();

        return view;
    }

    private void loadData() {
        new AsyncTask<String, Void, Void>() {

            @Override
            protected Void doInBackground(String... params) {
                mAlbumList.clear();
                mAlbumList.addAll(SongDatabase.getAlbumFromDevice(getContext()));
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                adapter.notifyDataSetChanged();

            }
        }.execute("");
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_search, menu);
        itemSearch = menu.findItem(R.id.itemSearch);
        searchView = (SearchView) MenuItemCompat.getActionView(itemSearch);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemList:
                boolean isSwitched = adapter.toggleItemViewType();
                lvAlbums.setLayoutManager(isSwitched ? new GridLayoutManager(getContext(), 2)
                        : new LinearLayoutManager(getContext()));
                adapter.notifyDataSetChanged();
                break;

            case R.id.itemSearch:
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        filteredModelList = Utils.filterAlbum(mAlbumList, newText.trim());
                        adapter.setFilter(filteredModelList);
                        return true;
                    }
                });
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
