package com.example.windows10gamer.beautimusic.view.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
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

//import com.example.windows10gamer.beautimusic.view.activity.PlayMusic;
import com.example.windows10gamer.beautimusic.utilities.singleton.SongDatabase;
import com.example.windows10gamer.beautimusic.model.Playlist;
import com.example.windows10gamer.beautimusic.model.Song;
import com.example.windows10gamer.beautimusic.R;
import com.example.windows10gamer.beautimusic.utilities.RecyclerItemClickListener;
import com.example.windows10gamer.beautimusic.utilities.Utils;
import com.example.windows10gamer.beautimusic.view.activity.DetailAlbumArtist;
import com.example.windows10gamer.beautimusic.view.activity.PlayMusicActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {
    private List<Playlist> mPlaylist = new ArrayList<>();
    private List<Song> mSongList;
    private Context mContext;
    private SongDatabase songDatabase;
    private Dialog dialogAddSong, dialogChoosePlaylist;
    private PlaylistDialogAdapter playlistAdapter;
    private RecyclerView recyPlaylist;
    private LinearLayoutManager linearLayoutManager;
    private Song mSong;
    private EditText edtNamePlaylist;
    private PopupMenu popupMenu;
    private View view;
    private Gson gson;

    List<Song> listSong = new ArrayList<>();



    public SongAdapter(List<Song> mSongList, Context mContext) {
        this.mSongList = mSongList;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_song, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int i) {

        mSong = mSongList.get(i);
        holder.mTvNameSong.setText(mSong.getNameSong());
        holder.mTvNameArtist.setText(mSong.getNameArtist());
        Picasso.with(mContext)
                .load(mSong.getImageSong())
                .placeholder(R.drawable.ic_musicqh)
                .error(R.drawable.ic_musicqh)
                .into(holder.mImgView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, PlayMusicActivity.class)
                        .putParcelableArrayListExtra(Utils.LIST_SONG, (ArrayList) mSongList)
                        .putExtra(Utils.POSITION, i));
                mContext.sendBroadcast(new Intent().setAction(Utils.PLAY_KEY));
            }
        });
        holder.mImgMoreVert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenu = new PopupMenu(mContext, holder.mImgMoreVert);
                popupMenu.inflate(R.menu.menu_more_vert);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.itemAddPlayList:

                                songDatabase = SongDatabase.getInstance(mContext.getApplicationContext());
                                mPlaylist.clear();
                                mPlaylist.addAll(songDatabase.getPlaylist());
                                dialogAddSong = new Dialog(mContext);
                                dialogChoosePlaylist = new Dialog(mContext);
                                dialogChoosePlaylist.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialogChoosePlaylist.setContentView(R.layout.dialog_addsong_playlist);
                                dialogChoosePlaylist.show();

                                recyPlaylist = (RecyclerView) dialogChoosePlaylist.findViewById(R.id.lvPlaylist);
                                recyPlaylist.setHasFixedSize(true);
                                linearLayoutManager = new LinearLayoutManager(mContext);
                                recyPlaylist.setLayoutManager(linearLayoutManager);
                                playlistAdapter = new PlaylistDialogAdapter(mContext, mPlaylist);
                                recyPlaylist.setAdapter(playlistAdapter);
                                listPlaylistItemClick(i);

                                view = dialogChoosePlaylist.findViewById(R.id.viewAddPlaylist);
                                view.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialogChoosePlaylist.dismiss();

                                        dialogAddSong.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                        dialogAddSong.setContentView(R.layout.dialog_add_playlist);

                                        edtNamePlaylist = (EditText) dialogAddSong.findViewById(R.id.edtAddPlayList);

                                        dialogAddSong.findViewById(R.id.btnYes).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                List<Song> s = new ArrayList<>();
                                                gson = new Gson();
                                                s.add(mSongList.get(i));
                                                String listSong = gson.toJson(s);

                                                if (edtNamePlaylist.getText().toString().equals("")) {
                                                    Toast.makeText(mContext, "Please input name playlist !", Toast.LENGTH_SHORT).show();
                                                } else if (Utils.checkString(edtNamePlaylist.getText().toString())) {
                                                    Toast.makeText(mContext, "Input all space, Please Input again!", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    songDatabase.addPlayList(edtNamePlaylist.getText().toString(), listSong);
                                                    Toast.makeText(mContext, "Added into playlist !", Toast.LENGTH_SHORT).show();
                                                    dialogAddSong.dismiss();
                                                }

                                            }
                                        });
                                        dialogAddSong.findViewById(R.id.btnNo).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialogAddSong.dismiss();
                                            }
                                        });

                                        dialogAddSong.show();

                                    }
                                });
                                break;

                            case R.id.itemPlay:
                                mContext.startActivity(new Intent(mContext, PlayMusicActivity.class)
                                        .putParcelableArrayListExtra(Utils.LIST_SONG, (ArrayList<Song>) mSongList)
                                        .putExtra(Utils.POSITION, i));

                                break;

                            case R.id.itemGoToArtist:
                                mContext.startActivity(new Intent(mContext, DetailAlbumArtist.class)
                                        .putExtra(Utils.NAME_ARTIST, mSongList.get(i).getNameArtist())
                                        .putExtra(Utils.ARTIST_ID, mSongList.get(i).getArtistId())
                                        .putExtra(Utils.TAG, Utils.TAG_ARTIST));

                                break;

                            case R.id.itemGoToAlbum:
                                mContext.startActivity(new Intent(mContext, DetailAlbumArtist.class)
                                        .putExtra(Utils.NAME_ALBUM, mSongList.get(i).getNameAlbum())
                                        .putExtra(Utils.ALBUM_ID, mSongList.get(i).getAlbumId())
                                        .putExtra(Utils.TAG, Utils.TAG_ALBUM));
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();

            }

        });
    }


    private void listPlaylistItemClick(final int i) {
        recyPlaylist.addOnItemTouchListener(new RecyclerItemClickListener(mContext, recyPlaylist,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        int sum = 0;
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<Song>>() {
                        }.getType();
                        listSong = gson.fromJson(mPlaylist.get(position).getListIdSong(), type);
                        for (Song s : listSong) {
                            if (s.getId().equals(mSongList.get(i).getId())) {
                                sum = 1;
                            }
                        }

                        if (sum == 0) {
                            listSong.add(mSongList.get(i));
                        }

                        songDatabase.updatePlaylist(gson.toJson(listSong), mPlaylist.get(position).getId());
                        Toast.makeText(mContext, "Added into playlist !", Toast.LENGTH_SHORT).show();
                        dialogChoosePlaylist.dismiss();
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                }));
    }


    @Override
    public int getItemCount() {
        return mSongList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mImgMoreVert;
        TextView mTvNameSong, mTvNameArtist;
        ImageView mImgView;

        public ViewHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            mTvNameSong = (TextView) itemView.findViewById(R.id.TvNameSong);
            mTvNameArtist = (TextView) itemView.findViewById(R.id.TvNameSinger);
            mImgView = (ImageView) itemView.findViewById(R.id.ImgView);
            mImgMoreVert = (ImageView) itemView.findViewById(R.id.imgMoreVert);

        }
    }


    public void setFilter(List<Song> mData) {
        mSongList = new ArrayList<>();
        mSongList.addAll(mData);
        notifyDataSetChanged();
    }


}
