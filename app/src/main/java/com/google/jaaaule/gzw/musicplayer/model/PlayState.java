package com.google.jaaaule.gzw.musicplayer.model;

/**
 * Created by admin on 2017/6/8.
 */

public interface PlayState {
    int IDLE = 0;                   //  空闲
    int PREPARED = 1;               //  准备
    int STARTED = 2;                //  开始播放
    int PAUSED = 3;                 //  暂停
    int STOPPED = 4;                //  停止
    int PLAY_BACK_COMPLETED = 5;    //  播放完成
    int ERROR = 6;                  //  错误
    int END = 7;                    //  结束
}
