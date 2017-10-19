package com.example.windows10gamer.beautimusic.utilities.dragandswipe;

import com.example.windows10gamer.beautimusic.model.Song;

import java.util.List;


public interface ListChangedListener {
    void onNoteListChanged(List<Song> mSong);
}
