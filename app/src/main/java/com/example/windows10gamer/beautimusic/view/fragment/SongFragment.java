package com.example.windows10gamer.beautimusic.view.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.windows10gamer.beautimusic.view.activity.PlayMusicActivity;
import com.example.windows10gamer.beautimusic.database.SongDatabase;
import com.example.windows10gamer.beautimusic.model.Song;
import com.example.windows10gamer.beautimusic.R;
import com.example.windows10gamer.beautimusic.view.adapter.RecyclerItemClickListener;
import com.example.windows10gamer.beautimusic.view.adapter.SongAdapter;
import com.example.windows10gamer.beautimusic.utilities.Utils;

import java.util.ArrayList;
import java.util.List;


public class SongFragment extends android.support.v4.app.Fragment {
    private View view;
    private List<Song> mSongList, filteredModelList;
    private SearchView searchView;
    private SongDatabase mSongDatabase;
    private RecyclerView lvSongs;
    private SongAdapter mSongAdapter;
    private LinearLayoutManager linearLayoutManager;
    private Dialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_song, container, false);
        setHasOptionsMenu(true);
        mSongDatabase = new SongDatabase(getContext());
        lvSongs = (RecyclerView) view.findViewById(R.id.mListViewSong);
        mSongList = SongDatabase.getSongFromDevice(getContext());

        lvSongs.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getContext());
        lvSongs.setLayoutManager(linearLayoutManager);
        mSongAdapter = new SongAdapter(mSongList, getActivity());
        lvSongs.setAdapter(mSongAdapter);
        addItemClick();

        return view;
    }

    private void addItemClick() {
        lvSongs.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), lvSongs,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getContext(), PlayMusicActivity.class);
                        Bundle b = new Bundle();
                        if (filteredModelList != null && filteredModelList.size() != 0) {
                            b.putParcelableArrayList(Utils.LIST_SONG, (ArrayList<Song>) filteredModelList);
                        } else {
                            b.putParcelableArrayList(Utils.LIST_SONG, (ArrayList<Song>) mSongList);
                        }
                        b.putInt(Utils.POSITION, position);
                        intent.putExtras(b);
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View view, final int position) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                        builder.setMessage("Bạn có muốn thêm bài hát vào playlist không ?");
                        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                mSongDatabase.addNewSong(mSongList.get(position));
                            }
                        });
                        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();

                    }
                }));
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
                filteredModelList = Utils.filter(mSongList, newText.trim());
                mSongAdapter.setFilter(filteredModelList);
                return true;
            }
        });
    }


}

