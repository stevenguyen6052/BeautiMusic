package com.example.windows10gamer.beautimusic.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.windows10gamer.beautimusic.view.activity.PlayMusicActivity;
import com.example.windows10gamer.beautimusic.view.utilities.ItemClickListener;
//import com.example.windows10gamer.beautimusic.view.activity.PlayMusic;
import com.example.windows10gamer.beautimusic.model.Song;
import com.example.windows10gamer.beautimusic.R;
import com.example.windows10gamer.beautimusic.view.utilities.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class SongAdapter1 extends RecyclerView.Adapter<SongAdapter1.ViewHolder> {
    private List<Song> mSongList;
    private Context mContext;

    public SongAdapter1(List<Song> mSongList, Context mContext) {
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
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Song mSong = mSongList.get(position);
        holder.mTvNameSong.setText(mSong.getNameSong());
        holder.mTvNameArtist.setText(mSong.getNameArtist());
        Picasso.with(mContext)
                .load(mSong.getImageSong())
                .placeholder(R.drawable.ic_musicqh)
                .error(R.drawable.ic_musicqh)
                .into(holder.mImgView);

        final int i = position;

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PlayMusicActivity.class);
                Bundle b = new Bundle();
                b.putParcelableArrayList(Utils.LIST_SONG, (ArrayList<Song>) mSongList);
                b.putInt(Utils.POSITION, i);
                intent.putExtras(b);
                mContext.startActivity(intent);

            }
        });
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (isLongClick) {

                } else {


                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mSongList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private ItemClickListener itemClickListener;
        TextView mTvNameSong, mTvNameArtist;
        ImageView mImgView;

        public ViewHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            mTvNameSong = (TextView) itemView.findViewById(R.id.TvNameSong);
            mTvNameArtist = (TextView) itemView.findViewById(R.id.TvNameSinger);
            mImgView = (ImageView) itemView.findViewById(R.id.ImgView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }


        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), false);
        }

        @Override
        public boolean onLongClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), false);
            return true;
        }
    }

}
