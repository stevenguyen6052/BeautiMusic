package com.example.windows10gamer.beautimusic.view.fragment;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.windows10gamer.beautimusic.model.Album;
import com.example.windows10gamer.beautimusic.model.Artist;
import com.example.windows10gamer.beautimusic.model.Song;

import java.util.ArrayList;
import java.util.List;

public class AdapterTab extends FragmentStatePagerAdapter {
    private static final String SONG = "Songs";
    private static final String ALBUM = "Albums";
    private static final String ARTIST = "Artists";
    private static final String PLAYLIST = "Play list";

    private String mListTab[] = {SONG, ALBUM, ARTIST,PLAYLIST};

    private SongFragment mSongFragment;
    private AlbumFragment mAlbumFragment;
    private ArtistFragment mArtistFragment;
    private PlayListFragment mPlayListFragment;

    public AdapterTab(FragmentManager fm) {
        super(fm);
        mSongFragment = new SongFragment();
        mAlbumFragment = new AlbumFragment();
        mArtistFragment = new ArtistFragment();
        mPlayListFragment = new PlayListFragment();

    }

    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            return mSongFragment;

        } else if (position == 1) {
            return mAlbumFragment;

        } else if (position == 2) {
            return mArtistFragment;
        }else if (position == 3) {

            return mPlayListFragment;
        }
        else {
        }
        return null;
    }

    @Override
    public int getCount() {
        return mListTab.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return SONG;
            case 1:
                return ALBUM;
            case 2:
                return ARTIST;
            case 3:
                return PLAYLIST;
        }
        return null;
    }

}
