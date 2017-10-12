package com.example.windows10gamer.beautimusic.view.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.windows10gamer.beautimusic.R;
import com.example.windows10gamer.beautimusic.database.SongDatabase;
import com.example.windows10gamer.beautimusic.model.Album;
import com.example.windows10gamer.beautimusic.model.Playlist;
import com.example.windows10gamer.beautimusic.model.Song;
import com.example.windows10gamer.beautimusic.utilities.Utils;
import com.example.windows10gamer.beautimusic.view.activity.DetailAlbumArtist;
import com.example.windows10gamer.beautimusic.view.activity.PlayMusicActivity;
import com.example.windows10gamer.beautimusic.view.fragment.PlayListFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Windows 10 Gamer on 09/10/2017.
 */

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> {
    private PlayListFragment mContext;
    private List<Playlist> mPlaylist;
    private List<Song> mSongList;
    private Gson gson;
    private Type type;
    private PopupMenu popupMenu;
    private Dialog dialog;
    private EditText edtName;
    private SongDatabase songDatabase;
    int pos;

    public PlaylistAdapter(PlayListFragment mContext, List<Playlist> mPlaylist) {
        this.mContext = mContext;
        this.mPlaylist = mPlaylist;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist, null);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int i) {
        pos = i;
        gson = new Gson();
        type = new TypeToken<List<Song>>() {
        }.getType();
        mSongList = gson.fromJson(mPlaylist.get(i).getListIdSong(), type);

        holder.tvNamePlaylist.setText(mPlaylist.get(i).getName());
        holder.tvSumSong.setText(mSongList.size() + " Songs");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext.getContext(), DetailAlbumArtist.class)
                        .putParcelableArrayListExtra(Utils.LIST_SONG, (ArrayList<Song>) gson.fromJson(mPlaylist.get(i).getListIdSong(), type))
                        .putExtra(Utils.TAG, Utils.PLAYLIST)
                        .putExtra(Utils.NAME_PLAYLIST, mPlaylist.get(i).getName())
                );
            }
        });
        holder.imgMoreVert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songDatabase = new SongDatabase(mContext.getContext());
                popupMenu = new PopupMenu(mContext.getContext(), holder.imgMoreVert);
                popupMenu.inflate(R.menu.menu_more_playlist);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.itemUpdatePlaylist:

                                dialog = new Dialog(mContext.getContext());
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setContentView(R.layout.dialog_add_playlist);

                                edtName = (EditText) dialog.findViewById(R.id.edtAddPlayList);
                                edtName.setText(mPlaylist.get(i).getName());

                                dialog.findViewById(R.id.btnYes).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        songDatabase.updateNamePlaylist(edtName.getText().toString(), mPlaylist.get(i).getId());
                                        mContext.mPlaylists.clear();
                                        mContext.mPlaylists.addAll(songDatabase.getPlaylist());
                                        mContext.mPlaylistAdapter.notifyDataSetChanged();
                                        dialog.dismiss();

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

                            case R.id.itemDeletePlaylist:
                                songDatabase.deletePlaylist(mPlaylist.get(i).getId());
                                mContext.mPlaylists.remove(i);
                                mContext.mPlaylistAdapter.notifyDataSetChanged();
                                break;

                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != mPlaylist ? mPlaylist.size() : 0);
    }
    public void setFilter(List<Playlist> mData) {
        mPlaylist = new ArrayList<>();
        mPlaylist.addAll(mData);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNamePlaylist, tvSumSong;
        ImageView imgArt, imgMoreVert;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNamePlaylist = (TextView) itemView.findViewById(R.id.tvNamePlaylist);
            tvSumSong = (TextView) itemView.findViewById(R.id.tvSumSong);
            imgArt = (ImageView) itemView.findViewById(R.id.imgArt);
            imgMoreVert = (ImageView) itemView.findViewById(R.id.imgMoreVertPl);
        }
    }
}
