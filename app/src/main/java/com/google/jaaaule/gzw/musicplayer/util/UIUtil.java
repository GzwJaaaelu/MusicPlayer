package com.google.jaaaule.gzw.musicplayer.util;

import android.content.Context;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.google.jaaaule.gzw.musicplayer.MyApplication;

public class UIUtil {
    /**
     * 全屏
     *
     * @param activity
     */
    public static void fullScreen(AppCompatActivity activity) {
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().hide();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //  透明状态栏
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //  透明导航栏
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * 获取全上下文
     */
    public static Context getContext() {
        return MyApplication.getContext();
    }

    @ColorInt
    public static int getColor(@ColorRes int id) {
        int color = ContextCompat.getColor(getContext(), id);
        return color;
    }
}
