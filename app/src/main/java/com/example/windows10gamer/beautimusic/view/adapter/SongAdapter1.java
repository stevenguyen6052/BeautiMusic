package com.example.windows10gamer.beautimusic.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.windows10gamer.beautimusic.view.ItemClickListener;
//import com.example.windows10gamer.beautimusic.view.activity.PlayMusic;
import com.example.windows10gamer.beautimusic.model.Song;
import com.example.windows10gamer.beautimusic.R;

import java.util.List;


/**
 * Created by Windows 10 Gamer on 30/08/2017.
 */

public class SongAdapter1 extends RecyclerView.Adapter<SongAdapter1.ViewHolder> {
    private static final String POSITION = "POSITION";
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        Song mSong = mSongList.get(position);
        holder.mTvNameSong.setText(mSong.getmNameSong());
        holder.mTvNameArtist.setText(mSong.getmNameArtist());
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (isLongClick) {

                } else {
//                    //Intent mIntent = new Intent(view.getContext(), PlayMusic.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putInt(POSITION, position);
//                    mIntent.putExtras(bundle);
//                    view.getContext().startActivity(mIntent);
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

        public ViewHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            mTvNameSong = (TextView) itemView.findViewById(R.id.TvNameSong);
            mTvNameArtist = (TextView) itemView.findViewById(R.id.TvNameSinger);
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
