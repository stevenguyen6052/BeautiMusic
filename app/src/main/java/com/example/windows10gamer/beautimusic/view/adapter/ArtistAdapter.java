package com.example.windows10gamer.beautimusic.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.windows10gamer.beautimusic.view.activity.DetailAlbumArtist;
import com.example.windows10gamer.beautimusic.utilities.Utils;
import com.example.windows10gamer.beautimusic.utilities.ItemClickListener;
import com.example.windows10gamer.beautimusic.model.Artist;
import com.example.windows10gamer.beautimusic.R;

import java.util.ArrayList;
import java.util.List;


public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ViewHolder> {
    private static final String NAME_ARTIST = "Name Artist";

    private static final int LIST_ITEM = 0;
    private static final int GRID_ITEM = 1;
    boolean isSwitchView = true;

    private List<Artist> mArtistList;
    private Context mContext;

    public ArtistAdapter(List<Artist> mArtistList, Context mContext) {
        this.mArtistList = mArtistList;
        this.mContext = mContext;
    }

    public boolean toggleItemViewType() {
        isSwitchView = !isSwitchView;
        return isSwitchView;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == LIST_ITEM) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_artist_list, null);
        } else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_artist_grid, null);
        }
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Artist artist = mArtistList.get(position);
        String s = artist.getSumAlbum() + " Albums | " + artist.getSumSong() + " Songs";
        holder.mTvNameArtist.setText(artist.getNameArtist());
        holder.mTvSumAlbum.setText(s);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getContext().startActivity(new Intent(v.getContext(), DetailAlbumArtist.class)
                        .putExtra(Utils.TAG, Utils.TAG_ARTIST)
                        .putExtra(NAME_ARTIST, mArtistList.get(position).getNameArtist())
                        .putExtra(Utils.ARTIST_ID, mArtistList.get(position).getId())
                );
            }
        });

    }

    public void setFilter(List<Artist> mData) {
        mArtistList = new ArrayList<>();
        mArtistList.addAll(mData);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (null != mArtistList ? mArtistList.size() : 0);
    }

    @Override
    public int getItemViewType(int position) {
        if (isSwitchView) {
            return GRID_ITEM;
        } else {
            return LIST_ITEM;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvNameArtist, mTvSumAlbum;

        public ViewHolder(View itemView) {
            super(itemView);
            mTvNameArtist = (TextView) itemView.findViewById(R.id.item_nameartist);
            mTvSumAlbum = (TextView) itemView.findViewById(R.id.item_sumg_album);
        }
    }
}
