package com.example.windows10gamer.beautimusic.view.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.windows10gamer.beautimusic.R;
import com.example.windows10gamer.beautimusic.database.SongDatabase;
import com.example.windows10gamer.beautimusic.model.Song;
import com.example.windows10gamer.beautimusic.view.activity.PlayMusicActivity;
import com.example.windows10gamer.beautimusic.view.adapter.RecyclerItemClickListener;
import com.example.windows10gamer.beautimusic.view.adapter.SongAdapter;
import com.example.windows10gamer.beautimusic.utilities.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Windows 10 Gamer on 03/10/2017.
 */

public class PlayListFragment extends Fragment {
    private List<Song> mSongList, filteredModelList;
    private SongDatabase mSongDatabase;
    private View view;
    private TextView tvSumSong;
    SearchView searchView;
    private RecyclerView lvSongs;
    private SongAdapter mSongAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_playlist, container, false);
        setHasOptionsMenu(true);
        lvSongs = (RecyclerView) view.findViewById(R.id.lvSongPlayList);
        tvSumSong = (TextView) view.findViewById(R.id.playlisstSum);
        mSongList = new ArrayList<>();


        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSongDatabase = new SongDatabase(getContext());
        mSongList.addAll(mSongDatabase.getAllListSong());

        lvSongs.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        lvSongs.setLayoutManager(linearLayoutManager);
        mSongAdapter = new SongAdapter(mSongList, getActivity());
        lvSongs.setAdapter(mSongAdapter);
        addItemClick();

        if (mSongList.size() != 0) {
            tvSumSong.setText(mSongList.size() + " Bài hát");
        } else {
            tvSumSong.setText("Không có bài hát nào !");
        }
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
                        builder.setMessage("Bạn có muốn xóa bài hát khỏi list yêu thích không ?");
                        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                mSongDatabase.deleteSong(mSongList.get(position));
                                mSongList.clear();
                                mSongList.addAll(mSongDatabase.getAllListSong());
                                mSongAdapter.notifyDataSetChanged();
                                if (mSongList.size() != 0) {
                                    tvSumSong.setText(mSongList.size() + " Bài hát");
                                } else {
                                    tvSumSong.setText("Không có bài hát nào !");
                                }
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

    @Override
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
