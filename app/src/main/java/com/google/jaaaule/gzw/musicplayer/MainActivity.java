package com.google.jaaaule.gzw.musicplayer;

import android.content.Intent;
import android.os.Handler;

import com.google.jaaaule.gzw.musicplayer.base.BaseActivity;
import com.google.jaaaule.gzw.musicplayer.util.UIUtil;

public class MainActivity extends BaseActivity {

    @Override
    protected void initView() {
        super.initView();
        UIUtil.fullScreen(this);
    }

    @Override
    protected void initData() {
        super.initData();
        //  暂时没考虑内存泄漏
        new Handler().postDelayed(() -> {
            startActivity(new Intent(MainActivity.this, MusicListActivity.class));
            finish();
        }, 2000);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }
}
