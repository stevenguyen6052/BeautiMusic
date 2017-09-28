package com.example.windows10gamer.beautimusic.view.fragment;


import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.windows10gamer.beautimusic.view.adapter.AlbumAdapter;
import com.example.windows10gamer.beautimusic.database.SongDatabase;
import com.example.windows10gamer.beautimusic.model.Album;
import com.example.windows10gamer.beautimusic.R;

import java.util.ArrayList;
import java.util.List;


public class AlbumFragment extends android.support.v4.app.Fragment {
    private View view;
    private List<Album> mAlbumList;
    private RecyclerView lvAlbums;
    private AlbumAdapter mAlbumAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.album_fragment, container, false);

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
}
