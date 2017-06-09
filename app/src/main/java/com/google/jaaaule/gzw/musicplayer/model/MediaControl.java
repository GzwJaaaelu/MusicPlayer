package com.google.jaaaule.gzw.musicplayer.model;

import android.media.MediaPlayer;

/**
 * Created by admin on 2017/6/9.
 */

public class MediaControl {
    private MediaPlayer mMediaPlayer;

//    /**
//     * 播放前的准备工作
//     *
//     * @throws IOException
//     */
//    private void prePlayer() throws IOException {
//        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//        mMediaPlayer.setDataSource(MyApplication.getContext(), Uri.parse(mCurrMusic.path));
//        //  这里偷懒就使用了同步
//        mMediaPlayer.prepare();
//        mMediaPlayer.start();
//        mPlayState = PlayState.STARTED;
//    }
//
//    private void pause() {
//        mMediaPlayer.pause();
//        mPlayState = PlayState.PAUSED;
//    }
//
//    private void stop() {
//        mMediaPlayer.stop();
//        mPlayState = PlayState.STOPPED;
//    }
//
//    private void reset() {
//        mMediaPlayer.reset();
//        mPlayState = PlayState.IDLE;
//    }
//
//    /**
//     * 播放一首歌
//     */
//    private void play(Music music) {
//        try {
//            mCurrMusic = music;
//            reset();
//            prePlayer();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
