package com.example.windows10gamer.beautimusic.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.windows10gamer.beautimusic.view.activity.DetailArtist;
import com.example.windows10gamer.beautimusic.view.ItemClickListener;
import com.example.windows10gamer.beautimusic.model.Artist;
import com.example.windows10gamer.beautimusic.R;

import java.util.List;

/**
 * Created by Windows 10 Gamer on 01/09/2017.
 */

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ViewHolder> {
    private static final String NAME_ARTIST="Name Artist";
    private List<Artist> mArtistList;
    private Context mContext;

    public ArtistAdapter(List<Artist> mArtistList, Context mContext) {
        this.mArtistList = mArtistList;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        itemView = inflater.inflate(R.layout.item_artist, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Artist mArtist = mArtistList.get(position);
        holder.mTvNameArtist.setText(mArtist.getNameArtist());
        holder.mTvSumAlbum.setText(mArtist.getSumAlbum()+" Album");
        holder.mTvSumSong.setText(mArtist.getSumSong()+" Song");
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (isLongClick){

                }else {
                    Intent intent = new Intent(view.getContext(), DetailArtist.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(NAME_ARTIST,mArtistList.get(position).getNameArtist());
                    intent.putExtras(bundle);
                    view.getContext().startActivity(intent);

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mArtistList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView mTvNameArtist, mTvSumAlbum, mTvSumSong;
        private ItemClickListener itemClickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            mTvNameArtist = (TextView) itemView.findViewById(R.id.item_artist);
            mTvSumAlbum = (TextView) itemView.findViewById(R.id.item_sum_album);
            mTvSumSong = (TextView) itemView.findViewById(R.id.item_sum_song);
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
