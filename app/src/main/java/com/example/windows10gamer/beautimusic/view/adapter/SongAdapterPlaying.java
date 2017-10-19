package com.example.windows10gamer.beautimusic.view.adapter;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.windows10gamer.beautimusic.R;
import com.example.windows10gamer.beautimusic.application.App;
import com.example.windows10gamer.beautimusic.model.Song;
import com.example.windows10gamer.beautimusic.utilities.service.MusicService;
import com.example.windows10gamer.beautimusic.view.activity.PlayMusicActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SongAdapterPlaying extends RecyclerView.Adapter<SongAdapterPlaying.ViewHolder> {
    private List<Song> mSongList;
    private Context mContext;
    int selectedPosition = -1;

    public SongAdapterPlaying(List<Song> mSongList, Context mContext) {
        this.mSongList = mSongList;
        this.mContext = mContext;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_song_playing, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int i) {
        Song song = mSongList.get(i);
        String indexPlay =  ((App) mContext.getApplicationContext()).getService().nameSong();

        holder.mTvNameSong.setText(song.getNameSong());
        holder.mTvNameArtist.setText(song.getNameArtist());
        Glide.with(mContext).load(song.getImageSong()).placeholder(R.drawable.ic_musicqh).into(holder.mImgView);

        if (indexPlay.equals(song.getNameSong()))
            holder.mTvNameSong.setTextColor(Color.CYAN);
        else
            holder.mTvNameSong.setTextColor(Color.BLACK);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSongList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTvNameSong, mTvNameArtist;
        ImageView mImgView;

        public ViewHolder(View itemView) {
            super(itemView);
            //mContext = itemView.getContext();
            mTvNameSong = (TextView) itemView.findViewById(R.id.TvNameSong);
            mTvNameArtist = (TextView) itemView.findViewById(R.id.TvNameSinger);
            mImgView = (ImageView) itemView.findViewById(R.id.ImgView);

        }
    }

    public void setFilter(List<Song> mData) {
        mSongList = new ArrayList<>();
        mSongList.addAll(mData);
        notifyDataSetChanged();
    }
}
