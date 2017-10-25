package com.example.windows10gamer.beautimusic.view.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import com.example.windows10gamer.beautimusic.R;
import com.example.windows10gamer.beautimusic.model.Song;
import com.example.windows10gamer.beautimusic.utilities.Utils;
import com.example.windows10gamer.beautimusic.utilities.dragandswipe.ListChangedListener;
import com.example.windows10gamer.beautimusic.utilities.dragandswipe.QueueAdapter;
import com.example.windows10gamer.beautimusic.utilities.dragandswipe.SimpleItemTouchHelperCallback;
import com.example.windows10gamer.beautimusic.view.fragment.FragmentMiniControl;
import java.util.ArrayList;
import java.util.List;
import static com.example.windows10gamer.beautimusic.utilities.Utils.PLAYING_QUEUE;

public class PlayingQueueActivity extends BaseActivity implements QueueAdapter.OnStartDragListener, ListChangedListener {

    private ItemTouchHelper mItemTouchHelper;
    private List<Song> mSongList;
    public int mPostion;
    public Boolean isClickItem = false;
    private View mMiniControl;

    private List<Song> getListSong;
    private QueueAdapter mQueueAdapter;
    private RecyclerView mLvSong;
    private ItemTouchHelper.Callback mCallback;
    private FragmentMiniControl mFragmentMiniControl;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mQueueAdapter.notifyDataSetChanged();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mQueueAdapter = new QueueAdapter(this, this, mSongList, this);
        mLvSong = (RecyclerView) findViewById(R.id.recycleQueue);
        mLvSong.setHasFixedSize(true);
        mLvSong.setAdapter(mQueueAdapter);
        mLvSong.setLayoutManager(new LinearLayoutManager(this));

        mCallback = new SimpleItemTouchHelperCallback(mQueueAdapter);
        mItemTouchHelper = new ItemTouchHelper(mCallback);
        mItemTouchHelper.attachToRecyclerView(mLvSong);

        registerReceiver(mReceiver,new IntentFilter(Utils.NEXT_PLAY));
        registerReceiver(mReceiver,new IntentFilter(Utils.PREVIOUS_PLAY));

    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_playing_queue;
    }

    @Override
    public String titleToolbar() {
        return PLAYING_QUEUE;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportFragmentManager().beginTransaction().
                replace(R.id.queueLayout, mFragmentMiniControl, FragmentMiniControl.class.getName()).commit();
    }

    public void initView() {
        mFragmentMiniControl = new FragmentMiniControl();
        mSongList = getIntent().getExtras().getParcelableArrayList(Utils.LIST_SONG);

        mMiniControl = findViewById(R.id.queueLayout);
        mMiniControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataResult();
                finish();
            }
        });
    }

    @Override
    public void initData() {

    }

    public void dataResult() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        boolean isCheckChange = false;

        //isCheckchange check xem đã sắp xếp list hay chưa
        // isClickItem để kiểm tra là ấn back hay là click vào item trong list song
        if (getListSong != null) {
            isCheckChange = !isCheckChange;
            bundle.putParcelableArrayList(Utils.LIST_SONG, (ArrayList<Song>) getListSong);
            bundle.putInt(Utils.POSITION, mPostion);
            bundle.putBoolean(Utils.TRUE, isCheckChange);
            bundle.putBoolean(Utils.CHECK, isClickItem);

        } else {
            bundle.putInt(Utils.POSITION, mPostion);
            bundle.putBoolean(Utils.TRUE, isCheckChange);
            bundle.putBoolean(Utils.CHECK, isClickItem);
        }
        intent.putExtras(bundle);
        setResult(Activity.RESULT_OK, intent);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
}