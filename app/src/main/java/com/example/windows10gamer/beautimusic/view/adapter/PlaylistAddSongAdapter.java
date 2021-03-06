package com.example.windows10gamer.beautimusic.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.windows10gamer.beautimusic.R;
import com.example.windows10gamer.beautimusic.model.Song;
import java.util.ArrayList;
import java.util.List;

public class PlaylistAddSongAdapter extends RecyclerView.Adapter<PlaylistAddSongAdapter.ViewHolder> {
    private Context mContext;
    private List<Song> mSongList, listSongAfterCheck = new ArrayList<>();
    private GetList getList;

    public PlaylistAddSongAdapter(Context mContext, List<Song> mSongList, GetList getList) {
        this.mContext = mContext;
        this.mSongList = mSongList;
        this.getList = getList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_checkbox, null);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int i) {

        holder.nameSong.setText(mSongList.get(i).getNameSong());
        holder.nameArtist.setText(mSongList.get(i).getNameArtist());
        Glide.with(mContext).load(mSongList.get(i).getImageSong()).placeholder(R.drawable.ic_empty_music).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mSongList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {

        TextView nameSong, nameArtist;
        ImageView imageView;
        CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            nameSong = (TextView) itemView.findViewById(R.id.itemNameSong);
            nameArtist = (TextView) itemView.findViewById(R.id.itemNameArt);
            imageView = (ImageView) itemView.findViewById(R.id.itemImg);
            checkBox = (CheckBox) itemView.findViewById(R.id.itemCheck);
            checkBox.setOnCheckedChangeListener(this);

        }


        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            // isChecked ->add to list , unChecked -> remove to list
            if (isChecked) {
                if (!listSongAfterCheck.contains(mSongList.get(getAdapterPosition())))
                    listSongAfterCheck.add(mSongList.get(getAdapterPosition()));

            } else {
                if (listSongAfterCheck.contains(mSongList.get(getAdapterPosition())))
                    listSongAfterCheck.remove(mSongList.get(getAdapterPosition()));
            }

            if (listSongAfterCheck != null && listSongAfterCheck.size() != 0)
                getList.sendList(listSongAfterCheck);

        }

    }

    public interface GetList {
        void sendList(List<Song> songs);
    }

}
