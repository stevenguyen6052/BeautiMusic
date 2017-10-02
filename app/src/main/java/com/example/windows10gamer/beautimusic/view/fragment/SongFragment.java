package com.example.windows10gamer.beautimusic.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.windows10gamer.beautimusic.view.activity.PlayMusicActivity;
import com.example.windows10gamer.beautimusic.database.SongDatabase;
import com.example.windows10gamer.beautimusic.model.Song;
import com.example.windows10gamer.beautimusic.R;
import com.example.windows10gamer.beautimusic.view.activity.SearchActivity;
import com.example.windows10gamer.beautimusic.view.adapter.SongAdapter;
import com.example.windows10gamer.beautimusic.view.utilities.Utils;

import java.util.ArrayList;
import java.util.List;


public class SongFragment extends android.support.v4.app.Fragment {
    private View view;
    private List<Song> mSongList, filteredModelList;
    private SongAdapter mSongAdapter;
    private ListView lvSongs;
    SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.songs_fragment, container, false);
        setHasOptionsMenu(true);
        lvSongs = (ListView) view.findViewById(R.id.mListViewSong);

        mSongList = SongDatabase.getSongFromDevice(getContext());
        mSongAdapter = new SongAdapter(getActivity(), mSongList, R.layout.item_song);
        lvSongs.setAdapter(mSongAdapter);

        lvSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), PlayMusicActivity.class);
                Bundle b = new Bundle();
                if (filteredModelList!= null && filteredModelList.size()!=0){
                    b.putParcelableArrayList(Utils.LIST_SONG, (ArrayList<Song>) filteredModelList);
                }else {
                    b.putParcelableArrayList(Utils.LIST_SONG, (ArrayList<Song>) mSongList);
                }
                b.putInt(Utils.POSITION, position);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
        return view;
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
                filteredModelList = filter(mSongList, newText.trim());

                mSongAdapter.setFilter(filteredModelList);
                return true;
            }
        });
    }

    private List<Song> filter(List<Song> mlistSong, String query) {
        String s = Utils.unAccent(query.toLowerCase());
        List<Song> filteredModelList = new ArrayList<>();

        for (Song song : mlistSong) {
            String text = Utils.unAccent(song.getNameSong().toLowerCase());
            if (text.contains(s)) {
                filteredModelList.add(song);
            }
        }
        return filteredModelList;
    }


}

