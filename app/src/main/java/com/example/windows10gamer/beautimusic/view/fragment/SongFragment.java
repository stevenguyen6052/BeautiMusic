package com.example.windows10gamer.beautimusic.view.fragment;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.windows10gamer.beautimusic.view.adapter.SongAdapter;
import com.example.windows10gamer.beautimusic.database.SongDatabase;
import com.example.windows10gamer.beautimusic.model.Song;
import com.example.windows10gamer.beautimusic.R;
import com.example.windows10gamer.beautimusic.view.SendDataPosition;
import com.example.windows10gamer.beautimusic.view.activity.MainActivity;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.List;


public class SongFragment extends android.support.v4.app.Fragment {
    private View mRootView;
    private SongDatabase mSongDatabase;
    private List<Song> mSongList;
    private List<Song> mSongList1;
    private SongAdapter mSongAdapter;
    private ListView mListView;
    private SendDataPosition mSendDataPosition;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mRootView.clearFocus();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mSendDataPosition = (SendDataPosition) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.songs_fragment, container, false);
        initView();
        onItemClick();

        return mRootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void initView() {
        mSongDatabase = new SongDatabase(getActivity());
        mSendDataPosition = (SendDataPosition) getActivity();
        mSongAdapter = new SongAdapter(getActivity(), mSongList1, R.layout.item_song);
        mListView = (ListView) mRootView.findViewById(R.id.mListViewSong);
        if (mSongList == null && mSongList1 == null) {
            mSongList = new ArrayList<>();
            mSongList1 = new ArrayList<>();
            mSongList = getAllMp3FromDevice(getActivity());

            for (int i = 0; i < mSongList.size(); i++) {
                Song mSong = new Song();
                mSong.setmId(mSongList.get(i).getmId());
                mSong.setmNameSong(mSongList.get(i).getmNameSong());
                mSong.setmNameAlbum(mSongList.get(i).getmNameAlbum());
                mSong.setmNameArtist(mSongList.get(i).getmNameArtist());
                mSong.setmFileSong(mSongList.get(i).getmFileSong());
                mSong.setmDuaration(mSongList.get(i).getmDuaration());
                mSong.setmAlbumId(mSongList.get(i).getmAlbumId());
                mSong.setmArtistId(mSongList.get(i).getmArtistId());
                mSong.setmAlbumKey(mSongList.get(i).getmAlbumKey());
                mSong.setmImage(R.drawable.imagemusi);

                mSongDatabase.addNewSong(mSong);
            }

            mSongList1 = mSongDatabase.getAllListSong();
        }
        mSongAdapter = new SongAdapter(getActivity(), mSongList1, R.layout.item_song);
        mListView.setAdapter(mSongAdapter);
    }

    private void onItemClick() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSendDataPosition.SendPosition(position);
                MainActivity.slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
            }
        });
    }

    public List<Song> getAllMp3FromDevice(final Context context) {

        mSongList = new ArrayList<>();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.AudioColumns.DATA, MediaStore.Audio.AudioColumns.ALBUM,
                MediaStore.Audio.ArtistColumns.ARTIST, MediaStore.Audio.AudioColumns._ID,
                MediaStore.Audio.AudioColumns.DURATION, MediaStore.Audio.AudioColumns.ALBUM_ID,
                MediaStore.Audio.AudioColumns.ARTIST_ID, MediaStore.Audio.AudioColumns.ALBUM_KEY,
        };
        Cursor c = context.getContentResolver().query(uri, projection, null, null, null);
        if (c != null) {
            while (c.moveToNext()) {
                String mPath, mAlbum, mArtist, mName, mId, mDuaration, mAlbumId, mArtistId, mAlbumKey;

                Song mSong = new Song();
                mPath = c.getString(0);
                mAlbum = c.getString(1);
                mArtist = c.getString(2);
                mId = c.getString(3);
                mDuaration = c.getString(4);
                mAlbumId = c.getString(5);
                mArtistId = c.getString(6);
                mAlbumKey = c.getString(7);
                mName = mPath.substring(mPath.lastIndexOf("/") + 1);

                mSong.setmId(mId);
                mSong.setmNameSong(mName);
                mSong.setmNameAlbum(mAlbum);
                mSong.setmNameArtist(mArtist);
                mSong.setmFileSong(mPath);
                mSong.setmDuaration(Integer.parseInt(mDuaration));
                mSong.setmAlbumId(mAlbumId);
                mSong.setmArtistId(mArtistId);
                mSong.setmAlbumKey(mAlbumKey);
                mSongList.add(mSong);
            }
            c.close();
        }
        return mSongList;
    }

    private Bitmap getImage(String path) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        byte[] rawArt;
        Bitmap art = null;
        BitmapFactory.Options bfo = new BitmapFactory.Options();
        mmr.setDataSource(path);
        rawArt = mmr.getEmbeddedPicture();
        if (null != rawArt)
            art = BitmapFactory.decodeByteArray(rawArt, 0, rawArt.length, bfo);
        return art;
    }
}

