package com.example.windows10gamer.beautimusic.view.adapter;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
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
import com.example.windows10gamer.beautimusic.utilities.NonScrollImageView;
import com.example.windows10gamer.beautimusic.utilities.singleton.SongDatabase;
import com.example.windows10gamer.beautimusic.model.Playlist;
import com.example.windows10gamer.beautimusic.model.Song;
import com.example.windows10gamer.beautimusic.utilities.Utils;
import com.example.windows10gamer.beautimusic.view.activity.DetailAlbumArtist;
import com.example.windows10gamer.beautimusic.view.fragment.PlayListFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> {
    private PlayListFragment mContext;
    private List<Playlist> mPlaylist;
    private Gson gson = new Gson();
    private PopupMenu popupMenu;
    private Dialog dialog;
    private EditText edtNamePlaylist;
    private SongDatabase songDatabase;
    private Type type = new TypeToken<List<Song>>() {
    }.getType();

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

        List<Song> mSongList = gson.fromJson(mPlaylist.get(i).getListIdSong(), type);

        holder.tvNamePlaylist.setText(mPlaylist.get(i).getName());
        holder.tvSumSong.setText(mSongList.size() + Utils.SONGS);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext.getActivity(), DetailAlbumArtist.class)
                        .putParcelableArrayListExtra(Utils.LIST_SONG, (ArrayList<Song>) gson.fromJson(mPlaylist.get(i).getListIdSong(), type))
                        .putExtra(Utils.TAG, Utils.PLAYLIST)
                        .putExtra(Utils.NAME_PLAYLIST, mPlaylist.get(i).getName())
                );
            }
        });

        holder.imgMoreVert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songDatabase = SongDatabase.getInstance(mContext.getContext());

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

                                edtNamePlaylist = (EditText) dialog.findViewById(R.id.edtAddPlayList);
                                edtNamePlaylist.setText(mPlaylist.get(i).getName());

                                dialog.findViewById(R.id.btnYes).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (edtNamePlaylist.getText().toString().equals(Utils.EMPTY)) {
                                            Toast.makeText(mContext.getContext(), Utils.INPUT_NAME_PLAYSLIST, Toast.LENGTH_SHORT).show();

                                        } else if (Utils.checkString(edtNamePlaylist.getText().toString())) {
                                            Toast.makeText(mContext.getContext(), Utils.INPUT_ALL_SPACE, Toast.LENGTH_SHORT).show();

                                        } else {

                                            songDatabase.updateNamePlaylist(edtNamePlaylist.getText().toString(), mPlaylist.get(i).getId());
                                            mContext.mPlaylists.clear();
                                            mContext.mPlaylists.addAll(songDatabase.getPlaylist());
                                            mContext.mAdapter.notifyDataSetChanged();
                                            dialog.dismiss();

                                            Toast.makeText(mContext.getContext(), Utils.UPDEATED, Toast.LENGTH_SHORT).show();
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

                            case R.id.itemDeletePlaylist:
                                songDatabase.deletePlaylist(mPlaylist.get(i).getId());
                                mContext.mPlaylists.remove(i);
                                mContext.mAdapter.notifyDataSetChanged();
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
        ImageView imgArt;
        NonScrollImageView imgMoreVert;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNamePlaylist = (TextView) itemView.findViewById(R.id.tvNamePlaylist);
            tvSumSong = (TextView) itemView.findViewById(R.id.tvSumSong);
            imgArt = (ImageView) itemView.findViewById(R.id.imgArt);
            imgMoreVert = (NonScrollImageView) itemView.findViewById(R.id.imgMoreVertPl);
        }
    }
}