package com.example.windows10gamer.beautimusic.view.utilities.dragandswipe;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.windows10gamer.beautimusic.R;


import com.example.windows10gamer.beautimusic.model.Song;
import com.example.windows10gamer.beautimusic.view.ItemClickListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QueueAdapter extends RecyclerView.Adapter<QueueAdapter.ItemViewHolder> implements ItemTouchHelperAdapter {
    private List<Song> mSongList = new ArrayList<>();
    private Context mContext;
    private ListChangedListener mListChangeListener;

    private OnStartDragListener mDragStartListener;

    public QueueAdapter(Context context, OnStartDragListener dragStartListener, List<Song> songList,ListChangedListener listChangedListener) {
        mDragStartListener = dragStartListener;
        mSongList = songList;
        mContext = context;
        mListChangeListener = listChangedListener;

    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song_queue, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        holder.mTvNameSong.setText(mSongList.get(position).getmNameSong());
        holder.mTvNameArtist.setText(mSongList.get(position).getmNameArtist());

        // Start a drag whenever the handle view it touched
        holder.handleView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(holder);
                }
                return false;
            }
        });
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (isLongClick) {

                } else {

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSongList.size();
    }

    @Override
    public void onItemDismiss(int position) {
        mSongList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mSongList, fromPosition, toPosition);
        mListChangeListener.onNoteListChanged(mSongList);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }


    public interface OnStartDragListener {
        void onStartDrag(RecyclerView.ViewHolder viewHolder);

    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder, View.OnClickListener, View.OnLongClickListener {
        public TextView mTvNameSong, mTvNameArtist;
        public ImageView handleView;
        private ItemClickListener itemClickListener;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mTvNameSong = (TextView) itemView.findViewById(R.id.itm_que_nameSong);
            mTvNameArtist = (TextView) itemView.findViewById(R.id.itm_que_nameArtist);
            handleView = (ImageView) itemView.findViewById(R.id.handle);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
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

