package com.example.windows10gamer.beautimusic.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.windows10gamer.beautimusic.model.Song;
import com.example.windows10gamer.beautimusic.R;

import java.util.List;

/**
 * Created by Windows 10 Gamer on 21/08/2017.
 */

public class SongAdapter extends BaseAdapter {
    private Context mContext;
    private List<Song> mSongList;
    private int mLayout;

    public SongAdapter(Context mContext, List<Song> mSongList, int mLayout) {
        this.mContext = mContext;
        this.mSongList = mSongList;
        this.mLayout = mLayout;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mLayout, null);
            mViewHolder = new ViewHolder();
            mViewHolder.mTvNameSong = (TextView) convertView.findViewById(R.id.TvNameSong);
            mViewHolder.mTvNameArtist = (TextView) convertView.findViewById(R.id.TvNameSinger);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        Song song = mSongList.get(position);
        mViewHolder.mTvNameSong.setText(song.getmNameSong());
        mViewHolder.mTvNameArtist.setText(song.getmNameArtist());
        return convertView;
    }

    private class ViewHolder {
        TextView mTvNameSong, mTvNameArtist;
    }

}
