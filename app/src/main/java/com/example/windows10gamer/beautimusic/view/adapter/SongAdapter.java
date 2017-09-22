package com.example.windows10gamer.beautimusic.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.windows10gamer.beautimusic.model.Song;
import com.example.windows10gamer.beautimusic.R;
import com.example.windows10gamer.beautimusic.view.activity.MainActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SongAdapter extends BaseAdapter {
    private Context mContext;
    private List<Song> mSongList;
    private int mLayout;
    private List<Song> mSongListChange;

    public SongAdapter(Context mContext, List<Song> mSongList, int mLayout) {
        this.mContext = mContext;
        this.mSongList = mSongList;
        this.mLayout = mLayout;
        //this.mSongListChange = new ArrayList<>();
        //this.mSongListChange.addAll(mSongList);
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
//    public void filter(String charText) {
//        charText = charText.toLowerCase(Locale.getDefault());
//        mSongList.clear();
//        if (charText.length() == 0) {
//            mSongList.addAll(mSongListChange);
//            MainActivity.musicService.mSongList = mSongList;
//        } else {
//
//            for (Song song : mSongListChange) {
//                if (song.getmNameSong().toLowerCase(Locale.getDefault()).contains(charText)) {
//                    mSongList.add(song);
//                }
//            }
//            MainActivity.musicService.mSongList = mSongList;
//        }
//        notifyDataSetChanged();
//    }

    private class ViewHolder {
        TextView mTvNameSong, mTvNameArtist;
    }

}
