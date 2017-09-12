package com.example.windows10gamer.beautimusic.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.example.windows10gamer.beautimusic.R;
import com.example.windows10gamer.beautimusic.database.SongDatabase;
import com.example.windows10gamer.beautimusic.model.Song;
import com.example.windows10gamer.beautimusic.view.helper.dragandswipe.QueueAdapter;
import com.example.windows10gamer.beautimusic.view.helper.dragandswipe.SimpleItemTouchHelperCallback;

import java.util.ArrayList;
import java.util.List;

public class PlayingQueue extends AppCompatActivity implements QueueAdapter.OnStartDragListener {
    private ItemTouchHelper mItemTouchHelper;
    private List<Song> mSongList;
    private SongDatabase mSongDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSongDatabase = new SongDatabase(this);
        mSongList = new ArrayList<>();
        mSongList = mSongDatabase.getAllListSong();

        setContentView(R.layout.activity_playing_queue);

        QueueAdapter adapter = new QueueAdapter(this, this, mSongList);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycleQueue);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }


}
