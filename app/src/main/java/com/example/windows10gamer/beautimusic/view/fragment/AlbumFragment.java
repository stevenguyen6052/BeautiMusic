package com.example.windows10gamer.beautimusic.view.fragment;


import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.windows10gamer.beautimusic.model.Song;
import com.example.windows10gamer.beautimusic.view.adapter.AlbumAdapter;
import com.example.windows10gamer.beautimusic.database.SongDatabase;
import com.example.windows10gamer.beautimusic.model.Album;
import com.example.windows10gamer.beautimusic.R;
import com.example.windows10gamer.beautimusic.view.utilities.Utils;

import java.util.ArrayList;
import java.util.List;


public class AlbumFragment extends android.support.v4.app.Fragment {
    private View view;
    private List<Album> mAlbumList,filteredModelList;
    private RecyclerView lvAlbums;
    private AlbumAdapter mAlbumAdapter;
    SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.album_fragment, container, false);
        setHasOptionsMenu(true);
        mAlbumList = new ArrayList<>();
        lvAlbums = (RecyclerView) view.findViewById(R.id.recycleViewAl);
        lvAlbums.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        lvAlbums.setLayoutManager(gridLayoutManager);

        mAlbumList = SongDatabase.getAlbumFromDevice(getContext());
        mAlbumAdapter = new AlbumAdapter(getContext(), mAlbumList);
        lvAlbums.setAdapter(mAlbumAdapter);

        return view;
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
                filteredModelList = filter(mAlbumList, newText.trim());

                mAlbumAdapter.setFilter(filteredModelList);
                return true;
            }
        });
    }
    private List<Album> filter(List<Album> mAlbumList, String query) {
        String s = Utils.unAccent(query.toLowerCase());
        List<Album> filteredModelList = new ArrayList<>();

        for (Album album : mAlbumList) {
            String text = Utils.unAccent(album.getNameAlbum().toLowerCase());
            if (text.contains(s)) {
                filteredModelList.add(album);
            }
        }
        return filteredModelList;
    }
}
