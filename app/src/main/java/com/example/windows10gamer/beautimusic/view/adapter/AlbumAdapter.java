package com.example.windows10gamer.beautimusic.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.windows10gamer.beautimusic.view.ItemClickListener;
import com.example.windows10gamer.beautimusic.model.Album;
import com.example.windows10gamer.beautimusic.R;
import com.example.windows10gamer.beautimusic.view.activity.DetailAlbum;

import java.util.List;

/**
 * Created by Windows 10 Gamer on 31/08/2017.
 */

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {
    private static final String NAME_ALBUM = "Name Album";
    private static final String NAME_ARTIST = "Name Artist";
    private Context mContext;
    private List<Album> albumList;

    public AlbumAdapter(Context mContext, List<Album> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        itemView = inflater.inflate(R.layout.item_album, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Album mAlbum = albumList.get(position);
        holder.nameAlbum.setText(mAlbum.getNameAlbum());
        holder.nameArtist.setText(mAlbum.getNameArtist());
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (isLongClick) {

                } else {
                    Intent intent = new Intent(view.getContext(), DetailAlbum.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(NAME_ALBUM, albumList.get(position).getNameAlbum());
                    bundle.putString(NAME_ARTIST, albumList.get(position).getNameArtist());
                    intent.putExtras(bundle);
                    view.getContext().startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private ItemClickListener itemClickListener;
        TextView nameAlbum, nameArtist;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            nameAlbum = (TextView) itemView.findViewById(R.id.alTvNameAlbum);
            nameArtist = (TextView) itemView.findViewById(R.id.alTvNameArtist);
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
