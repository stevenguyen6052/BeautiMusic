package com.example.windows10gamer.beautimusic.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.windows10gamer.beautimusic.view.activity.MainActivity;
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
        setHasOptionsMenu(true);
        initView();
        mArtistList.addAll(SongDatabase.getArtistFromDevice(getContext()));
        mArtistAdapter.notifyDataSetChanged();
        return view;
    }

    private void initView() {
        mArtistList = new ArrayList<>();
        lvArtist = (RecyclerView) view.findViewById(R.id.recycleViewAr);
        lvArtist.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        lvArtist.setLayoutManager(gridLayoutManager);
        mArtistAdapter = new ArtistAdapter(mArtistList, getActivity());
        lvArtist.setAdapter(mArtistAdapter);
    }
    private void initListDisplay() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lvArtist.setLayoutManager(layoutManager);
        lvArtist.setAdapter(mArtistAdapter);
    }

    private void initGridDisplay() {
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        lvArtist.setLayoutManager(layoutManager);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_item2, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemList:
                initListDisplay();
                break;
            case R.id.itemGrid:
                initGridDisplay();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
