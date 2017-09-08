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

import java.util.List;


public class AlbumFragment extends android.support.v4.app.Fragment {
    private View mRootView;
    private List<Album> mAlbumList;
    private RecyclerView mRecycleView;
    private SongDatabase mSongDatabase;
    private AlbumAdapter mAlbumAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.album_fragment, container, false);
        initView();
        setUpAdapter();

        return mRootView;
    }

    private void setUpAdapter() {
        mAlbumAdapter = new AlbumAdapter(getContext(), mAlbumList);
        mRecycleView.setAdapter(mAlbumAdapter);
    }

    private void initView() {
        mSongDatabase = new SongDatabase(getActivity());
        mAlbumList = mSongDatabase.getAllAlbum1();

        mRecycleView = (RecyclerView) mRootView.findViewById(R.id.recycleViewAl);
        mRecycleView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        mRecycleView.setLayoutManager(gridLayoutManager);
    }
}
