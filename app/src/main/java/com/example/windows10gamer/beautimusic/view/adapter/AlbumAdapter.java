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

import com.example.windows10gamer.beautimusic.view.utilities.ItemClickListener;
import com.example.windows10gamer.beautimusic.model.Album;
import com.example.windows10gamer.beautimusic.R;
import com.example.windows10gamer.beautimusic.view.activity.DetailAlbumArtist;
import com.example.windows10gamer.beautimusic.view.utilities.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {

    private static final String NAME_ALBUM = "Name Album";
    private Context mContext;
    private List<Album> mAlbumList;
    private static final int LIST_ITEM=0;
    private static final int GRID_ITEM = 1;
    boolean isSwitchView = true;

    public AlbumAdapter(Context mContext, List<Album> albumList) {
        this.mContext = mContext;
        this.mAlbumList = albumList;
    }
    public boolean toggleItemViewType () {
        isSwitchView = !isSwitchView;
        return isSwitchView;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == LIST_ITEM){
            itemView = LayoutInflater.from(parent.getContext()).inflate( R.layout.item_album_list, null);
        }else{
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album_grid, null);
        }
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int i) {

        if (mAlbumList.get(i).getNameAlbum().length() > 40) {
            holder.nameAlbum.setText(mAlbumList.get(i).getNameAlbum().substring(0, 37) + "...");
        } else {
            holder.nameAlbum.setText(mAlbumList.get(i).getNameAlbum());
        }

        if (mAlbumList.get(i).getNameArtist().length() > 40) {
            holder.nameArtist.setText(mAlbumList.get(i).getNameArtist().substring(0, 37) + "...");
        } else {
            holder.nameArtist.setText(mAlbumList.get(i).getNameArtist());
        }

        Picasso.with(mContext)
                .load(mAlbumList.get(i).getImage())
                .placeholder(R.drawable.icon_music)
                .error(R.drawable.icon_music)
                .into(holder.imageView);

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (isLongClick) {

                } else {
                    Intent intent = new Intent(view.getContext(), DetailAlbumArtist.class);
                    Bundle b = new Bundle();
                    b.putString(Utils.TAG, Utils.TAG_ALBUM);
                    b.putString(NAME_ALBUM, mAlbumList.get(position).getNameAlbum());
                    b.putInt(Utils.ALBUM_ID, mAlbumList.get(position).getId());
                    intent.putExtras(b);
                    view.getContext().startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemViewType(int position) {
        if (isSwitchView){
            return GRID_ITEM;
        }else{
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
        return mAlbumList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private ItemClickListener itemClickListener;
        TextView nameAlbum, nameArtist;
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            nameAlbum = (TextView) itemView.findViewById(R.id.alTvNameAlbum);
            nameArtist = (TextView) itemView.findViewById(R.id.alTvNameArtist);
            imageView = (ImageView) itemView.findViewById(R.id.alImgViewAlbum);
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
