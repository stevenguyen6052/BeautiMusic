package com.example.windows10gamer.beautimusic.view.fragment;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.windows10gamer.beautimusic.view.utilities.InitClass;

import java.util.List;


public class SongFragment extends android.support.v4.app.Fragment {
    private View mRootView;
    private List<Song> mSongList;
    private SongAdapter mSongAdapter;
    private ListView lvSongs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.songs_fragment, container, false);
        initView();
        onItemClick();
        return mRootView;
    }
    private void initView() {
        lvSongs = (ListView) mRootView.findViewById(R.id.mListViewSong);
        mSongList = SongDatabase.getSongFromDevice(getContext());
        mSongAdapter = new SongAdapter(getActivity(), mSongList, R.layout.item_song);
        lvSongs.setAdapter(mSongAdapter);
    }
    private void onItemClick() {
        lvSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), PlayMusicActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(InitClass.TAG, InitClass.TAG_SONG);
                bundle.putInt(InitClass.POSITION, position);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}

