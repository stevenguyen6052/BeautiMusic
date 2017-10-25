package com.example.windows10gamer.beautimusic.view.fragment;


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
import com.example.windows10gamer.beautimusic.utilities.LoadData;
import com.example.windows10gamer.beautimusic.view.adapter.AlbumAdapter;
import com.example.windows10gamer.beautimusic.model.Album;
import com.example.windows10gamer.beautimusic.R;
import com.example.windows10gamer.beautimusic.utilities.Utils;
import java.util.ArrayList;
import java.util.List;


public class AlbumFragment extends android.support.v4.app.Fragment {
    private View mRootView;
    private List<Album> mAlbumList, mSearchList;
    private RecyclerView mLvAlbum;
    private AlbumAdapter mAdapter;
    private GridLayoutManager mGridLayout;
    private SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_album, container, false);
        setHasOptionsMenu(true);

        mAlbumList = new ArrayList<>();

        mGridLayout = new GridLayoutManager(getActivity(), 2);
        mLvAlbum = (RecyclerView) mRootView.findViewById(R.id.recycleViewAl);
        mLvAlbum.setHasFixedSize(true);
        mLvAlbum.setLayoutManager(mGridLayout);

        mAdapter = new AlbumAdapter(getActivity(), mAlbumList);
        mLvAlbum.setAdapter(mAdapter);

        loadData();

        return mRootView;
    }

    private void loadData() {
        new AsyncTask<String, Void, Void>() {

            @Override
            protected Void doInBackground(String... params) {
                mAlbumList.clear();
                mAlbumList.addAll(LoadData.getAlbumFromDevice(getActivity()));
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                mAdapter.notifyDataSetChanged();

            }
        }.execute("");
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem itemSearch;
        itemSearch = menu.findItem(R.id.itemSearch);
        searchView = (SearchView) MenuItemCompat.getActionView(itemSearch);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemList:
                boolean isSwitched = mAdapter.toggleItemViewType();
                mLvAlbum.setLayoutManager(isSwitched ? new GridLayoutManager(getActivity(), 2)
                        : new LinearLayoutManager(getActivity()));
                mAdapter.notifyDataSetChanged();
                break;

            case R.id.itemSearch:
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        mSearchList = Utils.filterAlbum(mAlbumList, newText.trim());
                        mAdapter.setFilter(mSearchList);
                        return true;
                    }
                });
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
