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
        final Artist mArtist = mArtistList.get(position);
        String songNumber = mArtistList.get(position).getSumAlbum()+" Album | "+mArtistList.get(position).getSumSong()+" Song";
        holder.mTvNameArtist.setText(mArtist.getNameArtist());
        holder.mTvSumAlbum.setText(songNumber);

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (isLongClick) {

                } else {
                    Intent intent = new Intent(view.getContext(), DetailAlbumArtist.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("TAG", "ARTIST");
                    bundle.putString(NAME_ARTIST, mArtistList.get(position).getNameArtist());
                    bundle.putInt("ARTISTID",mArtistList.get(position).getId());
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
