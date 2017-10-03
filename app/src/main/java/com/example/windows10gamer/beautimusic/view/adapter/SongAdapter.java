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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SongAdapter extends BaseAdapter {

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
    public View getView(int i, View v, ViewGroup parent) {
        ViewHolder viewHolder;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(mLayout, null);
            viewHolder = new ViewHolder();
            viewHolder.tvNameSong = (TextView) v.findViewById(R.id.TvNameSong);
            viewHolder.tvNameArtist = (TextView) v.findViewById(R.id.TvNameSinger);
            viewHolder.mImageView = (ImageView) v.findViewById(R.id.ImgView);
            v.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) v.getTag();
        }
        Song song = mSongList.get(i);
        if (mSongList.get(i).getNameSong().length() > 40) {
            viewHolder.tvNameSong.setText(mSongList.get(i).getNameSong().substring(0, 37) + "...");
        } else {
            viewHolder.tvNameSong.setText(mSongList.get(i).getNameSong());
        }

        if (mSongList.get(i).getNameArtist().length() > 40) {
            viewHolder.tvNameArtist.setText(mSongList.get(i).getNameArtist().substring(0, 37) + "...");
        } else {
            viewHolder.tvNameArtist.setText(mSongList.get(i).getNameArtist());
        }
        Picasso.with(mContext)
                .load(song.getImageSong())
                .placeholder(R.drawable.ic_musicqh)
                .error(R.drawable.ic_musicqh)
                .into(viewHolder.mImageView);
        return v;
    }

    public void setFilter(List<Song> mData) {
        mSongList = new ArrayList<>();
        mSongList.addAll(mData);
        notifyDataSetChanged();

    }

    private class ViewHolder {
        TextView tvNameSong, tvNameArtist;
        ImageView mImageView;
    }

}
