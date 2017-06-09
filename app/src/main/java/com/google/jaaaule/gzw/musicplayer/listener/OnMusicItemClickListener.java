package com.google.jaaaule.gzw.musicplayer.listener;

import android.os.RemoteException;

import com.google.jaaaule.gzw.musicplayer.model.Music;

import java.io.IOException;

/**
 * Created by admin on 2017/6/7.
 */

public interface OnMusicItemClickListener {

    void play(Music music, int position) throws IOException, RemoteException;
}
