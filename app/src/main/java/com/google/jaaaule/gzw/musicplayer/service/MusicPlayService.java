package com.google.jaaaule.gzw.musicplayer.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.google.jaaaule.gzw.musicplayer.IMusicManager;
import com.google.jaaaule.gzw.musicplayer.model.Music;
import com.google.jaaaule.gzw.musicplayer.model.PlayState;
import com.google.jaaaule.gzw.musicplayer.receiver.MusicCompletionReceiver;

import java.io.IOException;

/**
 * Created by admin on 2017/6/7.
 */

public class MusicPlayService extends Service implements MediaPlayer.OnErrorListener {
    private MediaPlayer mMediaPlayer;
    private Music mCurrMusic;
    private int mPlayState = PlayState.IDLE;
    private final IMusicManager.Stub mBinder = new IMusicManager.Stub() {
        @Override
        public int getPlayState() throws RemoteException {
            return mPlayState;
        }

        @Override
        public Music getCurrMusic() throws RemoteException {
            return mCurrMusic;
        }

        @Override
        public void play(Music music) throws RemoteException {
            //  如果是暂停就直接播放不需要重置
            if (mPlayState == PlayState.PAUSED &&
                    music.path.equals(mCurrMusic.path)) {
                mMediaPlayer.start();
                mPlayState = PlayState.STARTED;
                return;
            } else if (mPlayState == PlayState.STARTED &&
                    music.path.equals(mCurrMusic.path)) {
                return;
            }
            //  重新播放
            MusicPlayService.this.play(music);
        }

        @Override
        public void pause() throws RemoteException {
            MusicPlayService.this.pause();
        }

        @Override
        public int getCurrProgress() throws RemoteException {
            int position = mMediaPlayer.getCurrentPosition();
            return position;
        }
    };

    @Override
    public void onCreate() {
        mMediaPlayer = new MediaPlayer();
        initMediaPlayerListener();
    }

    private void initMediaPlayerListener() {
        mMediaPlayer.setOnCompletionListener(mp -> {
            mPlayState = PlayState.PLAY_BACK_COMPLETED;
            reset();
            sendBroadcast(new Intent(MusicCompletionReceiver.MUSIC_COMPLETION));
        });
        mMediaPlayer.setOnErrorListener(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mPlayState = PlayState.ERROR;
        reset();
        return false;
    }

    /**
     * 播放前的准备工作
     *
     * @throws IOException
     */
    private void prePlayer() throws IOException {
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setDataSource(getApplicationContext(), Uri.parse(mCurrMusic.path));
        //  这里偷懒就使用了同步
        mMediaPlayer.prepare();
        mMediaPlayer.start();
        mPlayState = PlayState.STARTED;
    }

    private void pause() {
        mMediaPlayer.pause();
        mPlayState = PlayState.PAUSED;
    }

    private void stop() {
        mMediaPlayer.stop();
        mPlayState = PlayState.STOPPED;
    }

    private void reset() {
        mMediaPlayer.reset();
        mPlayState = PlayState.IDLE;
    }

    /**
     * 播放一首歌
     */
    private void play(Music music) {
        try {
            mCurrMusic = music;
            reset();
            prePlayer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //  释放资源
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer = null;
            mPlayState = PlayState.END;
        }
    }
}
