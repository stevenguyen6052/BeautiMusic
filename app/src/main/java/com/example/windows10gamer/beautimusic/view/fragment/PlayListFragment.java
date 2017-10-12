package com.example.windows10gamer.beautimusic.view.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
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
import com.example.windows10gamer.beautimusic.database.SongDatabase;
import com.example.windows10gamer.beautimusic.model.Playlist;
import com.example.windows10gamer.beautimusic.model.Song;
import com.example.windows10gamer.beautimusic.view.activity.AddSongToPlaylisActivity;
import com.example.windows10gamer.beautimusic.view.activity.DetailAlbumArtist;
import com.example.windows10gamer.beautimusic.view.activity.PlayMusicActivity;
import com.example.windows10gamer.beautimusic.view.adapter.PlaylistAdapter;
import com.example.windows10gamer.beautimusic.view.adapter.RecyclerItemClickListener;
import com.example.windows10gamer.beautimusic.view.adapter.SongAdapter;
import com.example.windows10gamer.beautimusic.utilities.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.example.windows10gamer.beautimusic.utilities.Utils.NAME_ALBUM;

/**
 * Created by Windows 10 Gamer on 03/10/2017.
 */

public class PlayListFragment extends Fragment implements View.OnClickListener {
    //private List<Song> mSongList, filteredModelList;
    public List<Playlist> mPlaylists,filteredModelList;
    public PlaylistAdapter mPlaylistAdapter;
    private SongDatabase mSongDatabase;
    private View view;
    private TextView tvSumSong;
    SearchView searchView;
    private RecyclerView lvSongs;
    private List<Song> mSongList;
    private LinearLayoutManager linearLayoutManager;
    private Gson gson;
    private Type type;
    private FloatingActionButton floatButton;
    private Dialog dialog;
    private EditText edtInputPlaylist;
    private SongDatabase songDatabase;
    private static final int REQUEST_CODE = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_playlist, container, false);
        setHasOptionsMenu(true);
        mSongList = new ArrayList<>();
        mPlaylists = new ArrayList<>();
        songDatabase = new SongDatabase(getContext());

        lvSongs = (RecyclerView) view.findViewById(R.id.lvSongPlayList);
        tvSumSong = (TextView) view.findViewById(R.id.playlisstSum);
        floatButton = (FloatingActionButton) view.findViewById(R.id.floatButton);
        floatButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        lvSongs.setHasFixedSize(true);
        gson = new Gson();
        type = new TypeToken<List<Song>>() {
        }.getType();
        mSongDatabase = new SongDatabase(getContext());
        linearLayoutManager = new LinearLayoutManager(getContext());
        mPlaylistAdapter = new PlaylistAdapter(this, mPlaylists);
        lvSongs.setLayoutManager(linearLayoutManager);
        lvSongs.setAdapter(mPlaylistAdapter);

        loadData();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            mPlaylists.clear();
            mPlaylists.addAll(mSongDatabase.getPlaylist());
            mPlaylistAdapter.notifyDataSetChanged();
        }
    }

    private void loadData(){
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
                mPlaylistAdapter.notifyDataSetChanged();

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
                filteredModelList = Utils.filterPlaylist(mPlaylists,newText);
                mPlaylistAdapter.setFilter(filteredModelList);
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
                        if (edtInputPlaylist.getText().toString().equals("")) {
                            Toast.makeText(getContext(), "Please input name playlist !", Toast.LENGTH_SHORT).show();
                        } else if (Utils.checkString(namePlaylist)) {
                            Toast.makeText(getContext(), "Input all space, Please Input again!", Toast.LENGTH_SHORT).show();
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
