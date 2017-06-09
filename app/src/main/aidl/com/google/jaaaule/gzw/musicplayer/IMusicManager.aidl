// IMusicManager.aidl
package com.google.jaaaule.gzw.musicplayer;

import com.google.jaaaule.gzw.musicplayer.model.Music;

// Declare any non-default types here with import statements

interface IMusicManager {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    int getPlayState();

    Music getCurrMusic();

    void play(in Music music);

    void pause();

    int getCurrProgress();
}
