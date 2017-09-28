package com.example.windows10gamer.beautimusic.view.adapter;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.windows10gamer.beautimusic.model.Song;
import com.example.windows10gamer.beautimusic.R;
import com.example.windows10gamer.beautimusic.view.activity.MainActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SongAdapter extends BaseAdapter {
    private android.media.MediaMetadataRetriever mmr = new MediaMetadataRetriever();
    private Context mContext;
    private List<Song> mSongList;
    private int mLayout;
    private List<Song> mSongListChange = new ArrayList<>();

    public SongAdapter(Context mContext, List<Song> mSongList, int mLayout) {
        this.mContext = mContext;
        this.mSongList = mSongList;
        this.mLayout = mLayout;
        this.mSongListChange.addAll(mSongList);
    }

    @Override
    public int getCount() {
        if (mSongList.size() > 0) {
            return mSongList.size();
        } else return 0;
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
            mViewHolder.mImageView = (ImageView) convertView.findViewById(R.id.ImgView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        Song song = mSongList.get(position);
        if (song.getNameSong().length() > 30) {
            mViewHolder.mTvNameSong.setText(song.getNameSong().substring(0, 27) + "...");
        } else {
            mViewHolder.mTvNameSong.setText(song.getNameSong());
        }

        if (song.getNameArtist().length() > 40) {
            mViewHolder.mTvNameArtist.setText(song.getNameArtist().substring(0, 27) + "...");
        } else {
            mViewHolder.mTvNameArtist.setText(song.getNameArtist());
        }

        return convertView;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        mSongList.clear();
        if (charText.length() == 0) {
            mSongList.addAll(mSongListChange);
            MainActivity.musicService.mSongList = mSongList;
            //MainActivity.musicService.mPosition = 0;
        } else {

            for (Song song : mSongListChange) {
                if (charText.length() != 0 && song.getNameSong().toLowerCase(Locale.getDefault()).contains(charText)) {
                    mSongList.add(song);
                } else if (charText.length() != 0 && song.getNameAlbum().toLowerCase(Locale.getDefault()).contains(charText)) {
                    mSongList.add(song);
                } else if (charText.length() != 0 && song.getNameArtist().toLowerCase(Locale.getDefault()).contains(charText)) {
                    mSongList.add(song);
                }
            }
            //MainActivity.musicService.mPlayer.get
            MainActivity.musicService.mSongList = mSongList;

            MainActivity.musicService.mPosition = 0;
        }
    }

    public void setFilter(List<Song> mData) {

        mSongList = new ArrayList<>();
        mSongList.addAll(mData);

        notifyDataSetChanged();
    }

    private class ViewHolder {
        TextView mTvNameSong, mTvNameArtist;
        ImageView mImageView;
    }

}
