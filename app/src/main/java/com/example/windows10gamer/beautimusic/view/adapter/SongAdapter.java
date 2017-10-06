package com.example.windows10gamer.beautimusic.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

//import com.example.windows10gamer.beautimusic.view.activity.PlayMusic;
import com.example.windows10gamer.beautimusic.model.Song;
import com.example.windows10gamer.beautimusic.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {
    private List<Song> mSongList;
    private Context mContext;

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
    public void onBindViewHolder(ViewHolder holder, int i) {
        Song mSong = mSongList.get(i);

        if (mSongList.get(i).getNameSong().length() > 40) {
            holder.mTvNameSong.setText(mSong.getNameSong().substring(0, 37) + "...");
        } else {
            holder.mTvNameSong.setText(mSong.getNameSong());
        }

        if (mSongList.get(i).getNameArtist().length() > 40) {
            holder.mTvNameArtist.setText(mSong.getNameArtist().substring(0, 37) + "...");
        } else {
            holder.mTvNameArtist.setText(mSong.getNameArtist());
        }
        Picasso.with(mContext)
                .load(mSong.getImageSong())
                .placeholder(R.drawable.ic_musicqh)
                .error(R.drawable.ic_musicqh)
                .into(holder.mImgView);
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
            mContext = itemView.getContext();
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
