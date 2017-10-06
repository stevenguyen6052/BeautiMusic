package com.example.windows10gamer.beautimusic.view.fragment;


import android.content.Intent;
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
    private AlbumAdapter mAlbumAdapter;
    private GridLayoutManager gridLayoutManager;
    private SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_album, container, false);
        setHasOptionsMenu(true);
        mAlbumList = new ArrayList<>();
        lvAlbums = (RecyclerView) view.findViewById(R.id.recycleViewAl);
        lvAlbums.setHasFixedSize(true);
        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        lvAlbums.setLayoutManager(gridLayoutManager);

        mAlbumList = SongDatabase.getAlbumFromDevice(getContext());
        mAlbumAdapter = new AlbumAdapter(getContext(), mAlbumList);
        lvAlbums.setAdapter(mAlbumAdapter);
        lvAlbums.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), lvAlbums,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(view.getContext(), DetailAlbumArtist.class);
                        Bundle b = new Bundle();
                        b.putString(Utils.TAG, Utils.TAG_ALBUM);
                        b.putString(NAME_ALBUM, mAlbumList.get(position).getNameAlbum());
                        b.putInt(Utils.ALBUM_ID, mAlbumList.get(position).getId());
                        intent.putExtras(b);
                        view.getContext().startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                }));

        return view;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem itemSearch = menu.findItem(R.id.itemSearch);
        searchView = (SearchView) MenuItemCompat.getActionView(itemSearch);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemList:
                boolean isSwitched = mAlbumAdapter.toggleItemViewType();
                lvAlbums.setLayoutManager(isSwitched ? new GridLayoutManager(getContext(), 2)
                        : new LinearLayoutManager(getContext()));
                mAlbumAdapter.notifyDataSetChanged();
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

                        mAlbumAdapter.setFilter(filteredModelList);
                        return true;
                    }
                });
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
