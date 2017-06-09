package com.google.jaaaule.gzw.musicplayer;

import android.app.LoaderManager;
import android.content.ComponentName;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.jaaaule.gzw.musicplayer.adapter.MusicAdapter;
import com.google.jaaaule.gzw.musicplayer.base.BaseActivity;
import com.google.jaaaule.gzw.musicplayer.listener.OnNextMusicListener;
import com.google.jaaaule.gzw.musicplayer.model.Music;
import com.google.jaaaule.gzw.musicplayer.model.PlayState;
import com.google.jaaaule.gzw.musicplayer.receiver.MusicCompletionReceiver;
import com.google.jaaaule.gzw.musicplayer.service.MusicPlayService;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MusicListActivity extends BaseActivity implements OnNextMusicListener {
    @BindView(R.id.rl_mini_bar)
    RelativeLayout mMiniBar;
    @BindView(R.id.rv_show_music)
    RecyclerView mShowMusic;
    @BindView(R.id.iv_music_album)
    ImageView mMusicAlbum;
    @BindView(R.id.tv_music_title)
    TextView mMusicTitle;
    @BindView(R.id.tv_music_artist)
    TextView mMusicArtist;
    @BindView(R.id.iv_next)
    ImageView mNext;
    @BindView(R.id.iv_play)
    ImageView mPlay;
    @BindView(R.id.iv_previous)
    ImageView mPrevious;
    @BindView(R.id.pb_music_progress)
    ProgressBar mMusicProgress;
    private List<Music> mMusicList = new ArrayList<>();
    private static final int MUSIC_LOADER_ID = 1;
    private MusicAdapter mAdapter;
    private IMusicManager mMusicManager;
    private int mCurrIndex;
    private ProgressHandler mProgressHandler;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMusicManager = IMusicManager.Stub.asInterface(service);
            //  没播放音乐的时候隐藏播放条
            showOrNotMiniBar();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mMusicManager = null;
        }
    };
    private Music mCurrMusic;
    private MusicCompletionReceiver mReceiver;

    private static class ProgressHandler extends Handler {
        private WeakReference<ProgressBar> mProgressBar;

        private ProgressHandler(ProgressBar progressBar) {
            mProgressBar = new WeakReference<>(progressBar);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mProgressBar.get() != null) {
                double process = (msg.arg1 * 1.0 / (long) msg.obj) * 100;
                mProgressBar.get().setProgress((int) process);
            }
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_music_list;
    }

    @Override
    protected void initData() {
        super.initData();
        LoaderCallback loaderCallback = new LoaderCallback();
        //  通过 Loader 加载音乐
        getLoaderManager().initLoader(MUSIC_LOADER_ID, null, loaderCallback);
        //  绑定服务又启动服务，为了让 Activity 被销毁时服务还在运行
        startService(new Intent(this, MusicPlayService.class));
        bindService(new Intent(this, MusicPlayService.class), mServiceConnection, BIND_AUTO_CREATE);
        //  下首歌的广播
        mReceiver = new MusicCompletionReceiver();
        mReceiver.setNextMusicListener(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(MusicCompletionReceiver.MUSIC_COMPLETION);
        registerReceiver(mReceiver, filter);
//        mProgressHandler = new ProgressHandler(mMusicProgress);
    }

    @Override
    protected void initView() {
        super.initView();
        //  初始化列表
        mShowMusic.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MusicAdapter(mMusicList, this::clickMusicItem);
        mShowMusic.setAdapter(mAdapter);
        hideMiniBar();
    }

    private void showOrNotMiniBar() {
        try {
            mCurrMusic = mMusicManager.getCurrMusic();
            if (mCurrMusic == null) {
                hideMiniBar();
            } else {
                changeMiniBar(mCurrMusic);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 点击单个音乐时
     *
     * @param music
     * @param position
     * @throws RemoteException
     */
    private void clickMusicItem(Music music, int position) throws RemoteException {
        mCurrMusic = music;
        mCurrIndex = position;
        play();
        changeMiniBar(music);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
        unregisterReceiver(mReceiver);
    }

    /**
     * 改变播放条的显示
     *
     * @param music
     */
    private void changeMiniBar(Music music) throws RemoteException {
        showMiniBar();
        changeDisplayMusicInfo(music);
        changePlayState();
    }

    /**
     * 改变播放条展示的音乐信息
     *
     * @param music
     */
    private void changeDisplayMusicInfo(Music music) {
        mMusicArtist.setText(music.artist);
        mMusicTitle.setText(music.title);
    }

    /**
     * 改变播放条的播放状态
     *
     * @throws RemoteException
     */
    private void changePlayState() throws RemoteException {
        switch (mMusicManager.getPlayState()) {
            case PlayState.STARTED:
                mPlay.setImageResource(R.drawable.ic_pause_music);
                break;
            case PlayState.PAUSED:
                mPlay.setImageResource(R.drawable.ic_play_music);
                break;
        }
    }

    /**
     * 显示底部播放条
     */
    private void showMiniBar() {
        mMiniBar.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏底部播放挑
     */
    private void hideMiniBar() {
        mMiniBar.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.iv_next, R.id.iv_play, R.id.iv_previous})
    public void onViewClicked(View view) {
        try {
            switch (view.getId()) {
                case R.id.iv_next:
                    nextMusic();
                    break;
                case R.id.iv_play:
                    playOrPause();
                    changePlayState();
                    break;
                case R.id.iv_previous:
                    previousMusic();
                    break;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 上一首歌
     *
     * @throws RemoteException
     */
    private void previousMusic() throws RemoteException {
        mCurrMusic = getPreviousMusic();
        changeDisplayMusicInfo(mCurrMusic);
        if (!(mMusicManager.getPlayState() == PlayState.PAUSED)) {
            play();
        }
    }

    private Music getPreviousMusic() {
        Music music;
        if (--mCurrIndex < 0) {
            mCurrIndex = mMusicList.size() - 1;
            music = mMusicList.get(mCurrIndex);
        } else {
            music = mMusicList.get(mCurrIndex);
        }
        return music;
    }

    /**
     * 播放或者暂停
     *
     * @throws RemoteException
     */
    private void playOrPause() throws RemoteException {
        switch (mMusicManager.getPlayState()) {
            case PlayState.STARTED:
                mMusicManager.pause();
                break;
            case PlayState.PAUSED:
                play();
                break;
        }
    }

    private void play() throws RemoteException {
        mMusicManager.play(mCurrMusic);
//        changeMusicProgress();
    }

    /**
     * 更新进度条的方法有 Bug
     *
     * @throws RemoteException
     */
    private void changeMusicProgress() throws RemoteException {
        new Thread(() -> {
            try {
                while (mMusicManager.getPlayState() == PlayState.STARTED) {
                    Message message = Message.obtain();
                    message.arg1 = mMusicManager.getCurrProgress();
                    message.obj = mCurrMusic.duration;
                    mProgressHandler.sendMessage(message);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * 下一首歌
     *
     * @throws RemoteException
     */
    private void nextMusic() throws RemoteException {
        //  下一首
        mCurrMusic = getNextMusic();
        changeDisplayMusicInfo(mCurrMusic);
        //  如果是暂停时切换不要播放
        if (!(mMusicManager.getPlayState() == PlayState.PAUSED)) {
            play();
        }
    }

    private Music getNextMusic() {
        Music music;
        if (++mCurrIndex > (mMusicList.size() - 1)) {
            mCurrIndex = 0;
            music = mMusicList.get(mCurrIndex);
        } else {
            music = mMusicList.get(mCurrIndex);
        }
        return music;
    }

    @Override
    public Music playNextMusic() {
        try {
            nextMusic();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    class LoaderCallback implements LoaderManager.LoaderCallbacks<Cursor> {
        private final String[] MUSIC_PROJECTION = new String[]{
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.IS_MUSIC
        };

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            if (id == MUSIC_LOADER_ID) {
                return new CursorLoader(MusicListActivity.this,
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        MUSIC_PROJECTION,
                        null, null, MUSIC_PROJECTION[3] + " ASC");
            }
            return null;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data != null && data.getCount() > 0) {
                while (data.moveToNext()) {
                    //  音乐id
                    long id = data.getLong(0);
                    //  音乐标题
                    String title = data.getString(1);
                    //  艺术家
                    String artist = data.getString(2);
                    //  时长
                    long duration = data.getLong(3);
                    //  文件大小
                    long size = data.getLong(4);
                    //  文件路径
                    String url = data.getString(5);
                    //  唱片图片
                    String album = data.getString(6);
                    //  是否为音乐
                    int isMusic = data.getInt(7);
                    //  是音乐且长度在 60 秒以上就加入列表
                    if (isMusic != 0 && duration / (1000 * 60) >= 1) {
                        Music music = new Music();
                        music.id = id;
                        music.title = title;
                        music.artist = artist;
                        music.duration = duration;
                        music.size = size;
                        music.path = url;
                        music.album = album;
                        mMusicList.add(music);
                    }
                }
                mAdapter.setMusicList(mMusicList);
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    }
}
