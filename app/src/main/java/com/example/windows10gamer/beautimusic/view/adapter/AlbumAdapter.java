package com.example.windows10gamer.beautimusic.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.windows10gamer.beautimusic.database.SongDatabase;
import com.example.windows10gamer.beautimusic.model.Song;
import com.example.windows10gamer.beautimusic.view.utilities.ItemClickListener;
import com.example.windows10gamer.beautimusic.model.Album;
import com.example.windows10gamer.beautimusic.R;
import com.example.windows10gamer.beautimusic.view.activity.DetailAlbumArtist;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Windows 10 Gamer on 31/08/2017.
 */

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {
    private static final String NAME_ALBUM = "Name Album";
    private Context mContext;
    private List<Album> mAlbumList;
    private List<Song> mSongList;
    private SongDatabase mSongDatabase;
    private android.media.MediaMetadataRetriever mmr = new MediaMetadataRetriever();

    public AlbumAdapter(Context mContext, List<Album> albumList) {
        this.mContext = mContext;
        this.mAlbumList = albumList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        mSongDatabase = new SongDatabase(mContext);
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        itemView = inflater.inflate(R.layout.item_album, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Album mAlbum = mAlbumList.get(position);
        holder.nameAlbum.setText(mAlbum.getNameAlbum());
        holder.nameArtist.setText(mAlbum.getNameArtist());
        if (mSongList==null){
            mSongList = new ArrayList<>();
            mSongList = mSongDatabase.getAllListSong();
        }
        mmr.setDataSource(mSongList.get(position).getmFileSong());
        byte[] dataImageDisc = mmr.getEmbeddedPicture();
        if (dataImageDisc != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(dataImageDisc, 0, dataImageDisc.length);
            holder.imageView.setImageBitmap(bitmap);
        } else {
            holder.imageView.setImageResource(R.drawable.ic_empty_music2);
        }


        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (isLongClick) {

                } else {
                    Intent intent = new Intent(view.getContext(), DetailAlbumArtist.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("TAG","ALBUM");
                    bundle.putString(NAME_ALBUM, mAlbumList.get(position).getNameAlbum());
                    intent.putExtras(bundle);
                    view.getContext().startActivity(intent);
                }
            }
        });

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
