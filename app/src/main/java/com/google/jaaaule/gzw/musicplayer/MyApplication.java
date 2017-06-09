package com.google.jaaaule.gzw.musicplayer;

import android.app.Application;
import android.content.Context;

/**
 * Created by admin on 2016/12/13.
 */

public class MyApplication extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getContext() {
        return mContext;
    }
    
}
