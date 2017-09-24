package com.example.windows10gamer.beautimusic.view.fragment;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
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
import android.widget.ImageView;

import com.example.windows10gamer.beautimusic.view.adapter.AlbumAdapter;
import com.example.windows10gamer.beautimusic.database.SongDatabase;
import com.example.windows10gamer.beautimusic.model.Album;
import com.example.windows10gamer.beautimusic.R;

import java.util.ArrayList;
import java.util.List;


public class AlbumFragment extends android.support.v4.app.Fragment {
    private View mRootView;
    private List<Album> mAlbumList;
    private RecyclerView lvAlbums;
    private SongDatabase mSongDatabase;
    private AlbumAdapter mAlbumAdapter;
    private SearchView searchView;
    private android.media.MediaMetadataRetriever mmr = new MediaMetadataRetriever();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.album_fragment, container, false);
        setHasOptionsMenu(true);
        initView();
        setUpAdapter();
        mAlbumList.addAll(mSongDatabase.getAllAlbum1());
        mAlbumAdapter.notifyDataSetChanged();

        return mRootView;
    }

    private void setUpAdapter() {
        mAlbumAdapter = new AlbumAdapter(getContext(), mAlbumList);
        lvAlbums.setAdapter(mAlbumAdapter);
    }

    private void initView() {
        mSongDatabase = new SongDatabase(getActivity());
        //mAlbumList = mSongDatabase.getAllAlbum1();
        mAlbumList = new ArrayList<>();
        lvAlbums = (RecyclerView) mRootView.findViewById(R.id.recycleViewAl);
        lvAlbums.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        lvAlbums.setLayoutManager(gridLayoutManager);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_item2, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.itemList:
                initListDisplay();
                break;
            case R.id.itemGrid:
                initGridDisplay();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void initListDisplay(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lvAlbums.setLayoutManager(layoutManager);
        lvAlbums.setAdapter(mAlbumAdapter);
    }

    // Display the Grid
    private void initGridDisplay(){
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        lvAlbums.setLayoutManager(layoutManager);
    }
}
