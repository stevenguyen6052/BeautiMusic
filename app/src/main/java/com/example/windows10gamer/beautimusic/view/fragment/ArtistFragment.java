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

import com.example.windows10gamer.beautimusic.model.Album;
import com.example.windows10gamer.beautimusic.view.adapter.ArtistAdapter;
import com.example.windows10gamer.beautimusic.database.SongDatabase;
import com.example.windows10gamer.beautimusic.model.Artist;
import com.example.windows10gamer.beautimusic.R;
import com.example.windows10gamer.beautimusic.view.utilities.Utils;

import java.util.ArrayList;
import java.util.List;


public class ArtistFragment extends android.support.v4.app.Fragment {
    private View view;
    private List<Artist> mArtistList,filteredModelList;
    private ArtistAdapter mArtistAdapter;
    private RecyclerView lvArtist;
    SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.artists_fragment, container, false);
        setHasOptionsMenu(true);
        mArtistList = new ArrayList<>();
        mArtistList = SongDatabase.getArtistFromDevice(getContext());
        lvArtist = (RecyclerView) view.findViewById(R.id.recycleViewAr);
        lvArtist.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        lvArtist.setLayoutManager(gridLayoutManager);
        mArtistAdapter = new ArtistAdapter(mArtistList, getActivity());
        lvArtist.setAdapter(mArtistAdapter);
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
                filteredModelList = filter(mArtistList, newText.trim());

                mArtistAdapter.setFilter(filteredModelList);
                return true;
            }
        });
    }
    private List<Artist> filter(List<Artist> mArtistList, String query) {
        String s = Utils.unAccent(query.toLowerCase());
        List<Artist> filteredModelList = new ArrayList<>();

        for (Artist artist : mArtistList) {
            String text = Utils.unAccent(artist.getNameArtist().toLowerCase());
            if (text.contains(s)) {
                filteredModelList.add(artist);
            }
        }
        return filteredModelList;
    }

}
