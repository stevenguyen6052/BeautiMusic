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
    private View mRootView2;
    private List<Artist> mArtistList;
    private SongDatabase mSongDatabase;
    private ArtistAdapter mArtistAdapter;
    private RecyclerView mRecycleView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView2 = inflater.inflate(R.layout.artists_fragment, container, false);
        initView();
        setUpAdapter();
        return mRootView2;
    }
    private void setUpAdapter(){
        mArtistList = mSongDatabase.getAllArtist();
        mArtistAdapter = new ArtistAdapter(mArtistList, getActivity());
        mRecycleView.setAdapter(mArtistAdapter);
    }

    private void initView() {
        mArtistList = new ArrayList<>();
        mSongDatabase = new SongDatabase(getActivity());
        mRecycleView = (RecyclerView) mRootView2.findViewById(R.id.recycleViewAr);
        mRecycleView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        mRecycleView.setLayoutManager(gridLayoutManager);
    }
}
