package com.example.windows10gamer.beautimusic.view.utilities.dragandswipe;

import com.example.windows10gamer.beautimusic.model.Song;

import java.util.List;

/**
 * Created by Windows 10 Gamer on 19/09/2017.
 */

public interface ListChangedListener {
    void onNoteListChanged(List<Song> mSong);
}
