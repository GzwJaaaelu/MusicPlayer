package com.google.jaaaule.gzw.musicplayer.adapter;

import android.content.Context;
import android.os.RemoteException;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.jaaaule.gzw.musicplayer.model.Music;
import com.google.jaaaule.gzw.musicplayer.R;
import com.google.jaaaule.gzw.musicplayer.listener.OnMusicItemClickListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by admin on 2017/6/7.
 */

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicHolder> {
    private List<Music> mMusicList;
    private Context mContext;
    private OnMusicItemClickListener mItemClickListener;

    public MusicAdapter(List<Music> musicList, OnMusicItemClickListener itemClickListener) {
        mMusicList = musicList;
        mItemClickListener = itemClickListener;
    }

    public void setMusicList(List<Music> musicList) {
        notifyDataSetChanged();
    }

    @Override
    public MusicHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_music, parent, false);
        mContext = parent.getContext();
        return new MusicHolder(view);
    }

    @Override
    public void onBindViewHolder(MusicHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return mMusicList.size();
    }

    class MusicHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_music_album)
        ImageView mMusicAlbum;
        @BindView(R.id.tv_music_title)
        TextView mMusicTitle;
        @BindView(R.id.tv_music_artist)
        TextView mMusicArtist;
        @BindView(R.id.tv_music_duration)
        TextView mMusicDuration;

        public MusicHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(v -> {
                try {
                    mItemClickListener.play(mMusicList.get(getAdapterPosition()), getAdapterPosition());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            });
        }

        public void setData(int position) {
            Music music = mMusicList.get(position);
            mMusicTitle.setText(music.title);
            mMusicArtist.setText(music.artist);
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
            String time = formatter.format(new Date(music.duration));
            mMusicDuration.setText(time.split(":")[1] + ":" + time.split(":")[2]);
        }
    }
}
