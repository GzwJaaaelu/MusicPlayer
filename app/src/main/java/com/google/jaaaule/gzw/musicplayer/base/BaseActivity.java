package com.google.jaaaule.gzw.musicplayer.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * Created by admin on 2017/5/21.
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!initArgs(getIntent().getExtras())) {
            finish();
        }
        setContentView(getLayoutResId());
        initView();
        initData();
    }

    /**
     * 初始化相关参数
     *
     * @param bundle
     * @return 如果传输正确返回 True，否则返回 False
     */
    protected boolean initArgs(Bundle bundle) {
        return true;
    }

    /**
     * 获取当前界面的资源 Id
     *
     * @return 资源 Id
     */
    protected abstract int getLayoutResId();

    /**
     * 初始化控件
     */
    protected void initView() {
        ButterKnife.bind(this);
    }

    /**
     * 初始化数据
     */
    protected void initData() {
    }
}
