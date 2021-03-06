package com.example.windows10gamer.beautimusic.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.example.windows10gamer.beautimusic.R;
import com.example.windows10gamer.beautimusic.utilities.LoadData;
import com.example.windows10gamer.beautimusic.utilities.singleton.SongDatabase;
import com.example.windows10gamer.beautimusic.model.Song;
import com.example.windows10gamer.beautimusic.utilities.Utils;
import com.example.windows10gamer.beautimusic.view.adapter.PlaylistAddSongAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static com.example.windows10gamer.beautimusic.utilities.Utils.ADD_TO_PLAYLIST;


public class AddSongToPlaylisActivity extends BaseActivity implements PlaylistAddSongAdapter.GetList {
    private static final String YES = "YES";
    private static final String NO = "NO";
    private static final String SAVE_LIST = "Do do you want to save this playlist ?";
    private RecyclerView mLvSong;
    private PlaylistAddSongAdapter mAdapter;
    private List<Song> mSongList;
    private List<Song> mGetList;
    private LinearLayoutManager mLinearLayout;
    private SongDatabase mDbHanler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_add_song_to_playlis;
    }

    @Override
    public String titleToolbar() {
        return ADD_TO_PLAYLIST;
    }

    public void initView() {
        mGetList = new ArrayList<>();
        mSongList = new ArrayList<>();
        mDbHanler = SongDatabase.getInstance(this);

        mLvSong = (RecyclerView) findViewById(R.id.recycleCheckList);
        mLvSong.setHasFixedSize(true);
        mLinearLayout = new LinearLayoutManager(this);
        mLvSong.setLayoutManager(mLinearLayout);
        mAdapter = new PlaylistAddSongAdapter(this, mSongList, this);
        mLvSong.setAdapter(mAdapter);

    }

    @Override
    public void initData() {
        new AsyncTask<String, Void, Void>() {

            @Override
            protected Void doInBackground(String... params) {
                mSongList.clear();
                mSongList.addAll(LoadData.getSongFromDevice(AddSongToPlaylisActivity.this));
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                mAdapter.notifyDataSetChanged();

            }
        }.execute("");
    }

    @Override
    public void sendList(List<Song> songs) {
        mGetList.clear();
        mGetList.addAll(songs);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_done, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemDone:
                setData();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        savePlaylist();
    }

    private void savePlaylist() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddSongToPlaylisActivity.this);
        builder.setMessage(SAVE_LIST).setCancelable(false).setPositiveButton(YES, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                setData();
            }
        }).setNegativeButton(NO, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void setData(){
        Gson gson = new Gson();
        String namePlaylist = getIntent().getStringExtra(Utils.NAME_PLAYLIST);
        String listSong = gson.toJson(mGetList);

        if (mGetList.size() != 0)
            mDbHanler.addPlayList(namePlaylist, listSong);

        setResult(RESULT_OK, new Intent());
        finish();
    }
}
