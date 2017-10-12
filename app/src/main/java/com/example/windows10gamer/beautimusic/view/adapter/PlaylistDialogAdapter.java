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

public class PlaylistDialogAdapter extends RecyclerView.Adapter<PlaylistDialogAdapter.ViewHolder> {
    private Context mContext;
    private List<Playlist> mPlaylist;
    private List<Song> mSongList;
    private Gson gson;
    private Type type;
    private PopupMenu popupMenu;
    private Dialog dialog;
    private EditText edtName;
    private SongDatabase songDatabase;


    public PlaylistDialogAdapter(Context mContext, List<Playlist> mPlaylist) {
        this.mContext = mContext;
        this.mPlaylist = mPlaylist;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dialog_playlist, null);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int i) {

        gson = new Gson();
        type = new TypeToken<List<Song>>() {
        }.getType();
        mSongList = gson.fromJson(mPlaylist.get(i).getListIdSong(), type);

        holder.tvNamePlaylist.setText(mPlaylist.get(i).getName());
        holder.tvSumSong.setText(mSongList.size() + " Songs");

    }

    @Override
    public int getItemCount() {
        return (null != mPlaylist ? mPlaylist.size() : 0);
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
