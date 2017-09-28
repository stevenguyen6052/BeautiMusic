package com.example.windows10gamer.beautimusic.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.windows10gamer.beautimusic.view.adapter.ArtistAdapter;
import com.example.windows10gamer.beautimusic.database.SongDatabase;
import com.example.windows10gamer.beautimusic.model.Artist;
import com.example.windows10gamer.beautimusic.R;

import java.util.ArrayList;
import java.util.List;


public class ArtistFragment extends android.support.v4.app.Fragment {
    private View view;
    private List<Artist> mArtistList;
    private ArtistAdapter mArtistAdapter;
    private RecyclerView lvArtist;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.artists_fragment, container, false);

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

}
