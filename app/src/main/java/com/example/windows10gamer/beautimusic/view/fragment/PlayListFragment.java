package com.example.windows10gamer.beautimusic.view.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.windows10gamer.beautimusic.R;
import com.example.windows10gamer.beautimusic.utilities.singleton.SongDatabase;
import com.example.windows10gamer.beautimusic.model.Playlist;
import com.example.windows10gamer.beautimusic.view.activity.AddSongToPlaylisActivity;
import com.example.windows10gamer.beautimusic.view.adapter.PlaylistAdapter;
import com.example.windows10gamer.beautimusic.utilities.Utils;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class PlayListFragment extends Fragment implements View.OnClickListener {

    public List<Playlist> mPlaylists, mSearchList;
    public PlaylistAdapter mAdapter;
    private SongDatabase mSongDatabase;
    private View view;
    private TextView mTvNotifi;
    private SearchView searchView;
    private RecyclerView mLvPlaylist;
    private LinearLayoutManager mLinearLayout;
    private FloatingActionButton mBtnAdd;
    private Dialog dialog;
    private EditText edtInputPlaylist;
    private static final int REQUEST_CODE = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_playlist, container, false);

        setHasOptionsMenu(true);

        mPlaylists = new ArrayList<>();
        mLvPlaylist = (RecyclerView) view.findViewById(R.id.lvSongPlayList);
        mTvNotifi = (TextView) view.findViewById(R.id.playlistSum);
        mBtnAdd = (FloatingActionButton) view.findViewById(R.id.floatButton);
        mBtnAdd.setOnClickListener(this);

        mLvPlaylist.setHasFixedSize(true);
        mSongDatabase = SongDatabase.getInstance(getActivity());
        mLinearLayout = new LinearLayoutManager(getActivity());
        mAdapter = new PlaylistAdapter(this, mPlaylists);
        mLvPlaylist.setLayoutManager(mLinearLayout);
        mLvPlaylist.setAdapter(mAdapter);

        loadData();

        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {

            mPlaylists.clear();
            mPlaylists.addAll(mSongDatabase.getPlaylist());
            mAdapter.notifyDataSetChanged();

            if (mPlaylists.size() == 0)
                mTvNotifi.setText(getString(R.string.dont_have_playlist));
            else
                mTvNotifi.setText(Utils.EMPTY);

        }
    }

    private void loadData() {
        new AsyncTask<String, Void, Void>() {

            @Override
            protected Void doInBackground(String... params) {
                mPlaylists.clear();
                mPlaylists.addAll(mSongDatabase.getPlaylist());

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (mPlaylists.size() == 0) {
                    mTvNotifi.setText(Utils.NO_PLAYLIST);
                } else {
                    mTvNotifi.setText(Utils.EMPTY);
                }
                mAdapter.notifyDataSetChanged();

            }
        }.execute("");
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem itemSearch = menu.findItem(R.id.itemSearch);

        searchView = (SearchView) MenuItemCompat.getActionView(itemSearch);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mSearchList = Utils.filterPlaylist(mPlaylists, newText);
                mAdapter.setFilter(mSearchList);
                return true;
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.floatButton:

                dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_add_playlist);
                edtInputPlaylist = (EditText) dialog.findViewById(R.id.edtAddPlayList);

                dialog.findViewById(R.id.btnYes).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String namePlaylist = edtInputPlaylist.getText().toString();

                        if (edtInputPlaylist.getText().toString().equals(Utils.EMPTY)) {
                            Toast.makeText(getContext(), Utils.INPUT_NAME_PLAYSLIST, Toast.LENGTH_SHORT).show();

                        } else if (Utils.checkString(namePlaylist)) {
                            Toast.makeText(getContext(), Utils.INPUT_ALL_SPACE, Toast.LENGTH_SHORT).show();

                        } else {
                            startActivityForResult(new Intent(getContext(), AddSongToPlaylisActivity.class)
                                    .putExtra(Utils.NAME_PLAYLIST, namePlaylist), REQUEST_CODE);
                            dialog.dismiss();
                        }
                    }
                });
                dialog.findViewById(R.id.btnNo).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
                break;
        }
    }

}
