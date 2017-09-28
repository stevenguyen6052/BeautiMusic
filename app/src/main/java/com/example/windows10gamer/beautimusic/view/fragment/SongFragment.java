package com.example.windows10gamer.beautimusic.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.windows10gamer.beautimusic.view.activity.PlayMusicActivity;
import com.example.windows10gamer.beautimusic.view.adapter.SongAdapter;
import com.example.windows10gamer.beautimusic.database.SongDatabase;
import com.example.windows10gamer.beautimusic.model.Song;
import com.example.windows10gamer.beautimusic.R;
import com.example.windows10gamer.beautimusic.view.utilities.Utils;

import java.util.ArrayList;
import java.util.List;


public class SongFragment extends android.support.v4.app.Fragment {
    private View view;
    private List<Song> mSongList;
    private SongAdapter mSongAdapter;
    private ListView lvSongs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.songs_fragment, container, false);

        lvSongs = (ListView) view.findViewById(R.id.mListViewSong);
        mSongList = SongDatabase.getSongFromDevice(getContext());
        mSongAdapter = new SongAdapter(getActivity(), mSongList, R.layout.item_song);
        lvSongs.setAdapter(mSongAdapter);

        lvSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), PlayMusicActivity.class);
                Bundle b = new Bundle();
                b.putParcelableArrayList(Utils.LIST_SONG, (ArrayList<Song>) mSongList);
                b.putInt(Utils.POSITION, position);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
        return view;
    }

}

