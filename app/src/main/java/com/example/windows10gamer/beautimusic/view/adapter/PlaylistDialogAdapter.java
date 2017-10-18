package com.example.windows10gamer.beautimusic.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.windows10gamer.beautimusic.R;
import com.example.windows10gamer.beautimusic.model.Playlist;
import com.example.windows10gamer.beautimusic.model.Song;
import com.example.windows10gamer.beautimusic.utilities.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Windows 10 Gamer on 09/10/2017.
 */

public class PlaylistDialogAdapter extends RecyclerView.Adapter<PlaylistDialogAdapter.ViewHolder> {
    private Context mContext;
    private List<Playlist> mPlaylist;
    private List<Song> mSongList;
    private Gson gson = new Gson();
    private Type type = new TypeToken<List<Song>>() {
    }.getType();

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

        mSongList = gson.fromJson(mPlaylist.get(i).getListIdSong(), type);

        holder.tvNamePlaylist.setText(mPlaylist.get(i).getName());
        holder.tvSumSong.setText(mSongList.size() + Utils.SONGS);

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
