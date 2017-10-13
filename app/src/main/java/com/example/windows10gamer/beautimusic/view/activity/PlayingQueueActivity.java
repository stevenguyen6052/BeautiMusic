package com.example.windows10gamer.beautimusic.view.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.windows10gamer.beautimusic.R;
import com.example.windows10gamer.beautimusic.model.Song;
import com.example.windows10gamer.beautimusic.utilities.Utils;
import com.example.windows10gamer.beautimusic.utilities.dragandswipe.ListChangedListener;
import com.example.windows10gamer.beautimusic.utilities.dragandswipe.QueueAdapter;
import com.example.windows10gamer.beautimusic.utilities.dragandswipe.SimpleItemTouchHelperCallback;
import com.example.windows10gamer.beautimusic.utilities.service.MusicService;
import com.example.windows10gamer.beautimusic.view.fragment.FragmentMiniControl;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PlayingQueueActivity extends AppCompatActivity implements QueueAdapter.OnStartDragListener, ListChangedListener {

    private ItemTouchHelper mItemTouchHelper;
    private List<Song> mSongList;
    public int mPostion;
    public String check = "";
    private View mLayoutOpenPlayMusic;

    private List<Song> getListSong;
    private QueueAdapter mQueueAdapter;
    private RecyclerView mRecycleview;
    private ItemTouchHelper.Callback callback;
    private Toolbar toolbar;
    private FragmentMiniControl mFragmentMiniControl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing_queue);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initView();
        setUpAdapter();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportFragmentManager().beginTransaction().
                replace(R.id.queueLayout, mFragmentMiniControl, FragmentMiniControl.class.getName()).commit();
    }

    private void initView() {
        mFragmentMiniControl = new FragmentMiniControl();
        mSongList = getIntent().getExtras().getParcelableArrayList(Utils.LIST_SONG);
        mLayoutOpenPlayMusic = findViewById(R.id.queueLayout);

        mLayoutOpenPlayMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataResult();
                finish();
            }
        });
    }

    public void dataResult() {
        Intent intent = new Intent();
        Bundle b = new Bundle();
        boolean isCheckChange = false;

        if (getListSong != null) {
            //isCheckchange check xem đã sắp xếp list hay chưa
            // string check để kiểm tra là ấn back hay là click vào item trong list song
            isCheckChange = true;
            b.putParcelableArrayList(Utils.LIST_SONG, (ArrayList<Song>) getListSong);
            b.putInt(Utils.POSITION, mPostion);
            b.putBoolean(Utils.TRUE, isCheckChange);
            b.putString(Utils.CHECK, check);

        } else {
            isCheckChange = false;
            b.putInt(Utils.POSITION, mPostion);
            b.putBoolean(Utils.TRUE, isCheckChange);
            b.putString(Utils.CHECK, check);
        }
        intent.putExtras(b);
        setResult(Activity.RESULT_OK, intent);
    }

    private void setUpAdapter() {
        mQueueAdapter = new QueueAdapter(this, this, mSongList, this);
        mRecycleview = (RecyclerView) findViewById(R.id.recycleQueue);
        mRecycleview.setHasFixedSize(true);
        mRecycleview.setAdapter(mQueueAdapter);
        mRecycleview.setLayoutManager(new LinearLayoutManager(this));
        callback = new SimpleItemTouchHelperCallback(mQueueAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecycleview);
    }


    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }


    @Override
    public void onNoteListChanged(List<Song> mSongs) {
        if (mSongs != null) {
            getListSong = mSongs;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        dataResult();
        finish();
        return true;
    }

}