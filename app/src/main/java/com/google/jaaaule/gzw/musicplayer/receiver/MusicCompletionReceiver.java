package com.google.jaaaule.gzw.musicplayer.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.jaaaule.gzw.musicplayer.listener.OnNextMusicListener;

/**
 * Created by admin on 2017/6/8.
 */

public class MusicCompletionReceiver extends BroadcastReceiver {
    public static final String MUSIC_COMPLETION = "com.jaaaelu.gzw.music_completion";
    private OnNextMusicListener mNextMusicListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (mNextMusicListener != null) {
            mNextMusicListener.playNextMusic();
        }
    }

    public void setNextMusicListener(OnNextMusicListener nextMusicListener) {
        mNextMusicListener = nextMusicListener;
    }
}
