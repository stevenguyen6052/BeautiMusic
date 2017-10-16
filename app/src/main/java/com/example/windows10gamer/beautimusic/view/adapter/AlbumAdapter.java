package com.example.windows10gamer.beautimusic.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.windows10gamer.beautimusic.model.Album;
import com.example.windows10gamer.beautimusic.R;
import com.example.windows10gamer.beautimusic.utilities.Utils;
import com.example.windows10gamer.beautimusic.view.activity.DetailAlbumArtist;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.example.windows10gamer.beautimusic.utilities.Utils.NAME_ALBUM;


public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {

    private static final String NAME_ALBUM = "Name Album";
    private Context mContext;
    private List<Album> mAlbumList;
    private static final int LIST_ITEM = 0;
    private static final int GRID_ITEM = 1;
    boolean isSwitchView = true;

    public AlbumAdapter(Context mContext, List<Album> albumList) {
        this.mContext = mContext;
        this.mAlbumList = albumList;
    }

    public boolean toggleItemViewType() {
        isSwitchView = !isSwitchView;
        return isSwitchView;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == LIST_ITEM) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album_list, null);
        } else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album_grid, null);
        }
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int i) {

        holder.nameAlbum.setText(mAlbumList.get(i).getNameAlbum());
        holder.nameArtist.setText(mAlbumList.get(i).getNameArtist());

        Picasso.with(mContext)
                .load(mAlbumList.get(i).getImage())
                .placeholder(R.drawable.ic_splash)
                .error(R.drawable.ic_splash)
                .into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getContext().startActivity(new Intent(v.getContext(), DetailAlbumArtist.class)
                        .putExtra(Utils.TAG, Utils.TAG_ALBUM)
                        .putExtra(NAME_ALBUM, mAlbumList.get(i).getNameAlbum())
                        .putExtra(Utils.ALBUM_ID, mAlbumList.get(i).getId())
                );
            }
        });

    }

    @Override
    public int getItemViewType(int position) {
        if (isSwitchView) {
            return GRID_ITEM;
        } else {
            return LIST_ITEM;
        }
    }

    public void setFilter(List<Album> mData) {
        mAlbumList = new ArrayList<>();
        mAlbumList.addAll(mData);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (null != mAlbumList ? mAlbumList.size() : 0);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameAlbum, nameArtist;
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            nameAlbum = (TextView) itemView.findViewById(R.id.alTvNameAlbum);
            nameArtist = (TextView) itemView.findViewById(R.id.alTvNameArtist);
            imageView = (ImageView) itemView.findViewById(R.id.alImgViewAlbum);
        }
    }
}
