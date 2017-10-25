package com.example.windows10gamer.beautimusic.view.fragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import static com.example.windows10gamer.beautimusic.utilities.Utils.ALBUMS;
import static com.example.windows10gamer.beautimusic.utilities.Utils.ARTIST;
import static com.example.windows10gamer.beautimusic.utilities.Utils.PLAYLIST;
import static com.example.windows10gamer.beautimusic.utilities.Utils.SONGS;

public class AdapterTab extends FragmentStatePagerAdapter {

    private String mListTab[] = {SONGS, ALBUMS, ARTIST,PLAYLIST};

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
                return SONGS;
            case 1:
                return ALBUMS;
            case 2:
                return ARTIST;
            case 3:
                return PLAYLIST;
        }
        return null;
    }

}
