package com.example.windows10gamer.beautimusic.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.windows10gamer.beautimusic.R;
import com.example.windows10gamer.beautimusic.application.App;
import com.example.windows10gamer.beautimusic.model.Song;
import com.example.windows10gamer.beautimusic.utilities.service.MusicService;

import java.util.ArrayList;
import java.util.List;

public class PlayingAdapter extends BaseAdapter {
    private Context mContext;
    private List<Song> mSongList;
    private MusicService mService;


    public PlayingAdapter(Context mContext, List<Song> mList) {
        this.mContext = mContext;
        this.mSongList = mList;

    }

    @Override
    public int getCount() {
        return mSongList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder {
        TextView mTvNameSong, mTvNameArtist;
        ImageView mImgView;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder;
        if (view == null) {

            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.item_song_playing, null);

            viewHolder.mTvNameSong = (TextView) view.findViewById(R.id.TvNameSong);
            viewHolder.mTvNameArtist = (TextView) view.findViewById(R.id.TvNameSinger);
            viewHolder.mImgView = (ImageView) view.findViewById(R.id.ImgView);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Song song = mSongList.get(position);
        mService = ((App) mContext.getApplicationContext()).getService();

        if (mService.nameSong().equals(song.getNameSong()))
            viewHolder.mTvNameSong.setTextColor(Color.CYAN);
        else
            viewHolder.mTvNameSong.setTextColor(Color.BLACK);

        viewHolder.mTvNameSong.setText(song.getNameSong());
        viewHolder.mTvNameArtist.setText(song.getNameArtist());
        Glide.with(mContext).load(song.getImageSong()).placeholder(R.drawable.ic_musicqh).into(viewHolder.mImgView);
        return view;
    }

    public void setFilter(List<Song> mData) {
        mSongList = new ArrayList<>();
        mSongList.addAll(mData);
        notifyDataSetChanged();
    }
}
