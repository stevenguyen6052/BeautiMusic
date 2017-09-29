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
import com.example.windows10gamer.beautimusic.view.utilities.Utils;
import com.example.windows10gamer.beautimusic.view.utilities.ItemClickListener;
import com.example.windows10gamer.beautimusic.model.Artist;
import com.example.windows10gamer.beautimusic.R;

import java.util.List;


public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ViewHolder> {
    private static final String NAME_ARTIST = "Name Artist";

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
        final Artist artist = mArtistList.get(position);
        String s = artist.getSumAlbum() + " Album | " + artist.getSumSong() + " Song";
        holder.mTvNameArtist.setText(artist.getNameArtist());
        holder.mTvSumAlbum.setText(s);

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (isLongClick) {

                } else {
                    Intent intent = new Intent(view.getContext(), DetailAlbumArtist.class);
                    Bundle b = new Bundle();
                    b.putString(Utils.TAG, Utils.TAG_ARTIST);
                    b.putString(NAME_ARTIST, mArtistList.get(position).getNameArtist());
                    b.putInt(Utils.ARTIST_ID, mArtistList.get(position).getId());
                    intent.putExtras(b);
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
        private TextView mTvNameArtist, mTvSumAlbum;
        private ItemClickListener itemClickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            mTvNameArtist = (TextView) itemView.findViewById(R.id.item_nameartist);
            mTvSumAlbum = (TextView) itemView.findViewById(R.id.item_sumg_album);
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
