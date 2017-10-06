package com.example.windows10gamer.beautimusic.utilities.dragandswipe;

import com.example.windows10gamer.beautimusic.model.Song;

import java.util.List;

/**
 * Created by Windows 10 Gamer on 23/09/2017.
 */

public interface ListChangedListener {
    void onNoteListChanged(List<Song> mSong);
}
